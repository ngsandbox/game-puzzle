package org.game.puzzle.core.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.configs.GameProperties;
import org.game.puzzle.core.entities.Range;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.subscription.SubscriptionService;
import org.game.puzzle.core.subscription.events.MessageEvent;
import org.game.puzzle.core.utils.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class CharacteristicService {

    private final GameProperties properties;
    private final SubscriptionService subscriptionService;
    private final Generator generator;

    @Autowired
    public CharacteristicService(GameProperties properties,
                                 SubscriptionService subscriptionService,
                                 Generator generator) {
        this.properties = properties;
        this.subscriptionService = subscriptionService;
        this.generator = generator;
    }

    /**
     * Calculate experience accoring to the levels and characteristics of the winner and victims
     *
     * @param winner  the winner of the competition
     * @param victims the victims of the competition
     */
    public long calcExperience(@NonNull Species winner, List<Species> victims) {
        log.debug("Calculate experience for winner {} from victims {}", winner, victims);
        if (CollectionUtils.isEmpty(victims)) {
            return 0;
        }

        int intelligence = winner.getCharacteristic().getIntelligence();
        int minExperience = properties.getMinExperience();
        double coeff = properties.getExperienceCoeff();
        double experience = 0;
        for (Species victim : victims) {
            double victimCoeff = coeff * victim.getCharacteristic().getLevel();
            experience += minExperience + minExperience * victimCoeff;
        }


        experience += BigDecimal.valueOf(((experience * intelligence) / 100))
                .setScale(0, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        log.debug("Result experience {}", experience);
        subscriptionService.publish(MessageEvent.of("The result experience: " + experience));
        return (long) experience;
    }

    public boolean getLuck(@NonNull Species species) {
        log.debug("Try to get luck for species", species);
        int luck = species.getCharacteristic().getLuck();
        if (luck >= properties.getMinLuck()) {
            int minLuck = (luck - properties.getMinLuck()) * 1000;
            int maxLuck = (11 - properties.getMinLuck()) * 1000;
            double randomLuck = generator.nextDouble(minLuck, maxLuck);
            double diffLuck = maxLuck - randomLuck;
            if (diffLuck < (maxLuck * 10 / 100)) {
                log.debug("Get luck for {} and result coefficient {} less than 10% of {}", luck, diffLuck, maxLuck);
                return true;
            }
        }

        return false;
    }

    public long calcDamage(@NonNull Species attacker, @NonNull Species defender) {
        log.debug("Calculate damage from attacker {} to defender {}", attacker, defender);
        if (getLuck(defender)) {
            subscriptionService.publish(MessageEvent.of("Defender get luck and escape damage"));
            return 0;
        }

        Range defenderProtection = defender.getProtection();
        int endurance = attacker.getCharacteristic().getEndurance();

        double protectionCoeff = properties.getProtectionCoeff();
        double protection = generator.nextDouble(defenderProtection.getDown(),
                defenderProtection.getUp());
        log.debug("Increase protection {} by endurance {}, level {} and coefficient {}", protection, endurance,
                defender.getCharacteristic().getLevel(),
                protectionCoeff);
        protection += protection * protectionCoeff * endurance;
        protection += protection * protectionCoeff * defender.getCharacteristic().getLevel();


        double damageCoeff = properties.getDamageCoeff();
        Range attackerDamage = attacker.getDamage();
        int strength = attacker.getCharacteristic().getStrength();
        double damage = generator.nextDouble(attackerDamage.getDown(),
                attackerDamage.getUp());
        log.debug("Increase damage {} by strength {}, level {} and coefficient {}", damage, strength, attacker.getCharacteristic().getLevel(), damageCoeff);
        damage += damage * damageCoeff * strength;
        damage += damage * damageCoeff * attacker.getCharacteristic().getLevel();

        if (getLuck(attacker)) {
            log.debug("Got luck and double the damage {}", damage);
            subscriptionService.publish(MessageEvent.of("Attacker get luck and double the damage"));
            damage *= 2;
        }

        //remove 10% from protection
        if (protection > 0) {
            log.debug("Decrease damage {} by 50% of protection {}", damage, protection);
            damage -= (protection / 2);
        }

        if (damage < 0) {
            log.debug("Damage {} is less than 0 for protection {}", damage, protection);
            return 0;
        }

        damage = BigDecimal.valueOf(damage)
                .setScale(0, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        log.debug("Result damage {}", damage);
        subscriptionService.publish(MessageEvent.of("The result damage: " + damage));
        return (long) damage;
    }
}

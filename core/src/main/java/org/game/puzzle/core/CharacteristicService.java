package org.game.puzzle.core;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.configs.GameProperties;
import org.game.puzzle.core.entities.Armor;
import org.game.puzzle.core.entities.Species;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class CharacteristicService {

    private final GameProperties properties;

    @Autowired
    public CharacteristicService(@NonNull GameProperties properties) {
        this.properties = properties;
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
        double minExperienceCoeff = properties.getMinExperienceCoeff();
        double experience = 0;
        for (Species victim : victims) {
            double victimCoeff = minExperienceCoeff * victim.getCharacteristic().getLevel();
            if (victim.getArmor().map(Armor::getDamage).isPresent()) {
                victimCoeff += minExperienceCoeff;
            }

            if (victim.getArmor().map(Armor::getProtection).isPresent()) {
                victimCoeff += minExperienceCoeff;
            }

            experience += minExperience + minExperience * victimCoeff;
        }


        experience += BigDecimal.valueOf(((experience * intelligence) / 100))
                .setScale(0, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        return (long) experience;
    }

    public long calcDamage(@NonNull Species attacker, @NonNull Species defender) {
        log.debug("Calculate damage from attacker {} to defender {}", attacker, defender);
        double damage = 0;
        return (long) damage;
    }
}

package org.game.puzzle.core;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.configs.GameProperties;
import org.game.puzzle.core.entities.Armor;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.Gender;
import org.game.puzzle.core.entities.Species;
import org.game.puzzle.core.entities.species.Ant;
import org.game.puzzle.core.entities.species.Gecko;
import org.game.puzzle.core.entities.species.Human;
import org.game.puzzle.core.services.CharacteristicService;
import org.game.puzzle.core.subscription.SubscriptionService;
import org.game.puzzle.core.subscription.Topics;
import org.game.puzzle.core.subscription.events.SubscriptionEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Optional.ofNullable;


@SpringBootTest(classes = {TestConfiguration.class})
@ActiveProfiles("core-test")
@Slf4j
public class GamePuzzleApplicationTests {

    private final GameProperties properties;
    private final SubscriptionService subscriptionService;

    private final CharacteristicService characteristicService;

    @Autowired
    public GamePuzzleApplicationTests(GameProperties properties, SubscriptionService subscriptionService,
                                      CharacteristicService characteristicService) {
        this.properties = properties;
        this.subscriptionService = subscriptionService;
        this.characteristicService = characteristicService;
    }

    @Test
    void testCalculatedExperience() {
        Species winner = new Human("id1", "winner", testCharacterBuilder().level(5).build(), null);
        List<Species> victims = Arrays.asList(new Human("id1", "winner", testCharacterBuilder().level(2).build(), null),
                new Ant("id1", "winner", testCharacterBuilder().level(3).build(), new Armor("a", null, 2)),
                new Gecko("id1", "winner", testCharacterBuilder().level(4).build(), new Armor("a", 1, null)));
        long experience = characteristicService.calcExperience(winner, null);
        Assertions.assertEquals(0, experience, "The experience is not right for victims");

        experience = characteristicService.calcExperience(winner, victims);
        log.debug("Result experience {}", experience);
        Assertions.assertEquals(45, experience, "The experience is not right for victims");
    }

    @Test
    void testCalculatedDamage() {
        List<SubscriptionEvent> events = new ArrayList<>();
        subscriptionService.subscribe(Topics.MESSAGE_TOPIC)
                .doOnNext(events::add)
                .subscribe();
        Species atacker = new Human("id1", "atacker", testCharacterBuilder().level(5).build(), null);
        Human defender = new Human("id1", "defender", testCharacterBuilder().level(6).build(), null);
        long damage = characteristicService.calcDamage(atacker, defender);
        Assertions.assertEquals(7, damage, "The damage is not right for victims");

        atacker = new Human("id1", "atacker", testCharacterBuilder().level(15).build(), null);
        damage = characteristicService.calcDamage(atacker, defender);
        Assertions.assertEquals(11, damage, "The damage is not right for victims");

        defender = new Human("id1", "defender", testCharacterBuilder().level(30).build(), Armor.builder().id("as").protection(1).build());
        atacker = new Human("id1", "atacker", testCharacterBuilder().level(15).build(), Armor.builder().id("as").damage(2).build());
        damage = characteristicService.calcDamage(atacker, defender);
        Assertions.assertEquals(20, damage, "The damage is not right for victims");

        defender = new Human("id1", "defender", testCharacterBuilder().level(30).endurance(9).build(), Armor.builder().id("as").protection(1).build());
        atacker = new Human("id1", "atacker", testCharacterBuilder().level(15).strength(10).build(), Armor.builder().id("as").damage(2).build());
        damage = characteristicService.calcDamage(atacker, defender);
        Assertions.assertEquals(22, damage, "The damage is not right for victims");

        defender = new Human("id1", "defender", testCharacterBuilder().level(99).build(), Armor.builder().id("as").protection(10).build());
        damage = characteristicService.calcDamage(atacker, defender);

        Assertions.assertEquals(4, damage, "The damage is not right for victims");
        Assertions.assertEquals(5, events.size(), "Wrong events count after damage calculations " + events);

        defender = new Human("id1", "defender", testCharacterBuilder().level(99).luck(10).build(), Armor.builder().id("as").protection(10).build());
        damage = characteristicService.calcDamage(atacker, defender);
        damage = characteristicService.calcDamage(atacker, defender);

    }


    @Test
    void testGetLush() {
        Species maxLuckSpecies = new Human("id1", "species", testCharacterBuilder().level(99).luck(10).build(), null);
        Species minLuckSpecies = new Human("id1", "species", testCharacterBuilder().level(99).luck(0).build(), null);
        int minLuck = properties.getMinLuck();
        properties.setMinLuck(0);

        int counter = 0;
        while (!characteristicService.getLuck(maxLuckSpecies) && ++counter < 10000) ;
        Assertions.assertTrue(counter < 10_000, "There is no luck");

        counter = 0;
        while (characteristicService.calcDamage(minLuckSpecies, maxLuckSpecies) != 0 && ++counter < 10000) ;
        Assertions.assertTrue(counter < 10_000, "There is no luck for defender");

        counter = 0;
        while (characteristicService.calcDamage(maxLuckSpecies, minLuckSpecies) != 98 && ++counter < 10000) ;
        Assertions.assertTrue(counter < 10_000, "There is no luck for atacker");

        properties.setMinLuck(minLuck);
    }


    @Builder(builderMethodName = "testCharacterBuilder")
    private Characteristic getCharacteristic(int level,
                                             Integer endurance,
                                             Integer strength,
                                             Integer luck) {
        Characteristic.CharacteristicBuilder builder = Characteristic.builder();
        builder.id("id1");
        builder.gender(Gender.NONE);
        builder.strength(ofNullable(strength).orElse(8));
        builder.perception(9);
        builder.endurance(ofNullable(endurance).orElse(7));
        builder.charisma(6);
        builder.intelligence(5);
        builder.agility(4);
        builder.luck(ofNullable(luck).orElse(5));
        builder.experience(level * 100);
        builder.level(level);
        return builder.build();
    }

}


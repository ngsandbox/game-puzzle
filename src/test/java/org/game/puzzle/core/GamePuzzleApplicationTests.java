package org.game.puzzle.core;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.TestConfiguration;
import org.game.puzzle.core.configs.GameProperties;
import org.game.puzzle.core.entities.species.Scorpio;
import org.game.puzzle.core.entities.species.Gecko;
import org.game.puzzle.core.entities.species.Human;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.services.CharacteristicService;
import org.game.puzzle.core.subscription.SubscriptionService;
import org.game.puzzle.core.subscription.Topics;
import org.game.puzzle.core.subscription.events.SubscriptionEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.game.puzzle.core.utils.CalcUtils.percent;
import static org.game.puzzle.stubs.CharacteristicStub.getCharacteristic;


@SpringBootTest(classes = {TestConfiguration.class})
@Slf4j
public class GamePuzzleApplicationTests {

    private final GameProperties properties;
    private final SubscriptionService subscriptionService;

    private final CharacteristicService characteristicService;

    @Autowired
    GamePuzzleApplicationTests(GameProperties properties,
                               SubscriptionService subscriptionService,
                               CharacteristicService characteristicService) {
        this.properties = properties;
        this.subscriptionService = subscriptionService;
        this.characteristicService = characteristicService;
    }

    @Test
    void testPercents() {
        Assertions.assertEquals(70, percent(0, 10, 7));
        Assertions.assertEquals(40, percent(0, 1000, 400));
    }

    @Test
    void testCalculatedExperience() {
        Species winner = new Human("id1", "winner", getCharacteristic(0).toBuilder()
                .level(5).build());
        List<Species> victims = Arrays.asList(new Human("id1", "winner", getCharacteristic(0).toBuilder()
                        .level(2).build()),
                new Scorpio(null, "antVictim", getCharacteristic(0).toBuilder().level(3).build()),
                new Gecko(null, "geckoVictim", getCharacteristic(0).toBuilder().level(4).build()));
        long experience = characteristicService.calcExperience(winner, null);
        Assertions.assertEquals(0, experience, "The experience is not right for victims");

        experience = characteristicService.calcExperience(winner, victims);
        log.debug("Result experience {}", experience);
        Assertions.assertEquals(41, experience, "The experience is not right for victims");
    }

    @Test
    void testCalculatedDamage() {
        List<SubscriptionEvent> events = new ArrayList<>();
        subscriptionService.subscribe(Topics.MESSAGE_TOPIC)
                .doOnNext(events::add)
                .subscribe();
        Species atacker = new Human("id1", "atacker", getCharacteristic(0).toBuilder().level(5).build());
        Human defender = new Human("id1", "defender", getCharacteristic(0).toBuilder().level(6).build());
        long damage = characteristicService.calcDamage(atacker, defender);
        Assertions.assertEquals(7, damage, "The damage is not right for victims");

        atacker = new Human("id1", "atacker", getCharacteristic(0).toBuilder().level(15).build());
        damage = characteristicService.calcDamage(atacker, defender);
        Assertions.assertEquals(11, damage, "The damage is not right for victims");

        defender = new Human("id1", "defender", getCharacteristic(0).toBuilder().level(30).build());
        atacker = new Human("id1", "atacker", getCharacteristic(0).toBuilder().level(15).build());
        damage = characteristicService.calcDamage(atacker, defender);
        Assertions.assertEquals(11, damage, "The damage is not right for victims");

        defender = new Human("id1", "defender", getCharacteristic(0).toBuilder().level(30).endurance(9).build());
        atacker = new Human("id1", "atacker", getCharacteristic(0).toBuilder().level(15).strength(10).build());
        damage = characteristicService.calcDamage(atacker, defender);
        Assertions.assertEquals(13, damage, "The damage is not right for victims");

        defender = new Human("id1", "defender", getCharacteristic(0).toBuilder().level(99).build());
        damage = characteristicService.calcDamage(atacker, defender);

        Assertions.assertEquals(13, damage, "The damage is not right for victims");
        Assertions.assertEquals(5, events.size(), "Wrong events count after damage calculations " + events);
    }

    @Test
    void testCalculatedLife() {
        Species species = new Human("id1", "species", getCharacteristic(0).toBuilder().level(5).build());
        long life = characteristicService.calcLife(species);
        Assertions.assertEquals(112, life, "The life is not right for species");

        species = new Human("id1", "species", getCharacteristic(0).toBuilder().level(15).build());
        life = characteristicService.calcLife(species);
        Assertions.assertEquals(123, life, "The life is not right for species");

        species = new Human("id1", "species", getCharacteristic(0).toBuilder().level(55).endurance(10).build());
        life = characteristicService.calcLife(species);
        Assertions.assertEquals(171, life, "The life is not right for species");
    }


    @Test
    void testCalculatedSteps() {
        Species species = new Human("id1", "species", getCharacteristic(0).toBuilder().agility(7).build());
        long steps = characteristicService.calcSteps(species);
        Assertions.assertEquals(8, steps, "The steps is not right for species");

        species = new Human("id1", "species", getCharacteristic(0).toBuilder().agility(10).build());
        steps = characteristicService.calcSteps(species);
        Assertions.assertEquals(10, steps, "The steps is not right for species");

        species = new Human("id1", "species", getCharacteristic(0).toBuilder().agility(3).build());
        steps = characteristicService.calcSteps(species);
        Assertions.assertEquals(6, steps, "The steps is not right for species");
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Test
    void testGetLuck() {
        Species maxLuckSpecies = new Human("id1", "species", getCharacteristic(0).toBuilder().level(99).luck(10).build());
        Species minLuckSpecies = new Human("id1", "species", getCharacteristic(0).toBuilder().level(99).luck(0).build());
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


}


package org.game.puzzle.core;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.Armor;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.Gender;
import org.game.puzzle.core.entities.Species;
import org.game.puzzle.core.entities.species.Ant;
import org.game.puzzle.core.entities.species.Gecko;
import org.game.puzzle.core.entities.species.Human;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@SpringBootTest(classes = {CommonConfiguration.class})
@Slf4j
public class GamePuzzleApplicationTests {

    private final CharacteristicService characteristicService;

    @Autowired
    public GamePuzzleApplicationTests(CharacteristicService characteristicService) {
        this.characteristicService = characteristicService;
    }

    @Test
    void testCalculatedExperience() {
        Species winner = new Human(UUID.randomUUID().toString(), "winner", getCharacteristic(5), null);
        List<Species> victims = Arrays.asList(new Human(UUID.randomUUID().toString(), "winner", getCharacteristic(2), null),
                new Ant(UUID.randomUUID().toString(), "winner", getCharacteristic(3), new Armor("a", null, 2)),
                new Gecko(UUID.randomUUID().toString(), "winner", getCharacteristic(4), new Armor("a", 1, null)));
        long experience = characteristicService.calcExperience(winner, null);
        Assertions.assertEquals(0, experience, "The experience is not right for victims");

        experience = characteristicService.calcExperience(winner, victims);
        log.debug("Result experience {}", experience);
        Assertions.assertEquals(45, experience, "The experience is not right for victims");

    }


    private Characteristic getCharacteristic(int level) {
        return Characteristic.builder()
                .id(UUID.randomUUID().toString())
                .gender(Gender.NONE)
                .strength(8)
                .perception(9)
                .endurance(7)
                .charisma(6)
                .intelligence(5)
                .agility(4)
                .luck(8)
                .experience(level * 100)
                .level(level)
                .build();
    }

}


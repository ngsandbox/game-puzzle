package org.game.puzzle.db;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.TestConfiguration;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.species.Ant;
import org.game.puzzle.core.entities.species.Gecko;
import org.game.puzzle.core.entities.species.Human;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.exceptions.NotFoundException;
import org.game.puzzle.core.services.SpeciesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.game.puzzle.stubs.CharacteristicStub.getCharacteristic;


@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestConfiguration.class})
class DataBaseGameApplicationTests {

    private final SpeciesService speciesService;

    @Autowired
    DataBaseGameApplicationTests(SpeciesService speciesService) {
        this.speciesService = speciesService;
    }


    @BeforeEach
    void uploadData() {

    }

    @Test
    void testSaveSpecies() {

        Characteristic characteristic = getCharacteristic(0).toBuilder()
                .agility(10).charisma(10).intelligence(10).build();
        Species human = new Human("human1", "winner", characteristic,
                Arrays.asList(new Ant("victim1", characteristic),
                        new Gecko("victim2", characteristic)));
        String humanId = speciesService.save(human);
        Assertions.assertNotNull(humanId, "The result identifier is empty");
        Species result = speciesService.getSpeciesById(humanId);
        Assertions.assertNotNull(result, "Species not found by id: " + humanId);

        result = speciesService.getSpeciesByLogin(human.getLogin());
        Assertions.assertNotNull(result, "Species not found by login: " + human.getLogin());

        result = speciesService.getSpeciesByLogin("victim1");
        Assertions.assertNotNull(result, "Species not found by login: victim1");

        speciesService.removeSpeciesByLogin(humanId);
        Assertions.assertThrows(NotFoundException.class,
                () -> speciesService.getSpeciesById(humanId),
                "Species still exist with login: " + humanId);
    }

    @Test
    void testUpdateCharacteristic() {
        Characteristic characteristic = getCharacteristic(0).toBuilder()
                .agility(10).charisma(8).intelligence(7).build();
        Species human = new Human("human1", "winner", characteristic);
        String humanId = speciesService.save(human);
        Assertions.assertNotNull(humanId, "The result identifier is empty");

        Species species = speciesService.getSpeciesByLogin(human.getLogin());
        Assertions.assertNotNull(species, "Species not found by id: " + humanId);
        characteristic = species.getCharacteristic();
        Assertions.assertNotNull(characteristic, "Species characteristics not specified");
        Assertions.assertEquals(10, characteristic.getAgility(), "Species agility is not right");
        Assertions.assertEquals(8, characteristic.getCharisma(), "Species charisma is not right");
        Assertions.assertEquals(0, characteristic.getExperience(), "Species experience is not right");
        Assertions.assertEquals(0, characteristic.getLevel(), "Species level is not right");

        characteristic = characteristic.toBuilder().experience(198776).level(6).build();
        speciesService.updateCharacteristic(characteristic);


        species = speciesService.getSpeciesByLogin(human.getLogin());
        Assertions.assertNotNull(species, "Species not found by id: " + humanId);
        characteristic = species.getCharacteristic();
        Assertions.assertNotNull(characteristic, "Species characteristics not specified");
        Assertions.assertEquals(198776, characteristic.getExperience(), "Species experience is not right");
        Assertions.assertEquals(6, characteristic.getLevel(), "Species level is not right");


        speciesService.removeSpeciesByLogin(humanId);
        Assertions.assertThrows(NotFoundException.class,
                () -> speciesService.getSpeciesById(humanId),
                "Species still exist with login: " + humanId);
    }
}


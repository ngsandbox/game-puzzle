package org.game.puzzle.db;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.dao.GameDAO;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.species.Ant;
import org.game.puzzle.core.entities.species.Gecko;
import org.game.puzzle.core.entities.species.Human;
import org.game.puzzle.core.entities.species.Species;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.game.puzzle.core.stubs.CharacteristicTest.testCharacterBuilder;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestDatabaseConfiguration.class})
class DataBaseGameApplicationTests {

    @Autowired
    private GameDAO gameDAO;


    @BeforeEach
    void uploadData() {

    }

    @Test
    void testSaveSpecies() {
        Characteristic characteristic = testCharacterBuilder()
                .agility(10).charisma(10).intelligence(10).build();
        Species human = new Human("human1", "winner", characteristic,
                Arrays.asList(new Ant("victim1", characteristic),
                        new Gecko("victim2", characteristic)));
        String humanId = gameDAO.save(human);
        Assertions.assertNotNull(humanId, "The result identifier is empty");
        Optional<Species> result = gameDAO.getById(humanId);
        Assertions.assertTrue(result.isPresent(), "Species not found by id: " + humanId);

        result = gameDAO.getByLogin(human.getLogin());
        Assertions.assertTrue(result.isPresent(), "Species not found by login: " + human.getLogin());

        result = gameDAO.getByLogin("victim1");
        Assertions.assertTrue(result.isPresent(), "Species not found by login: victim1");

        gameDAO.removeById(humanId);
        result = gameDAO.getById(humanId);
        Assertions.assertFalse(result.isPresent(), "Species still exist with id: " + humanId);

        result = gameDAO.getByLogin("victim1");
        Assertions.assertFalse(result.isPresent(), "Species still exist with login: victim1");
    }

    @Test
    void testUpdateCharacteristic() {
        Characteristic characteristic = testCharacterBuilder().agility(10).charisma(8).intelligence(7).build();
        Species human = new Human("human1", "winner", characteristic);
        String humanId = gameDAO.save(human);
        Assertions.assertNotNull(humanId, "The result identifier is empty");

        Optional<Species> species = gameDAO.getByLogin(human.getLogin());
        Assertions.assertTrue(species.isPresent(), "Species not found by id: " + humanId);
        characteristic = species.get().getCharacteristic();
        Assertions.assertNotNull(characteristic, "Species characteristics not specified");
        Assertions.assertEquals(10, characteristic.getAgility(), "Species agility is not right");
        Assertions.assertEquals(8, characteristic.getCharisma(), "Species charisma is not right");
        Assertions.assertEquals(0, characteristic.getExperience(), "Species experience is not right");
        Assertions.assertEquals(0, characteristic.getLevel(), "Species level is not right");

        characteristic = characteristic.toBuilder().experience(198776).level(6).build();
        gameDAO.updateCharacteristic(characteristic);


        species = gameDAO.getByLogin(human.getLogin());
        Assertions.assertTrue(species.isPresent(), "Species not found by id: " + humanId);
        characteristic = species.get().getCharacteristic();
        Assertions.assertNotNull(characteristic, "Species characteristics not specified");
        Assertions.assertEquals(198776, characteristic.getExperience(), "Species experience is not right");
        Assertions.assertEquals(6, characteristic.getLevel(), "Species level is not right");


        gameDAO.removeById(humanId);
        species = gameDAO.getById(humanId);
        Assertions.assertFalse(species.isPresent(), "Species still exist with id: " + humanId);
    }
}


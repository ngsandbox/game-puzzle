package org.game.puzzle.db;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.TestConfiguration;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.species.Scorpio;
import org.game.puzzle.core.entities.species.Gecko;
import org.game.puzzle.core.entities.species.Human;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.exceptions.NotFoundException;
import org.game.puzzle.core.services.SpeciesService;
import org.game.puzzle.db.factories.CharacteristicFactory;
import org.game.puzzle.db.factories.SpeciesFactory;
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
    void testSaveWinnersAndVictims() {

        Characteristic characteristic = getCharacteristic(0).toBuilder()
                .agility(10).charisma(10).intelligence(10).build();
        Species winner = new Human(null, "winner11", characteristic,
                Arrays.asList(new Scorpio(null, "victim11", characteristic),
                        new Gecko(null, "victim12", characteristic)));
        String humanId = speciesService.save(winner);
        Assertions.assertNotNull(humanId, "The result identifier is empty");
        Species result = speciesService.getSpeciesById(humanId);
        Assertions.assertNotNull(result, "Species not found by id: " + humanId);

        result = speciesService.getSpeciesByLogin(winner.getLogin());
        Assertions.assertNotNull(result, "Species not found by login: " + winner.getLogin());

        result = speciesService.getSpeciesByLogin("victim11");
        Assertions.assertNotNull(result, "Species not found by login: victim1");

        result = speciesService.getSpeciesByLogin(winner.getLogin());
        Assertions.assertNotNull(result, "Species not found by login: " + winner.getLogin());
        Assertions.assertEquals(2, result.getVictims().size());


        Human victim3 = new Human(null, "victim13", characteristic);
        speciesService.addVictim(winner.getLogin(), victim3);
        Assertions.assertNotNull(result, "Victim not found by login: " + victim3.getLogin());

        Assertions.assertThrows(DbException.class,
                () -> speciesService.addVictim("winner42", victim3),
                "Winner still exist with login: winner42");

        result = speciesService.getSpeciesByLogin(winner.getLogin());
        Assertions.assertNotNull(result, "Species not found by login: " + winner.getLogin());
        Assertions.assertEquals(3, result.getVictims().size());


        Species victim3Result = speciesService.getSpeciesByLogin(victim3.getLogin());
        Assertions.assertNotNull(victim3Result, "Species not found by login: " + victim3.getLogin());

        speciesService.removeSpeciesByLogin("victim11");
        speciesService.removeSpeciesByLogin("victim12");
        speciesService.removeSpeciesByLogin("victim13");
        speciesService.removeSpeciesByLogin("winner11");
        Assertions.assertThrows(NotFoundException.class,
                () -> speciesService.getSpeciesById(humanId),
                "Species still exist with login: " + humanId);
    }

    @Test
    void testSaveUser() {
        Characteristic characteristic = getCharacteristic(0).toBuilder()
                .agility(10).charisma(10).intelligence(10).build();
        Species user = new Human(null, "login", characteristic);
        Assertions.assertFalse(speciesService.doesRegistered(user.getLogin()),
                "User is already registered");

        String userId = speciesService.save(user);
        Assertions.assertNotNull(userId, "The result identifier is empty");

        Assertions.assertTrue(speciesService.doesRegistered(user.getLogin()),
                "User is not registered");

        speciesService.removeSpeciesByLogin("login");
    }

    @Test
    void testUpdateCharacteristic() {
        Characteristic characteristic = getCharacteristic(0).toBuilder()
                .agility(10).charisma(8).intelligence(7).build();
        Species human = new Human(null, "winner2", characteristic);
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


        speciesService.removeSpeciesByLogin("winner2");
        Assertions.assertThrows(NotFoundException.class,
                () -> speciesService.getSpeciesById(humanId),
                "Species still exist with login: " + humanId);
    }

    @Test
    void testFactories() {
        Assertions.assertNull(SpeciesFactory.convert(null, true));
        Assertions.assertNull(SpeciesFactory.from(null));
        Assertions.assertNull(CharacteristicFactory.from(null));
        Assertions.assertNull(CharacteristicFactory.to(null));
    }
}


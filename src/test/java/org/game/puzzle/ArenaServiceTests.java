package org.game.puzzle;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.TestConfiguration;
import org.game.puzzle.core.entities.FightSession;
import org.game.puzzle.core.entities.grid.Coordinate;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.services.SpeciesService;
import org.game.puzzle.web.models.MoveModel;
import org.game.puzzle.web.services.ArenaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptySet;


@SpringBootTest(classes = {TestConfiguration.class})
@Slf4j
public class ArenaServiceTests {

    private final ArenaService arenaService;
    private final SpeciesService speciesService;

    @Autowired
    public ArenaServiceTests(ArenaService arenaService,
                             SpeciesService speciesService) {
        this.arenaService = arenaService;
        this.speciesService = speciesService;
    }

    @Test
    void testFindPath() {
        List<Coordinate> route = arenaService.findRoute(Coordinate.of(1, 1), Coordinate.of(6, 8), 20, emptySet());
        Assertions.assertEquals(7, route.size(), "Route length is not right");

        route = arenaService.findRoute(Coordinate.of(1, 1), Coordinate.of(6, 8), 5, Collections.singleton(Coordinate.of(1, 1)));
        Assertions.assertEquals(5, route.size(), "Route length is not right");

        route = arenaService.findRoute(Coordinate.of(1, 2), Coordinate.of(2, 2), 2, emptySet());
        Assertions.assertEquals(1, route.size(), "Route length is not right");

        route = arenaService.findRoute(Coordinate.of(1, 2), Coordinate.of(1, 2), 5, emptySet());
        Assertions.assertEquals(0, route.size(), "Route length is not right");
    }

    @Test
    void testFight() {
        Species attacker = speciesService.createSpecies(10);
        FightSession session = arenaService.createFightSession(attacker);
        Assertions.assertTrue(session.isUserHit(), "User hit expected");
        MoveModel moveModel = arenaService.makeMove(session.getUserPosition(), session);
        Assertions.assertNotNull(moveModel, "Model is null");
        Assertions.assertTrue(session.isUserHit(), "User hit expected");
        Assertions.assertEquals(0, moveModel.getRoute().size(), "Click on the user's position");

        moveModel = arenaService.makeMove(session.getEnemyPosition(), session);
        Assertions.assertNotNull(moveModel, "Model is null");
        Assertions.assertFalse(session.isUserHit(), "Enemy hit expected");

        moveModel = arenaService.makeMove(session.getUserPosition(), session);
        Assertions.assertNotNull(moveModel, "Model is null");
        Assertions.assertTrue(session.isUserHit(), "User hit expected");

        moveModel = arenaService.makeMove(session.getEnemyPosition(), session);
        Assertions.assertNotNull(moveModel, "Model is null");
        Assertions.assertFalse(session.isUserHit(), "User hit expected");

        int counter = 0;
        while (!moveModel.isFinish() && counter < 100) {
            counter++;
            moveModel = arenaService.makeMove(session.isUserHit() ?
                    session.getEnemyPosition() :
                    session.getUserPosition(), session);
            Assertions.assertNotNull(moveModel, "Model is null");
        }
    }
}


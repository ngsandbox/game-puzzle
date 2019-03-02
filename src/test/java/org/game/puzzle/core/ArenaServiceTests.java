package org.game.puzzle.core;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.TestConfiguration;
import org.game.puzzle.core.entities.grid.Coordinate;
import org.game.puzzle.core.services.ArenaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest(classes = {TestConfiguration.class})
@Slf4j
public class ArenaServiceTests {

    private final ArenaService arenaService;

    @Autowired
    public ArenaServiceTests(ArenaService arenaService) {
        this.arenaService = arenaService;
    }

    @Test
    void testFindPath() {
        List<Coordinate> route = arenaService.findRoute(Coordinate.of(1, 1), Coordinate.of(6, 8), 20);
        Assertions.assertEquals(7, route.size(), "Route length is not right");

        route = arenaService.findRoute(Coordinate.of(1, 1), Coordinate.of(6, 8), 5);
        Assertions.assertEquals(5, route.size(), "Route length is not right");

        route = arenaService.findRoute(Coordinate.of(1, 2), Coordinate.of(2, 2), 2);
        Assertions.assertEquals(1, route.size(), "Route length is not right");

        route = arenaService.findRoute(Coordinate.of(1, 2), Coordinate.of(1, 2), 5);
        Assertions.assertEquals(0, route.size(), "Route length is not right");
    }

}


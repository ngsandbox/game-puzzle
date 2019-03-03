package org.game.puzzle.core;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.TestConfiguration;
import org.game.puzzle.core.entities.grid.Coordinate;
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

    @Autowired
    public ArenaServiceTests(ArenaService arenaService) {
        this.arenaService = arenaService;
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

}


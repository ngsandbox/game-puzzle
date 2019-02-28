package org.game.puzzle.core;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.configs.GameProperties;
import org.game.puzzle.core.entities.grid.Coordinate;
import org.game.puzzle.core.services.ArenaService;
import org.game.puzzle.core.subscription.SubscriptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;


@SpringBootTest(classes = {TestConfiguration.class})
@ActiveProfiles("core-test")
@Slf4j
public class ArenaServiceTests {

    private final GameProperties properties;
    private final SubscriptionService subscriptionService;

    private final ArenaService arenaService;

    @Autowired
    public ArenaServiceTests(GameProperties properties,
                             SubscriptionService subscriptionService,
                             ArenaService arenaService) {
        this.properties = properties;
        this.subscriptionService = subscriptionService;
        this.arenaService = arenaService;
    }

    @Test
    void testFindPath() {
        List<Coordinate> route = arenaService.findRoute(Coordinate.of(1, 1), Coordinate.of(6, 8));
        Assertions.assertEquals(7, route.size(), "Route length is not right");

        route = arenaService.findRoute(Coordinate.of(1, 2), Coordinate.of(2, 2));
        Assertions.assertEquals(1, route.size(), "Route length is not right");

        route = arenaService.findRoute(Coordinate.of(1, 2), Coordinate.of(1, 2));
        Assertions.assertEquals(0, route.size(), "Route length is not right");
    }

}


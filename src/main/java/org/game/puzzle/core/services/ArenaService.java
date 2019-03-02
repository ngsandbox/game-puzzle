package org.game.puzzle.core.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.configs.GameProperties;
import org.game.puzzle.core.entities.grid.Arena;
import org.game.puzzle.core.entities.grid.Cell;
import org.game.puzzle.core.entities.grid.Coordinate;
import org.game.puzzle.core.subscription.SubscriptionService;
import org.game.puzzle.core.utils.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArenaService {

    private final CharacteristicService characteristicService;
    private final GameProperties properties;
    private final SubscriptionService subscriptionService;
    private final Generator generator;

    @Autowired
    public ArenaService(CharacteristicService characteristicService,
                        GameProperties properties,
                        SubscriptionService subscriptionService,
                        Generator generator) {
        this.characteristicService = characteristicService;
        this.properties = properties;
        this.subscriptionService = subscriptionService;
        this.generator = generator;
    }

    /**
     * Create new arena
     */
    public Arena createArena() {
        log.debug("Create new arena {}", properties.getArenaSize());
        return new Arena(properties.getArenaSize());
    }

    /**
     * Find path from the source to the destination coordinates
     */
    public List<Coordinate> findRoute(@NonNull Coordinate source,
                                      @NonNull Coordinate destination,
                                      int steps) {
        Cell sourceCell = Cell.newCell(source, new ArrayList<>());
        Optional<Cell> route = getPath(sourceCell, destination);
        log.debug("The result route {}", route);
        List<Coordinate> coordinates = route.map(Cell::getRoute).orElse(new LinkedList<>());
        if (coordinates.size() > steps) {
            log.debug("Truncate the route to the available steps {}", steps);
            return coordinates.stream().limit(steps).collect(Collectors.toList());
        }

        return coordinates;
    }

    private int nextPosition(int currAxis, int destAxis) {
        return currAxis > destAxis ? currAxis - 1 :
                currAxis < destAxis ? currAxis + 1 : destAxis;
    }

    private Optional<Cell> getPath(Cell current,
                                   Coordinate destination) {
        if (current.getCoordinate().equals(destination)) {
            log.debug("Found destination with route {}", current);
            return Optional.of(current);
        }

        List<Coordinate> newRoute = new ArrayList<>(current.getRoute());
        newRoute.add(current.getCoordinate());

        int currRow = current.getCoordinate().getRow();
        int currCol = current.getCoordinate().getCol();
        int nextRow = nextPosition(currRow, destination.getRow());
        int nextCol = nextPosition(currCol, destination.getCol());

        return getPath(Cell.newCell(Coordinate.of(nextRow, nextCol), newRoute), destination);
    }

}

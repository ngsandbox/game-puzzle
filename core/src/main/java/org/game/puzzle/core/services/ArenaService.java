package org.game.puzzle.core.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.configs.GameProperties;
import org.game.puzzle.core.entities.Armor;
import org.game.puzzle.core.entities.Cell;
import org.game.puzzle.core.entities.Range;
import org.game.puzzle.core.entities.Species;
import org.game.puzzle.core.subscription.SubscriptionService;
import org.game.puzzle.core.subscription.events.MessageEvent;
import org.game.puzzle.core.utils.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

import static org.game.puzzle.core.entities.Cell.visited;

@Slf4j
@Service
public class ArenaService {

    private final GameProperties properties;
    private final SubscriptionService subscriptionService;
    private final Generator generator;

    @Autowired
    public ArenaService(@NonNull GameProperties properties,
                        @NonNull SubscriptionService subscriptionService,
                        @NonNull Generator generator) {
        this.properties = properties;
        this.subscriptionService = subscriptionService;
        this.generator = generator;
    }


    public int breadthFirstSearch(@NonNull Cell[][] grid,
                                  @NonNull Cell source,
                                  @NonNull Cell destination) {
        // To keep track of visited QItems. Marking
        // blocked cells as visited.

        int max = grid.length;
        Deque<Cell> path = new ArrayDeque<>(max * max);
        path.push(source);
        while (!path.isEmpty()) {
            Cell cell = path.poll();

            // Destination found;
            if (grid[cell.getRow()][cell.getCol()].equals(destination))
                return cell.getDist();

            // move to the up cell
            if (isNotVisited((byte) (cell.getRow() - 1), cell.getCol(), grid)) {
                path.push(grid[cell.getRow() - 1][cell.getCol()].visit());
            }

            // move to the down cell
            if (isNotVisited((byte) (cell.getRow() + 1), cell.getCol(), grid)) {
                path.push(grid[cell.getRow() + 1][cell.getCol()].visit());
            }

            // move to the left cell
            if (isNotVisited(cell.getRow(), (byte) (cell.getCol() - 1), grid)) {
                path.push(grid[cell.getRow()][cell.getCol() - 1].visit());
            }

            // move to the right cell
            if (isNotVisited(cell.getRow(), (byte) (cell.getCol() + 1), grid)) {
                path.push(grid[cell.getRow()][cell.getCol() + 1].visit());
            }
        }

        return -1;
    }

    private boolean isNotVisited(byte row, byte col, Cell[][] grid) {
        return row - 1 >= 0 &&
                !grid[row][col].isVisited();
    }
}

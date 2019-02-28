package org.game.puzzle.core.entities.grid;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.GameException;

@Slf4j
@ToString
@EqualsAndHashCode(of = "grid")
public class Arena {

    private final byte size;

    private final Coordinate[][] grid;

    public byte getSize() {
        return size;
    }

    public Arena(byte size) {
        if (size <= 0) {
            throw new GameException("The size of arena should be greater than zero. " + size);
        }

        this.size = size;
        grid = new Coordinate[size][];
        for (int i = 0; i < size; i++) {
            Coordinate[] cells = new Coordinate[size];
            grid[i] = cells;
            for (int j = 0; j < size; j++) {
                grid[i][j] = Coordinate.of(i, j);
            }
        }
    }


}

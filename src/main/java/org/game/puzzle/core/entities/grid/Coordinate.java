package org.game.puzzle.core.entities.grid;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = {"row", "col"})
@ToString
public class Coordinate {
    private final int row;
    private final int col;

    private Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Coordinate of(int row, int col) {
        return new Coordinate(row, col);
    }
}

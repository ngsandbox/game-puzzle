package org.game.puzzle.core.entities.grid;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;

@Getter
@EqualsAndHashCode(of = {"row", "col"})
@ToString
public class Coordinate implements Comparable<Coordinate> {
    private static final Comparator<Coordinate> COMPARATOR = Comparator.comparing(Coordinate::getRow)
            .thenComparing(Coordinate::getCol);

    private final int row;
    private final int col;

    private Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Coordinate of(int row, int col) {
        return new Coordinate(row, col);
    }

    @Override
    public int compareTo(Coordinate coordinate) {
        if (coordinate == null) {
            return 1;
        }

        return COMPARATOR.compare(this, coordinate);
    }
}

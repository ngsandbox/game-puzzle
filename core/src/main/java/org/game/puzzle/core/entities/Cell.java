package org.game.puzzle.core.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(of = {"row", "col"})
public class Cell {
    private byte row;
    private byte col;
    private int dist;
    private boolean visited;

    private Cell(byte row, byte col, int dist, boolean visited) {
        this.row = row;
        this.col = col;
        this.dist = dist;
        this.visited = visited;
    }

    public Cell visit(){
        visited = true;
        dist++;
        return this;
    }

    public static Cell newCell(byte row, byte col) {
        return new Cell(row, col, 0, false);
    }

    public static Cell visited(byte row, byte col, int dist) {
        return new Cell(row, col, dist, true);
    }
}

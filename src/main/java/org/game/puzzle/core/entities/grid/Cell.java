package org.game.puzzle.core.entities.grid;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(of = {"coordinate"})
@ToString
public class Cell {
    @Getter
    private final Coordinate coordinate;

    private final List<Coordinate> route;

    public List<Coordinate> getRoute() {
        return new ArrayList<>(route);
    }

    private Cell(@NonNull Coordinate coordinate, List<Coordinate> route) {
        this.coordinate = coordinate;
        this.route = route;
    }

    public static Cell newCell(@NonNull Coordinate coordinate, @NonNull List<Coordinate> path) {
        return new Cell(coordinate, path);
    }
}

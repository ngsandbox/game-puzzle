package org.game.puzzle.web.models;

import lombok.*;
import org.game.puzzle.core.entities.grid.Coordinate;

import javax.validation.constraints.NotNull;


@EqualsAndHashCode(of = {"row", "col"})
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Position {
    @NotNull(message = "Row number is not provided")
    private int row;
    @NotNull(message = "Column number is not provided")
    private int col;

    public Coordinate convert() {
        return Coordinate.of(row, col);
    }
}




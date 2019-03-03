package org.game.puzzle.web.models;

import lombok.*;
import org.game.puzzle.core.entities.grid.Coordinate;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@EqualsAndHashCode(of = {"row", "col"})
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Position implements Serializable {

    private static final long serialVersionUID = 6992321728782519173L;

    @NotNull(message = "Row number is not provided")
    private int row;
    @NotNull(message = "Column number is not provided")
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Position convert(Coordinate coordinate) {
        if (coordinate == null) {
            return null;
        }

        return new Position(coordinate.getRow(), coordinate.getCol());
    }

    public Coordinate convert() {
        return Coordinate.of(row, col);
    }
}




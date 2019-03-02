package org.game.puzzle.core.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(of = {"down", "up"})
public class Range {

    private final int down;
    private final int up;

    public Range(int down, int up) {
        this.down = down;
        this.up = up;
    }
}

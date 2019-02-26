package org.game.puzzle.core.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Range {

    public final int down;
    public final int up;

    public Range(int down, int up) {
        this.down = down;
        this.up = up;
    }
}

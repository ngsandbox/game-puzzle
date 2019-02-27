package org.game.puzzle.core.stubs;

import org.game.puzzle.core.utils.Generator;

public class TestGenerator extends Generator {
    @Override
    public double nextDouble(double origin, double bound) {
        return (origin + bound) / 2;
    }
}

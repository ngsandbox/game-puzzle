package org.game.puzzle.core.utils;

import java.util.concurrent.ThreadLocalRandom;

public class Generator {

    private final ThreadLocalRandom random;

    public Generator() {
        this.random = ThreadLocalRandom.current();
    }

    public double nextDouble(double origin, double bound) {
        return random.nextDouble(origin, bound);
    }
}

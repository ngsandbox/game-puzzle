package org.game.puzzle.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class Generator {

    private final ThreadLocalRandom random;

    public Generator() {
        this.random = ThreadLocalRandom.current();
    }

    public double nextDouble(double origin, double bound) {
        log.trace("Get random from {} to {}", origin, bound);
        return random.nextDouble(origin, bound);
    }
}

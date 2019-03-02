package org.game.puzzle.stubs;

import org.game.puzzle.core.utils.Generator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class TestGeneratorStub extends Generator {
    @Override
    public double nextDouble(double origin, double bound) {
        return (origin + bound) / 2;
    }
}

package org.game.puzzle.core.entities.species;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.Range;

import java.util.Collections;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Gecko extends Species {
    @Override
    public SpeciesType getType() {
        return SpeciesType.GECKO;
    }

    @Override
    public Range getDamage() {
        return new Range(1, 2);
    }

    @Override
    public Range getProtection() {
        return new Range(0, 0);
    }

    public Gecko(@NonNull String id,
                 @NonNull Characteristic characteristic) {
        super(id, id, characteristic, Collections.emptyList());
    }
}

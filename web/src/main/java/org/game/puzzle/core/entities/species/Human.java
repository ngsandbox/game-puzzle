package org.game.puzzle.core.entities.species;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.Range;

import java.util.Collections;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Human extends Species {


    @Override
    public SpeciesType getType() {
        return SpeciesType.HUMAN;
    }

    @Override
    public Range getDamage() {
        return new Range(1, 4);
    }

    @Override
    public Range getProtection() {
        return new Range(0, 0);
    }

    public Human(@NonNull String id,
                 @NonNull String login,
                 @NonNull Characteristic characteristic) {
        this(id, login, characteristic, Collections.emptyList());
    }

    public Human(@NonNull String id,
                 @NonNull String login,
                 @NonNull Characteristic characteristic,
                 @NonNull List<Species> victims) {
        super(id, login, characteristic, victims);
    }
}

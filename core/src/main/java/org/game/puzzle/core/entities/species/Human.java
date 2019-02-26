package org.game.puzzle.core.entities.species;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.game.puzzle.core.entities.Armor;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.Range;
import org.game.puzzle.core.entities.Species;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Human extends Species {
    @Override
    public Range getDamage() {
        return new Range(1, 4);
    }

    @Override
    public Range getProtection() {
        return new Range(0, 0);
    }

    public Human(@NonNull String id, @NonNull String code, @NonNull Characteristic characteristic, Armor armor) {
        super(id, code, characteristic, armor);
    }

}

package org.game.puzzle.core.entities.species;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.Range;

import java.util.List;


@Getter
@EqualsAndHashCode(of = "id")
@ToString
public abstract class Species {

    private final String id;

    private final String login;

    private final List<Species> victims;

    public abstract SpeciesType getType();

    public abstract Range getDamage();

    public abstract Range getProtection();

    private final Characteristic characteristic;

    public Species(@NonNull String id, @NonNull String login,
            @NonNull Characteristic characteristic,
            @NonNull List<Species> victims) {
        this.id = id;
        this.login = login;
        this.characteristic = characteristic;
        this.victims = victims;
    }
}

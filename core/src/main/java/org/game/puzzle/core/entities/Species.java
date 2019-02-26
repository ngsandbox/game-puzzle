package org.game.puzzle.core.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.Optional;

@Getter
@EqualsAndHashCode(of = "id")
public abstract class Species {

    private final String id;
    private final String login;
    public final Armor armor;

    public Optional<Armor> getArmor() {
        return Optional.ofNullable(armor);
    }

    public abstract Range getDamage();

    public abstract Range getProtection();

    private final Characteristic characteristic;

    public Species(@NonNull String id, @NonNull String login, @NonNull Characteristic characteristic, Armor armor) {
        this.id = id;
        this.login = login;
        this.characteristic = characteristic;
        this.armor = armor;
    }
}

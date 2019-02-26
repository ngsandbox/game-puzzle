package org.game.puzzle.core.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.Optional;

@EqualsAndHashCode(of = "id")
@ToString
public class Armor {
    private final String id;
    private final Integer damage;
    private final Integer protection;

    public String getId() {
        return id;
    }

    public Optional<Integer> getDamage() {
        return Optional.ofNullable(damage);
    }

    public Optional<Integer> getProtection() {
        return Optional.ofNullable(protection);
    }

    @Builder
    public Armor(@NonNull String id, Integer damage, Integer protection) {
        this.id = id;
        this.damage = damage;
        this.protection = protection;
    }
}

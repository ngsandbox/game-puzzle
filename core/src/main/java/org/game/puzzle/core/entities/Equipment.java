package org.game.puzzle.core.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.Optional;

@EqualsAndHashCode(of = {"damage", "protection"})
@ToString
public class Equipment {
    private final Integer damage;
    private final Integer protection;

    public Optional<Integer> getDamage() {
        return Optional.ofNullable(damage);
    }

    public Optional<Integer> getProtection() {
        return Optional.ofNullable(protection);
    }

    @Builder
    public Equipment(Integer damage, Integer protection) {
        this.damage = damage;
        this.protection = protection;
    }
}

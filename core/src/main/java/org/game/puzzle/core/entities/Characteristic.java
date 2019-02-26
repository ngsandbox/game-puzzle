package org.game.puzzle.core.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Characteristic {
    /**
     * Unique identifier for species characteristic
     */
    private final String id;
    private final Gender gender;
    private final int strength;
    private final int perception;
    private final int endurance;
    private final int charisma;
    private final int intelligence;
    private final int agility;
    private final int luck;

    private final long experience;
    private final int level;

    @Builder
    public Characteristic(String id,
                          Gender gender,
                          int strength,
                          int perception,
                          int endurance,
                          int charisma,
                          int intelligence,
                          int agility,
                          int luck,
                          long experience,
                          int level) {
        this.id = id;
        this.gender = gender;
        this.strength = strength;
        this.perception = perception;
        this.endurance = endurance;
        this.charisma = charisma;
        this.intelligence = intelligence;
        this.agility = agility;
        this.luck = luck;
        this.experience = experience;
        this.level = level;
    }
}

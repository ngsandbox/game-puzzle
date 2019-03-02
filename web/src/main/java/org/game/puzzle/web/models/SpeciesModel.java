package org.game.puzzle.web.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.species.Gender;
import org.game.puzzle.core.entities.species.Human;
import org.game.puzzle.core.entities.species.Species;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Slf4j
@Getter
@Setter
@ToString
public class SpeciesModel {
    private final static String MIN_STAT = "Stat should be greater than 0";
    private final static String MAX_STAT = "Stat should be less than 10";

    private String id;

    @NotNull(message = "Gender should be specified")
    private Gender gender;

    @Min(value = 1, message = MIN_STAT)
    @Max(value = 10, message = MAX_STAT)
    @NotNull(message = "strength should be specified")
    private Integer strength;

    @Min(value = 1, message = MIN_STAT)
    @Max(value = 10, message = MAX_STAT)
    @NotNull(message = "Perception should be specified")
    private Integer perception;

    @Min(value = 1, message = MIN_STAT)
    @Max(value = 10, message = MAX_STAT)
    @NotNull(message = "Endurance should be specified")
    private Integer endurance;

    @Min(value = 1, message = MIN_STAT)
    @Max(value = 10, message = MAX_STAT)
    @NotNull(message = "Charisma should be specified")
    private Integer charisma;

    @Min(value = 1, message = MIN_STAT)
    @Max(value = 10, message = MAX_STAT)
    @NotNull(message = "Intelligence should be specified")
    private Integer intelligence;

    @Min(value = 1, message = MIN_STAT)
    @Max(value = 10, message = MAX_STAT)
    @NotNull(message = "Agility should be specified")
    private Integer agility;

    @Min(value = 1, message = MIN_STAT)
    @Max(value = 10, message = MAX_STAT)
    @NotNull(message = "Luck should be specified")
    private Integer luck;

    public Human convert(String login) {
        log.debug("Convert model {} {}", login, this);
        Characteristic characteristic = Characteristic.builder()
                .strength(strength)
                .perception(perception)
                .endurance(endurance)
                .charisma(charisma)
                .intelligence(intelligence)
                .agility(agility)
                .luck(luck)
                .build();
        return new Human(id, login, characteristic);
    }
}

package org.game.puzzle.web.models;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.species.Gender;
import org.game.puzzle.core.entities.species.Human;
import org.game.puzzle.core.entities.species.Species;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.game.puzzle.core.utils.CalcUtils.percent;

@Slf4j
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class SpeciesStats {
    private final static String MIN_STAT = "Stat should be greater than 0";
    private final static String MAX_STAT = "Stat should be less than 10";

    private Integer pctStrength;
    private Integer pctPerception;
    private Integer pctEndurance;
    private Integer pctLuck;
    private Integer pctAgility;
    private Integer pctIntelligence;
    private Integer pctCharisma;
    private Long minExperience;
    private Long maxExperience;
    private Long pctExperience;


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

    private Integer level = 0;

    private Long experience = 0L;

    /**
     * Maximum life points
     */
    private Integer life = 0;

    /**
     * Maximum possible steps
     */
    private Integer steps = 0;

    @Builder
    public SpeciesStats(String id,
                        Gender gender,
                        int strength,
                        int perception,
                        int endurance,
                        int charisma,
                        int intelligence,
                        int agility,
                        int luck,
                        int level,
                        long experience,
                        int life,
                        int steps) {
        this.id = id;
        this.gender = gender;
        this.strength = strength;
        this.perception = perception;
        this.endurance = endurance;
        this.charisma = charisma;
        this.intelligence = intelligence;
        this.agility = agility;
        this.luck = luck;
        this.level = level;
        this.experience = experience;
        this.pctStrength = percent(0, 10, strength);
        this.pctPerception = percent(0, 10, perception);
        this.pctEndurance = percent(0, 10, endurance);
        this.pctCharisma = percent(0, 10, charisma);
        this.pctIntelligence = percent(0, 10, intelligence);
        this.pctAgility = percent(0, 10, agility);
        this.pctLuck = percent(0, 10, luck);
        this.minExperience = (long)(level * 1000);
        this.maxExperience = (long)((level + 1) * 1000);
        this.pctExperience = percent(minExperience, maxExperience, getExperience());
        this.life = life;
        this.steps = steps;
    }

    private static Characteristic vCharacteristic;
    private static List<Species> victims;

    /**
     * Create new Human instance from user's login with profided characteristics
     */
    public Human createHuman(String login) {
        log.debug("Convert model {} {}", login, this);
        Characteristic characteristic = convert();
        return new Human(id, login, characteristic);
    }

    public Characteristic convert() {
        return Characteristic.builder()
                .id(id)
                .gender(gender)
                .strength(strength)
                .perception(perception)
                .endurance(endurance)
                .charisma(charisma)
                .intelligence(intelligence)
                .agility(agility)
                .luck(luck)
                .experience(experience)
                .level(level)
                .build();
    }
}

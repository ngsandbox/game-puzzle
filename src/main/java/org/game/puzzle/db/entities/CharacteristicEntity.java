package org.game.puzzle.db.entities;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.species.Gender;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @see org.game.puzzle.core.entities.Characteristic
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "characteristicId")
@Entity
public class CharacteristicEntity {
    @Id
    @Column(name = "characteristicId")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String characteristicId;

    private Gender gender;

    /**
     * Current species experience
     */
    @Column(name = "experience", nullable = false)
    private long experience;

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "strength", nullable = false)
    private int strength;

    @Column(name = "perception", nullable = false)
    private int perception;

    @Column(name = "endurance", nullable = false)
    private int endurance;

    @Column(name = "charisma", nullable = false)
    private int charisma;

    @Column(name = "intelligence", nullable = false)
    private int intelligence;

    @Column(name = "agility", nullable = false)
    private int agility;

    @Column(name = "luck", nullable = false)
    private int luck;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SpeciesEntity species;

    public CharacteristicEntity(String characteristicId,
                                Gender gender,
                                long experience,
                                int level,
                                int strength,
                                int perception,
                                int endurance,
                                int charisma,
                                int intelligence,
                                int agility, int luck) {
        this.characteristicId = characteristicId;
        this.gender = gender;
        this.experience = experience;
        this.level = level;
        this.strength = strength;
        this.perception = perception;
        this.endurance = endurance;
        this.charisma = charisma;
        this.intelligence = intelligence;
        this.agility = agility;
        this.luck = luck;
    }
}

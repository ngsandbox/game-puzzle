package org.game.puzzle.core.configs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@ToString
@ConfigurationProperties(prefix = "org.game.puzzle")
@Validated
public class GameProperties {

    /**
     * Minimum experience for win
     */
    private int minExperience = 10;

    /**
     * Experience coefficient for calalculation, according to level of the enemy  and S.P.E.C.I.A.L characteristics
     */
    private double experienceCoeff = 0.1;

    /**
     * Damage coefficient for calalculation, according to level the level of the species and S.P.E.C.I.A.L characteristics
     */
    private double damageCoeff = 0.1;

    /**
     * Protection coefficient for calalculation, according to level the level of the species and S.P.E.C.I.A.L characteristics
     */
    private double protectionCoeff = 0.1;

    /**
     * Minimum luck level when species can: find additional items on the buttle field, crit chance or avoid an atacker's hit
     */
    private int minLuck = 8;

    /**
     * The amount of rows and columns of the fighting arena
     */
    private byte arenaSize = 15;

}

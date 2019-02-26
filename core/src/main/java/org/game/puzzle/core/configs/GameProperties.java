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
     * Minimum experience for win, when your victim was an absolute weak
     */
    private int minExperience = 10;

    /**
     * Minimum coefficient for
     */
    private double minExperienceCoeff = 0.1;

}

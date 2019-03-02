package org.game.puzzle.core.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.game.puzzle.core.entities.grid.Arena;
import org.game.puzzle.core.entities.grid.Coordinate;
import org.game.puzzle.web.models.SpeciesInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Component
@Scope("session")
public class FightSession implements Serializable {

    private static final long serialVersionUID = -3074807802176531154L;

    /**
     * Current user login
     */
    private String login;

    /**
     * Rendered arena
     */
    private Arena arena;

    /**
     * True if it is the user hit, false - enemy
     */
    private boolean userHit;

    /**
     * Current user position
     */
    private Coordinate userPosition;

    /**
     * User characteristics
     */
    private SpeciesInfo userInfo;
    /**
     * Curreny enemy position
     */
    private Coordinate enemyPosition;

    /**
     * Enemy characteristics
     */
    private SpeciesInfo enemyInfo;

    public FightSession copy() {
        FightSession session = new FightSession();
        session.login = login;
        session.arena = arena;
        session.userHit = userHit;
        session.userPosition = userPosition;
        session.enemyPosition = enemyPosition;
        session.userInfo = userInfo;
        session.enemyInfo = enemyInfo;
        return session;
    }
}

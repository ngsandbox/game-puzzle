package org.game.puzzle.core.dao;

import lombok.NonNull;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.species.Species;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface GameDAO {

    /**
     * Save species and return identifier
     */
    String save(Species species);

    /**
     * Find species by identifier
     */
    Optional<Species> getById(String id);

    /**
     * Find species by login
     */
    Optional<Species> getByLogin(String login);

    /**
     * Update species characteristics (level, experience, etc)
     * @return characteristic id
     */
    String updateCharacteristic(@NonNull Characteristic characteristic);

    /**
     * Remove species by login
     */
    void removeByLogin(String login);

    boolean doesRegistered(String login);

    void addVictim(String login, Species victim);
}

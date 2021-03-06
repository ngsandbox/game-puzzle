package org.game.puzzle.core.dao;

import org.game.puzzle.core.entities.species.Species;

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
     */
    void updateExperience(String characteristicId, long experience);

    /**
     * Remove species by login
     */
    void removeByLogin(String login);

    /**
     * Check login existance
     */
    boolean doesRegistered(String login);

    /**
     * Register victory for winner
     * @param login winner
     */
    void addVictim(String login, Species victim);
}

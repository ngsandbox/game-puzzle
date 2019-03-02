package org.game.puzzle.core.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.dao.GameDAO;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.species.Human;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Service
public class SpeciesService {

    private final GameDAO gameDAO;

    @Autowired
    public SpeciesService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    /**
     * Save species and return identifier
     */
    public String save(@NonNull Species species) {
        log.debug("Save species info {}", species);
        return gameDAO.save(species);
    }

    /**
     * Find species by identifier
     */
    public Species getSpeciesById(String id) {
        log.debug("Get species by id {}", id);
        Optional<Species> species = gameDAO.getById(id);
        if (species.isPresent()) {
            return species.get();
        }

        log.error("Species not found by id {}", id);
        throw new NotFoundException("Species not fount by " + id);
    }

    /**
     * Find species by login
     */
    public Species getSpeciesByLogin(String login) {
        log.debug("Get species by login{}", login);
        Optional<Species> species = gameDAO.getByLogin(login);
        if (species.isPresent()) {
            return species.get();
        }

        log.error("Species not found by login {}", login);
        throw new NotFoundException("Species not fount by " + login);
    }

    /**
     * Update species characteristics (level, experience, etc)
     *
     * @return characteristic id
     */
    public String updateCharacteristic(@NonNull Characteristic characteristic) {
        log.debug("Save characteristic {}", characteristic);
        return gameDAO.updateCharacteristic(characteristic);
    }

    /**
     * Remove species by login
     */
    public void removeSpeciesByLogin(String login) {
        log.debug("Remove species by login {}", login);
        gameDAO.removeByLogin(login);
    }

    public boolean doesRegistered(@NonNull String login) {
        return gameDAO.doesRegistered(login);
    }

    public void addVictim(@NotNull String login, @NotNull Species victim) {
        log.debug("For winner {} add victim {} ", login, victim);
        gameDAO.addVictim(login, victim);
    }
}

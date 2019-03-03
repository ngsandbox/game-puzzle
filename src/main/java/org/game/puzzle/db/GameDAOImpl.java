package org.game.puzzle.db;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.dao.GameDAO;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.db.entities.SpeciesEntity;
import org.game.puzzle.db.factories.SpeciesFactory;
import org.game.puzzle.db.repositories.CharacteristicRepository;
import org.game.puzzle.db.repositories.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Transactional(rollbackOn = Exception.class)
@Component
public class GameDAOImpl implements GameDAO {

    private final SpeciesRepository speciesRepository;
    private final CharacteristicRepository characteristicRepository;

    @Autowired
    public GameDAOImpl(SpeciesRepository speciesRepository,
                       CharacteristicRepository characteristicRepository) {
        this.speciesRepository = speciesRepository;
        this.characteristicRepository = characteristicRepository;
    }

    @Override
    public String save(@NonNull Species species) {
        log.debug("Save species {}", species);
        SpeciesEntity speciesEntity = SpeciesFactory.from(species);
        List<SpeciesEntity> victims = species.getVictims().stream().map(SpeciesFactory::from).collect(toList());
        SpeciesEntity result = speciesRepository.save(speciesEntity);
        victims.forEach(v -> v.setWinner(result));
        speciesRepository.saveAll(victims);
        log.trace("Species was saved {}", result);
        return result.getSpeciesId();
    }

    @Override
    public Optional<Species> getById(String id) {
        log.debug("FInd species by indetifier {}", id);
        return speciesRepository.findById(id)
                .map(s -> SpeciesFactory.convert(s, false));
    }

    @Override
    public Optional<Species> getByLogin(String login) {
        log.debug("Find species by login {}", login);
        return speciesRepository.findByLoginIgnoreCase(login)
                .map(s -> SpeciesFactory.convert(s, true));
    }

    @Override
    public void updateExperience(@NotNull String characteristicId, long experience) {
        log.debug("Update {} {}", characteristicId, experience);
        characteristicRepository.findById(characteristicId)
                .map(c -> {
                    c.setExperience(experience);
                    characteristicRepository.save(c);
                    return c;
                }).orElseThrow(() -> new DbException("Characteristic not found " + characteristicId));
    }

    @Override
    public void removeByLogin(String login) {
        log.info("Remove species by login {}", login);
        speciesRepository.removeByLogin(login);
    }

    @Override
    public boolean doesRegistered(@NonNull String login) {
        log.info("Check existance by login {}", login);
        return speciesRepository.existsByLogin(login);
    }

    @Override
    public void addVictim(@NotNull String login, @NotNull Species victim) {
        log.debug("Add to winner {} new victim {}", login, victim);
        SpeciesEntity winner = speciesRepository
                .findByLoginIgnoreCase(login)
                .orElseThrow(() -> new DbException("Winner not found by login " + login));
        SpeciesEntity victimEntity = SpeciesFactory.from(victim);
        victimEntity.setWinner(winner);
        speciesRepository.save(victimEntity);
    }
}

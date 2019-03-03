package org.game.puzzle.core.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.dao.GameDAO;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.species.Gender;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.entities.species.SpeciesType;
import org.game.puzzle.core.exceptions.NotFoundException;
import org.game.puzzle.core.utils.Generator;
import org.game.puzzle.db.DbException;
import org.game.puzzle.db.factories.SpeciesFactory;
import org.game.puzzle.web.models.SpeciesInfo;
import org.game.puzzle.web.models.SpeciesStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SpeciesService {

    private final GameDAO gameDAO;
    private final CharacteristicService characteristicService;
    private final Generator generator;

    @Autowired
    public SpeciesService(GameDAO gameDAO,
                          CharacteristicService characteristicService,
                          Generator generator) {
        this.gameDAO = gameDAO;
        this.characteristicService = characteristicService;
        this.generator = generator;
    }

    /**
     * Create species according to max level
     */
    public Species createSpecies(int maxLevel) {
        int num = (int) generator.nextDouble(0, SpeciesType.values().length);
        log.debug("Create species by number {}. {}", num, SpeciesType.values());
        SpeciesType type = SpeciesType.values()[num];
        Characteristic.CharacteristicBuilder builder = Characteristic.builder();
        builder.gender(Gender.NONE);
        builder.strength((int) generator.nextDouble(5, 10));
        builder.perception((int) generator.nextDouble(5, 10));
        builder.endurance((int) generator.nextDouble(5, 10));
        builder.charisma((int) generator.nextDouble(5, 10));
        builder.intelligence((int) generator.nextDouble(5, 10));
        builder.agility((int) generator.nextDouble(5, 10));
        builder.luck((int) generator.nextDouble(5, 10));
        builder.experience(maxLevel * 100);
        builder.level((maxLevel > 5) ? (int) generator.nextDouble(maxLevel - 2, maxLevel + 1) : maxLevel);
        Characteristic characteristic = builder.build();
        Species species = SpeciesFactory.from("Enemy-" + UUID.randomUUID().toString(), type, characteristic);
        String speciesId = gameDAO.save(species);
        return gameDAO.getById(speciesId).orElseThrow(() -> new DbException("Created species not found by id " + speciesId));
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
    public void updateExperience(@NonNull String characteristicId, long experience) {
        log.debug("Save for characteristic {} experience {}", characteristicId, experience);
        gameDAO.updateExperience(characteristicId, experience);
    }

    /**
     * Remove species by login
     */
    public void removeSpeciesByLogin(String login) {
        log.debug("Remove species by login {}", login);
        gameDAO.removeByLogin(login);
    }

    /**
     * Check that login is already registered
     */
    public boolean doesRegistered(@NonNull String login) {
        return gameDAO.doesRegistered(login);
    }

    /**
     * Register victim for provided login
     */
    public void addVictim(@NotNull String login, @NotNull Species victim) {
        log.debug("For winner {} add victim {} ", login, victim);
        gameDAO.addVictim(login, victim);
    }


    /**
     * Convert and calculate species parameters and characteristics
     */
    public SpeciesInfo calcSpeciesInfo(Species species) {
        log.debug("Convert to model {}", species);
        if (species == null) {
            return null;
        }

        return new SpeciesInfo(
                species.getId(),
                species.getLogin(),
                calcStats(species),
                species.getType(),
                species.getDamage(),
                species.getProtection(),
                species.getVictims().stream()
                        .map(this::calcSpeciesInfo)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Convert and calculate species stats
     */
    public SpeciesStats calcStats(Species species) {
        if (species == null || species.getCharacteristic() == null) {
            return null;
        }

        Characteristic characteristic = species.getCharacteristic();
        return SpeciesStats.builder()
                .id(characteristic.getId())
                .experience(characteristic.getExperience())
                .gender(characteristic.getGender())
                .strength(characteristic.getStrength())
                .perception(characteristic.getPerception())
                .endurance(characteristic.getEndurance())
                .charisma(characteristic.getCharisma())
                .intelligence(characteristic.getIntelligence())
                .agility(characteristic.getAgility())
                .luck(characteristic.getLuck())
                .level(characteristic.getLevel())
                .life(characteristicService.calcLife(species))
                .steps(characteristicService.calcSteps(species))
                .build();
    }
}

package org.game.puzzle.db;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.dao.GameDAO;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.db.entities.CharacteristicEntity;
import org.game.puzzle.db.entities.SpeciesEntity;
import org.game.puzzle.db.factories.CharacteristicFactory;
import org.game.puzzle.db.factories.SpeciesFactory;
import org.game.puzzle.db.repositories.CharacteristicRepository;
import org.game.puzzle.db.repositories.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

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
    public String save(@NonNull Species human) {
        log.debug("Save species {}", human);
        SpeciesEntity result = speciesRepository.save(SpeciesFactory.from(human));
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
    public String updateCharacteristic(@NonNull Characteristic characteristic) {
        log.debug("Update species characteristic {}", characteristic);
        CharacteristicEntity entity = CharacteristicFactory.from(characteristic);
        CharacteristicEntity save = characteristicRepository.save(entity);
        return save.getCharacteristicId();
    }

    @Override
    public void removeByLogin(String id) {
        log.info("Remove species by id {}", id);
        speciesRepository.deleteById(id);
    }

    @Override
    public boolean doesRegistered(@NonNull String login) {
        log.info("Check existance by login {}", login);
        return speciesRepository.existsByLogin(login);
    }
}

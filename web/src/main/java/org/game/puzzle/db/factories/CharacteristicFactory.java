package org.game.puzzle.db.factories;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.db.entities.CharacteristicEntity;

@Slf4j
public class CharacteristicFactory {
    public static Characteristic to(CharacteristicEntity entity) {
        log.debug("Convert to domain object {}", entity);
        if (entity == null) {
            return null;
        }

        return new Characteristic(
                entity.getCharacteristicId(),
                entity.getGender(),
                entity.getStrength(),
                entity.getPerception(),
                entity.getEndurance(),
                entity.getCharisma(),
                entity.getIntelligence(),
                entity.getAgility(),
                entity.getLuck(),
                entity.getExperience(),
                entity.getLevel()
        );
    }

    public static CharacteristicEntity from(Characteristic characteristic) {
        log.debug("Convert from domain object {}", characteristic);
        if (characteristic == null) {
            return null;
        }

        return new CharacteristicEntity(
                characteristic.getId(),
                characteristic.getGender(),
                characteristic.getExperience(),
                characteristic.getLevel(),
                characteristic.getStrength(),
                characteristic.getPerception(),
                characteristic.getEndurance(),
                characteristic.getCharisma(),
                characteristic.getIntelligence(),
                characteristic.getAgility(),
                characteristic.getLuck()
        );
    }
}

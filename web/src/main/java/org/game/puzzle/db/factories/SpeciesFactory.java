package org.game.puzzle.db.factories;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.species.Scorpio;
import org.game.puzzle.core.entities.species.Gecko;
import org.game.puzzle.core.entities.species.Human;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.db.DbException;
import org.game.puzzle.db.entities.SpeciesEntity;

import java.util.Collections;

import static java.util.stream.Collectors.toList;

@Slf4j
public class SpeciesFactory {


    public static SpeciesEntity from(Species species) {
        if (species == null) {
            return null;
        }

        return new SpeciesEntity(species.getId(),
                species.getLogin(),
                species.getType(),
                CharacteristicFactory.from(species.getCharacteristic()));
        //species.getVictims().stream().map(SpeciesFactory::from).collect(toList()));
    }

    public static Species convert(SpeciesEntity entity, boolean full) {
        if (entity == null) {
            return null;
        }

        switch (entity.getType()) {
            case SCORPIO:
                return getScorpio(entity);
            case GECKO:
                return getGecko(entity);
            case HUMAN:
                return getHuman(entity, full);
            default:
                log.error("Species converter is not implemented for {}", entity);
                throw new DbException("Species type is not implemented: " + entity.getType());
        }
    }

    private static Scorpio getScorpio(SpeciesEntity entity) {
        return new Scorpio(entity.getSpeciesId(),
                entity.getLogin(),
                CharacteristicFactory.to(entity.getCharacteristic()));
    }

    private static Gecko getGecko(SpeciesEntity entity) {
        return new Gecko(entity.getSpeciesId(),
                entity.getLogin(),
                CharacteristicFactory.to(entity.getCharacteristic()));
    }

    private static Human getHuman(SpeciesEntity entity, boolean full) {
        return new Human(entity.getSpeciesId(),
                entity.getLogin(),
                CharacteristicFactory.to(entity.getCharacteristic()),
                full
                        ? entity.getVictims().stream().map(v -> convert(v, false)).collect(toList())
                        : Collections.emptyList());
    }
}

package org.game.puzzle.db.factories;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.species.*;
import org.game.puzzle.db.DbException;
import org.game.puzzle.db.entities.SpeciesEntity;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

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
    }

    /**
     * Convert from database entity to domain object
     */
    public static Species convert(SpeciesEntity entity, boolean full) {
        if (entity == null) {
            return null;
        }

        String speciesId = entity.getSpeciesId();
        String login = entity.getLogin();
        SpeciesType type = entity.getType();
        Characteristic characteristic = CharacteristicFactory.to(entity.getCharacteristic());
        List<Species> victims = full
                ? entity.getVictims().stream().map(v -> SpeciesFactory.convert(v, false)).collect(toList())
                : Collections.emptyList();
        return convert(speciesId, login, type, characteristic, victims);
    }

    public static Species convert(String id,
                                  @NotNull String login,
                                  @NotNull SpeciesType type,
                                  @NotNull Characteristic characteristic,
                                  @NotNull List<Species> victims) {

        switch (type) {
            case SCORPIO:
                return getScorpio(id, login, characteristic);
            case GECKO:
                return getGecko(id, login, characteristic);
            case HUMAN:
                return getHuman(id, login, characteristic, victims);
            default:
                log.error("Species converter is not implemented for {}", type);
                throw new DbException("Species type is not implemented: " + type);
        }
    }

    public static Species from(@NotNull String login, @NotNull SpeciesType type, @NotNull Characteristic characteristic) {
        return convert(null, login, type, characteristic, Collections.emptyList());
    }


    private static Scorpio getScorpio(String id, String login, Characteristic characteristic) {
        return new Scorpio(id, login, characteristic);
    }

    private static Gecko getGecko(String id, String login, Characteristic characteristic) {
        return new Gecko(id, login, characteristic);
    }

    private static Human getHuman(String id, String login, Characteristic characteristic, List<Species> victims) {
        return new Human(id, login, characteristic, victims);
    }
}

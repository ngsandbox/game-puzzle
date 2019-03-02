package org.game.puzzle.web.models;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.entities.species.SpeciesType;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class SpeciesInfo {
    private final String id;
    private final String login;
    private final SpeciesStats stats;
    private final SpeciesType type;
    private final List<SpeciesInfo> victims;


    private SpeciesInfo(@NotNull String id,
                        @NonNull String login,
                        @NotNull SpeciesStats stats,
                        @NotNull SpeciesType type,
                        @NotNull List<SpeciesInfo> victims) {
        this.id = id;
        this.login = login;
        this.stats = stats;
        this.type = type;
        this.victims = victims;
    }

    public static SpeciesInfo of(Species species) {
        log.debug("Convert to model {}", species);
        if (species == null) {
            return null;
        }

        return new SpeciesInfo(
                species.getId(),
                species.getLogin(),
                SpeciesStats.of(species.getCharacteristic()),
                species.getType(),
                species.getVictims().stream()
                        .map(SpeciesInfo::of)
                        .collect(Collectors.toList())
        );
    }
}

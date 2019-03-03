package org.game.puzzle.web.models;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.Range;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.entities.species.SpeciesType;
import org.game.puzzle.db.factories.SpeciesFactory;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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
    private final Range damage;
    private final Range protection;
    private final List<SpeciesInfo> victims;


    public SpeciesInfo(@NotNull String id,
                       @NonNull String login,
                       @NotNull SpeciesStats stats,
                       @NotNull SpeciesType type,
                       @NotNull Range damage,
                       @NotNull Range protection,
                       @NotNull List<SpeciesInfo> victims) {
        this.id = id;
        this.login = login;
        this.stats = stats;
        this.type = type;
        this.damage = damage;
        this.protection = protection;
        this.victims = victims;
    }

    /**
     * convert from dto to domain object
     */
    public Species convert() {
        return SpeciesFactory.convert(id, login, type
                , stats.convert(),
                victims.stream().map(SpeciesInfo::convert).collect(toList()));
    }
}

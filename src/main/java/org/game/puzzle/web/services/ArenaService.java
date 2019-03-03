package org.game.puzzle.web.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.configs.GameProperties;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.FightSession;
import org.game.puzzle.core.entities.grid.Arena;
import org.game.puzzle.core.entities.grid.Cell;
import org.game.puzzle.core.entities.grid.Coordinate;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.services.CharacteristicService;
import org.game.puzzle.core.services.SpeciesService;
import org.game.puzzle.core.subscription.SubscriptionService;
import org.game.puzzle.core.utils.Generator;
import org.game.puzzle.web.models.MoveModel;
import org.game.puzzle.web.models.Position;
import org.game.puzzle.web.models.SpeciesInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class ArenaService {

    private final CharacteristicService characteristicService;
    private final GameProperties properties;
    private final SubscriptionService subscriptionService;
    private final SpeciesService speciesService;
    private final Generator generator;

    @Autowired
    public ArenaService(CharacteristicService characteristicService,
                        GameProperties properties,
                        SubscriptionService subscriptionService,
                        SpeciesService speciesService, Generator generator) {
        this.characteristicService = characteristicService;
        this.properties = properties;
        this.subscriptionService = subscriptionService;
        this.speciesService = speciesService;
        this.generator = generator;
    }

    /**
     * Create new arena
     */
    public Arena createArena() {
        log.debug("Create new arena {}", properties.getArenaSize());
        return new Arena(properties.getArenaSize());
    }

    public MoveModel makeMove(@NonNull Position position, @NotNull FightSession fightSession) {
        log.debug("Calculate move to position {}", position);
        boolean userHit = fightSession.isUserHit();
        Coordinate toPosition = position.convert();
        Coordinate fromAtacker = userHit ? fightSession.getUserPosition() : fightSession.getEnemyPosition();
        if (fromAtacker.equals(toPosition)) {
            log.debug("Skip on select the same atacker's location");
            new MoveModel(userHit, 0, Collections.emptyList(), false, false);
        }

        Coordinate defenderPosition = userHit ? fightSession.getEnemyPosition() : fightSession.getUserPosition();

        SpeciesInfo atacker = userHit ? fightSession.getUserInfo() : fightSession.getEnemyInfo();
        SpeciesInfo defender = userHit ? fightSession.getEnemyInfo() : fightSession.getUserInfo();

        Integer steps = atacker.getStats().getSteps();
        List<Coordinate> route = findRoute(fromAtacker, toPosition, steps,
                new TreeSet<>(asList(fightSession.getEnemyPosition(), fightSession.getUserPosition())));
        int restSteps = steps - route.size();
        int damage = 0;
        boolean finish = false;
        Species atackerSpecies = atacker.convert();
        Species defenderSpecies = defender.convert();
        if (toPosition.equals(defenderPosition) && restSteps > 0) {
            log.debug("Calculate additional damage for the rest steps {}", restSteps);
            for (int i = 0; i < restSteps; i++) {
                damage += characteristicService.calcDamage(atackerSpecies, defenderSpecies);
            }

            Integer life = defender.getStats().getLife();
            defender.getStats().setLife(life - damage);

            finish = defender.getStats().getLife() <= 0;
        }

        if (route.size() > 0) {
            if (userHit) {
                fightSession.setUserPosition(route.get(route.size() - 1));
            } else {
                fightSession.setEnemyPosition(route.get(route.size() - 1));
            }
        }


        if (finish) {
            long experience = characteristicService.calcExperience(atackerSpecies, Collections.singletonList(defenderSpecies));
            log.debug("Update winner experience {}", experience);
            speciesService.updateExperience(atackerSpecies.getCharacteristic().getId(), experience);
            speciesService.addVictim(atacker.getLogin(), defenderSpecies);
        }

        fightSession.setUserHit(!userHit);

        log.debug("Result damage {}, is finished {} and route {}", damage, route, finish);
        return new MoveModel(userHit, damage, route, finish, true);
    }

    /**
     * Find path from the source to the destination coordinates
     */
    public List<Coordinate> findRoute(@NonNull Coordinate source,
                                      @NonNull Coordinate destination,
                                      int steps,
                                      @NotNull Set<Coordinate> exclusions) {
        Cell sourceCell = Cell.newCell(source, new ArrayList<>());
        Optional<Cell> route = getPath(sourceCell, destination);
        log.debug("The result route {}", route);
        List<Coordinate> coordinates = new ArrayList<>();
        if (route.isPresent()) {
            coordinates = route.get().getRoute();
            coordinates.add(route.get().getCoordinate());
            // exclude source coordinate and
            coordinates = coordinates.stream().filter(s -> !s.equals(source) && !exclusions.contains(s)).collect(toList());
            if (coordinates.size() > steps) {
                log.debug("Truncate the route to the available steps {}", steps);
                return coordinates.stream().limit(steps).collect(toList());
            }
        }

        return coordinates;
    }

    private int nextPosition(int currAxis, int destAxis) {
        return currAxis > destAxis ? currAxis - 1 :
                currAxis < destAxis ? currAxis + 1 : destAxis;
    }

    private Optional<Cell> getPath(Cell current,
                                   Coordinate destination) {
        if (current.getCoordinate().equals(destination)) {
            log.debug("Found destination with route {}", current);
            return Optional.of(current);
        }

        List<Coordinate> newRoute = new ArrayList<>(current.getRoute());
        newRoute.add(current.getCoordinate());

        int currRow = current.getCoordinate().getRow();
        int currCol = current.getCoordinate().getCol();
        int nextRow = nextPosition(currRow, destination.getRow());
        int nextCol = nextPosition(currCol, destination.getCol());

        return getPath(Cell.newCell(Coordinate.of(nextRow, nextCol), newRoute), destination);
    }

}

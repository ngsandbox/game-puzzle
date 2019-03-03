package org.game.puzzle.web.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.configs.GameProperties;
import org.game.puzzle.core.entities.FightSession;
import org.game.puzzle.core.entities.grid.Arena;
import org.game.puzzle.core.entities.grid.Cell;
import org.game.puzzle.core.entities.grid.Coordinate;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.services.CharacteristicService;
import org.game.puzzle.core.services.SpeciesService;
import org.game.puzzle.web.models.MoveModel;
import org.game.puzzle.web.models.Position;
import org.game.puzzle.web.models.SpeciesInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class ArenaService {

    private final CharacteristicService characteristicService;
    private final GameProperties properties;
    private final SpeciesService speciesService;

    @Autowired
    public ArenaService(CharacteristicService characteristicService,
                        GameProperties properties,
                        SpeciesService speciesService) {
        this.characteristicService = characteristicService;
        this.properties = properties;
        this.speciesService = speciesService;
    }

    public FightSession createFightSession(@NotNull Species human) {
        log.debug("Create new fight session for login", human.getLogin());
        Species enemy = speciesService.createSpecies(human.getCharacteristic().getLevel());
        FightSession fightSession = new FightSession();
        fightSession.setLogin(human.getLogin());
        fightSession.setUserHit(true);
        fightSession.setArena(createArena());
        fightSession.setUserInfo(speciesService.calcSpeciesInfo(human));
        fightSession.setEnemyInfo(speciesService.calcSpeciesInfo(enemy));
        fightSession.setUserPosition(Coordinate.of(1, 1));
        fightSession.setEnemyPosition(Coordinate.of(fightSession.getArena().getSize() - 2, fightSession.getArena().getSize() - 2));
        return fightSession;
    }

    /**
     * Create new arena
     */
    public Arena createArena() {
        log.debug("Create new arena {}", properties.getArenaSize());
        return new Arena(properties.getArenaSize());
    }

    public MoveModel makeMove(@NonNull Coordinate toCoordinate, @NotNull FightSession fightSession) {
        log.debug("Calculate move to coordinates {}", toCoordinate);
        boolean userHit = fightSession.isUserHit();
        Coordinate fromAtacker = userHit ? fightSession.getUserPosition() : fightSession.getEnemyPosition();
        if (fromAtacker.equals(toCoordinate)) {
            log.debug("Skip on select the same attacker's location");
            return new MoveModel(userHit, 0, Collections.emptyList(), false, false);
        }

        Coordinate defenderPosition = userHit ? fightSession.getEnemyPosition() : fightSession.getUserPosition();

        SpeciesInfo attacker = userHit ? fightSession.getUserInfo() : fightSession.getEnemyInfo();
        SpeciesInfo defender = userHit ? fightSession.getEnemyInfo() : fightSession.getUserInfo();

        Integer steps = attacker.getStats().getSteps();
        List<Coordinate> route = findRoute(fromAtacker, toCoordinate, steps,
                new TreeSet<>(asList(fightSession.getEnemyPosition(), fightSession.getUserPosition())));
        int restSteps = steps - route.size();
        int damage = 0;
        boolean finish = false;
        Species attackerSpecies = attacker.convert();
        Species defenderSpecies = defender.convert();
        if (toCoordinate.equals(defenderPosition) && restSteps > 0) {
            log.debug("Calculate additional damage for the rest steps {}", restSteps);
            for (int i = 0; i < restSteps; i++) {
                damage += characteristicService.calcDamage(attackerSpecies, defenderSpecies);
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
            long experience = characteristicService.calcExperience(attackerSpecies, Collections.singletonList(defenderSpecies));
            log.debug("Update winner experience {}", experience);
            speciesService.updateExperience(attackerSpecies.getCharacteristic().getId(), experience);
            speciesService.addVictim(attacker.getLogin(), defenderSpecies);
        }

        fightSession.setUserHit(!userHit);

        List<Position> coordinates = route.stream().map(Position::convert).collect(toList());
        log.debug("Result damage {}, is finished {} and route {}", damage, coordinates, finish);
        return new MoveModel(userHit, damage, coordinates, finish, true);
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

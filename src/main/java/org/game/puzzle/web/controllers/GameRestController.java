package org.game.puzzle.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.FightSession;
import org.game.puzzle.core.entities.grid.Coordinate;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.exceptions.GameException;
import org.game.puzzle.core.services.ArenaService;
import org.game.puzzle.core.services.CharacteristicService;
import org.game.puzzle.core.services.SpeciesService;
import org.game.puzzle.web.models.Position;
import org.game.puzzle.web.models.SpeciesInfo;
import org.game.puzzle.web.models.SpeciesStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/")
public class GameRestController {

    private final SpeciesService speciesService;
    private final ArenaService arenaService;

    private final CharacteristicService characteristicService;

    private final SimpMessagingTemplate webSocket;

    @Autowired
    public GameRestController(SpeciesService speciesService,
                              ArenaService arenaService,
                              CharacteristicService characteristicService,
                              SimpMessagingTemplate webSocket) {
        this.speciesService = speciesService;
        this.arenaService = arenaService;
        this.characteristicService = characteristicService;
        this.webSocket = webSocket;
    }

    @GetMapping("/species/{login}")
    public Species getSpeciesByLogin(@PathVariable String login) {
        log.debug("Find species by login {}", login);
        return speciesService.getSpeciesByLogin(login);
    }

    private String getLogin() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/species")
    public void saveHumanSpecies(@Valid @RequestBody SpeciesStats species) {
        log.debug("Save species model {}", species);
        speciesService.save(species.convert(getLogin()));
    }

    private FightSession getFightSession(HttpSession session) {
        FightSession fightSession = (FightSession) session.getAttribute("userSession");
        if (fightSession == null) {
            log.error("Fighting session is empty");
            throw new GameException("Fighting session is not initialized");
        }

        return fightSession;
    }

    @PostMapping("/goto")
    public List<Coordinate> goToNextPosition(@Valid @RequestBody Position position,
                                             HttpSession session) {
        log.debug("Go to the next position {}", position);
        FightSession fightSession = getFightSession(session);
        Coordinate from = fightSession.isUserHit() ? fightSession.getUserPosition() : fightSession.getEnemyPosition();
        SpeciesInfo atacker = fightSession.isUserHit() ? fightSession.getUserInfo() : fightSession.getEnemyInfo();
        return arenaService.findRoute(from, position.convert(), atacker.getStats().getSteps());
    }
}

package org.game.puzzle.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.services.ArenaService;
import org.game.puzzle.core.services.CharacteristicService;
import org.game.puzzle.core.services.SpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/v1/")
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
}

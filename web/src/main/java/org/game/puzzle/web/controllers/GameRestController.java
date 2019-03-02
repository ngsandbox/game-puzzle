package org.game.puzzle.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.services.ArenaService;
import org.game.puzzle.core.services.CharacteristicService;
import org.game.puzzle.core.services.SpeciesService;
import org.game.puzzle.web.models.SpeciesStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    private String getLogin(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/species")
    public void saveHumanSpecies(@Valid @RequestBody SpeciesStats species) {
        log.debug("Save species model {}", species);
        speciesService.save(species.convert(getLogin()));
    }
}

package org.game.puzzle.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.FightSession;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.exceptions.GameException;
import org.game.puzzle.core.services.SpeciesService;
import org.game.puzzle.core.subscription.SubscriptionService;
import org.game.puzzle.core.subscription.Topics;
import org.game.puzzle.core.subscription.events.MessageEvent;
import org.game.puzzle.web.models.MoveModel;
import org.game.puzzle.web.models.Position;
import org.game.puzzle.web.models.SpeciesStats;
import org.game.puzzle.web.services.ArenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/")
public class GameRestController {

    private final SpeciesService speciesService;
    private final ArenaService arenaService;
    private final SubscriptionService subscriptionService;
    private final SimpMessagingTemplate webSocket;

    @Autowired
    public GameRestController(SpeciesService speciesService,
                              ArenaService arenaService,
                              SubscriptionService subscriptionService,
                              SimpMessagingTemplate webSocket) {
        this.speciesService = speciesService;
        this.arenaService = arenaService;
        this.subscriptionService = subscriptionService;
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
        speciesService.save(species.createHuman(getLogin()));
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
    public MoveModel goToNextPosition(@Valid @RequestBody Position position,
                                      HttpSession session) {
        log.debug("Go to the next position {}", position);
        FightSession fightSession = getFightSession(session);
        return arenaService.makeMove(position.convert(), fightSession);
    }

    @GetMapping(value = "/listen", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void listenStrory() {
        log.debug("Listen to messages events ");
        subscriptionService.subscribe(Topics.MESSAGE_TOPIC)
                .filter(s -> s instanceof MessageEvent)
                .map(s -> (MessageEvent) s)
                .doOnNext(this::emitMessage)
                .subscribe();
    }

    private void emitMessage(MessageEvent event) {
        log.debug("Send message to subscriber's websockets {}", event);
        webSocket.convertAndSend("/topic/game", event);
    }
}

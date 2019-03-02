package org.game.puzzle.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.FightSession;
import org.game.puzzle.core.entities.grid.Coordinate;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.services.ArenaService;
import org.game.puzzle.core.services.CharacteristicService;
import org.game.puzzle.core.services.SpeciesService;
import org.game.puzzle.web.models.SpeciesInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class GameMvcController {

    private final SpeciesService speciesService;
    private final ArenaService arenaService;
    private final CharacteristicService characteristicService;

    @Autowired
    public GameMvcController(SpeciesService speciesService,
                             ArenaService arenaService,
                             CharacteristicService characteristicService) {
        this.speciesService = speciesService;
        this.arenaService = arenaService;
        this.characteristicService = characteristicService;
    }


    @GetMapping("/")
    public ModelAndView redirect() {
        return new ModelAndView("redirect:/game");
    }

    @GetMapping("/game")
    public String displayTasks(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        if (speciesService.doesRegistered(login)) {
            Species human = speciesService.getSpeciesByLogin(login);
            SpeciesInfo userInfo = speciesService.calcSpeciesInfo(human);
            Species enemy = speciesService.createSpecies(human.getCharacteristic().getLevel());

            FightSession fightSession = (FightSession) session.getAttribute("userSession");
            if (fightSession == null) {
                fightSession = new FightSession();
                fightSession.setLogin(login);
                fightSession.setUserHit(true);
                fightSession.setArena(arenaService.createArena());
                fightSession.setUserInfo(speciesService.calcSpeciesInfo(human));
                fightSession.setEnemyInfo(speciesService.calcSpeciesInfo(enemy));
                fightSession.setUserPosition(Coordinate.of(1, 1));
                fightSession.setEnemyPosition(Coordinate.of(fightSession.getArena().getSize() - 2, fightSession.getArena().getSize() - 2));
                session.setAttribute("userSession", fightSession);
            }

            model.addAttribute("fight", fightSession.copy());
            model.addAttribute("view", "fragments/stats");
            model.addAttribute("species", userInfo);
        } else {
            model.addAttribute("view", "fragments/new");
        }

        return "home";
    }
}

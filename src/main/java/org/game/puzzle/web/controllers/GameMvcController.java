package org.game.puzzle.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.FightSession;
import org.game.puzzle.core.entities.species.Species;
import org.game.puzzle.core.services.SpeciesService;
import org.game.puzzle.web.models.SpeciesInfo;
import org.game.puzzle.web.services.ArenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class GameMvcController {

    private final SpeciesService speciesService;
    private final ArenaService arenaService;

    @Autowired
    public GameMvcController(SpeciesService speciesService,
                             ArenaService arenaService) {
        this.speciesService = speciesService;
        this.arenaService = arenaService;
    }


    @GetMapping("/")
    public ModelAndView redirect() {
        return new ModelAndView("redirect:/game");
    }

    @GetMapping("/game")
    public String displayGame(Model model) {
        String login = getLogin();

        if (speciesService.doesRegistered(login)) {
            Species human = speciesService.getSpeciesByLogin(login);
            SpeciesInfo userInfo = speciesService.calcSpeciesInfo(human);
            model.addAttribute("view", "fragments/stats");
            model.addAttribute("species", userInfo);
        } else {
            model.addAttribute("view", "fragments/new");
        }

        return "home";
    }

    @GetMapping("/fight")
    public ModelAndView redirectToGame() {
        return new ModelAndView("redirect:/game");
    }

    @PostMapping("/fight")
    public String startFighting(Model model, HttpSession session) {
        Species human = speciesService.getSpeciesByLogin(getLogin());
        FightSession fightSession = arenaService.createFightSession(human);
        session.setAttribute("userSession", fightSession);
        model.addAttribute("fight", fightSession.copy());
        return "fight";
    }

    private String getLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}

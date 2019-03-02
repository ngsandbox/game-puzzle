package org.game.puzzle.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.services.SpeciesService;
import org.game.puzzle.web.models.SpeciesInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class GameMvcController {

    private final SpeciesService speciesService;

    public GameMvcController(SpeciesService speciesService) {
        this.speciesService = speciesService;
    }


    @GetMapping("/")
    public ModelAndView redirect() {
        return new ModelAndView("redirect:/game");
    }

    @GetMapping("/game")
    public String displayTasks(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        if (speciesService.doesRegistered(login)) {
            model.addAttribute("view", "fragments/stats");
            model.addAttribute("species", SpeciesInfo.of(speciesService.getSpeciesByLogin(login)));
        } else {
            model.addAttribute("view", "fragments/new");
        }

        return "home";
    }
}

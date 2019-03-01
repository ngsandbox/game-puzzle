package org.game.puzzle.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.services.SpeciesService;
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
//        model.addAttribute("tasks", taskInfos);
//        model.addAttribute("strategies", strategies);
//        model.addAttribute("schedulers", Schedulers.SCHEDULERS);
        return "home";
    }
}

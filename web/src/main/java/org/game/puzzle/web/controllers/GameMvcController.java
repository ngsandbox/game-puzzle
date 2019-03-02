package org.game.puzzle.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.services.SpeciesService;
import org.game.puzzle.web.models.SpeciesModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
            model.addAttribute("isNew", false);
            model.addAttribute("species", speciesService.getSpeciesByLogin(login));
        } else {
            model.addAttribute("isNew", true);
        }

        return "home";
    }
}

package com.example.sdproject.controller;

import com.example.sdproject.data.dto.GameDTO;
import com.example.sdproject.data.dto.UserDTO;
import com.example.sdproject.data.entity.Event;
import com.example.sdproject.data.entity.Game;
import com.example.sdproject.data.exception.PasswordMismatchException;
import com.example.sdproject.data.exception.UserAlreadyExistException;
import com.example.sdproject.data.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

@Controller
public class DataController {
    @Autowired
    private UserService userService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private GameService gameService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private EventService eventService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    public String index() {
        return "index";
    }

    /**
     *
     * @param m
     * @return
     */
    @GetMapping("teams")
    private String listTeams(Model m) {
        m.addAttribute("teams", teamService.getAllTeams());
        return "/lists/teams";
    }

    @GetMapping("games")
    private String listGames(Model m) {
        m.addAttribute("games", gameService.getAllGamesSortByStarts());
        return "/lists/games";
    }

    @GetMapping("game")
    private String showGame(@RequestParam("id") Integer id, Model m) {
        Optional<Game> game = gameService.getGame(id);
        if (game.isEmpty()) {
            return "redirect:/404";
        }

        GameDTO g = game.get().toDTO();
        g.getEvents().sort(Comparator.comparing(Event::getStamp).thenComparing(Event::getId));
        m.addAttribute("game", g);
        m.addAttribute("started", !g.getStarts().after(new Date()));
        m.addAttribute("ended", game.get().getWinner() != null || game.get().isTied());
        return "/gameDetails";
    }

    @GetMapping("event/new")
    private String newEvent(@RequestParam("id") Integer gameId, Model m) {
        Event event = new Event();
        Optional<Game> game = gameService.getGame(gameId);
        if (game.isEmpty()) {
            return "redirect:/404";
        }
        Game g = game.get();
        event.setGame(g);
        m.addAttribute("players", g.getPlayers());
        m.addAttribute("event", event);
        m.addAttribute("dateMin", g.getStarts());
        return "/forms/event";
    }

    @PostMapping("event/save")
    private String saveEvent(@ModelAttribute @Valid Event e, RedirectAttributes redirectAttributes) {
        Integer gameId = e.getGame().getId();
        redirectAttributes.addAttribute("id", gameId);
        try {
            this.eventService.addEvent(e);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/game";
    }

    @GetMapping("players")
    private String listPlayers(Model m) {
        m.addAttribute("players", playerService.getAllPlayers());
        return "/lists/players";
    }

    @GetMapping("register")
    private String register(Model m) {
        m.addAttribute("formTitle", "Register");
        m.addAttribute("user", new UserDTO());
        return "/forms/user";
    }

    /**
     * Register a user
     * @param u - user to register
     * @param r - atributes
     * @return - either a redirect to the register page if there is an error or towards the index if correctly made
     */
    @PostMapping("saveUser")
    private String saveUser(@ModelAttribute @Valid UserDTO u, RedirectAttributes r) {
        try {
            userService.addUser(u);
        } catch (UserAlreadyExistException | PasswordMismatchException e) {
            r.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
        return "redirect:/";
    }

    /**
     * Display best scorer and teams statistics
     * @param m - the object for statistical analisys
     * @return stats request
     */
    @GetMapping("stats")
    private String stats(Model m) {
        m.addAttribute("teams", teamService.getAllTeamsSorted());
        m.addAttribute("bestScorer", playerService.getBestScorer());
        return "/lists/stats";
    }
}

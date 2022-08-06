package com.example.sdproject.controller;

import com.example.sdproject.data.dto.GameDTO;
import com.example.sdproject.data.dto.UserDTO;
import com.example.sdproject.data.entity.Game;
import com.example.sdproject.data.entity.Player;
import com.example.sdproject.data.entity.Team;
import com.example.sdproject.data.exception.PlayerNotFoundException;
import com.example.sdproject.data.exception.UserNotFoundException;
import com.example.sdproject.data.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(path = "admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private GameService gameService;
    @Autowired
    private LeagueService leagueService;

    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("")
    private String index() {
        return "admin/index";
    }

    @GetMapping("team")
    private String editTeam(@RequestParam(name="id") Optional<Integer> id, Model m) {
        m.addAttribute("team", id.isPresent() ? teamService.getTeam(id.get()) : new Team());
        return "/forms/team";
    }

    @PostMapping("saveTeam")
    private String saveTeam(@ModelAttribute Team t) {
        if (t.getImage().equals("")) t.setImage(null);
        teamService.addTeam(t);
        return "redirect:/teams";
    }

    @GetMapping("delete/team")
    private String deleteTeam(@RequestParam("id") Integer id, RedirectAttributes r) {
        if (teamService.deleteTeam(id))  {
            r.addFlashAttribute("success", "Team deleted");
        } else {
            r.addFlashAttribute("error", "Team not found");
        }
        return "redirect:/teams";
    }

    @GetMapping("delete/team/all")
    private String deleteAllTeams(RedirectAttributes r) {
        teamService.deleteAll();
        r.addFlashAttribute("success", "Entities deleted");
        return "redirect:/teams";
    }

    @GetMapping("users")
    private String listUsers(Model m) {
        m.addAttribute("users", userService.getAllUsers());
        return "/lists/users";
    }

    @GetMapping("user")
    private String editUser(@RequestParam("id") Optional<Integer> id, Model m) {
        m.addAttribute("formTitle", "Edit User");
        m.addAttribute("user", id.isPresent() ? userService.getUser(id.get()) : new UserDTO());
        return "/forms/user";
    }

    @GetMapping("delete/user")
    private String deleteUser(@RequestParam("id") Integer id, RedirectAttributes r) {
        try {
            userService.deleteUser(id);
            r.addFlashAttribute("success", "User deleted");
        } catch (UserNotFoundException e) {
            r.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("delete/user/all")
    private String deleteAllUsers(RedirectAttributes r) {
        userService.deleteAll();
        r.addFlashAttribute("success", "Entities deleted");
        return "redirect:/admin/users";
    }

    @GetMapping("player")
    private String editPlayer(@RequestParam("id") Optional<Integer> id, Model m) {
        m.addAttribute("player", playerService.getPlayer(id.orElse(-1)).orElse(new Player()));
        m.addAttribute("teams", teamService.getAllTeamsSorted());
        return "/forms/player";
    }

    @PostMapping("savePlayer")
    private String savePlayer(@ModelAttribute Player p) {
        logger.info(p.toString());
        playerService.addPlayer(p);
        return "redirect:/players";
    }

    @GetMapping("delete/player")
    private String deletePlayer(@RequestParam("id") Integer id, RedirectAttributes r) {
        try {
            playerService.deletePlayer(id);
            r.addFlashAttribute("success", "Player deleted");
        } catch (PlayerNotFoundException e) {
            r.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/players";
    }

    @GetMapping("delete/player/all")
    private String deleteAllPlayers(RedirectAttributes r) {
        playerService.deleteAll();
        r.addFlashAttribute("success", "Entities deleted");
        return "redirect:/players";
    }

    @GetMapping("game")
    private String editGame(@RequestParam("id") Optional<Integer> id, Model m) {
        Game game = gameService.getGame(id.orElse(-1)).orElse(new Game());
        GameDTO gameDTO = game.toDTO();
        m.addAttribute("game", gameDTO);
        m.addAttribute("teams", teamService.getAllTeamsSorted());
        return "/forms/game";
    }

    @PostMapping("saveGame")
    private String saveGame(@ModelAttribute @Valid GameDTO g) {
        gameService.addGame(g.toGame());
        return "redirect:/games";
    }

    @GetMapping("delete/game")
    private String deleteGame(@RequestParam("id") Integer id, RedirectAttributes r) {
        try {
            gameService.deleteGame(id);
            r.addFlashAttribute("success", "Game deleted");
        } catch (Exception e) {
            r.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/games";
    }

    @GetMapping("delete/game/all")
    private String deleteAllGames(RedirectAttributes r) {
        gameService.deleteAll();
        r.addFlashAttribute("success", "Entities deleted");
        return "redirect:/games";
    }

    @GetMapping("leagues")
    private String getLeagues(Model m) {
        m.addAttribute("leagues", leagueService.getAllLeaguesSorted());
        return "/lists/leagues";
    }

    @GetMapping("delete/league/all")
    private String deleteAllLeagues(RedirectAttributes r) {
        leagueService.deleteAll();
        r.addFlashAttribute("success", "Entities deleted");
        return "redirect:/admin/leagues";
    }
}

package com.example.sdproject.rest;

import com.example.sdproject.data.entity.Player;
import com.example.sdproject.data.entity.Team;
import com.example.sdproject.data.exception.UserAlreadyExistException;
import com.example.sdproject.data.service.PlayerService;
import com.example.sdproject.data.service.TeamService;
import com.example.sdproject.data.service.UserService;
import com.example.sdproject.data.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("rest")
public class RESTController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private PlayerService playerService;

    @GetMapping(value = "users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody List<User> getUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping(value = "players", produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody List<Player> getPlayers() {
        return this.playerService.getAllPlayers();
    }

    @PostMapping(value = "createUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, Object> createUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userService.addUser(user);
        } catch (UserAlreadyExistException e) {
            response.put("status", "error");
            response.put("error", e.getMessage());
            return response;
        }
        response.put("status", "success");
        response.put("user", user);
        return response;
    }

    @PostMapping(value = "createTeam", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, Object> createTeam(@RequestBody Team team) {
        Map<String, Object> response = new HashMap<>();
        teamService.addTeam(team);
        response.put("status", "success");
        response.put("team", team);
        return response;
    }

    @PostMapping(value = "createPlayer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, Object> createTeam(@RequestBody Player player) {
        Map<String, Object> response = new HashMap<>();
        playerService.addPlayer(player);
        response.put("status", "success");
        response.put("player", player);
        return response;
    }

    @PostMapping(value = "setTeam", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, Object> setTeam(@RequestBody SetTeam body) {
        logger.info(body.toString());
        Map<String, Object> response = new HashMap<>();
        Player p = playerService.getPlayer(body.playerId).orElse(null);
        if (p == null) {
            logger.error("No player with id %d".formatted(body.playerId));
            response.put("status", "error");
            response.put("error", "player not found");
            return response;
        }
        Team t = teamService.getTeam(body.teamId).orElse(null);
        if (t == null) {
            response.put("status", "error");
            response.put("error", "team not found");
            return response;
        }
        p.setTeam(t);
        playerService.addPlayer(p);
        response.put("status", "success");
        response.put("player", p);

        return response;
    }
}

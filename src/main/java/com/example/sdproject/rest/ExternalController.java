package com.example.sdproject.rest;

import com.example.sdproject.data.entity.League;
import com.example.sdproject.data.entity.Player;
import com.example.sdproject.data.entity.Team;
import com.example.sdproject.data.service.LeagueService;
import com.example.sdproject.data.service.PlayerService;
import com.example.sdproject.data.service.TeamService;
import com.example.sdproject.rest.mapper.LeagueMapper;
import com.example.sdproject.rest.mapper.PlayerMapper;
import com.example.sdproject.rest.mapper.TeamMapper;
import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Calendar;

@Controller
@RequestMapping(path = "external")
public class ExternalController {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TeamService teamService;
    @Autowired
    private  PlayerService playerService;
    @Autowired
    private LeagueService leagueService;
    @Autowired
    private PlayerMapper playerMapper;
    @Autowired
    private LeagueMapper leagueMapper;

    private final String season;

    public ExternalController() {
        Unirest.config()
                .defaultBaseUrl("https://v3.football.api-sports.io")
                .setDefaultHeader("x-rapidapi-key", "e950d4adeb6de87206368cec373c0165")
                .setDefaultHeader("Accept", "application/json")
                .setDefaultHeader("User-Agent", "scoresDEI");
        this.season = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }

    @GetMapping("data")
    private String getTeamsAndPlayers(@RequestParam("leagueId") Integer leagueId, RedirectAttributes r) {
        HttpResponse<JsonNode> httpResponse;
        JSONObject body;
        JSONArray response, errors;
        try {
            // Get teams
            httpResponse = Unirest.get("/teams")
                    .queryString("season", season)
                    .queryString("league", String.valueOf(leagueId))
                    .asJson();
            if (httpResponse.getStatus() != 200) // Assert status code == 200
                throw new Exception("HTTP Status Code: " + httpResponse.getStatus());

            body = httpResponse.getBody().getObject();
            logger.info(new Gson().toJson(body));

            // Assert that API didn't return errors
            try {
                errors = body.getJSONArray("errors");
                if (!errors.isEmpty()) {
                    throw new Exception("API Teams Request Error: " + new Gson().toJson(errors));
                }
            } catch (JSONException e) {
                logger.error(e.getMessage());
            }

            // Parse teams
            response = body.getJSONArray("response");
            for (int i = 0; i < response.length(); i++) { // Iterate over teams
                TeamMapper mapper = new TeamMapper(response.getJSONObject(i));
                Team t = mapper.map();
                logger.info(t.toString());
                teamService.addTeam(mapper.map());
            }

            // Get players
            httpResponse = Unirest.get("/players")
                    .queryString("season", season)
                    .queryString("league", String.valueOf(leagueId))
                    .asJson();
            if (httpResponse.getStatus() != 200) // Assert status code == 200
                throw new Exception("HTTP Status Code: " + httpResponse.getStatus());

            body = httpResponse.getBody().getObject();
            logger.info(new Gson().toJson(body));

            // Assert that API didn't return errors
            try {
                errors = body.getJSONArray("errors");
                if (!errors.isEmpty())
                    throw new Exception("API Players Request Error: " + new Gson().toJson(errors));
            } catch (JSONException e) {
                logger.error(e.getMessage());
            }

            // Parse players
            response = body.getJSONArray("response");
            for (int i = 0; i < response.length(); i++) { // Iterate over players
                Player p = playerMapper.map(response.getJSONObject(i));
                if (p == null) {
                    logger.error("Failed to parse a player");
                } else {
                    logger.info(p.toString());
                    playerService.addPlayer(p);
                }
            }

            r.addFlashAttribute("success", "Data imported successfully");
        } catch (Exception e) {
            logger.error(e.getMessage());
            r.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin";
    }

    @GetMapping("leagues")
    private String getLeagues(RedirectAttributes r) {
        if (!leagueService.isPopulated()) {
            try {
                HttpResponse<JsonNode> httpResponse = Unirest.get("/leagues")
                        .queryString("season", season)
                        .asJson();
                if (httpResponse.getStatus() == 200) {
                    JSONArray errors = httpResponse.getBody().getObject().getJSONArray("errors");
                    if (errors.isEmpty()) {
                        JSONArray response = httpResponse.getBody().getObject().getJSONArray("response");
                        for (int i = 0; i < response.length(); i++) {
                            League l = leagueMapper.map(response.getJSONObject(i));
                            logger.info(l.toString());
                            leagueService.addLeague(l);
                        }
                        r.addFlashAttribute("success", "Leagues imported successfully");
                    } else {
                        String parseErrors = new Gson().toJson(errors);
                        r.addFlashAttribute("error", parseErrors);
                        logger.error(parseErrors);
                    }
                } else r.addFlashAttribute("error", "Status code: " + httpResponse.getStatus());
            } catch (UnirestException e) {
                logger.error(e.getMessage());
                r.addFlashAttribute("error", e.getMessage());
            }
        } else r.addFlashAttribute("error", "Please delete all league entities first");
        return "redirect:/admin";
    }

}


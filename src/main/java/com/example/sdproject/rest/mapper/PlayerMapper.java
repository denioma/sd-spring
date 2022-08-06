package com.example.sdproject.rest.mapper;

import com.example.sdproject.data.entity.Player;
import com.example.sdproject.data.entity.Team;
import com.example.sdproject.data.service.TeamService;
import kong.unirest.json.JSONObject;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class PlayerMapper {
    private final TeamService teamService;

    public PlayerMapper(TeamService teamService) {
        this.teamService = teamService;
    }

    public Player map(JSONObject json) {
        JSONObject player = json.getJSONObject("player");
        JSONObject statistics = json.getJSONArray("statistics").getJSONObject(0);
        Integer id = statistics.getJSONObject("team").getInt("id");
        Optional<Team> team = teamService.getTeamByTeamId(id);
        if (team.isEmpty()) {
            return null;
        }

        Player p = new Player();
        p.setPlayerId(player.getInt("id"));
        p.setName(player.getString("name"));
        p.setBirthday(Date.valueOf(LocalDate.parse(player.getJSONObject("birth").getString("date"))));
        p.setPosition(statistics.getJSONObject("games").getString("position"));
        p.setTeam(team.get());
        return p;
    }
}

package com.example.sdproject.data.dto;

import com.example.sdproject.data.entity.Event;
import com.example.sdproject.data.entity.Game;
import com.example.sdproject.data.entity.Team;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import javax.validation.constraints.NotNull;
import java.util.*;

@Data
@NoArgsConstructor
public class GameDTO {
    private Integer id;
    @NotNull
    private Team teamA;
    @NotNull
    private Team teamB;
    @NotNull
    private String location;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date starts;
    private List<Event> events;
    private Integer goalsA;
    private Integer goalsB;

    public GameDTO(Integer id, Team teamA, Team teamB, String location, Date starts, List<Event> events) {
        this.id = id;
        this.teamA = teamA;
        this.teamB = teamB;
        this.location = location;
        this.starts = starts;
        this.events = events;
        checkGoals();
    }

    public Game toGame() {
        Game game = new Game();
        game.setId(id);
        List<Team> teams = new ArrayList<>();
        teams.add(teamA);
        teams.add(teamB);
        game.setTeams(teams);
        game.setLocation(location);
        game.setStarts(starts);
        game.setEvents(events);
        return game;
    }

    public void checkGoals() {
        goalsA = 0;
        goalsB = 0;
        for (Event event : events) {
            if (event.getPlayer() == null) continue;
            if (event.getPlayer().getTeam().equals(teamA)) goalsA++;
            else if (event.getPlayer().getTeam().equals(teamB)) goalsB++;
        }
    }
}

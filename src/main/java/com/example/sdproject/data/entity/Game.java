package com.example.sdproject.data.entity;

import com.example.sdproject.data.dto.GameDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToMany(cascade = CascadeType.DETACH)
    private List<Team> teams;
    private String location;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date starts;
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Event> events;
    @ManyToOne(cascade = CascadeType.DETACH)
    private Team winner;
    private boolean tied;

    public Game() {
        teams = new ArrayList<>();
        events = new ArrayList<>();
    }

    public GameDTO toDTO() {
        Team teamA = teams.size() == 2 ? teams.get(0) : null;
        Team teamB = teams.size() == 2 ? teams.get(1) : null;
        return new GameDTO(id, teamA, teamB, location, starts, events);
    }

    public List<Player> getPlayers() {
        if (teams.size() != 2) {
            return null;
        }

        List<Player> players = new ArrayList<>();
        Team teamA = teams.get(0);
        Team teamB = teams.get(1);
        players.addAll(teamA.getPlayers());
        players.addAll(teamB.getPlayers());
        return players;
    }

    public void end() {
        winner = null;
        tied = false;
        GameDTO dto = this.toDTO();
        dto.checkGoals();
        if (dto.getGoalsA() > dto.getGoalsB()) winner = dto.getTeamA();
        else if (dto.getGoalsA() < dto.getGoalsB()) winner = dto.getTeamB();
        else tied = true;
    }
}

package com.example.sdproject.data.entity;


import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Team implements Comparable<Team> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer teamId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String image;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    private List<Player> players;

    @JsonIncludeProperties("id")
    @ManyToMany(mappedBy = "teams", cascade = CascadeType.ALL)
    private List<Game> games;

    public Team(String name) {
        this.name = name;
    }

    public Team(String name, String image) {
        this(name);
        this.image = image;
    }

    public void addPlayer(Player player) { players.add(player); }

    @Override
    public int compareTo(Team o) {
        return name.compareTo(o.getName());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Team(id=%s, name=%s, image=%s, players=["
                .formatted(id, name, image != null ? image : "null")
        );
        if (players == null) players = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            builder.append(players.get(i).getName());
            if (i != players.size()) builder.append(",");
        }
        builder.append("])");
        return builder.toString();
    }

    public int getGoals() {
        int total = 0;
        if (players != null) {
            for (Player p : players) {
                for (Event e : p.getEvents()) {
                    if (e.getEventType() == EventType.GOAL) total++;
                }
            }
        }
        return total;
    }

    public int getWins() {
        int total = 0;
        if (games != null) {
            for (Game g : games) {
                if (g.getWinner() != null && g.getWinner().equals(this)) total++;
            }
        }
        return total;
    }

    public int getLosses() {
        int total = 0;
        if (games != null) {
            for (Game g : games) {
                if (g.getWinner() != null && !g.getWinner().equals(this)) total++;
            }
        }
        return total;
    }

    public int getTies() {
        int total = 0;
        if (games != null) {
            for (Game g : games) {
                if (g.isTied()) total++;
            }
        }
        return total;
    }
}

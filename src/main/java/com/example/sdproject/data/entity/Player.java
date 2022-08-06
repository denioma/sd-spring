package com.example.sdproject.data.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import kong.unirest.json.JSONObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Player {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer playerId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Date birthday;
    @Column(nullable = false)
    private String position;
    @JsonIncludeProperties("name")
    @ManyToOne
    private Team team;
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<Event> events;

    public Player (String name, Date birthday, String position) {
        this.name = name;
        this.birthday = birthday;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Player(id=%s, name=%s, birthday=%s, position=%s, team=%s)"
                .formatted(id, name, birthday, position, team != null ? team.getName() : "null");
    }
}

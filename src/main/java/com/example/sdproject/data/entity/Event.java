package com.example.sdproject.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Event {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date stamp;
    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    private Game game;
    @Column(nullable = false)
    private EventType eventType;
    @ManyToOne
    private Player player;

    @Override
    public String toString() {
        return "Event(id=%s, stamp=%s, gameId=%s, eventType=%s)"
                .formatted(id, stamp, game.getId(), eventType != null ? eventType.getDisplayValue() : null);
    }
}


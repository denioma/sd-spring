package com.example.sdproject.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer leagueId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String country;
}

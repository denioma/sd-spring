package com.example.sdproject.data.service;

import com.example.sdproject.data.entity.League;
import com.example.sdproject.data.repository.LeagueRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeagueService {
    private final LeagueRepository leagueRepository;

    public LeagueService(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

    public void addLeague(League l) { leagueRepository.save(l); }

    public boolean isPopulated() { return leagueRepository.existsAllBy(); }

    public List<League> getAllLeaguesSorted() { return leagueRepository.findAllByOrderByLeagueId(); }

    public void deleteAll() { leagueRepository.deleteAll(); }
}

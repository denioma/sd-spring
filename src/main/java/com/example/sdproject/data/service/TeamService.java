package com.example.sdproject.data.service;

import com.example.sdproject.data.entity.Team;
import com.example.sdproject.data.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        teamRepository.findAll().forEach(teams::add);
        return teams;
    }

    public List<Team> getAllTeamsSorted() { return teamRepository.findAllByOrderByName(); }

    public void addTeam(Team team) { teamRepository.save(team); }

    public Optional<Team> getTeam(Integer id) { return teamRepository.findById(id); }

    public Optional<Team> getTeamByTeamId(Integer teamId) { return teamRepository.findByTeamId(teamId); }

    public void deleteTeam(Team t) {teamRepository.delete(t); }

    public boolean deleteTeam(Integer id) {
        if (teamRepository.findById(id).isPresent()) {
            teamRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void deleteAll() { teamRepository.deleteAll(); }
}

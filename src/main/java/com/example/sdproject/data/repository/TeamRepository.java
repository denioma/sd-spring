package com.example.sdproject.data.repository;

import com.example.sdproject.data.entity.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team, Integer> {
    List<Team> findAllByOrderByName();
    Optional<Team> findByTeamId(Integer teamId);
}

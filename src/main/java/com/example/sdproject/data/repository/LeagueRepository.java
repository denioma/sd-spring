package com.example.sdproject.data.repository;

import com.example.sdproject.data.entity.League;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeagueRepository extends CrudRepository<League, Integer> {
    boolean existsAllBy();
    List<League> findAllByOrderByLeagueId();
}

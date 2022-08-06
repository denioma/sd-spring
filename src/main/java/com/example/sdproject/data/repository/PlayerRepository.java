package com.example.sdproject.data.repository;

import com.example.sdproject.data.entity.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Integer> {
    List<Player> findAllByOrderById();
    @Query("select p, count(e) from Player p, Event e where p.id = e.player.id and e.eventType = 2 group by p.id")
    List<Object[]> getPlayerGoals();
}

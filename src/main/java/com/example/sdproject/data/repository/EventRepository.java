package com.example.sdproject.data.repository;

import com.example.sdproject.data.entity.Event;
import com.example.sdproject.data.entity.Game;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends CrudRepository<Event, Integer> {
    List<Event> findAllByGame(Game game);

    @Query("select e from Event e where e.eventType = 0 and e.game = ?1")
    Optional<Event> findStart(Game game);

    @Query("select e from Event e where e.eventType = 1 and e.game = ?1")
    Optional<Event> findEnd(Game game);

    Optional<Event> findFirstByGameIsOrderByStampDescId(Game g);
}

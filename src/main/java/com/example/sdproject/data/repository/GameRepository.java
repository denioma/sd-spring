package com.example.sdproject.data.repository;

import com.example.sdproject.data.entity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {
    List<Game> findAllByOrderById();

    List<Game> findAllByOrderByStarts();
}

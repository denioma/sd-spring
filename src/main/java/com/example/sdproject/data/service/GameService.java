package com.example.sdproject.data.service;

import com.example.sdproject.data.entity.Game;
import com.example.sdproject.data.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public List<Game> getAllGames() { return gameRepository.findAllByOrderById(); }

    public List<Game> getAllGamesSortByStarts() { return gameRepository.findAllByOrderByStarts(); }

    public Optional<Game> getGame(Integer id) { return gameRepository.findById(id); }

    public void addGame(Game g) { gameRepository.save(g); }

    public void deleteGame(Game g) { gameRepository.delete(g); }

    public void deleteGame(Integer id) { gameRepository.deleteById(id); }

    public void deleteAll() { gameRepository.deleteAll(); }
}

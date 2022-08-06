package com.example.sdproject.data.service;

import com.example.sdproject.data.entity.Player;
import com.example.sdproject.data.exception.PlayerNotFoundException;
import com.example.sdproject.data.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PlayerService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> getAllPlayers() { return playerRepository.findAllByOrderById(); }

    public void addPlayer(Player player) { playerRepository.save(player); }

    public Optional<Player> getPlayer(Integer id) { return playerRepository.findById(id); }

    public void deletePlayer(Player p) { playerRepository.delete(p); }

    public void deletePlayer(Integer id) throws PlayerNotFoundException {
        if (playerRepository.findById(id).isEmpty()) {
            throw new PlayerNotFoundException("User does not exist");
        }
        playerRepository.deleteById(id);
    }

    public void deleteAll() { playerRepository.deleteAll(); }

    public Player getBestScorer() {
        List<Object[]> result = playerRepository.getPlayerGoals();
        Map<Player, Long> map = new HashMap<>();
        result.forEach(obj -> map.put((Player) obj[0], (Long) obj[1]));
        for (Map.Entry<Player, Long> entry : map.entrySet()) {
            logger.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        if (map.isEmpty()) return null;
        List<Map.Entry<Player, Long>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.<Player, Long>comparingByValue().reversed());
        return list.get(0).getKey();
    }
}

package com.example.sdproject.data.service;

import com.example.sdproject.data.entity.Event;
import com.example.sdproject.data.entity.EventType;
import com.example.sdproject.data.entity.Game;
import com.example.sdproject.data.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static List<EventType> notNulls = List.of(EventType.GOAL, EventType.YELLOW, EventType.RED);

    public void addEvent(Event e) throws Exception {
        Game g = e.getGame();
        EventType eventType = e.getEventType();
        List<Event> events = eventRepository.findAllByGame(g);
        Optional<Event> start = eventRepository.findStart(g);
        Optional<Event> end = eventRepository.findEnd(g);
        Optional<Event> last = eventRepository.findFirstByGameIsOrderByStampDescId(g);

        if (end.isPresent()) {
            throw new Exception("Game already ended");
        }

        if (eventType != EventType.START && start.isEmpty()) {
            throw new Exception("Game not started");
        }

        if (eventType == EventType.START && start.isPresent()) {
            throw new Exception("Game already started");
        }

        if (last.isPresent()) {
            EventType lastEventType = last.get().getEventType();
            if (eventType == EventType.INTERRUPT && lastEventType == EventType.INTERRUPT) {
                throw new Exception("Game already interrupted");
            }

            if (eventType == EventType.RESUME && lastEventType != EventType.INTERRUPT) {
                throw new Exception("Game is not interrupted");
            }

            if (eventType != EventType.RESUME && lastEventType == EventType.INTERRUPT) {
                throw new Exception("Game is interrupted");
            }
        }

        if (!notNulls.contains(eventType)) {
            e.setPlayer(null);
        }

        if (eventType == EventType.END) {
            g.end();
        }

        this.eventRepository.save(e);
    }
}

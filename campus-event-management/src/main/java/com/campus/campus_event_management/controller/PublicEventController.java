package com.campus.campus_event_management.controller;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campus.campus_event_management.entity.Event;
import com.campus.campus_event_management.repository.EventRepository;

@RestController
@RequestMapping("/api/public/events")
public class PublicEventController {

    private final EventRepository eventRepository;

    public PublicEventController(
            EventRepository eventRepository) {

        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<Event> getUpcomingApprovedEvents() {

        LocalDate today = LocalDate.now();

        return eventRepository
                .findByStatus("APPROVED")
                .stream()
                .filter(event ->
                        event.getDate() != null
                        && !event.getDate().isBefore(today)
                )
                .sorted(
                        Comparator.comparing(
                                Event::getDate
                        )
                )
                .limit(3)
                .toList();
    }
}
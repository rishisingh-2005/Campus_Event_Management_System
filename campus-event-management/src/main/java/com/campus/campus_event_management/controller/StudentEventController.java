package com.campus.campus_event_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campus.campus_event_management.entity.Event;
import com.campus.campus_event_management.repository.EventRepository;
import com.campus.campus_event_management.service.EventService;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/student/events")
public class StudentEventController {

    private final EventService eventService;
    private final EventRepository eventRepository;

    public StudentEventController(
            EventService eventService,
            EventRepository eventRepository) {

        this.eventService = eventService;
        this.eventRepository = eventRepository;
    }

    // Get only APPROVED events
    @GetMapping
    public List<Event> getApprovedEvents() {
        return eventRepository.findByStatus("APPROVED");
    }

    // Get approved event by ID
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {

        Event event = eventService.getEventById(id);

        if (event == null) {
            return null;
        }

        if (!"APPROVED".equalsIgnoreCase(event.getStatus())) {
            return null;
        }

        return event;
    }
    @GetMapping("/search")
    public List<Event> searchEvents(@RequestParam String title) {

    return eventRepository.findByStatusAndTitleContainingIgnoreCase(
            "APPROVED",
            title
    );
}
    @GetMapping("/category")
    public List<Event> filterByCategory(@RequestParam String category) {

    return eventRepository.findByStatusAndCategoryIgnoreCase(
            "APPROVED",
            category
    );
}
    @GetMapping("/date")
    public List<Event> filterByDate(@RequestParam LocalDate date) {

    return eventRepository.findByStatusAndDate(
            "APPROVED",
            date
    );
}
}

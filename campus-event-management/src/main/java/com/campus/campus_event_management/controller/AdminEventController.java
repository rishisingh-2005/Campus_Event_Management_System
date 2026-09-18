package com.campus.campus_event_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campus.campus_event_management.entity.Event;
import com.campus.campus_event_management.service.EventService;

@RestController
@RequestMapping("/api/admin/events")
public class AdminEventController {

    private final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PutMapping("/{id}/approve")
    public Event approveEvent(@PathVariable Long id) {

        return eventService.updateEventStatus(id, "APPROVED");
    }

    @PutMapping("/{id}/reject")
    public Event rejectEvent(@PathVariable Long id) {

        return eventService.updateEventStatus(id, "REJECTED");
    }
}

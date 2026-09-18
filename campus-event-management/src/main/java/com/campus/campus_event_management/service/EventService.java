package com.campus.campus_event_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.campus.campus_event_management.entity.Event;
import com.campus.campus_event_management.repository.EventRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Create Event
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // Get All Events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Get Event By ID
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    // Update Event
    public Event updateEvent(Long id, Event eventDetails) {

        Event existingEvent =
                eventRepository.findById(id).orElse(null);

        if (existingEvent == null) {
            return null;
        }

        existingEvent.setTitle(eventDetails.getTitle());

        existingEvent.setDescription(
                eventDetails.getDescription()
        );

        existingEvent.setDate(
                eventDetails.getDate()
        );

        existingEvent.setTime(
                eventDetails.getTime()
        );

        existingEvent.setVenue(
                eventDetails.getVenue()
        );

        existingEvent.setCategory(
                eventDetails.getCategory()
        );

        existingEvent.setCapacity(
                eventDetails.getCapacity()
        );

        existingEvent.setRegistrationDeadline(
                eventDetails.getRegistrationDeadline()
        );

        // Organizer updates must go for admin approval again
        existingEvent.setStatus("PENDING");

        return eventRepository.save(existingEvent);
    }

    // Delete Event
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    // Update Event Status
    // Used by Admin for APPROVED / REJECTED
    public Event updateEventStatus(Long id, String status) {

        Event event =
                eventRepository.findById(id).orElse(null);

        if (event == null) {
            return null;
        }

        event.setStatus(status);

        return eventRepository.save(event);
    }
}

package com.campus.campus_event_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.campus.campus_event_management.entity.Event;
import com.campus.campus_event_management.repository.EventRepository;

@Service
public class OrganizerEventService {

    private final EventRepository eventRepository;

    public OrganizerEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Create a new event
    public Event createEvent(Event event, Long organizerId) {

        event.setId(null);

        event.setOrganizerId(organizerId);

        // Every newly created event must wait for admin approval
        event.setStatus("PENDING");

        return eventRepository.save(event);
    }


    // Get all events created by a particular organizer
    public List<Event> getEventsByOrganizer(Long organizerId) {

        return eventRepository.findByOrganizerId(organizerId);
    }


    // Get one event
    public Event getEventById(Long eventId) {

        return eventRepository.findById(eventId).orElse(null);
    }


    // Update an event
    public Event updateEvent(
            Long eventId,
            Event updatedEvent,
            Long organizerId) {

        Event existingEvent =
                eventRepository.findById(eventId).orElse(null);

        if (existingEvent == null) {
            throw new RuntimeException("Event not found");
        }

        // Make sure the organizer owns this event
        if (!organizerId.equals(existingEvent.getOrganizerId())) {
            throw new RuntimeException(
                    "You are not allowed to modify this event"
            );
        }

        existingEvent.setTitle(updatedEvent.getTitle());

        existingEvent.setDescription(
                updatedEvent.getDescription()
        );

        existingEvent.setDate(
                updatedEvent.getDate()
        );

        existingEvent.setTime(
                updatedEvent.getTime()
        );

        existingEvent.setVenue(
                updatedEvent.getVenue()
        );

        existingEvent.setCategory(
                updatedEvent.getCategory()
        );

        existingEvent.setCapacity(
                updatedEvent.getCapacity()
        );

        existingEvent.setRegistrationDeadline(
                updatedEvent.getRegistrationDeadline()
        );

        // Editing an event sends it back for approval
        existingEvent.setStatus("PENDING");

        return eventRepository.save(existingEvent);
    }
}

package com.campus.campus_event_management.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.campus.campus_event_management.entity.Event;
import com.campus.campus_event_management.entity.Registration;
import com.campus.campus_event_management.entity.User;
import com.campus.campus_event_management.repository.EventRepository;
import com.campus.campus_event_management.repository.RegistrationRepository;
import com.campus.campus_event_management.repository.UserRepository;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public RegistrationService(
            RegistrationRepository registrationRepository,
            UserRepository userRepository,
            EventRepository eventRepository) {

        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    // Register Student
    public Registration registerStudent(Long userId, Long eventId) {

        // Check user
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Only students can register
        if (!"STUDENT".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException(
                    "Only students can register for events"
            );
        }

        // Check event
        Event event = eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        // Check if student already has an active registration
        List<Registration> existingRegistrations =
                registrationRepository.findByUserId(userId);

        boolean alreadyRegistered =
                existingRegistrations.stream()
                        .anyMatch(r ->
                                r.getEventId().equals(eventId)
                                && "REGISTERED".equalsIgnoreCase(
                                        r.getStatus()
                                )
                        );

        if (alreadyRegistered) {
            throw new RuntimeException(
                    "Student is already registered for this event"
            );
        }

        // Count only active registrations
        List<Registration> registrations =
                registrationRepository.findByEventId(eventId)
                        .stream()
                        .filter(r ->
                                "REGISTERED".equalsIgnoreCase(
                                        r.getStatus()
                                )
                        )
                        .toList();

        // Check event capacity
        if (registrations.size() >= event.getCapacity()) {
            throw new RuntimeException(
                    "Event is full. No more registrations are allowed"
            );
        }

        // Check registration deadline
        if (event.getRegistrationDeadline() != null
                && LocalDate.now().isAfter(
                        event.getRegistrationDeadline())) {

            throw new RuntimeException(
                    "Registration deadline has passed"
            );
        }

        // Event must be approved
        if (!"APPROVED".equalsIgnoreCase(event.getStatus())) {
            throw new RuntimeException(
                    "Registration is not available for this event"
            );
        }

        // Create registration
        Registration registration = new Registration();

        registration.setUserId(userId);
        registration.setEventId(eventId);
        registration.setRegistrationDate(LocalDateTime.now());
        registration.setStatus("REGISTERED");

        return registrationRepository.save(registration);
    }

    // Get registrations of a student
    public List<Registration> getRegistrationsByUser(Long userId) {

        return registrationRepository.findByUserId(userId);
    }

    // Get registrations for an event
    public List<Registration> getRegistrationsByEvent(Long eventId) {

        return registrationRepository.findByEventId(eventId);
    }

    // Cancel registration by deleting it
    public void cancelRegistration(Long id, Long userId) {

        Registration registration =
                registrationRepository.findById(id).orElse(null);

        if (registration == null) {
            throw new RuntimeException("Registration not found");
        }

        if (!registration.getUserId().equals(userId)) {
            throw new RuntimeException(
                    "You are not allowed to cancel this registration"
            );
        }

        registrationRepository.deleteById(id);
    }

    // Cancel registration by changing status
    public Registration cancelRegistrationByStatus(
            Long id,
            Long userId) {

        Registration registration =
                registrationRepository.findById(id).orElse(null);

        if (registration == null) {
            throw new RuntimeException("Registration not found");
        }

        if (!registration.getUserId().equals(userId)) {
            throw new RuntimeException(
                    "You are not allowed to cancel this registration"
            );
        }

        registration.setStatus("CANCELLED");

        return registrationRepository.save(registration);
    }
}
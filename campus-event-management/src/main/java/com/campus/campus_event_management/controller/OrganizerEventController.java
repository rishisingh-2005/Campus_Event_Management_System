package com.campus.campus_event_management.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.campus.campus_event_management.entity.Attendance;
import com.campus.campus_event_management.entity.Event;
import com.campus.campus_event_management.entity.Registration;
import com.campus.campus_event_management.entity.User;
import com.campus.campus_event_management.repository.UserRepository;
import com.campus.campus_event_management.service.AttendanceService;
import com.campus.campus_event_management.service.OrganizerEventService;
import com.campus.campus_event_management.service.RegistrationService;

@RestController
@RequestMapping("/api/organizer/events")
public class OrganizerEventController {

    private final OrganizerEventService organizerEventService;
    private final RegistrationService registrationService;
    private final AttendanceService attendanceService;
    private final UserRepository userRepository;

    public OrganizerEventController(
            OrganizerEventService organizerEventService,
            RegistrationService registrationService,
            AttendanceService attendanceService,
            UserRepository userRepository) {

        this.organizerEventService = organizerEventService;
        this.registrationService = registrationService;
        this.attendanceService = attendanceService;
        this.userRepository = userRepository;
    }

    // =========================================================
    // CREATE EVENT
    // =========================================================

    @PostMapping
    public Event createEvent(
            @RequestBody Event event,
            Authentication authentication) {

        Long organizerId = getOrganizerId(authentication);

        return organizerEventService.createEvent(
                event,
                organizerId
        );
    }

    // =========================================================
    // GET MY EVENTS
    // =========================================================

    @GetMapping
    public List<Event> getMyEvents(
            Authentication authentication) {

        Long organizerId = getOrganizerId(authentication);

        return organizerEventService
                .getEventsByOrganizer(organizerId);
    }

    // =========================================================
    // GET EVENT BY ID
    // =========================================================

    @GetMapping("/{id}")
    public Event getEventById(
            @PathVariable Long id,
            Authentication authentication) {

        Long organizerId = getOrganizerId(authentication);

        Event event =
                organizerEventService.getEventById(id);

        if (event == null) {
            throw new RuntimeException(
                    "Event not found"
            );
        }

        if (!organizerId.equals(event.getOrganizerId())) {
            throw new RuntimeException(
                    "You are not allowed to view this event"
            );
        }

        return event;
    }

    // =========================================================
    // UPDATE EVENT
    // =========================================================

    @PutMapping("/{id}")
    public Event updateEvent(
            @PathVariable Long id,
            @RequestBody Event updatedEvent,
            Authentication authentication) {

        Long organizerId = getOrganizerId(authentication);

        return organizerEventService.updateEvent(
                id,
                updatedEvent,
                organizerId
        );
    }

    // =========================================================
    // GET EVENT REGISTRATIONS
    // =========================================================

    @GetMapping("/{id}/registrations")
    public List<Registration> getEventRegistrations(
            @PathVariable Long id,
            Authentication authentication) {

        Long organizerId =
                getOrganizerId(authentication);

        Event event =
                organizerEventService.getEventById(id);

        if (event == null) {
            throw new RuntimeException(
                    "Event not found"
            );
        }

        if (!organizerId.equals(event.getOrganizerId())) {
            throw new RuntimeException(
                    "You are not allowed to view registrations for this event"
            );
        }

        return registrationService
                .getRegistrationsByEvent(id);
    }

    // =========================================================
    // ATTENDANCE MANAGEMENT
    // =========================================================

    // Get attendance for an event
    @GetMapping("/{id}/attendance")
    public List<Attendance> getEventAttendance(
            @PathVariable Long id,
            Authentication authentication) {

        Long organizerId =
                getOrganizerId(authentication);

        Event event =
                organizerEventService.getEventById(id);

        if (event == null) {
            throw new RuntimeException(
                    "Event not found"
            );
        }

        if (!organizerId.equals(event.getOrganizerId())) {
            throw new RuntimeException(
                    "You are not allowed to view attendance for this event"
            );
        }

        return attendanceService
                .getAttendanceByEvent(id);
    }

    // Mark attendance for ONE student
    @PostMapping("/{id}/attendance")
    public Attendance markAttendance(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam String status,
            Authentication authentication) {

        Long organizerId =
                getOrganizerId(authentication);

        Event event =
                organizerEventService.getEventById(id);

        if (event == null) {
            throw new RuntimeException(
                    "Event not found"
            );
        }

        if (!organizerId.equals(event.getOrganizerId())) {
            throw new RuntimeException(
                    "You are not allowed to mark attendance for this event"
            );
        }

        return attendanceService.markAttendance(
                userId,
                id,
                status
        );
    }

    // =========================================================
    // MARK ALL REGISTERED STUDENTS AS PRESENT
    // =========================================================

    @PostMapping("/{id}/attendance/mark-all-present")
    public String markAllPresent(
            @PathVariable Long id,
            Authentication authentication) {

        Long organizerId =
                getOrganizerId(authentication);

        Event event =
                organizerEventService.getEventById(id);

        if (event == null) {
            throw new RuntimeException(
                    "Event not found"
            );
        }

        if (!organizerId.equals(event.getOrganizerId())) {
            throw new RuntimeException(
                    "You are not allowed to mark attendance for this event"
            );
        }

        attendanceService.markAllPresent(id);

        return "All registered students marked as PRESENT";
    }

    // =========================================================
    // ORGANIZER AUTHENTICATION
    // =========================================================

    private Long getOrganizerId(
            Authentication authentication) {

        if (authentication == null
                || authentication.getName() == null) {

            throw new RuntimeException(
                    "Organizer authentication required"
            );
        }

        String email =
                authentication.getName();

        User user =
                userRepository
                        .findByEmail(email)
                        .orElse(null);

        if (user == null) {
            throw new RuntimeException(
                    "Organizer not found"
            );
        }

        if (!"ORGANIZER".equalsIgnoreCase(
                user.getRole())) {

            throw new RuntimeException(
                    "Only organizers can perform this action"
            );
        }

        return user.getId();
    }
}
package com.campus.campus_event_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;


import com.campus.campus_event_management.entity.Registration;
import com.campus.campus_event_management.service.RegistrationService;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // Register student for an event
    @PostMapping
    public Registration registerStudent(
            @RequestParam Long userId,
            @RequestParam Long eventId) {

        return registrationService.registerStudent(userId, eventId);
    }

    // Get all registrations of a student
    @GetMapping("/user/{userId}")
    public List<Registration> getRegistrationsByUser(
            @PathVariable Long userId) {

        return registrationService.getRegistrationsByUser(userId);
    }

    // Get all participants of an event
    @GetMapping("/event/{eventId}")
    public List<Registration> getRegistrationsByEvent(
            @PathVariable Long eventId) {

        return registrationService.getRegistrationsByEvent(eventId);
    }

    // Cancel registration
   @DeleteMapping("/{id}")
    public String cancelRegistration(
        @PathVariable Long id,
        @RequestParam Long userId) {

    registrationService.cancelRegistration(id, userId);

    return "Registration cancelled successfully";
}
    @PutMapping("/{id}/cancel")
    public Registration cancelRegistrationByStatus(
        @PathVariable Long id,
        @RequestParam Long userId) {

    return registrationService.cancelRegistrationByStatus(
            id,
            userId
    );
}
}

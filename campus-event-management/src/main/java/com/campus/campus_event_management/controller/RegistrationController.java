package com.campus.campus_event_management.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campus.campus_event_management.entity.Registration;
import com.campus.campus_event_management.entity.User;
import com.campus.campus_event_management.service.RegistrationService;
import com.campus.campus_event_management.service.UserService;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final UserService userService;


    public RegistrationController(
            RegistrationService registrationService,
            UserService userService) {

        this.registrationService = registrationService;
        this.userService = userService;
    }


    // ==========================================
    // REGISTER STUDENT FOR EVENT
    // ==========================================

    @PostMapping
    public Registration registerStudent(
            @RequestParam Long userId,
            @RequestParam Long eventId,
            Authentication authentication) {

        Long authenticatedUserId =
                getAuthenticatedStudentId(authentication);

        // A student can only register themselves
        if (!authenticatedUserId.equals(userId)) {
            throw new RuntimeException(
                "You are not allowed to register another student"
            );
        }

        return registrationService.registerStudent(
                userId,
                eventId
        );
    }


    // ==========================================
    // GET STUDENT REGISTRATIONS
    // ==========================================

    @GetMapping("/user/{userId}")
    public List<Registration> getRegistrationsByUser(
            @PathVariable Long userId,
            Authentication authentication) {

        Long authenticatedUserId =
                getAuthenticatedStudentId(authentication);

        // A student can only view their own registrations
        if (!authenticatedUserId.equals(userId)) {
            throw new RuntimeException(
                "You are not allowed to view these registrations"
            );
        }

        return registrationService.getRegistrationsByUser(
                userId
        );
    }


    // ==========================================
    // GET EVENT REGISTRATIONS
    // ==========================================

    @GetMapping("/event/{eventId}")
    public List<Registration> getRegistrationsByEvent(
            @PathVariable Long eventId) {

        return registrationService.getRegistrationsByEvent(
                eventId
        );
    }


    // ==========================================
    // CANCEL REGISTRATION
    // ==========================================

    @DeleteMapping("/{id}")
    public String cancelRegistration(
            @PathVariable Long id,
            @RequestParam Long userId,
            Authentication authentication) {

        Long authenticatedUserId =
                getAuthenticatedStudentId(authentication);

        // A student can only cancel their own registration
        if (!authenticatedUserId.equals(userId)) {
            throw new RuntimeException(
                "You are not allowed to cancel another student's registration"
            );
        }

        registrationService.cancelRegistration(
                id,
                userId
        );

        return "Registration cancelled successfully";
    }


    // ==========================================
    // CANCEL REGISTRATION BY STATUS
    // ==========================================

    @PutMapping("/{id}/cancel")
    public Registration cancelRegistrationByStatus(
            @PathVariable Long id,
            @RequestParam Long userId,
            Authentication authentication) {

        Long authenticatedUserId =
                getAuthenticatedStudentId(authentication);

        // A student can only cancel their own registration
        if (!authenticatedUserId.equals(userId)) {
            throw new RuntimeException(
                "You are not allowed to cancel another student's registration"
            );
        }

        return registrationService.cancelRegistrationByStatus(
                id,
                userId
        );
    }


    // ==========================================
    // GET AUTHENTICATED STUDENT ID
    // ==========================================

    private Long getAuthenticatedStudentId(
            Authentication authentication) {

        if (authentication == null
                || authentication.getName() == null) {

            throw new RuntimeException(
                "Authentication required"
            );
        }

        String email = authentication.getName();

        User user =
                userService.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException(
                "User not found"
            );
        }

        if (!"STUDENT".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException(
                "Only students can perform this action"
            );
        }

        return user.getId();
    }
}
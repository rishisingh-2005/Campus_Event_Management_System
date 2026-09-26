package com.campus.campus_event_management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campus.campus_event_management.entity.Certificate;
import com.campus.campus_event_management.entity.Event;
import com.campus.campus_event_management.entity.User;
import com.campus.campus_event_management.service.CertificateService;
import com.campus.campus_event_management.service.EventService;
import com.campus.campus_event_management.service.UserService;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    private final UserService userService;

    private final EventService eventService;


    public CertificateController(
            CertificateService certificateService,
            UserService userService,
            EventService eventService) {

        this.certificateService =
                certificateService;

        this.userService =
                userService;

        this.eventService =
                eventService;
    }


    // ==========================================
    // GENERATE SINGLE CERTIFICATE
    // ==========================================

    @PostMapping
    public Certificate generateCertificate(
            @RequestParam Long userId,
            @RequestParam Long eventId,
            Authentication authentication) {

        User loggedInUser =
                getAuthenticatedUser(
                        authentication
                );


        // --------------------------------------
        // ADMIN
        // --------------------------------------

        if ("ADMIN".equalsIgnoreCase(
                loggedInUser.getRole())) {

            return certificateService
                    .generateCertificate(
                            userId,
                            eventId
                    );
        }


        // --------------------------------------
        // ORGANIZER
        // --------------------------------------

        if ("ORGANIZER".equalsIgnoreCase(
                loggedInUser.getRole())) {

            Event event =
                    eventService.getEventById(
                            eventId
                    );

            if (event == null) {

                throw new RuntimeException(
                        "Event not found"
                );
            }


            if (!loggedInUser.getId().equals(
                    event.getOrganizerId())) {

                throw new RuntimeException(
                        "You are not allowed to generate certificates for this event"
                );
            }


            return certificateService
                    .generateCertificate(
                            userId,
                            eventId
                    );
        }


        // --------------------------------------
        // OTHER ROLES
        // --------------------------------------

        throw new RuntimeException(
                "Only admin or organizer can generate certificates"
        );
    }


    // ==========================================
    // GENERATE ALL CERTIFICATES
    // ==========================================
    //
    // Only students marked PRESENT receive
    // certificates.
    //
    // Existing certificates are not duplicated.
    //
    // ==========================================

    @PostMapping(
            "/event/{eventId}/generate-all"
    )
    public Map<String, Object> generateCertificatesForAll(
            @PathVariable Long eventId,
            Authentication authentication) {

        User loggedInUser =
                getAuthenticatedUser(
                        authentication
                );


        // --------------------------------------
        // ADMIN
        // --------------------------------------

        if ("ADMIN".equalsIgnoreCase(
                loggedInUser.getRole())) {

            return certificateService
                    .generateCertificatesForAll(
                            eventId
                    );
        }


        // --------------------------------------
        // ORGANIZER
        // --------------------------------------

        if ("ORGANIZER".equalsIgnoreCase(
                loggedInUser.getRole())) {

            Event event =
                    eventService.getEventById(
                            eventId
                    );

            if (event == null) {

                throw new RuntimeException(
                        "Event not found"
                );
            }


            if (!loggedInUser.getId().equals(
                    event.getOrganizerId())) {

                throw new RuntimeException(
                        "You are not allowed to generate certificates for this event"
                );
            }


            return certificateService
                    .generateCertificatesForAll(
                            eventId
                    );
        }


        // --------------------------------------
        // OTHER ROLES
        // --------------------------------------

        throw new RuntimeException(
                "Only admin or organizer can generate certificates"
        );
    }


    // ==========================================
    // GET CERTIFICATES BY USER
    // ==========================================

    @GetMapping("/user/{userId}")
    public List<Certificate> getCertificatesByUser(
            @PathVariable Long userId,
            Authentication authentication) {

        User loggedInUser =
                getAuthenticatedUser(
                        authentication
                );


        // --------------------------------------
        // STUDENT
        // --------------------------------------

        if ("STUDENT".equalsIgnoreCase(
                loggedInUser.getRole())) {

            if (!loggedInUser.getId().equals(
                    userId)) {

                throw new RuntimeException(
                        "You are not authorized to view these certificates"
                );
            }
        }


        // --------------------------------------
        // CHECK ROLE
        // --------------------------------------

        if (!"STUDENT".equalsIgnoreCase(
                loggedInUser.getRole())
                && !"ADMIN".equalsIgnoreCase(
                        loggedInUser.getRole())
                && !"ORGANIZER".equalsIgnoreCase(
                        loggedInUser.getRole())) {

            throw new RuntimeException(
                    "You are not authorized to view certificates"
            );
        }


        return certificateService
                .getCertificatesByUser(
                        userId
                );
    }


    // ==========================================
    // GET CERTIFICATE BY ID
    // ==========================================

    @GetMapping("/{id}")
    public Certificate getCertificateById(
            @PathVariable Long id,
            Authentication authentication) {

        Certificate certificate =
                certificateService
                        .getCertificateById(id);


        if (certificate == null) {

            return null;
        }


        User loggedInUser =
                getAuthenticatedUser(
                        authentication
                );


        // --------------------------------------
        // STUDENT
        // --------------------------------------

        if ("STUDENT".equalsIgnoreCase(
                loggedInUser.getRole())) {

            if (!loggedInUser.getId().equals(
                    certificate.getUserId())) {

                throw new RuntimeException(
                        "You are not authorized to view this certificate"
                );
            }

            return certificate;
        }


        // --------------------------------------
        // ADMIN
        // --------------------------------------

        if ("ADMIN".equalsIgnoreCase(
                loggedInUser.getRole())) {

            return certificate;
        }


        // --------------------------------------
        // ORGANIZER
        // --------------------------------------

        if ("ORGANIZER".equalsIgnoreCase(
                loggedInUser.getRole())) {

            Event event =
                    eventService.getEventById(
                            certificate.getEventId()
                    );

            if (event == null) {

                throw new RuntimeException(
                        "Event not found"
                );
            }


            if (!loggedInUser.getId().equals(
                    event.getOrganizerId())) {

                throw new RuntimeException(
                        "You are not authorized to view this certificate"
                );
            }


            return certificate;
        }


        throw new RuntimeException(
                "You are not authorized to view this certificate"
        );
    }


    // ==========================================
    // GET AUTHENTICATED USER
    // ==========================================

    private User getAuthenticatedUser(
            Authentication authentication) {

        if (authentication == null
                || authentication.getName() == null) {

            throw new RuntimeException(
                    "Authentication required"
            );
        }


        String email =
                authentication.getName();


        User user =
                userService.getUserByEmail(
                        email
                );


        if (user == null) {

            throw new RuntimeException(
                    "Logged-in user not found"
            );
        }


        return user;
    }
}
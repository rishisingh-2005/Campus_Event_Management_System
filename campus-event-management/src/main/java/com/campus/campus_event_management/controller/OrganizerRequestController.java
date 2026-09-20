package com.campus.campus_event_management.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.campus.campus_event_management.entity.OrganizerRequest;
import com.campus.campus_event_management.service.OrganizerRequestService;

@RestController
@RequestMapping("/api/organizer-requests")
public class OrganizerRequestController {

    private final OrganizerRequestService organizerRequestService;

    public OrganizerRequestController(
            OrganizerRequestService organizerRequestService) {

        this.organizerRequestService =
                organizerRequestService;
    }

    @PostMapping
    public ResponseEntity<?> createRequest(
            @RequestBody OrganizerRequest request) {

        try {

            OrganizerRequest savedRequest =
                    organizerRequestService
                            .createRequest(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedRequest);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
}
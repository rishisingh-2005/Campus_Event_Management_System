package com.campus.campus_event_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.campus.campus_event_management.entity.OrganizerRequest;
import com.campus.campus_event_management.service.OrganizerRequestService;

@RestController
@RequestMapping("/api/organizer-requests")
public class AdminOrganizerRequestController {

    private final OrganizerRequestService organizerRequestService;

    public AdminOrganizerRequestController(
            OrganizerRequestService organizerRequestService) {

        this.organizerRequestService =
                organizerRequestService;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<OrganizerRequest>> getPendingRequests() {

        return ResponseEntity.ok(
                organizerRequestService.getPendingRequests()
        );
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveRequest(
            @PathVariable Long id) {

        try {

            OrganizerRequest approvedRequest =
                    organizerRequestService
                            .approveRequest(id);

            return ResponseEntity.ok(
                    approvedRequest
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectRequest(
            @PathVariable Long id) {

        try {

            OrganizerRequest rejectedRequest =
                    organizerRequestService
                            .rejectRequest(id);

            return ResponseEntity.ok(
                    rejectedRequest
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
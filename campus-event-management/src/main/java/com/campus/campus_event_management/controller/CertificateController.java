package com.campus.campus_event_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campus.campus_event_management.entity.Certificate;
import com.campus.campus_event_management.service.CertificateService;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(
            CertificateService certificateService) {

        this.certificateService = certificateService;
    }

    // Generate certificate
    @PostMapping
    public Certificate generateCertificate(
            @RequestParam Long userId,
            @RequestParam Long eventId) {

        return certificateService.generateCertificate(
                userId,
                eventId
        );
    }

    // Get certificates of a student
    @GetMapping("/user/{userId}")
    public List<Certificate> getCertificatesByUser(
            @PathVariable Long userId) {

        return certificateService.getCertificatesByUser(userId);
    }

    // Get certificate by ID
    @GetMapping("/{id}")
    public Certificate getCertificateById(
            @PathVariable Long id) {

        return certificateService.getCertificateById(id);
    }
}

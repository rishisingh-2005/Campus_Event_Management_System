package com.campus.campus_event_management.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.campus.campus_event_management.entity.Attendance;
import com.campus.campus_event_management.entity.Certificate;
import com.campus.campus_event_management.entity.Event;
import com.campus.campus_event_management.entity.User;
import com.campus.campus_event_management.repository.AttendanceRepository;
import com.campus.campus_event_management.repository.CertificateRepository;
import com.campus.campus_event_management.repository.EventRepository;
import com.campus.campus_event_management.repository.UserRepository;

@Service
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public CertificateService(
            CertificateRepository certificateRepository,
            AttendanceRepository attendanceRepository,
            UserRepository userRepository,
            EventRepository eventRepository) {

        this.certificateRepository = certificateRepository;
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    // ==========================================
    // GENERATE SINGLE CERTIFICATE
    // ==========================================

    public Certificate generateCertificate(Long userId, Long eventId) {

        // Check attendance
        Attendance attendance =
                attendanceRepository
                        .findByUserIdAndEventId(userId, eventId)
                        .orElse(null);

        if (attendance == null) {
            throw new RuntimeException(
                    "Attendance not found. Certificate cannot be generated"
            );
        }

        // Student must be PRESENT
        if (!"PRESENT".equalsIgnoreCase(attendance.getStatus())) {
            throw new RuntimeException(
                    "Certificate can only be generated for students marked PRESENT"
            );
        }

        // Check if certificate already exists
        Certificate existingCertificate =
                certificateRepository
                        .findByUserIdAndEventId(userId, eventId)
                        .orElse(null);

        if (existingCertificate != null) {
            return existingCertificate;
        }

        // Find student
        User user =
                userRepository
                        .findById(userId)
                        .orElse(null);

        if (user == null) {
            throw new RuntimeException("Student not found");
        }

        // Find event
        Event event =
                eventRepository
                        .findById(eventId)
                        .orElse(null);

        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        // Create certificate
        Certificate certificate = new Certificate();

        certificate.setUserId(userId);
        certificate.setEventId(eventId);

        // Store student name
        certificate.setStudentName(
                user.getName()
        );

        // Store event name
        certificate.setEventName(
                event.getTitle()
        );

        // Generate certificate number
        certificate.setCertificateNumber(
                "CERT-" + UUID.randomUUID()
        );

        // Set issue date
        certificate.setIssueDate(
                LocalDate.now().toString()
        );

        // Save certificate
        return certificateRepository.save(certificate);
    }


    // ==========================================
    // GENERATE CERTIFICATES FOR ALL PRESENT
    // ==========================================

    public Map<String, Object> generateCertificatesForAll(
            Long eventId) {

        // Check event
        Event event =
                eventRepository
                        .findById(eventId)
                        .orElse(null);

        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        // Get all attendance records for this event
        List<Attendance> attendanceRecords =
                attendanceRepository.findByEventId(eventId);

        int presentStudents = 0;
        int generatedCertificates = 0;
        int existingCertificates = 0;

        List<Certificate> certificates =
                new ArrayList<>();

        for (Attendance attendance : attendanceRecords) {

            // Only PRESENT students are eligible
            if (!"PRESENT".equalsIgnoreCase(
                    attendance.getStatus())) {

                continue;
            }

            presentStudents++;

            Long userId =
                    attendance.getUserId();

            // Check existing certificate
            Certificate existingCertificate =
                    certificateRepository
                            .findByUserIdAndEventId(
                                    userId,
                                    eventId
                            )
                            .orElse(null);

            if (existingCertificate != null) {

                existingCertificates++;

                certificates.add(
                        existingCertificate
                );

                continue;
            }

            // Generate new certificate
            Certificate certificate =
                    generateCertificate(
                            userId,
                            eventId
                    );

            generatedCertificates++;

            certificates.add(certificate);
        }

        // ==========================================
        // RESPONSE
        // ==========================================

        Map<String, Object> result =
                new LinkedHashMap<>();

        result.put(
                "eventId",
                eventId
        );

        result.put(
                "eventName",
                event.getTitle()
        );

        result.put(
                "presentStudents",
                presentStudents
        );

        result.put(
                "generatedCertificates",
                generatedCertificates
        );

        result.put(
                "existingCertificates",
                existingCertificates
        );

        result.put(
                "totalCertificates",
                certificates.size()
        );

        result.put(
                "certificates",
                certificates
        );

        return result;
    }


    // ==========================================
    // GET CERTIFICATES BY USER
    // ==========================================

    public List<Certificate> getCertificatesByUser(
            Long userId) {

        return certificateRepository
                .findByUserId(userId);
    }


    // ==========================================
    // GET CERTIFICATE BY ID
    // ==========================================

    public Certificate getCertificateById(
            Long id) {

        return certificateRepository
                .findById(id)
                .orElse(null);
    }
}
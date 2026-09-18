package com.campus.campus_event_management.service;

import java.time.LocalDate;
import java.util.List;
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
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new RuntimeException("Student not found");
        }

        // Find event
        Event event = eventRepository.findById(eventId).orElse(null);

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

    public List<Certificate> getCertificatesByUser(Long userId) {

        return certificateRepository.findByUserId(userId);
    }

    public Certificate getCertificateById(Long id) {

        return certificateRepository
                .findById(id)
                .orElse(null);
    }
}

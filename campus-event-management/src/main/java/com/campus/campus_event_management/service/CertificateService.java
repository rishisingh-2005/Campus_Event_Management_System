package com.campus.campus_event_management.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.campus.campus_event_management.entity.Attendance;
import com.campus.campus_event_management.entity.Certificate;
import com.campus.campus_event_management.repository.AttendanceRepository;
import com.campus.campus_event_management.repository.CertificateRepository;

@Service
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final AttendanceRepository attendanceRepository;

    public CertificateService(
            CertificateRepository certificateRepository,
            AttendanceRepository attendanceRepository) {

        this.certificateRepository = certificateRepository;
        this.attendanceRepository = attendanceRepository;
    }

    // Generate certificate
    public Certificate generateCertificate(
            Long userId,
            Long eventId) {

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
        if (!"PRESENT".equalsIgnoreCase(
                attendance.getStatus())) {

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

        // Create certificate
        Certificate certificate = new Certificate();

        certificate.setUserId(userId);
        certificate.setEventId(eventId);

        certificate.setCertificateNumber(
                "CERT-" + UUID.randomUUID()
        );

        certificate.setIssueDate(
                LocalDate.now().toString()
        );

        return certificateRepository.save(certificate);
    }

    // Get certificates of a student
    public List<Certificate> getCertificatesByUser(
            Long userId) {

        return certificateRepository.findByUserId(userId);
    }

    // Get certificate by ID
    public Certificate getCertificateById(Long id) {

        return certificateRepository
                .findById(id)
                .orElse(null);
    }
}

package com.campus.campus_event_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.campus.campus_event_management.entity.Attendance;
import com.campus.campus_event_management.entity.Registration;
import com.campus.campus_event_management.repository.AttendanceRepository;
import com.campus.campus_event_management.repository.RegistrationRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final RegistrationRepository registrationRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            RegistrationRepository registrationRepository) {

        this.attendanceRepository = attendanceRepository;
        this.registrationRepository = registrationRepository;
    }

    // =========================================================
    // MARK ATTENDANCE - ONE STUDENT AT A TIME
    // =========================================================
    public Attendance markAttendance(
            Long userId,
            Long eventId,
            String status) {

        // Check whether student is registered
        List<Registration> registrations =
                registrationRepository.findByUserId(userId);

        boolean registered = registrations.stream()
                .anyMatch(r ->
                        r.getEventId().equals(eventId)
                        && "REGISTERED".equalsIgnoreCase(r.getStatus())
                );

        if (!registered) {
            throw new RuntimeException(
                    "Student is not registered for this event"
            );
        }

        // Check if attendance already exists
        Attendance attendance =
                attendanceRepository
                        .findByUserIdAndEventId(userId, eventId)
                        .orElse(null);

        if (attendance == null) {
            attendance = new Attendance();
            attendance.setUserId(userId);
            attendance.setEventId(eventId);
        }

        // Only PRESENT or ABSENT
        if (!"PRESENT".equalsIgnoreCase(status)
                && !"ABSENT".equalsIgnoreCase(status)) {

            throw new RuntimeException(
                    "Attendance status must be PRESENT or ABSENT"
            );
        }

        attendance.setStatus(status.toUpperCase());

        return attendanceRepository.save(attendance);
    }

    // =========================================================
    // MARK ALL REGISTERED STUDENTS AS PRESENT
    // =========================================================
    public void markAllPresent(Long eventId) {

        // Get all registrations for this event
        List<Registration> registrations =
                registrationRepository.findByEventId(eventId);

        // Process each registered student
        for (Registration registration : registrations) {

            // Ignore cancelled registrations
            if (!"REGISTERED".equalsIgnoreCase(
                    registration.getStatus())) {
                continue;
            }

            Long userId = registration.getUserId();

            // Check if attendance already exists
            Attendance attendance =
                    attendanceRepository
                            .findByUserIdAndEventId(
                                    userId,
                                    eventId
                            )
                            .orElse(null);

            // Create attendance if it doesn't exist
            if (attendance == null) {

                attendance = new Attendance();

                attendance.setUserId(userId);
                attendance.setEventId(eventId);
            }

            // Mark student as PRESENT
            attendance.setStatus("PRESENT");

            // Save attendance
            attendanceRepository.save(attendance);
        }
    }

    // =========================================================
    // GET ATTENDANCE FOR AN EVENT
    // =========================================================
    public List<Attendance> getAttendanceByEvent(
            Long eventId) {

        return attendanceRepository.findByEventId(eventId);
    }

    // =========================================================
    // GET ATTENDANCE OF A STUDENT
    // =========================================================
    public List<Attendance> getAttendanceByUser(
            Long userId) {

        return attendanceRepository.findByUserId(userId);
    }
}
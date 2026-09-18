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

    // Mark attendance
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

    // Get attendance for an event
    public List<Attendance> getAttendanceByEvent(Long eventId) {
        return attendanceRepository.findByEventId(eventId);
    }

    // Get attendance of a student
    public List<Attendance> getAttendanceByUser(Long userId) {
        return attendanceRepository.findByUserId(userId);
    }
}

package com.campus.campus_event_management.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campus.campus_event_management.entity.Attendance;
import com.campus.campus_event_management.entity.User;
import com.campus.campus_event_management.repository.UserRepository;
import com.campus.campus_event_management.service.AttendanceService;

@RestController
@RequestMapping("/api/student/attendance")
public class StudentAttendanceController {

    private final AttendanceService attendanceService;
    private final UserRepository userRepository;

    public StudentAttendanceController(
            AttendanceService attendanceService,
            UserRepository userRepository) {

        this.attendanceService = attendanceService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Attendance> getMyAttendance(
            Authentication authentication) {

        if (authentication == null ||
                authentication.getName() == null) {

            throw new RuntimeException(
                    "Student authentication required");
        }

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElse(null);

        if (user == null) {
            throw new RuntimeException("Student not found");
        }

        if (!"STUDENT".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException(
                    "Only students can view their attendance");
        }

        return attendanceService.getAttendanceByUser(
                user.getId()
        );
    }
}

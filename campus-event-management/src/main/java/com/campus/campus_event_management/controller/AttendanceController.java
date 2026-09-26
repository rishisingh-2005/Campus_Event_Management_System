package com.campus.campus_event_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campus.campus_event_management.entity.Attendance;
import com.campus.campus_event_management.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(
            AttendanceService attendanceService) {

        this.attendanceService = attendanceService;
    }

    // =========================================================
    // MARK ATTENDANCE - ONE STUDENT AT A TIME
    // =========================================================
    @PostMapping
    public Attendance markAttendance(
            @RequestParam Long userId,
            @RequestParam Long eventId,
            @RequestParam String status) {

        return attendanceService.markAttendance(
                userId,
                eventId,
                status
        );
    }

    // =========================================================
    // MARK ALL REGISTERED STUDENTS AS PRESENT
    // =========================================================
    @PostMapping("/event/{eventId}/mark-all-present")
    public String markAllPresent(
            @PathVariable Long eventId) {

        attendanceService.markAllPresent(eventId);

        return "All registered students marked as PRESENT";
    }

    // =========================================================
    // GET ATTENDANCE FOR AN EVENT
    // =========================================================
    @GetMapping("/event/{eventId}")
    public List<Attendance> getAttendanceByEvent(
            @PathVariable Long eventId) {

        return attendanceService.getAttendanceByEvent(eventId);
    }

    // =========================================================
    // GET ATTENDANCE OF A STUDENT
    // =========================================================
    @GetMapping("/user/{userId}")
    public List<Attendance> getAttendanceByUser(
            @PathVariable Long userId) {

        return attendanceService.getAttendanceByUser(userId);
    }
}
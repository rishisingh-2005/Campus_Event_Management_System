package com.campus.campus_event_management.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campus.campus_event_management.service.AdminDashboardService;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    public AdminDashboardController(AdminDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public Map<String, Object> getDashboardStats() {

        Map<String, Object> stats = new LinkedHashMap<>();

        stats.put("totalUsers",
                dashboardService.getTotalUsers());

        stats.put("totalEvents",
                dashboardService.getTotalEvents());

        stats.put("totalRegistrations",
                dashboardService.getTotalRegistrations());

        stats.put("totalFeedback",
                dashboardService.getTotalFeedback());

        stats.put("totalStudents",
                dashboardService.getTotalStudents());

        stats.put("totalOrganizers",
                dashboardService.getTotalOrganizers());

        stats.put("totalAdmins",
                dashboardService.getTotalAdmins());

        stats.put("approvedEvents",
                dashboardService.getApprovedEvents());

        stats.put("pendingEvents",
                dashboardService.getPendingEvents());

        stats.put("rejectedEvents",
                dashboardService.getRejectedEvents());

        stats.put("registeredParticipants",
                dashboardService.getRegisteredParticipants());

        stats.put("cancelledRegistrations",
                dashboardService.getCancelledRegistrations());

        stats.put("averageFeedbackRating",
        dashboardService.getAverageFeedbackRating());

        return stats;
    }
}

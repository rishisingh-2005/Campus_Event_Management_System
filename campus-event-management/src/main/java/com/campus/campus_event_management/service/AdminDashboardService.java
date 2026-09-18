package com.campus.campus_event_management.service;

import org.springframework.stereotype.Service;

import com.campus.campus_event_management.entity.Feedback;
import com.campus.campus_event_management.entity.Registration;
import com.campus.campus_event_management.repository.EventRepository;
import com.campus.campus_event_management.repository.FeedbackRepository;
import com.campus.campus_event_management.repository.RegistrationRepository;
import com.campus.campus_event_management.repository.UserRepository;

@Service
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final FeedbackRepository feedbackRepository;

    public AdminDashboardService(
            UserRepository userRepository,
            EventRepository eventRepository,
            RegistrationRepository registrationRepository,
            FeedbackRepository feedbackRepository) {

        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.feedbackRepository = feedbackRepository;
    }

    // Total users
    public long getTotalUsers() {
        return userRepository.count();
    }

    // Total events
    public long getTotalEvents() {
        return eventRepository.count();
    }

    // Total registrations
    public long getTotalRegistrations() {
        return registrationRepository.count();
    }

    // Total feedback
    public long getTotalFeedback() {
        return feedbackRepository.count();
    }

    // Total students
    public long getTotalStudents() {
        return userRepository.findAll()
                .stream()
                .filter(user ->
                        "STUDENT".equalsIgnoreCase(user.getRole()))
                .count();
    }

    // Total organizers
    public long getTotalOrganizers() {
        return userRepository.findAll()
                .stream()
                .filter(user ->
                        "ORGANIZER".equalsIgnoreCase(user.getRole()))
                .count();
    }

    // Total admins
    public long getTotalAdmins() {
        return userRepository.findAll()
                .stream()
                .filter(user ->
                        "ADMIN".equalsIgnoreCase(user.getRole()))
                .count();
    }

    // Approved events
    public long getApprovedEvents() {
        return eventRepository.findByStatus("APPROVED").size();
    }

    // Pending events
    public long getPendingEvents() {
        return eventRepository.findByStatus("PENDING").size();
    }

    // Rejected events
    public long getRejectedEvents() {
        return eventRepository.findByStatus("REJECTED").size();
    }

    // Registered participants
    public long getRegisteredParticipants() {
        return registrationRepository.findAll()
                .stream()
                .filter(registration ->
                        "REGISTERED".equalsIgnoreCase(
                                registration.getStatus()))
                .count();
    }

    // Cancelled registrations
    public long getCancelledRegistrations() {
        return registrationRepository.findAll()
                .stream()
                .filter(registration ->
                        "CANCELLED".equalsIgnoreCase(
                                registration.getStatus()))
                .count();
    }

    // Average feedback rating
    public double getAverageFeedbackRating() {
        return feedbackRepository.findAll()
                .stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
    }
}

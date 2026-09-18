package com.campus.campus_event_management.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.campus.campus_event_management.entity.Feedback;
import com.campus.campus_event_management.entity.Registration;
import com.campus.campus_event_management.repository.FeedbackRepository;
import com.campus.campus_event_management.repository.RegistrationRepository;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final RegistrationRepository registrationRepository;

    public FeedbackService(
            FeedbackRepository feedbackRepository,
            RegistrationRepository registrationRepository) {

        this.feedbackRepository = feedbackRepository;
        this.registrationRepository = registrationRepository;
    }

    // Submit feedback
    public Feedback submitFeedback(
            Long userId,
            Long eventId,
            Integer rating,
            String comment) {

        // Check whether student registered for event
        List<Registration> registrations =
                registrationRepository.findByUserId(userId);

        boolean registered = registrations.stream()
                .anyMatch(r ->
                        r.getEventId().equals(eventId)
                        && "REGISTERED".equalsIgnoreCase(
                                r.getStatus()
                        )
                );

        if (!registered) {
            throw new RuntimeException(
                    "Student is not registered for this event"
            );
        }

        // Rating must be between 1 and 5
        if (rating < 1 || rating > 5) {
            throw new RuntimeException(
                    "Rating must be between 1 and 5"
            );
        }

        // Check duplicate feedback
        Feedback existingFeedback =
                feedbackRepository
                        .findByUserIdAndEventId(userId, eventId)
                        .orElse(null);

        if (existingFeedback != null) {
            throw new RuntimeException(
                    "Feedback already submitted for this event"
            );
        }

        Feedback feedback = new Feedback();

        feedback.setUserId(userId);
        feedback.setEventId(eventId);
        feedback.setRating(rating);
        feedback.setComment(comment);
        feedback.setFeedbackDate(LocalDateTime.now());

        return feedbackRepository.save(feedback);
    }

    // Get feedback submitted by a student
    public List<Feedback> getFeedbackByUser(Long userId) {
        return feedbackRepository.findByUserId(userId);
    }

    // Get all feedback for an event
    public List<Feedback> getFeedbackByEvent(Long eventId) {
        return feedbackRepository.findByEventId(eventId);
    }
}

package com.campus.campus_event_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.campus.campus_event_management.entity.Feedback;
import com.campus.campus_event_management.service.FeedbackService;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public Feedback submitFeedback(
            @RequestParam Long userId,
            @RequestParam Long eventId,
            @RequestParam Integer rating,
            @RequestParam String comment) {

        return feedbackService.submitFeedback(
                userId, eventId, rating, comment
        );
    }

    @GetMapping("/user/{userId}")
    public List<Feedback> getFeedbackByUser(
            @PathVariable Long userId) {

        return feedbackService.getFeedbackByUser(userId);
    }

    @GetMapping("/event/{eventId}")
    public List<Feedback> getFeedbackByEvent(
            @PathVariable Long eventId) {

        return feedbackService.getFeedbackByEvent(eventId);
    }
}

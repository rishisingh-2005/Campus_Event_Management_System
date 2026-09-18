package com.campus.campus_event_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campus.campus_event_management.entity.Feedback;

public interface FeedbackRepository
        extends JpaRepository<Feedback, Long> {

    List<Feedback> findByUserId(Long userId);

    List<Feedback> findByEventId(Long eventId);

    Optional<Feedback> findByUserIdAndEventId(
            Long userId,
            Long eventId
    );
}

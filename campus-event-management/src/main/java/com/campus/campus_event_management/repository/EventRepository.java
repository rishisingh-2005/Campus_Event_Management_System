package com.campus.campus_event_management.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campus.campus_event_management.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatus(String status);

    List<Event> findByStatusAndTitleContainingIgnoreCase(
            String status,
            String title
    );

    List<Event> findByStatusAndCategoryIgnoreCase(
            String status,
            String category
    );

    List<Event> findByStatusAndDate(
            String status,
            LocalDate date
    );

    List<Event> findByOrganizerId(Long organizerId);
}

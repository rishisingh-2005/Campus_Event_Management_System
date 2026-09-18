package com.campus.campus_event_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campus.campus_event_management.entity.Registration;

public interface RegistrationRepository
        extends JpaRepository<Registration, Long> {

    List<Registration> findByUserId(Long userId);

    List<Registration> findByEventId(Long eventId);

    boolean existsByUserIdAndEventId(Long userId, Long eventId);
}

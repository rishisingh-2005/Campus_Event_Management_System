package com.campus.campus_event_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campus.campus_event_management.entity.Attendance;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Long> {

    List<Attendance> findByEventId(Long eventId);

    List<Attendance> findByUserId(Long userId);

    Optional<Attendance> findByUserIdAndEventId(
            Long userId,
            Long eventId
    );
}

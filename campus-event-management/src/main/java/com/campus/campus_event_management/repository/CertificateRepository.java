package com.campus.campus_event_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campus.campus_event_management.entity.Certificate;

public interface CertificateRepository
        extends JpaRepository<Certificate, Long> {

    List<Certificate> findByUserId(Long userId);

    Optional<Certificate> findByUserIdAndEventId(
            Long userId,
            Long eventId
    );
}

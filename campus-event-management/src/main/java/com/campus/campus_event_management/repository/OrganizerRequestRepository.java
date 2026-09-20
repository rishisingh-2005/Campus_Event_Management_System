package com.campus.campus_event_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campus.campus_event_management.entity.OrganizerRequest;

public interface OrganizerRequestRepository
        extends JpaRepository<OrganizerRequest, Long> {

    List<OrganizerRequest> findByStatus(String status);

    Optional<OrganizerRequest> findByEmail(String email);
}
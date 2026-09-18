package com.campus.campus_event_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campus.campus_event_management.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}

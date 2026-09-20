package com.campus.campus_event_management.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.campus.campus_event_management.entity.OrganizerRequest;
import com.campus.campus_event_management.entity.User;
import com.campus.campus_event_management.repository.OrganizerRequestRepository;
import com.campus.campus_event_management.repository.UserRepository;

@Service
public class OrganizerRequestService {

    private final OrganizerRequestRepository organizerRequestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public OrganizerRequestService(
            OrganizerRequestRepository organizerRequestRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.organizerRequestRepository = organizerRequestRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public OrganizerRequest createRequest(
            OrganizerRequest request) {

        if (request.getEmail() == null
                || request.getEmail().trim().isEmpty()) {

            throw new RuntimeException(
                    "Email is required"
            );
        }

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException(
                    "An account already exists with this email"
            );
        }

        if (organizerRequestRepository
                .findByEmail(email)
                .isPresent()) {

            throw new RuntimeException(
                    "An organizer request already exists with this email"
            );
        }

        request.setEmail(email);

        request.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        request.setStatus("PENDING");

        return organizerRequestRepository.save(request);
    }

    public List<OrganizerRequest> getPendingRequests() {

        return organizerRequestRepository
                .findByStatus("PENDING");
    }

    public OrganizerRequest approveRequest(Long id) {

        OrganizerRequest request =
                organizerRequestRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Organizer request not found"
                                )
                        );

        if (!"PENDING".equalsIgnoreCase(
                request.getStatus())) {

            throw new RuntimeException(
                    "This request has already been processed"
            );
        }

        if (userRepository
                .findByEmail(request.getEmail())
                .isPresent()) {

            throw new RuntimeException(
                    "An account already exists with this email"
            );
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole("ORGANIZER");

        userRepository.save(user);

        request.setStatus("APPROVED");

        return organizerRequestRepository.save(request);
    }

    public OrganizerRequest rejectRequest(Long id) {

        OrganizerRequest request =
                organizerRequestRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Organizer request not found"
                                )
                        );

        if (!"PENDING".equalsIgnoreCase(
                request.getStatus())) {

            throw new RuntimeException(
                    "This request has already been processed"
            );
        }

        request.setStatus("REJECTED");

        return organizerRequestRepository.save(request);
    }
}
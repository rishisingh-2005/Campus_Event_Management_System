package com.campus.campus_event_management.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.campus.campus_event_management.dto.LoginResponse;
import com.campus.campus_event_management.entity.User;
import com.campus.campus_event_management.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    // ==========================================
    // CREATE USER
    // ==========================================

    public User createUser(User user) {

        // Check whether email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {

            throw new RuntimeException(
                "Email already registered"
            );
        }


        // ==========================================
        // SECURITY:
        // PUBLIC REGISTRATION CAN ONLY CREATE
        // STUDENT ACCOUNTS
        // ==========================================

        user.setRole("STUDENT");


        // ==========================================
        // ENCRYPT PASSWORD
        // ==========================================

        user.setPassword(
            passwordEncoder.encode(
                user.getPassword()
            )
        );


        // Save user
        return userRepository.save(user);
    }


    // ==========================================
    // GET ALL USERS
    // ==========================================

    public List<User> getAllUsers() {

        return userRepository.findAll();

    }


    // ==========================================
    // GET USER BY ID
    // ==========================================

    public User getUserById(Long id) {

        return userRepository
                .findById(id)
                .orElse(null);

    }


    // ==========================================
    // DELETE USER
    // ==========================================

    public void deleteUser(Long id) {

        userRepository.deleteById(id);

    }


    // ==========================================
    // GET USER BY EMAIL
    // ==========================================

    public User getUserByEmail(String email) {

        return userRepository
                .findByEmail(email)
                .orElse(null);

    }


    // ==========================================
    // LOGIN USER
    // ==========================================

    public LoginResponse login(
            String email,
            String password) {


        // Find user by email
        User user = userRepository
                .findByEmail(email)
                .orElse(null);


        // ==========================================
        // CHECK USER AND PASSWORD
        // ==========================================

        if (user == null ||
            !passwordEncoder.matches(
                password,
                user.getPassword()
            )) {

            throw new RuntimeException(
                "Invalid email or password"
            );
        }


        // ==========================================
        // GENERATE JWT TOKEN
        // ==========================================

        String token = jwtService.generateToken(
            user.getEmail(),
            user.getRole()
        );


        // ==========================================
        // RETURN LOGIN RESPONSE
        // ==========================================

        return new LoginResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            token
        );
    }
}
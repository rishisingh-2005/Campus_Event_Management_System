package com.campus.campus_event_management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campus.campus_event_management.dto.LoginRequest;
import com.campus.campus_event_management.dto.LoginResponse;
import com.campus.campus_event_management.entity.User;
import com.campus.campus_event_management.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // ==========================================
    // CREATE USER
    // ==========================================

    @PostMapping
    public User createUser(@RequestBody User user) {

        return userService.createUser(user);

    }


    // ==========================================
    // LOGIN
    // ==========================================

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {

        return userService.login(
                request.getEmail(),
                request.getPassword()
        );

    }


    // ==========================================
    // GET ALL USERS
    // ==========================================

    @GetMapping
    public List<User> getAllUsers() {

        return userService.getAllUsers();

    }


    // ==========================================
    // GET USER BY ID
    // ==========================================

    @GetMapping("/{id}")
    public User getUserById(
            @PathVariable Long id) {

        return userService.getUserById(id);

    }


    // ==========================================
    // GET USER BY EMAIL
    // ==========================================

    @GetMapping("/email/{email}")
    public User getUserByEmail(
            @PathVariable String email) {

        return userService.getUserByEmail(email);

    }


    // ==========================================
    // DELETE USER
    // ==========================================

    @DeleteMapping("/{id}")
    public String deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return "User deleted successfully";

    }
}

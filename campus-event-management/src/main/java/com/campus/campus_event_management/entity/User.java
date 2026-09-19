package com.campus.campus_event_management.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {

    // ===============================
    // ID
    // ===============================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // ===============================
    // NAME
    // ===============================

    @NotBlank(message = "Name is required")
    private String name;


    // ===============================
    // EMAIL
    // ===============================

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;


    // ===============================
    // PASSWORD
    // ===============================

    /*
     * WRITE_ONLY means:
     *
     * - Password CAN be received from frontend
     * - Password WILL NOT be returned in JSON responses
     *
     * This is important because registration needs to
     * receive the password while protecting it from API output.
     */

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password is required")
    private String password;


    // ===============================
    // ROLE
    // ===============================

    private String role;


    // ===============================
    // DEFAULT CONSTRUCTOR
    // ===============================

    public User() {
    }


    // ===============================
    // GET ID
    // ===============================

    public Long getId() {
        return id;
    }


    // ===============================
    // SET ID
    // ===============================

    public void setId(Long id) {
        this.id = id;
    }


    // ===============================
    // GET NAME
    // ===============================

    public String getName() {
        return name;
    }


    // ===============================
    // SET NAME
    // ===============================

    public void setName(String name) {
        this.name = name;
    }


    // ===============================
    // GET EMAIL
    // ===============================

    public String getEmail() {
        return email;
    }


    // ===============================
    // SET EMAIL
    // ===============================

    public void setEmail(String email) {
        this.email = email;
    }


    // ===============================
    // GET PASSWORD
    // ===============================

    public String getPassword() {
        return password;
    }


    // ===============================
    // SET PASSWORD
    // ===============================

    public void setPassword(String password) {
        this.password = password;
    }


    // ===============================
    // GET ROLE
    // ===============================

    public String getRole() {
        return role;
    }


    // ===============================
    // SET ROLE
    // ===============================

    public void setRole(String role) {
        this.role = role;
    }
}
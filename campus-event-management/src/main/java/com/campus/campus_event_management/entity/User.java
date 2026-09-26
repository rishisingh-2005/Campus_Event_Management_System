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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password is required")
    private String password;


    // ===============================
    // ROLE
    // ===============================

    private String role;


    // ===============================
    // STUDENT PROFILE
    // ===============================

    /*
     * These fields are used for STUDENT accounts.
     *
     * They are intentionally not @NotBlank because
     * ORGANIZER and ADMIN accounts do not require
     * student profile information.
     */

    private String rollNo;

    private String branch;

    private String section;


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


    // ===============================
    // GET ROLL NO
    // ===============================

    public String getRollNo() {
        return rollNo;
    }


    // ===============================
    // SET ROLL NO
    // ===============================

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }


    // ===============================
    // GET BRANCH
    // ===============================

    public String getBranch() {
        return branch;
    }


    // ===============================
    // SET BRANCH
    // ===============================

    public void setBranch(String branch) {
        this.branch = branch;
    }


    // ===============================
    // GET SECTION
    // ===============================

    public String getSection() {
        return section;
    }


    // ===============================
    // SET SECTION
    // ===============================

    public void setSection(String section) {
        this.section = section;
    }
}

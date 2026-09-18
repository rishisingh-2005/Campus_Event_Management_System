package com.campus.campus_event_management.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long organizerId;

    private String title;

    private String description;

    private LocalDate date;

    private LocalTime time;

    private String venue;

    private String category;

    private Integer capacity;

    private LocalDate registrationDeadline;

    private String status;


    public Event() {
    }


    public Event(
            Long organizerId,
            String title,
            String description,
            LocalDate date,
            LocalTime time,
            String venue,
            String category,
            Integer capacity,
            LocalDate registrationDeadline,
            String status) {

        this.organizerId = organizerId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.category = category;
        this.capacity = capacity;
        this.registrationDeadline = registrationDeadline;
        this.status = status;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }


    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }


    public LocalDate getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(
            LocalDate registrationDeadline) {

        this.registrationDeadline =
                registrationDeadline;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
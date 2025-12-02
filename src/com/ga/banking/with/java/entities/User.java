package com.ga.banking.with.java.entities;

import com.ga.banking.with.java.enums.Status;
import com.ga.banking.with.java.enums.UserRole;

import java.time.LocalDate;
import java.util.UUID;

public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String username;
    private String passwordHash;
    private String email;
    private String phoneNumber;
    private int failedAttempts;
    private LocalDate lockUntil;
    private LocalDate createdAt;
    private UserRole role;
    private Status status;
    private String salt;


    public User(String firstName, String lastName, String userName, String passwordHash, String email,
                  String phoneNumber, UserRole role) {
        this.userId = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = userName;
        this.passwordHash = passwordHash;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.failedAttempts = 0;
        this.lockUntil = null;
        this.createdAt = LocalDate.now();
        this.role = role;
        this.status = Status.Active;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordString) {
        this.passwordHash = passwordString;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public void setLockUntil(LocalDate lockUntil) {
        this.lockUntil = lockUntil;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String toString() {
        return "{" +
                "\"userId\": \"" + userId + "\"," +
                "\"firstName\": \"" + firstName + "\"," +
                "\"lastName\": \"" + lastName + "\"," +
                "\"username\": \"" + username + "\"," +
                "\"passwordHash\": \"" + passwordHash + "\"," +
                "\"email\": \"" + email + "\"," +
                "\"phoneNumber\": \"" + phoneNumber + "\"," +
                "\"failedAttempts\": " + failedAttempts + "," +
                "\"lockUntil\": \"" + (lockUntil != null ? lockUntil : "null") + "\"," +
                "\"createdAt\": \"" + createdAt + "\"," +
                "\"role\": \"" + role + "\"," +
                "\"status\": \"" + status + "\"," +
                "\"salt\": \"" + salt + "\"" +
                "}";
    }

}

package com.ga.banking.with.java.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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


    public User(String firstName, String lastName, String userName, String passwordHash, String salt, String email,
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
        this.salt = salt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordString) {
        this.passwordHash = passwordString;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public LocalDate getLockUntil() {
        return lockUntil;
    }

    public void setLockUntil(LocalDate lockUntil) {
        this.lockUntil = lockUntil;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSalt() {
        return salt;
    }

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

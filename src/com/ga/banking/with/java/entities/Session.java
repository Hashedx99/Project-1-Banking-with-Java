package com.ga.banking.with.java.entities;

import com.ga.banking.with.java.enums.SessionStatus;
import com.ga.banking.with.java.enums.UserRole;

public class Session {
    private User user;
    private SessionStatus status;
    private UserRole role;

    public Session() {
        this.status = SessionStatus.Unauthenticated;
    }

    public Session(User user) {
        this.user = user;
        this.status = SessionStatus.Active;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean isAuthenticated() {
        return this.status == SessionStatus.Active;
    }

    public boolean isNotAuthenticated() {
        return this.status == SessionStatus.Unauthenticated;
    }

    public void initializeSession(User user) {
        this.user = user;
        this.status = SessionStatus.Active;
        this.role = user.getRole();
    }
}

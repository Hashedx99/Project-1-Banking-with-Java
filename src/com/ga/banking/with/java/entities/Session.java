package com.ga.banking.with.java.entities;

import com.ga.banking.with.java.enums.SessionStatus;

public class Session {
    private User user;
    private SessionStatus status;

    public Session() {
        this.status = SessionStatus.Active;
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

    public void initializeSession(User user) {
        this.user = user;
        this.status = SessionStatus.Active;
    }
}

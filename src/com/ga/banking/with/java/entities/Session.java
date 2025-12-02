package com.ga.banking.with.java.entities;

public class Session {
    private User user;

    public Session(User user) {
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

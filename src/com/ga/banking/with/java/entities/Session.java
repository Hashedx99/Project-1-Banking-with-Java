package com.ga.banking.with.java.entities;

import com.ga.banking.with.java.enums.SessionStatus;
import com.ga.banking.with.java.enums.UserRole;
import com.ga.banking.with.java.features.Auth;

import java.util.Scanner;

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

    public void terminateSession() {
        this.status = SessionStatus.Terminated;
    }

    public boolean isNotTerminated() {
        return this.status != SessionStatus.Terminated;
    }

    public void getUserMenu(Auth auth) {
        Scanner input = new Scanner(System.in);
        if (this.isAuthenticated() && this.user.getRole() == UserRole.Banker) {
            System.out.println("Banker Menu:");
            System.out.println("Choose an option:");
            System.out.println("1. Create Customer Account");
            System.out.println("Q. Quit");
            switch (input.nextLine()) {
                case "1" -> {
                    auth.createCustomerAccount();
                }
                case "Q", "q" -> {
                    System.out.println("Exiting Banker Menu.");
                    this.terminateSession();
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
}

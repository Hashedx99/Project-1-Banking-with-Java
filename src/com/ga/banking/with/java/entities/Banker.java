package com.ga.banking.with.java.entities;

import com.ga.banking.with.java.enums.UserRole;

public class Banker extends User {

    public Banker(String firstName, String lastName, String userName, String password, byte[] salt, String email,
                  String phoneNumber) {
        super(firstName, lastName, userName, password, salt, email, phoneNumber, UserRole.Banker);
    }



}

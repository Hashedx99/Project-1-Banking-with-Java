package com.ga.banking.with.java.entities;

import com.ga.banking.with.java.enums.UserRole;

public class Customer extends User {

    public Customer(String firstName, String lastName, String userName, String password, String salt, String email,
                    String phoneNumber) {
        super(firstName, lastName, userName, password, salt, email, phoneNumber, UserRole.Customer);
    }

}

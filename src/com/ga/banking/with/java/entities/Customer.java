package com.ga.banking.with.java.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ga.banking.with.java.enums.UserRole;

public class Customer extends User {


    public Customer(String firstName, String lastName, String userName, String password, String salt, String email,
                    String phoneNumber) {
        super(firstName, lastName, userName, password, salt, email, phoneNumber, UserRole.Customer);
    }

    @JsonCreator
    public Customer(
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("username") String userName,
            @JsonProperty("passwordHash") String passwordHash,
            @JsonProperty("salt") String salt,
            @JsonProperty("email") String email,
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("role") UserRole role) {
        super(firstName, lastName, userName, passwordHash, salt, email, phoneNumber, role);
    }

}

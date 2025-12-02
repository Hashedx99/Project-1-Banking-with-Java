package com.ga.banking.with.java;

import com.ga.banking.with.java.entities.Banker;
import com.ga.banking.with.java.helpers.BankerFileHandler;
import com.ga.banking.with.java.helpers.PasswordHasher;

import java.util.Scanner;

public class BankingApp {
    public static void main(String[] args) {
        byte[] salt = PasswordHasher.generateSalt();
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to Shadow Moses Bank \n" +
                "The System is and has no accounts, please create a Banker account\n");
//        System.out.println("Please enter your First Name: ");
//        String firstName = input.nextLine();
//        System.out.println("please enter your Last Name: ");
//        String lastName = input.nextLine();
//        System.out.println("Please enter your username: ");
//        String username = input.nextLine();
//        System.out.println("Please enter your password: ");
//        String password = PasswordHasher.getPasswordHash(input.nextLine(), salt);
//        System.out.println("Please enter your email: ");
//        String email = input.nextLine();
//        System.out.println("Please enter your phone number: ");
//        String phoneNumber = input.nextLine();


        Banker banker = new Banker("Hamza", "Ali", "Hashed", PasswordHasher.getPasswordHash("123456", salt),
                "hshabx99@gmail.com", "36080069");

        BankerFileHandler bankerFileHandler = new BankerFileHandler();
        bankerFileHandler.writeToFile(banker.getUsername(), banker.getUserId(), banker.toJson());
        System.out.println(banker.getPasswordHash());
        System.out.println("Enter password to validate: ");
        String enteredPassword = input.nextLine();
        System.out.println(PasswordHasher.validatePassword(enteredPassword, salt, banker.getPasswordHash()) ?
                "Password is valid!" : "Invalid Password!");
    }
}

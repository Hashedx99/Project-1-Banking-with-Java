package com.ga.banking.with.java;

import com.ga.banking.with.java.Features.Auth;
import com.ga.banking.with.java.entities.Session;
import com.ga.banking.with.java.helpers.CommonUtil;

public class BankingApp {

    private static final Session session = new Session();
    private static final Auth auth = new Auth();

    public static void main(String[] args) {

        CommonUtil.printSeparatorLine();
        System.out.println(" ".repeat(36) + "Welcome to Shadow Moses Bank" + " ".repeat(36));
        CommonUtil.printSeparatorLine();
        session.initializeSession(auth.authenticate());
        while (session.isNotTerminated()) {

        }

    }

}

package com.ga.banking.with.java;

import com.ga.banking.with.java.entities.User;
import com.ga.banking.with.java.enums.AccountType;
import com.ga.banking.with.java.service.Auth;
import com.ga.banking.with.java.service.Session;
import com.ga.banking.with.java.helpers.CommonUtil;

public class BankingApp {

    private static final Session session = new Session();
    private static final Auth auth = new Auth();

    public static void main(String[] args) {

        CommonUtil.printSeparatorLine();
        System.out.println(" ".repeat(36) + "Welcome to Shadow Moses Bank" + " ".repeat(36));
        CommonUtil.printSeparatorLine();
        User user = auth.authenticate();
        session.initializeSession(user, auth.loadUserAccounts(user), auth.loadUserDebitCard(user,
                        AccountType.Savings), auth.loadUserDebitCard(user, AccountType.Checking),
                auth.loadUserTransactions(user));
        while (session.isNotTerminated()) {
            session.getUserMenu(auth);
        }

    }

}

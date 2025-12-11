package com.ga.banking.with.java.service;

import com.ga.banking.with.java.entities.*;
import com.ga.banking.with.java.enums.AccountType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Files.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthTest {

    @Test
    @DisplayName("if account with same id exists in Accounts directory, getAccountById returns the account")
    void returnsAccountWhenAccountExistsInAccountsDirectory() throws Exception {
        String previousUserDir = System.getProperty("user.dir");
        Path tempDir = createTempDirectory("banking-data-");
        try {
            System.setProperty("user.dir", tempDir.toAbsolutePath().toString());
            Path dataAccounts = tempDir.resolve("Data").resolve("Accounts");
            createDirectories(dataAccounts);
            Account account = new SavingsAccount("cust-1", 150.0);
            ObjectMapper mapper = new ObjectMapper();
            Path file = dataAccounts.resolve("testAccount.json");
            mapper.writeValue(file.toFile(), List.of(account));
            Auth auth = new Auth();
            Account found = auth.getAccountById(account.getAccountId());
            assertNotNull(found);
            assertEquals(account.getAccountId(), found.getAccountId());
        } finally {
            System.setProperty("user.dir", previousUserDir);
        }
    }

    @Test
    @DisplayName("if Accounts directory is missing, getAccountById returns null")
    void returnsNullWhenAccountsDirectoryIsMissing() throws Exception {
        String previousUserDir = System.getProperty("user.dir");
        Path tempDir = createTempDirectory("banking-data-");
        try {
            System.setProperty("user.dir", tempDir.toAbsolutePath().toString());
            Auth auth = new Auth();
            assertNull(auth.getAccountById("non-existent-account-id"));
        } finally {
            System.setProperty("user.dir", previousUserDir);
        }
    }

    @Test
    @DisplayName("if user is Banker, loadUserAccounts returns empty list")
    void returnsEmptyListOfAccountsForBankerRole() {
        Banker banker = new Banker("First", "Last", "bankerUser", "passHash", "salt", "email@email.com", "12345678");
        Auth auth = new Auth();
        java.util.List<Account> accounts = auth.loadUserAccounts(banker);
        assertNotNull(accounts);
        assertTrue(accounts.isEmpty());
    }

    @Test
    @DisplayName("if user is Banker, loadUserDebitCard returns null")
    void loadUserDebitCardReturnsNullForBankerRole() {
        Banker banker = new Banker("First", "Last", "bankerUser", "passHash", "salt", "email@email.com", "12345678");
        Auth auth = new Auth();
        assertNull(auth.loadUserDebitCard(banker, AccountType.Savings));
    }

    @Test
    @DisplayName("if user is Banker, loadUserTransactions returns empty list")
    void loadUserTransactionsReturnsEmptyListForBankerRole() {
        Banker banker = new Banker("First", "Last", "bankerUser", "passHash", "salt", "email@email.com", "12345678");
        Auth auth = new Auth();
        List<Transaction> transactions = auth.loadUserTransactions(banker);
        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }


}
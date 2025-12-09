package com.ga.banking.with.java.helpers;

import com.ga.banking.with.java.service.PasswordHasher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PasswordHasherTest {

    @Test
    @DisplayName("Generate Salt Test: Should generate a non-null, non-empty, unique salt of 16 bytes")
    void generateSalt() {
        String salt1 = PasswordHasher.generateSalt();
        String salt2 = PasswordHasher.generateSalt();
        assertNotNull(salt1);
        assertNotNull(salt2);
        assertFalse(salt1.isEmpty());
        assertFalse(salt2.isEmpty());
        assertNotEquals(salt1, salt2);
        assertEquals(16, Base64.getDecoder().decode(salt1).length);
        assertEquals(16, Base64.getDecoder().decode(salt2).length);
    }

    @Test
    @DisplayName("Get Password Hash Test: Should return consistent hashes for same input and different hashes for different inputs")
    void getPasswordHash() {
        String password = "123456";
        String salt = PasswordHasher.generateSalt();
        String hash1 = PasswordHasher.getPasswordHash(password, salt);
        String hash2 = PasswordHasher.getPasswordHash(password, salt);
        assertNotNull(hash1);
        assertNotNull(hash2);
        assertEquals(hash1, hash2);

        String differentPassword = "654321";
        String hash3 = PasswordHasher.getPasswordHash(differentPassword, salt);
        assertNotEquals(hash1, hash3);

        String differentSalt = PasswordHasher.generateSalt();
        String hash4 = PasswordHasher.getPasswordHash(password, differentSalt);
        assertNotEquals(hash1, hash4);
    }

    @Test
    @DisplayName("Validate Password Test: Should correctly validate matching and non-matching passwords")
    void validatePassword() {
        String password = "123456";
        String salt = PasswordHasher.generateSalt();
        String hashedPassword = PasswordHasher.getPasswordHash(password, salt);

        assertTrue(PasswordHasher.validatePassword(password, salt, hashedPassword));
        assertFalse(PasswordHasher.validatePassword("wrongPassword", salt, hashedPassword));
    }
}
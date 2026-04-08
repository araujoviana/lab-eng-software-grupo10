package com.zelodesk.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "123456";
        String hash = encoder.encode(password);
        
        System.out.println("Password: " + password);
        System.out.println("BCrypt Hash: " + hash);
        System.out.println("\nTest verification:");
        System.out.println("Matches: " + encoder.matches(password, hash));
        
        // Test with existing hash from import.sql
        String existingHash = "$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYIYBM4yus7Y0bTi";
        System.out.println("\nExisting hash matches: " + encoder.matches(password, existingHash));
    }
}

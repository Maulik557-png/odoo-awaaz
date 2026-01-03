package com.odoo.hackathon.hrms.service.impl;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.odoo.hackathon.hrms.service.PasswordGeneratorService;

@Service
public class PasswordGeneratorServiceImpl implements PasswordGeneratorService {
    
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*";
    private static final String ALL_CHARS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARS;
    
    private final SecureRandom random = new SecureRandom();
    
    /**
     * Generates a secure random password of specified length
     * Password will contain at least 1 uppercase, 1 lowercase, 1 digit, and 1 special character
     */
    public String generatePassword(int length) {
        if (length < 8) {
            length = 8; // Minimum password length
        }
        
        StringBuilder password = new StringBuilder(length);
        
        // Ensure at least one character from each category
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));
        
        // Fill the rest with random characters
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }
        
        // Shuffle the password to avoid predictable pattern
        return shuffleString(password.toString());
    }
    
    public String shuffleString(String input) {
        List<Character> characters = input.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.toList());
        Collections.shuffle(characters, random);
        return characters.stream()
            .map(String::valueOf)
            .collect(Collectors.joining());
    }
}

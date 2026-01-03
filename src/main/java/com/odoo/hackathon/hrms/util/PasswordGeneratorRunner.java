package com.odoo.hackathon.hrms.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGeneratorRunner {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("Admin@123: " + encoder.encode("Admin@123"));
        System.out.println("Password@123: " + encoder.encode("Password@123"));
    }
}

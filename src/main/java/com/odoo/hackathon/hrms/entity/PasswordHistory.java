package com.odoo.hackathon.hrms.entity;

import java.time.Instant;

import lombok.Data;

@Data
public class PasswordHistory {
    private String username;
    private String passwordHash;
    private Instant changedAt = Instant.now();
}

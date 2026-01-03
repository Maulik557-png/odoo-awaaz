package com.odoo.hackathon.hrms.service;

public interface JwtService {
    // Generate a JWT for the given username and role
    String generateToken(String username, String role);
}
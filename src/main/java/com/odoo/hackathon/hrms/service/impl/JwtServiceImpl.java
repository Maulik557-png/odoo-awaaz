package com.odoo.hackathon.hrms.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.odoo.hackathon.hrms.service.JwtService;
import com.odoo.hackathon.hrms.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtTokenProvider provider;

    @Value("${jwt.dev-mode:true}")
    private boolean devMode;

    @Override
    public String generateToken(String username, String role) {
        if (devMode) {
            String payload = username + ":" + (role == null ? "USER" : role);
            String encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
            return "devtoken-" + encoded;
        }
        return provider.generateToken(username, role);
    }
}
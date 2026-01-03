package com.odoo.hackathon.hrms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.odoo.hackathon.hrms.dto.request.LoginRequest;
import com.odoo.hackathon.hrms.dto.request.AdminSignUpRequest;
import com.odoo.hackathon.hrms.dto.response.LoginResponse;
import com.odoo.hackathon.hrms.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
        LoginResponse resp = authService.login(request);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<String> signupAdmin(@Validated @RequestBody AdminSignUpRequest request) {
        String result = authService.signupAdmin(request);
        return ResponseEntity.ok(result);
    }
}
package com.odoo.hackathon.hrms.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.odoo.hackathon.hrms.dto.request.AdminSignUpRequest;
import com.odoo.hackathon.hrms.dto.request.ChangePasswordRequest;
import com.odoo.hackathon.hrms.dto.request.LoginRequest;
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

    @PostMapping({"/signup/admin", "/signup", "/register"})
    public ResponseEntity<String> signupAdmin(@Validated @RequestBody AdminSignUpRequest request) {
        String result = authService.signupAdmin(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/signup/employee")
    public ResponseEntity<String> signupEmployee(@Validated @RequestBody AdminSignUpRequest request) {
        if (request.getRole() == null || request.getRole().isBlank()) {
            request.setRole("Employee");
        }
        String result = authService.signupAdmin(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Principal principal,
            @Validated @RequestBody ChangePasswordRequest request) {
        authService.changePassword(principal.getName(), request);
        return ResponseEntity.ok("Password changed successfully");
    }
}
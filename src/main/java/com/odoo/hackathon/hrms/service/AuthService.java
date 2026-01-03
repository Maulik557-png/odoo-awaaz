package com.odoo.hackathon.hrms.service;

import com.odoo.hackathon.hrms.dto.request.AdminSignUpRequest;
import com.odoo.hackathon.hrms.dto.request.LoginRequest;
import com.odoo.hackathon.hrms.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    // returns generated loginId for employee or a success message for admin
    String signupAdmin(AdminSignUpRequest request);
}
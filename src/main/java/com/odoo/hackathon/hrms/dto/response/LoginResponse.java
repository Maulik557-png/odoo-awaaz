package com.odoo.hackathon.hrms.dto.response;

import com.odoo.hackathon.hrms.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String loginId;
    private String name;
    private String email;
    private Role role;
    private boolean isFirstLogin;
    private String message;
}
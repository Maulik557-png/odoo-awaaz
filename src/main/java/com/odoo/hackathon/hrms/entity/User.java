package com.odoo.hackathon.hrms.entity;

import java.time.LocalDateTime;

import com.odoo.hackathon.hrms.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String loginId; // OIJODO20220001
    private String password;
    private Role role; // ADMIN, HR_OFFICER, EMPLOYEE
    private Boolean isFirstLogin = true;
    private Boolean isActive = true;
    private Employee employee;
    private Company company;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
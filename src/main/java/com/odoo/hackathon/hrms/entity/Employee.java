package com.odoo.hackathon.hrms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.odoo.hackathon.hrms.enums.EmploymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfJoining;
    private String department;
    private String designation;
    private String about;
    private User user;
    @Builder.Default
    private Company company = new Company();
    @Builder.Default
    private EmploymentStatus status = EmploymentStatus.ACTIVE;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
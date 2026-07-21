package com.odoo.hackathon.hrms.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    private Long id;
    private String name;
    private String companyCode; // OI for Odoo India
    private String email;
    private String phone;
    @Builder.Default
    private List<User> users = new ArrayList<>();
    @Builder.Default
    private List<Employee> employees = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
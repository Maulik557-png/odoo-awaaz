package com.odoo.hackathon.hrms.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeSkill {
    private Long id;
    private Long employeeId;
    private String skillName;
    private String proficiency; // 'BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT'
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

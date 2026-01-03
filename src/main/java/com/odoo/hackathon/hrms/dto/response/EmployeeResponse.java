package com.odoo.hackathon.hrms.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class EmployeeResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfJoining;
    private String department;
    private String designation;
    private String about;
    private String status;
    private SalaryResponse currentSalary;
    private List<SkillResponse> skills;
}

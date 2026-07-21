package com.odoo.hackathon.hrms.dto.request;

import com.odoo.hackathon.hrms.enums.EmploymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String department;
    private String designation;
    private String about;
    private EmploymentStatus status;
}

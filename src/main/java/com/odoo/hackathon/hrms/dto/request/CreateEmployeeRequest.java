package com.odoo.hackathon.hrms.dto.request;

import java.time.LocalDate;

import com.odoo.hackathon.hrms.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Phone is required")
    private String phone;
    
    @NotNull(message = "Date of joining is required")
    private LocalDate dateOfJoining;
    
    @NotBlank(message = "Department is required")
    private String department;
    
    @NotBlank(message = "Designation is required")
    private String designation;
    
    // Role will be EMPLOYEE by default, but HR can specify HR_OFFICER
    private Role role = Role.EMPLOYEE;
}
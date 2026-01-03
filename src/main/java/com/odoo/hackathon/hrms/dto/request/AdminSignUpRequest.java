package com.odoo.hackathon.hrms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminSignUpRequest {
    @NotBlank(message = "Company name is required")
    private String companyName;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{9,14}$", message = "Invalid phone number")
    private String phone;
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    // optional role: "Admin" or "Employee" (default Admin for this endpoint)
    private String role;
}
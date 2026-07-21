package com.odoo.hackathon.hrms.service;

import java.util.List;

import com.odoo.hackathon.hrms.dto.request.CreateEmployeeRequest;
import com.odoo.hackathon.hrms.dto.request.UpdateProfileRequest;
import com.odoo.hackathon.hrms.dto.response.EmployeeResponse;

public interface EmployeeService {
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse getEmployeeById(Long id);
    EmployeeResponse getEmployeeProfile(String userLoginId);
    EmployeeResponse createEmployee(CreateEmployeeRequest request);
    EmployeeResponse updateEmployee(Long id, UpdateProfileRequest request);
}
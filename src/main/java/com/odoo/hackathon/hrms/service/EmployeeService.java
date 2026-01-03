package com.odoo.hackathon.hrms.service;

import java.util.List;

import com.odoo.hackathon.hrms.dto.response.EmployeeResponse;

public interface EmployeeService {
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse getEmployeeById(Long id);
}
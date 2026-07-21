package com.odoo.hackathon.hrms.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.odoo.hackathon.hrms.dto.request.CreateEmployeeRequest;
import com.odoo.hackathon.hrms.dto.request.UpdateProfileRequest;
import com.odoo.hackathon.hrms.dto.response.EmployeeResponse;
import com.odoo.hackathon.hrms.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/api/employees", "/api/employee"})
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> list() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/profile")
    public ResponseEntity<EmployeeResponse> getProfile(Principal principal) {
        return ResponseEntity.ok(employeeService.getEmployeeProfile(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@Validated @RequestBody CreateEmployeeRequest request) {
        return ResponseEntity.ok(employeeService.createEmployee(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }
}
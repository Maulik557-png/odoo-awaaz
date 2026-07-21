package com.odoo.hackathon.hrms.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.odoo.hackathon.hrms.dto.request.UpdateSalaryRequest;
import com.odoo.hackathon.hrms.dto.response.PayrollResponse;
import com.odoo.hackathon.hrms.service.PayrollService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    @GetMapping
    public ResponseEntity<List<PayrollResponse>> getAllPayroll() {
        return ResponseEntity.ok(payrollService.getAllPayroll());
    }

    @GetMapping("/my-payroll")
    public ResponseEntity<PayrollResponse> getMyPayroll(Principal principal) {
        return ResponseEntity.ok(payrollService.getMyPayroll(principal.getName()));
    }

    @PostMapping("/salary")
    public ResponseEntity<PayrollResponse> updateSalary(@Validated @RequestBody UpdateSalaryRequest request) {
        return ResponseEntity.ok(payrollService.updateSalary(request));
    }
}

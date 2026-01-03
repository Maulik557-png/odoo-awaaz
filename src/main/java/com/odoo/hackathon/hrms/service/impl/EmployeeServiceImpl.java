package com.odoo.hackathon.hrms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.odoo.hackathon.hrms.dto.response.EmployeeResponse;
import com.odoo.hackathon.hrms.entity.Employee;
import com.odoo.hackathon.hrms.repository.EmployeeRepository;
import com.odoo.hackathon.hrms.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeResponse> res = new ArrayList<>();
        for (Employee e : employees) {
            EmployeeResponse r = new EmployeeResponse();
            r.setId(String.valueOf(e.getId()));
            r.setFirstName(e.getFirstName());
            r.setLastName(e.getLastName());
            r.setEmail(e.getEmail());
            res.add(r);
        }
        return res;
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee e = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("employee not found"));
        EmployeeResponse r = new EmployeeResponse();
        r.setId(String.valueOf(e.getId()));
        r.setFirstName(e.getFirstName());
        r.setLastName(e.getLastName());
        r.setEmail(e.getEmail());
        return r;
    }
}
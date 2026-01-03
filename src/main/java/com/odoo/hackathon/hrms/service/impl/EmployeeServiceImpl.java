package com.odoo.hackathon.hrms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.odoo.hackathon.hrms.dto.response.EmployeeResponse;
import com.odoo.hackathon.hrms.dto.response.SalaryResponse;
import com.odoo.hackathon.hrms.dto.response.SkillResponse;
import com.odoo.hackathon.hrms.entity.Employee;
import com.odoo.hackathon.hrms.entity.EmployeeSkill;
import com.odoo.hackathon.hrms.entity.Salary;
import com.odoo.hackathon.hrms.repository.EmployeeRepository;
import com.odoo.hackathon.hrms.repository.EmployeeSkillRepository;
import com.odoo.hackathon.hrms.repository.SalaryRepository;
import com.odoo.hackathon.hrms.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final SalaryRepository salaryRepository;
    private final EmployeeSkillRepository skillRepository;

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeResponse> res = new ArrayList<>();
        for (Employee e : employees) {
            res.add(mapToResponse(e, false));
        }
        return res;
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee e = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("employee not found"));
        return mapToResponse(e, true);
    }
    
    private EmployeeResponse mapToResponse(Employee e, boolean includeDetails) {
        EmployeeResponse r = new EmployeeResponse();
        r.setId(String.valueOf(e.getId()));
        r.setFirstName(e.getFirstName());
        r.setLastName(e.getLastName());
        r.setEmail(e.getEmail());
        r.setPhone(e.getPhone());
        r.setDateOfJoining(e.getDateOfJoining());
        r.setDepartment(e.getDepartment());
        r.setDesignation(e.getDesignation());
        r.setAbout(e.getAbout());
        r.setStatus(e.getStatus() != null ? e.getStatus().name() : null);
        
        if (includeDetails) {
             salaryRepository.findCurrentSalary(e.getId()).ifPresent(s -> {
                 SalaryResponse sr = new SalaryResponse();
                 sr.setAmount(s.getAmount());
                 sr.setCurrency(s.getCurrency());
                 sr.setEffectiveDate(s.getEffectiveDate());
                 r.setCurrentSalary(sr);
             });
             
             List<EmployeeSkill> skills = skillRepository.findByEmployeeId(e.getId());
             if (skills != null && !skills.isEmpty()) {
                 r.setSkills(skills.stream().map(sk -> {
                     SkillResponse skr = new SkillResponse();
                     skr.setSkillName(sk.getSkillName());
                     skr.setProficiency(sk.getProficiency());
                     return skr;
                 }).collect(Collectors.toList()));
             }
        }
        return r;
    }
}
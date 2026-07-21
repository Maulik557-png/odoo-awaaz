package com.odoo.hackathon.hrms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.odoo.hackathon.hrms.dto.request.CreateEmployeeRequest;
import com.odoo.hackathon.hrms.dto.request.UpdateProfileRequest;
import com.odoo.hackathon.hrms.dto.response.EmployeeResponse;
import com.odoo.hackathon.hrms.dto.response.SalaryResponse;
import com.odoo.hackathon.hrms.dto.response.SkillResponse;
import com.odoo.hackathon.hrms.entity.Employee;
import com.odoo.hackathon.hrms.entity.EmployeeSkill;
import com.odoo.hackathon.hrms.entity.User;
import com.odoo.hackathon.hrms.enums.EmploymentStatus;
import com.odoo.hackathon.hrms.enums.Role;
import com.odoo.hackathon.hrms.exception.DuplicateResourceException;
import com.odoo.hackathon.hrms.exception.ResourceNotFoundException;
import com.odoo.hackathon.hrms.repository.EmployeeRepository;
import com.odoo.hackathon.hrms.repository.EmployeeSkillRepository;
import com.odoo.hackathon.hrms.repository.SalaryRepository;
import com.odoo.hackathon.hrms.repository.UserRepository;
import com.odoo.hackathon.hrms.service.EmployeeService;
import com.odoo.hackathon.hrms.service.LoginIdGeneratorService;
import com.odoo.hackathon.hrms.service.PasswordGeneratorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final SalaryRepository salaryRepository;
    private final EmployeeSkillRepository skillRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginIdGeneratorService loginIdGenerator;
    private final PasswordGeneratorService passwordGenerator;

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
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return mapToResponse(e, true);
    }

    @Override
    public EmployeeResponse getEmployeeProfile(String userLoginId) {
        User user = userRepository.findByLoginId(userLoginId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with loginId: " + userLoginId));
        Employee e = employeeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found for user: " + userLoginId));
        return mapToResponse(e, true);
    }

    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Employee email already exists: " + request.getEmail());
        }

        String loginId = loginIdGenerator.generateLoginId(request.getFirstName(), request.getLastName(), request.getDateOfJoining(), null);
        while (userRepository.existsByLoginId(loginId)) {
            loginId = loginId + "1";
        }

        String rawPassword = passwordGenerator.generatePassword(10);
        User user = new User();
        user.setLoginId(loginId);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(request.getRole() != null ? request.getRole() : Role.EMPLOYEE);
        user.setIsFirstLogin(true);
        user.setIsActive(true);
        Long userId = userRepository.save(user);
        user.setId(userId);

        Employee e = new Employee();
        e.setFirstName(request.getFirstName());
        e.setLastName(request.getLastName());
        e.setEmail(request.getEmail());
        e.setPhone(request.getPhone());
        e.setDateOfJoining(request.getDateOfJoining());
        e.setDepartment(request.getDepartment());
        e.setDesignation(request.getDesignation());
        e.setStatus(EmploymentStatus.ACTIVE);
        e.setUser(user);

        Long empId = employeeRepository.save(e);
        e.setId(empId);

        return mapToResponse(e, true);
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, UpdateProfileRequest request) {
        Employee e = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        if (request.getFirstName() != null) e.setFirstName(request.getFirstName());
        if (request.getLastName() != null) e.setLastName(request.getLastName());
        if (request.getPhone() != null) e.setPhone(request.getPhone());
        if (request.getDepartment() != null) e.setDepartment(request.getDepartment());
        if (request.getDesignation() != null) e.setDesignation(request.getDesignation());
        if (request.getAbout() != null) e.setAbout(request.getAbout());
        if (request.getStatus() != null) e.setStatus(request.getStatus());

        employeeRepository.update(e);
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
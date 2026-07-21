package com.odoo.hackathon.hrms.service.impl;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.odoo.hackathon.hrms.dto.request.AdminSignUpRequest;
import com.odoo.hackathon.hrms.dto.request.ChangePasswordRequest;
import com.odoo.hackathon.hrms.dto.request.LoginRequest;
import com.odoo.hackathon.hrms.dto.response.LoginResponse;
import com.odoo.hackathon.hrms.entity.Company;
import com.odoo.hackathon.hrms.entity.Employee;
import com.odoo.hackathon.hrms.entity.User;
import com.odoo.hackathon.hrms.enums.Role;
import com.odoo.hackathon.hrms.exception.DuplicateResourceException;
import com.odoo.hackathon.hrms.exception.InvalidCredentialsException;
import com.odoo.hackathon.hrms.repository.CompanyRepository;
import com.odoo.hackathon.hrms.repository.EmployeeRepository;
import com.odoo.hackathon.hrms.repository.UserRepository;
import com.odoo.hackathon.hrms.service.AuthService;
import com.odoo.hackathon.hrms.service.JwtService;
import com.odoo.hackathon.hrms.service.LoginIdGeneratorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final LoginIdGeneratorService loginIdGenerator;

    // retain an optional cache for performance, but primary store is DB
    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public LoginResponse login(LoginRequest request) {
        // first check cache
        User user = users.get(request.getLoginId());
        if (user == null) {
            // try to find by loginId in users table
            user = userRepository.findByLoginId(request.getLoginId()).orElse(null);
            if (user == null) {
                // not found — try to find an employee by this identifier as email
                var empOpt = employeeRepository.findByEmail(request.getLoginId());
                if (empOpt.isPresent() && empOpt.get().getUser() != null && empOpt.get().getUser().getId() != null) {
                    Long userId = empOpt.get().getUser().getId();
                    user = userRepository.findById(userId).orElse(null);
                }
            }
            if (user != null) users.put(user.getLoginId(), user);
        }
        if (user == null) throw new InvalidCredentialsException("invalid loginId or password");
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("invalid loginId or password");
        }
        LoginResponse resp = new LoginResponse();
        resp.setLoginId(user.getLoginId());
        resp.setToken(jwtService.generateToken(user.getLoginId(), user.getRole() == null ? null : user.getRole().name()));
        return resp;
    }

    @Override
    public String signupAdmin(AdminSignUpRequest request) {
        String reqRole = request.getRole() == null ? "Admin" : request.getRole();
        if (reqRole.equalsIgnoreCase("Employee")) {
            // Employee signup: create a user with generated loginId and create employee record
            // ensure email is not already used in employees table
            if (employeeRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("email already exists");
            }

            // Ensure company exists: find by name or create with a generated company code
            Long companyId = null;
            try {
                Company company = companyRepository.findByName(request.getCompanyName()).orElse(null);
                if (company == null) {
                    Company c = new Company();
                    c.setName(request.getCompanyName());
                    // generate a simple 2-letter company code from the name
                    c.setCompanyCode(generateCompanyCode(request.getCompanyName()));
                    Long cid = companyRepository.save(c);
                    company = c;
                    company.setId(cid);
                }
                companyId = company.getId();
            } catch (Exception ex) {
                // ignore and proceed without companyId; generator may still work with null, fallback later
                companyId = null;
            }

            // generate loginId: prefer generator with companyId
            String loginId;
            try {
                String[] parts = request.getName().split(" ");
                String first = parts.length>0?parts[0]:request.getName();
                String last = parts.length>1?parts[parts.length-1]:"";
                loginId = loginIdGenerator.generateLoginId(first, last, LocalDate.now(), companyId);
            } catch (Exception ex) {
                // Previously we fell back to a simple hardcoded EMP+timestamp id here which caused
                // duplicate or unexpected login ids (e.g. OI...). We want to keep only the
                // dynamically generated id based on the company. If generation fails, surface
                // a clear error so caller can fix the input (company/name) instead of creating
                // a second, hardcoded id.
                throw new RuntimeException("failed to generate loginId for employee: " + ex.getMessage(), ex);
            }

            // ensure unique loginId
            int attempt = 0;
            String baseLogin = loginId;
            while (userRepository.existsByLoginId(loginId)) {
                attempt++;
                loginId = baseLogin + attempt;
            }

            User u = new User();
            u.setLoginId(loginId);
            u.setPassword(passwordEncoder.encode(request.getPassword()));
            u.setRole(Role.EMPLOYEE);
            Long uid = userRepository.save(u);
            u.setId(uid);
            users.put(u.getLoginId(), u);

            // Create Employee record
            Employee e = new Employee();
            String[] nameParts = request.getName().split(" ");
            e.setFirstName(nameParts.length>0?nameParts[0]:request.getName());
            e.setLastName(nameParts.length>1?nameParts[nameParts.length-1]:"");
            e.setEmail(request.getEmail());
            e.setPhone(request.getPhone());
            e.setDateOfJoining(LocalDate.now());
            e.setUser(u);
            if (companyId != null) {
                Company comp = new Company();
                comp.setId(companyId);
                e.setCompany(comp);
            }
            employeeRepository.save(e);

            return loginId;

        } else {
            // Admin signup: use email as login id
            if (userRepository.existsByLoginId(request.getEmail())) {
                throw new DuplicateResourceException("email already exists");
            }
            User u = new User();
            u.setLoginId(request.getEmail());
            u.setPassword(passwordEncoder.encode(request.getPassword()));
            u.setRole(Role.ADMIN);
            Long id = userRepository.save(u);
            u.setId(id);
            users.put(u.getLoginId(), u);
            return "admin-created";
        }
    }

    @Override
    public void changePassword(String userLoginId, ChangePasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        User user = userRepository.findByLoginId(userLoginId)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }
        String newEncoded = passwordEncoder.encode(request.getNewPassword());
        userRepository.updatePassword(user.getId(), newEncoded);
        user.setPassword(newEncoded);
        user.setIsFirstLogin(false);
        users.put(user.getLoginId(), user);
    }

    // helper to generate a 2-letter company code from company name
    private String generateCompanyCode(String companyName) {
        if (companyName == null || companyName.isBlank()) return "CO";
        String[] parts = companyName.trim().split("\\s+");
        if (parts.length == 1) {
            String p = parts[0];
            return p.length() >= 2 ? p.substring(0,2).toUpperCase() : (p.substring(0,1).toUpperCase() + "X");
        }
        String code = (parts[0].substring(0,1) + parts[1].substring(0,1)).toUpperCase();
        return code;
    }
}
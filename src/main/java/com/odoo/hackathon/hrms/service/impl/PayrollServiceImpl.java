package com.odoo.hackathon.hrms.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.odoo.hackathon.hrms.dto.request.UpdateSalaryRequest;
import com.odoo.hackathon.hrms.dto.response.PayrollResponse;
import com.odoo.hackathon.hrms.entity.Employee;
import com.odoo.hackathon.hrms.entity.Salary;
import com.odoo.hackathon.hrms.entity.User;
import com.odoo.hackathon.hrms.exception.ResourceNotFoundException;
import com.odoo.hackathon.hrms.repository.EmployeeRepository;
import com.odoo.hackathon.hrms.repository.SalaryRepository;
import com.odoo.hackathon.hrms.repository.UserRepository;
import com.odoo.hackathon.hrms.service.PayrollService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {

    private final SalaryRepository salaryRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    @Override
    public PayrollResponse getMyPayroll(String userLoginId) {
        Employee employee = getEmployeeByLoginId(userLoginId);
        Salary currentSalary = salaryRepository.findCurrentSalary(employee.getId()).orElse(null);
        return mapToResponse(employee, currentSalary);
    }

    @Override
    public List<PayrollResponse> getAllPayroll() {
        List<Employee> employees = employeeRepository.findAll();
        List<PayrollResponse> result = new ArrayList<>();
        for (Employee e : employees) {
            Salary currentSalary = salaryRepository.findCurrentSalary(e.getId()).orElse(null);
            result.add(mapToResponse(e, currentSalary));
        }
        return result;
    }

    @Override
    public PayrollResponse updateSalary(UpdateSalaryRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));

        Salary salary = salaryRepository.findCurrentSalary(employee.getId()).orElse(null);
        LocalDate effDate = request.getEffectiveDate() != null ? request.getEffectiveDate() : LocalDate.now();

        if (salary == null) {
            salary = Salary.builder()
                    .employeeId(employee.getId())
                    .amount(request.getAmount())
                    .currency(request.getCurrency() != null ? request.getCurrency() : "USD")
                    .effectiveDate(effDate)
                    .build();
        } else {
            salary.setAmount(request.getAmount());
            if (request.getCurrency() != null) salary.setCurrency(request.getCurrency());
            salary.setEffectiveDate(effDate);
        }

        salaryRepository.save(salary);

        return mapToResponse(employee, salary);
    }

    private Employee getEmployeeByLoginId(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with loginId: " + loginId));
        return employeeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found for user: " + loginId));
    }

    private PayrollResponse mapToResponse(Employee e, Salary s) {
        String name = e != null ? e.getFirstName() + " " + e.getLastName() : "Unknown";
        BigDecimal basic = s != null ? s.getAmount() : BigDecimal.ZERO;

        // Approximate standard allowances (10%) and deductions (5%) for calculation if not explicit
        BigDecimal allowances = basic.multiply(new BigDecimal("0.10"));
        BigDecimal deductions = basic.multiply(new BigDecimal("0.05"));
        BigDecimal netSalary = basic.add(allowances).subtract(deductions);

        return PayrollResponse.builder()
                .employeeId(e != null ? e.getId() : null)
                .employeeName(name)
                .basicSalary(basic)
                .allowances(allowances)
                .deductions(deductions)
                .netSalary(netSalary)
                .currency(s != null ? s.getCurrency() : "USD")
                .payDate(s != null ? s.getEffectiveDate() : LocalDate.now())
                .build();
    }
}

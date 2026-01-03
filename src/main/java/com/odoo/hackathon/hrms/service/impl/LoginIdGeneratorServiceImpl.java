package com.odoo.hackathon.hrms.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.odoo.hackathon.hrms.entity.Company;
import com.odoo.hackathon.hrms.exception.ResourceNotFoundException;
import com.odoo.hackathon.hrms.repository.CompanyRepository;
import com.odoo.hackathon.hrms.repository.EmployeeRepository;
import com.odoo.hackathon.hrms.service.LoginIdGeneratorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginIdGeneratorServiceImpl implements LoginIdGeneratorService {
    
    private final EmployeeRepository employeeRepository; // JPA Repository
    private final CompanyRepository companyRepository;   // JPA Repository
    
    public String generateLoginId(String firstName, String lastName, LocalDate dateOfJoining, Long companyId) {
        
        // Fetch company from MySQL
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        
        String companyCode = company.getCompanyCode();
        String namePart = extractNamePart(firstName, lastName);
        int year = dateOfJoining.getYear();
        String serialNumber = generateSerialNumber(year, companyId);
        
        return companyCode + namePart + year + serialNumber;
    }

    public String extractNamePart(String firstName, String lastName) {
        String firstPart = firstName.substring(0, Math.min(2, firstName.length())).toUpperCase();
        String lastPart = lastName.substring(0, Math.min(2, lastName.length())).toUpperCase();
        return firstPart + lastPart;
    }

	@Override
	public String generateSerialNumber(int year, Long companyId) {
		// Query MySQL database for count
        int count = employeeRepository.countByYearOfJoiningAndCompanyId(year, companyId);
        return String.format("%04d", count + 1);
	}

}

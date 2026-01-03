package com.odoo.hackathon.hrms.service;

import java.time.LocalDate;

public interface LoginIdGeneratorService {
	public String generateLoginId(String firstName, String lastName, LocalDate dateOfJoining, Long companyId);
	
	public String extractNamePart(String firstName, String lastName);
	
	public String generateSerialNumber(int year, Long companyId);
}

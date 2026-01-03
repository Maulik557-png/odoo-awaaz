package com.odoo.hackathon.hrms.service;

public interface PasswordGeneratorService {
	
	public String generatePassword(int length);
	
	public String shuffleString(String input);
	
}

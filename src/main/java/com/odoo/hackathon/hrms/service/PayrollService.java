package com.odoo.hackathon.hrms.service;

import java.util.List;

import com.odoo.hackathon.hrms.dto.request.UpdateSalaryRequest;
import com.odoo.hackathon.hrms.dto.response.PayrollResponse;

public interface PayrollService {
    PayrollResponse getMyPayroll(String userLoginId);
    List<PayrollResponse> getAllPayroll();
    PayrollResponse updateSalary(UpdateSalaryRequest request);
}

package com.odoo.hackathon.hrms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class SalaryResponse {
    private BigDecimal amount;
    private String currency;
    private LocalDate effectiveDate;
}

package com.odoo.hackathon.hrms.dto.response;

import lombok.Data;

@Data
public class EmployeeResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
}

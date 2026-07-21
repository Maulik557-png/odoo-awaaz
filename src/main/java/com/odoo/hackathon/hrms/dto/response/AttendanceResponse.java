package com.odoo.hackathon.hrms.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.odoo.hackathon.hrms.enums.AttendanceStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate attendanceDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String workHours;
    private AttendanceStatus status;
    private String remarks;
}

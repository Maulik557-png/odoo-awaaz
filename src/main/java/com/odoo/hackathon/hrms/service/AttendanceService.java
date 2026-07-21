package com.odoo.hackathon.hrms.service;

import java.time.LocalDate;
import java.util.List;

import com.odoo.hackathon.hrms.dto.request.AttendanceDto;
import com.odoo.hackathon.hrms.dto.response.AttendanceResponse;

public interface AttendanceService {
    AttendanceResponse checkIn(String userLoginId);
    AttendanceResponse checkOut(String userLoginId);
    AttendanceResponse recordAttendance(AttendanceDto dto);
    List<AttendanceResponse> getMyAttendance(String userLoginId, LocalDate startDate, LocalDate endDate);
    List<AttendanceResponse> getAllAttendance(LocalDate startDate, LocalDate endDate);
}

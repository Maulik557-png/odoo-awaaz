package com.odoo.hackathon.hrms.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.odoo.hackathon.hrms.dto.request.AttendanceDto;
import com.odoo.hackathon.hrms.dto.response.AttendanceResponse;
import com.odoo.hackathon.hrms.service.AttendanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResponse> checkIn(Principal principal) {
        return ResponseEntity.ok(attendanceService.checkIn(principal.getName()));
    }

    @PostMapping("/check-out")
    public ResponseEntity<AttendanceResponse> checkOut(Principal principal) {
        return ResponseEntity.ok(attendanceService.checkOut(principal.getName()));
    }

    @PostMapping
    public ResponseEntity<AttendanceResponse> recordAttendance(@RequestBody AttendanceDto dto) {
        return ResponseEntity.ok(attendanceService.recordAttendance(dto));
    }

    @GetMapping("/my-attendance")
    public ResponseEntity<List<AttendanceResponse>> getMyAttendance(
            Principal principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getMyAttendance(principal.getName(), startDate, endDate));
    }

    @GetMapping
    public ResponseEntity<List<AttendanceResponse>> getAllAttendance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(attendanceService.getAllAttendance(startDate, endDate));
    }
}

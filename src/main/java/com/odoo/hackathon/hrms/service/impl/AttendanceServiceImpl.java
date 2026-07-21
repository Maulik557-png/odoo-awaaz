package com.odoo.hackathon.hrms.service.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.odoo.hackathon.hrms.dto.request.AttendanceDto;
import com.odoo.hackathon.hrms.dto.response.AttendanceResponse;
import com.odoo.hackathon.hrms.entity.Attendance;
import com.odoo.hackathon.hrms.entity.Employee;
import com.odoo.hackathon.hrms.entity.User;
import com.odoo.hackathon.hrms.enums.AttendanceStatus;
import com.odoo.hackathon.hrms.exception.ResourceNotFoundException;
import com.odoo.hackathon.hrms.repository.AttendanceRepository;
import com.odoo.hackathon.hrms.repository.EmployeeRepository;
import com.odoo.hackathon.hrms.repository.UserRepository;
import com.odoo.hackathon.hrms.service.AttendanceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    @Override
    public AttendanceResponse checkIn(String userLoginId) {
        Employee employee = getEmployeeByLoginId(userLoginId);
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(employee.getId(), today)
                .orElse(null);

        if (attendance != null && attendance.getCheckInTime() != null) {
            throw new IllegalStateException("Already checked in for today");
        }

        if (attendance == null) {
            attendance = Attendance.builder()
                    .employeeId(employee.getId())
                    .attendanceDate(today)
                    .checkInTime(now)
                    .status(AttendanceStatus.PRESENT)
                    .build();
            attendanceRepository.save(attendance);
        } else {
            attendance.setCheckInTime(now);
            attendance.setStatus(AttendanceStatus.PRESENT);
            attendanceRepository.update(attendance);
        }

        return mapToResponse(attendance, employee);
    }

    @Override
    public AttendanceResponse checkOut(String userLoginId) {
        Employee employee = getEmployeeByLoginId(userLoginId);
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(employee.getId(), today)
                .orElseThrow(() -> new IllegalStateException("Cannot check out without checking in first"));

        if (attendance.getCheckOutTime() != null) {
            throw new IllegalStateException("Already checked out for today");
        }

        attendance.setCheckOutTime(now);
        attendanceRepository.update(attendance);

        return mapToResponse(attendance, employee);
    }

    @Override
    public AttendanceResponse recordAttendance(AttendanceDto dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.getEmployeeId()));

        LocalDate date = dto.getDate() != null ? dto.getDate() : LocalDate.now();
        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(employee.getId(), date)
                .orElse(null);

        if (attendance == null) {
            attendance = Attendance.builder()
                    .employeeId(employee.getId())
                    .attendanceDate(date)
                    .checkInTime(dto.getCheckInTime())
                    .checkOutTime(dto.getCheckOutTime())
                    .status(dto.getStatus() != null ? dto.getStatus() : AttendanceStatus.PRESENT)
                    .remarks(dto.getRemarks())
                    .build();
            attendanceRepository.save(attendance);
        } else {
            if (dto.getCheckInTime() != null) attendance.setCheckInTime(dto.getCheckInTime());
            if (dto.getCheckOutTime() != null) attendance.setCheckOutTime(dto.getCheckOutTime());
            if (dto.getStatus() != null) attendance.setStatus(dto.getStatus());
            if (dto.getRemarks() != null) attendance.setRemarks(dto.getRemarks());
            attendanceRepository.update(attendance);
        }

        return mapToResponse(attendance, employee);
    }

    @Override
    public List<AttendanceResponse> getMyAttendance(String userLoginId, LocalDate startDate, LocalDate endDate) {
        Employee employee = getEmployeeByLoginId(userLoginId);
        List<Attendance> list;
        if (startDate != null && endDate != null) {
            list = attendanceRepository.findByEmployeeIdAndDateRange(employee.getId(), startDate, endDate);
        } else {
            list = attendanceRepository.findByEmployeeId(employee.getId());
        }
        return list.stream().map(a -> mapToResponse(a, employee)).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceResponse> getAllAttendance(LocalDate startDate, LocalDate endDate) {
        List<Attendance> list;
        if (startDate != null && endDate != null) {
            list = attendanceRepository.findAllByDateRange(startDate, endDate);
        } else {
            list = attendanceRepository.findAll();
        }
        return list.stream().map(a -> {
            Employee emp = employeeRepository.findById(a.getEmployeeId()).orElse(null);
            return mapToResponse(a, emp);
        }).collect(Collectors.toList());
    }

    private Employee getEmployeeByLoginId(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with loginId: " + loginId));
        return employeeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found for user: " + loginId));
    }

    private AttendanceResponse mapToResponse(Attendance a, Employee e) {
        String name = e != null ? e.getFirstName() + " " + e.getLastName() : "Unknown";
        String workHours = "-";
        if (a.getCheckInTime() != null && a.getCheckOutTime() != null) {
            long minutes = Duration.between(a.getCheckInTime(), a.getCheckOutTime()).toMinutes();
            long hours = minutes / 60;
            long mins = minutes % 60;
            workHours = hours + "h " + mins + "m";
        }
        return AttendanceResponse.builder()
                .id(a.getId())
                .employeeId(a.getEmployeeId())
                .employeeName(name)
                .attendanceDate(a.getAttendanceDate())
                .checkInTime(a.getCheckInTime())
                .checkOutTime(a.getCheckOutTime())
                .workHours(workHours)
                .status(a.getStatus())
                .remarks(a.getRemarks())
                .build();
    }
}

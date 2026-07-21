package com.odoo.hackathon.hrms.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.odoo.hackathon.hrms.dto.request.LeaveApprovalDto;
import com.odoo.hackathon.hrms.dto.request.LeaveRequestDto;
import com.odoo.hackathon.hrms.dto.response.LeaveRequestResponse;
import com.odoo.hackathon.hrms.entity.Employee;
import com.odoo.hackathon.hrms.entity.LeaveRequest;
import com.odoo.hackathon.hrms.entity.User;
import com.odoo.hackathon.hrms.enums.LeaveStatus;
import com.odoo.hackathon.hrms.exception.ResourceNotFoundException;
import com.odoo.hackathon.hrms.repository.EmployeeRepository;
import com.odoo.hackathon.hrms.repository.LeaveRequestRepository;
import com.odoo.hackathon.hrms.repository.UserRepository;
import com.odoo.hackathon.hrms.service.LeaveRequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    @Override
    public LeaveRequestResponse submitLeaveRequest(String userLoginId, LeaveRequestDto dto) {
        Employee employee;
        if (dto.getEmployeeId() != null) {
            employee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseGet(() -> getEmployeeByLoginId(userLoginId));
        } else {
            employee = getEmployeeByLoginId(userLoginId);
        }

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        int totalDays = (int) ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) + 1;

        LeaveRequest request = LeaveRequest.builder()
                .employeeId(employee.getId())
                .leaveType(dto.getLeaveType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .totalDays(totalDays)
                .reason(dto.getReason())
                .status(LeaveStatus.PENDING)
                .build();

        leaveRequestRepository.save(request);

        return mapToResponse(request, employee);
    }

    @Override
    public List<LeaveRequestResponse> getMyLeaveRequests(String userLoginId) {
        Employee employee = getEmployeeByLoginId(userLoginId);
        List<LeaveRequest> list = leaveRequestRepository.findByEmployeeId(employee.getId());
        return list.stream().map(l -> mapToResponse(l, employee)).collect(Collectors.toList());
    }

    @Override
    public List<LeaveRequestResponse> getAllLeaveRequests() {
        List<LeaveRequest> list = leaveRequestRepository.findAll();
        return list.stream().map(l -> {
            Employee emp = employeeRepository.findById(l.getEmployeeId()).orElse(null);
            return mapToResponse(l, emp);
        }).collect(Collectors.toList());
    }

    @Override
    public LeaveRequestResponse approveLeaveRequest(Long leaveId, String adminLoginId) {
        User admin = userRepository.findByLoginId(adminLoginId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        LeaveRequest request = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + leaveId));

        leaveRequestRepository.updateStatus(leaveId, LeaveStatus.APPROVED, admin.getId(), null);

        request.setStatus(LeaveStatus.APPROVED);
        request.setApprovedBy(admin.getId());
        request.setApprovedAt(LocalDateTime.now());

        Employee emp = employeeRepository.findById(request.getEmployeeId()).orElse(null);
        return mapToResponse(request, emp);
    }

    @Override
    public LeaveRequestResponse rejectLeaveRequest(Long leaveId, String adminLoginId, LeaveApprovalDto dto) {
        User admin = userRepository.findByLoginId(adminLoginId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        LeaveRequest request = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + leaveId));

        String rejectionReason = dto != null ? dto.getRejectionReason() : "Rejected by Admin";

        leaveRequestRepository.updateStatus(leaveId, LeaveStatus.REJECTED, admin.getId(), rejectionReason);

        request.setStatus(LeaveStatus.REJECTED);
        request.setApprovedBy(admin.getId());
        request.setApprovedAt(LocalDateTime.now());
        request.setRejectionReason(rejectionReason);

        Employee emp = employeeRepository.findById(request.getEmployeeId()).orElse(null);
        return mapToResponse(request, emp);
    }

    @Override
    public void deleteLeaveRequest(Long leaveId, String userLoginId) {
        LeaveRequest request = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + leaveId));
        leaveRequestRepository.deleteById(leaveId);
    }

    private Employee getEmployeeByLoginId(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with loginId: " + loginId));
        return employeeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found for user: " + loginId));
    }

    private LeaveRequestResponse mapToResponse(LeaveRequest l, Employee e) {
        String name = e != null ? e.getFirstName() + " " + e.getLastName() : "Unknown";
        return LeaveRequestResponse.builder()
                .id(l.getId())
                .employeeId(l.getEmployeeId())
                .employeeName(name)
                .leaveType(l.getLeaveType())
                .startDate(l.getStartDate())
                .endDate(l.getEndDate())
                .totalDays(l.getTotalDays())
                .reason(l.getReason())
                .status(l.getStatus())
                .approvedBy(l.getApprovedBy())
                .approvedAt(l.getApprovedAt())
                .rejectionReason(l.getRejectionReason())
                .createdAt(l.getCreatedAt())
                .build();
    }
}

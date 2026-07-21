package com.odoo.hackathon.hrms.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.odoo.hackathon.hrms.dto.request.LeaveApprovalDto;
import com.odoo.hackathon.hrms.dto.request.LeaveRequestDto;
import com.odoo.hackathon.hrms.dto.response.LeaveRequestResponse;
import com.odoo.hackathon.hrms.service.LeaveRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/api/leave-requests", "/api/leaves"})
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping
    public ResponseEntity<LeaveRequestResponse> submitLeaveRequest(
            Principal principal,
            @Validated @RequestBody LeaveRequestDto dto) {
        return ResponseEntity.ok(leaveRequestService.submitLeaveRequest(principal.getName(), dto));
    }

    @GetMapping("/my-requests")
    public ResponseEntity<List<LeaveRequestResponse>> getMyLeaveRequests(Principal principal) {
        return ResponseEntity.ok(leaveRequestService.getMyLeaveRequests(principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequestResponse>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequests());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveRequestResponse> approveLeaveRequest(
            @PathVariable Long id,
            Principal principal) {
        return ResponseEntity.ok(leaveRequestService.approveLeaveRequest(id, principal.getName()));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveRequestResponse> rejectLeaveRequest(
            @PathVariable Long id,
            Principal principal,
            @RequestBody(required = false) LeaveApprovalDto dto) {
        return ResponseEntity.ok(leaveRequestService.rejectLeaveRequest(id, principal.getName(), dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLeaveRequest(@PathVariable Long id, Principal principal) {
        leaveRequestService.deleteLeaveRequest(id, principal.getName());
        return ResponseEntity.ok("Leave request deleted successfully");
    }
}

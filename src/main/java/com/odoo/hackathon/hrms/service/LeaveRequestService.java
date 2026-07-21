package com.odoo.hackathon.hrms.service;

import java.util.List;

import com.odoo.hackathon.hrms.dto.request.LeaveApprovalDto;
import com.odoo.hackathon.hrms.dto.request.LeaveRequestDto;
import com.odoo.hackathon.hrms.dto.response.LeaveRequestResponse;

public interface LeaveRequestService {
    LeaveRequestResponse submitLeaveRequest(String userLoginId, LeaveRequestDto dto);
    List<LeaveRequestResponse> getMyLeaveRequests(String userLoginId);
    List<LeaveRequestResponse> getAllLeaveRequests();
    LeaveRequestResponse approveLeaveRequest(Long leaveId, String adminLoginId);
    LeaveRequestResponse rejectLeaveRequest(Long leaveId, String adminLoginId, LeaveApprovalDto dto);
    void deleteLeaveRequest(Long leaveId, String userLoginId);
}

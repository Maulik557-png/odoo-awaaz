package com.odoo.hackathon.hrms.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.odoo.hackathon.hrms.entity.LeaveRequest;
import com.odoo.hackathon.hrms.enums.LeaveStatus;
import com.odoo.hackathon.hrms.enums.LeaveType;

@Repository
public class LeaveRequestRepository {

    private final JdbcTemplate jdbc;

    public LeaveRequestRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<LeaveRequest> MAPPER = new RowMapper<>() {
        @Override
        public LeaveRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
            LeaveRequest l = new LeaveRequest();
            l.setId(rs.getLong("id"));
            l.setEmployeeId(rs.getLong("employee_id"));
            String lt = rs.getString("leave_type");
            if (lt != null) l.setLeaveType(LeaveType.valueOf(lt));
            java.sql.Date start = rs.getDate("start_date");
            if (start != null) l.setStartDate(start.toLocalDate());
            java.sql.Date end = rs.getDate("end_date");
            if (end != null) l.setEndDate(end.toLocalDate());
            l.setTotalDays(rs.getInt("total_days"));
            l.setReason(rs.getString("reason"));
            String st = rs.getString("status");
            if (st != null) l.setStatus(LeaveStatus.valueOf(st));
            long approver = rs.getLong("approved_by");
            if (!rs.wasNull()) l.setApprovedBy(approver);
            java.sql.Timestamp appAt = rs.getTimestamp("approved_at");
            if (appAt != null) l.setApprovedAt(appAt.toLocalDateTime());
            l.setRejectionReason(rs.getString("rejection_reason"));
            java.sql.Timestamp created = rs.getTimestamp("created_at");
            if (created != null) l.setCreatedAt(created.toLocalDateTime());
            java.sql.Timestamp updated = rs.getTimestamp("updated_at");
            if (updated != null) l.setUpdatedAt(updated.toLocalDateTime());
            return l;
        }
    };

    public Optional<LeaveRequest> findById(Long id) {
        List<LeaveRequest> list = jdbc.query("SELECT * FROM leave_requests WHERE id = ?", MAPPER, id);
        return list.stream().findFirst();
    }

    public List<LeaveRequest> findByEmployeeId(Long employeeId) {
        return jdbc.query("SELECT * FROM leave_requests WHERE employee_id = ? ORDER BY created_at DESC", MAPPER, employeeId);
    }

    public List<LeaveRequest> findAll() {
        return jdbc.query("SELECT * FROM leave_requests ORDER BY created_at DESC", MAPPER);
    }

    public Long save(LeaveRequest l) {
        jdbc.update("INSERT INTO leave_requests (employee_id, leave_type, start_date, end_date, total_days, reason, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())",
                l.getEmployeeId(),
                l.getLeaveType() != null ? l.getLeaveType().name() : null,
                l.getStartDate() != null ? java.sql.Date.valueOf(l.getStartDate()) : null,
                l.getEndDate() != null ? java.sql.Date.valueOf(l.getEndDate()) : null,
                l.getTotalDays(),
                l.getReason(),
                l.getStatus() != null ? l.getStatus().name() : LeaveStatus.PENDING.name());
        Long id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        l.setId(id);
        return id;
    }

    public void updateStatus(Long id, LeaveStatus status, Long approvedBy, String rejectionReason) {
        jdbc.update("UPDATE leave_requests SET status = ?, approved_by = ?, approved_at = NOW(), rejection_reason = ?, updated_at = NOW() WHERE id = ?",
                status.name(), approvedBy, rejectionReason, id);
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM leave_requests WHERE id = ?", id);
    }
}

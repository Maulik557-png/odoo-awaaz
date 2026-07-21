package com.odoo.hackathon.hrms.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.odoo.hackathon.hrms.entity.Attendance;
import com.odoo.hackathon.hrms.enums.AttendanceStatus;

@Repository
public class AttendanceRepository {

    private final JdbcTemplate jdbc;

    public AttendanceRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Attendance> MAPPER = new RowMapper<>() {
        @Override
        public Attendance mapRow(ResultSet rs, int rowNum) throws SQLException {
            Attendance a = new Attendance();
            a.setId(rs.getLong("id"));
            a.setEmployeeId(rs.getLong("employee_id"));
            java.sql.Date d = rs.getDate("attendance_date");
            if (d != null) a.setAttendanceDate(d.toLocalDate());
            java.sql.Time checkIn = rs.getTime("check_in_time");
            if (checkIn != null) a.setCheckInTime(checkIn.toLocalTime());
            java.sql.Time checkOut = rs.getTime("check_out_time");
            if (checkOut != null) a.setCheckOutTime(checkOut.toLocalTime());
            String st = rs.getString("status");
            if (st != null) a.setStatus(AttendanceStatus.valueOf(st));
            a.setRemarks(rs.getString("remarks"));
            java.sql.Timestamp created = rs.getTimestamp("created_at");
            if (created != null) a.setCreatedAt(created.toLocalDateTime());
            java.sql.Timestamp updated = rs.getTimestamp("updated_at");
            if (updated != null) a.setUpdatedAt(updated.toLocalDateTime());
            return a;
        }
    };

    public Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date) {
        List<Attendance> list = jdbc.query("SELECT * FROM attendance WHERE employee_id = ? AND attendance_date = ?", MAPPER, employeeId, java.sql.Date.valueOf(date));
        return list.stream().findFirst();
    }

    public List<Attendance> findByEmployeeId(Long employeeId) {
        return jdbc.query("SELECT * FROM attendance WHERE employee_id = ? ORDER BY attendance_date DESC", MAPPER, employeeId);
    }

    public List<Attendance> findByEmployeeIdAndDateRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        return jdbc.query("SELECT * FROM attendance WHERE employee_id = ? AND attendance_date BETWEEN ? AND ? ORDER BY attendance_date DESC",
                MAPPER, employeeId, java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate));
    }

    public List<Attendance> findAll() {
        return jdbc.query("SELECT * FROM attendance ORDER BY attendance_date DESC", MAPPER);
    }

    public List<Attendance> findAllByDateRange(LocalDate startDate, LocalDate endDate) {
        return jdbc.query("SELECT * FROM attendance WHERE attendance_date BETWEEN ? AND ? ORDER BY attendance_date DESC",
                MAPPER, java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate));
    }

    public Long save(Attendance a) {
        jdbc.update("INSERT INTO attendance (employee_id, attendance_date, check_in_time, check_out_time, status, remarks, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())",
                a.getEmployeeId(),
                a.getAttendanceDate() != null ? java.sql.Date.valueOf(a.getAttendanceDate()) : null,
                a.getCheckInTime() != null ? java.sql.Time.valueOf(a.getCheckInTime()) : null,
                a.getCheckOutTime() != null ? java.sql.Time.valueOf(a.getCheckOutTime()) : null,
                a.getStatus() != null ? a.getStatus().name() : null,
                a.getRemarks());
        Long id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        a.setId(id);
        return id;
    }

    public void update(Attendance a) {
        jdbc.update("UPDATE attendance SET check_in_time = ?, check_out_time = ?, status = ?, remarks = ?, updated_at = NOW() WHERE id = ?",
                a.getCheckInTime() != null ? java.sql.Time.valueOf(a.getCheckInTime()) : null,
                a.getCheckOutTime() != null ? java.sql.Time.valueOf(a.getCheckOutTime()) : null,
                a.getStatus() != null ? a.getStatus().name() : null,
                a.getRemarks(),
                a.getId());
    }
}

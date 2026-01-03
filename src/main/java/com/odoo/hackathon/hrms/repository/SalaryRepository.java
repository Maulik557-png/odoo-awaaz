package com.odoo.hackathon.hrms.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.odoo.hackathon.hrms.entity.Salary;

@Repository
public class SalaryRepository {

    private final JdbcTemplate jdbc;

    public SalaryRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Salary> SALARY_MAPPER = new RowMapper<>() {
        @Override
        public Salary mapRow(ResultSet rs, int rowNum) throws SQLException {
            Salary s = new Salary();
            s.setId(rs.getLong("id"));
            s.setEmployeeId(rs.getLong("employee_id"));
            s.setAmount(rs.getBigDecimal("amount"));
            s.setCurrency(rs.getString("currency"));
            java.sql.Date effDate = rs.getDate("effective_date");
            if (effDate != null) s.setEffectiveDate(effDate.toLocalDate());
            java.sql.Timestamp created = rs.getTimestamp("created_at");
            if (created != null) s.setCreatedAt(created.toLocalDateTime());
            java.sql.Timestamp updated = rs.getTimestamp("updated_at");
            if (updated != null) s.setUpdatedAt(updated.toLocalDateTime());
            return s;
        }
    };

    public List<Salary> findByEmployeeId(Long employeeId) {
        return jdbc.query("SELECT * FROM salaries WHERE employee_id = ? ORDER BY effective_date DESC", SALARY_MAPPER, employeeId);
    }

    public Optional<Salary> findCurrentSalary(Long employeeId) {
        List<Salary> list = jdbc.query("SELECT * FROM salaries WHERE employee_id = ? ORDER BY effective_date DESC LIMIT 1", SALARY_MAPPER, employeeId);
        return list.stream().findFirst();
    }

    public void save(Salary salary) {
        if (salary.getId() == null) {
            jdbc.update("INSERT INTO salaries (employee_id, amount, currency, effective_date, created_at, updated_at) VALUES (?, ?, ?, ?, NOW(), NOW())",
                    salary.getEmployeeId(), salary.getAmount(), salary.getCurrency(), salary.getEffectiveDate());
        } else {
            jdbc.update("UPDATE salaries SET amount = ?, currency = ?, effective_date = ?, updated_at = NOW() WHERE id = ?",
                    salary.getAmount(), salary.getCurrency(), salary.getEffectiveDate(), salary.getId());
        }
    }
}

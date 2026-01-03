package com.odoo.hackathon.hrms.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.odoo.hackathon.hrms.entity.Employee;
import com.odoo.hackathon.hrms.entity.User;
import com.odoo.hackathon.hrms.enums.EmploymentStatus;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbc;

    public EmployeeRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Employee> EMP_MAPPER = new RowMapper<>() {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee e = new Employee();
            e.setId(rs.getLong("id"));
            e.setFirstName(rs.getString("first_name"));
            e.setLastName(rs.getString("last_name"));
            e.setEmail(rs.getString("email"));
            e.setPhone(rs.getString("phone"));
            java.sql.Date doj = rs.getDate("date_of_joining");
            if (doj != null) e.setDateOfJoining(doj.toLocalDate());
            e.setAbout(rs.getString("about"));
            String s = rs.getString("status");
            if (s != null) e.setStatus(EmploymentStatus.valueOf(s));
            java.sql.Timestamp created = rs.getTimestamp("created_at");
            if (created != null) e.setCreatedAt(created.toLocalDateTime());
            java.sql.Timestamp updated = rs.getTimestamp("updated_at");
            if (updated != null) e.setUpdatedAt(updated.toLocalDateTime());
            // map user_id to Employee.user.id if present
            long userId = rs.getLong("user_id");
            if (!rs.wasNull() && userId > 0) {
                User u = new User();
                u.setId(userId);
                e.setUser(u);
            }
            // map company_id is intentionally omitted here; other logic can set it
            return e;
        }
    };

    public Optional<Employee> findByEmail(String email) {
        List<Employee> list = jdbc.query("SELECT * FROM employees WHERE email = ?", EMP_MAPPER, email);
        return list.stream().findFirst();
    }

    public boolean existsByEmail(String email) {
        Integer c = jdbc.queryForObject("SELECT COUNT(1) FROM employees WHERE email = ?", Integer.class, email);
        return c != null && c > 0;
    }

    public List<Employee> findByCompanyId(Long companyId) {
        return jdbc.query("SELECT * FROM employees WHERE company_id = ?", EMP_MAPPER, companyId);
    }

    public List<Employee> findByStatus(EmploymentStatus status) {
        return jdbc.query("SELECT * FROM employees WHERE status = ?", EMP_MAPPER, status.name());
    }

    public List<Employee> findByDepartment(String department) {
        return jdbc.query("SELECT * FROM employees WHERE department = ?", EMP_MAPPER, department);
    }

    public int countByYearOfJoiningAndCompanyId(int year, Long companyId) {
        Integer c = jdbc.queryForObject("SELECT COUNT(1) FROM employees WHERE YEAR(date_of_joining) = ? AND company_id = ?", Integer.class, year, companyId);
        return c == null ? 0 : c;
    }

    public int countByYearStringAndCompanyId(String year, Long companyId) {
        Integer c = jdbc.queryForObject("SELECT COUNT(1) FROM employees WHERE YEAR(date_of_joining) = ? AND company_id = ?", Integer.class, Integer.parseInt(year), companyId);
        return c == null ? 0 : c;
    }

    public List<Employee> findByYearOfJoining(int year) {
        return jdbc.query("SELECT * FROM employees WHERE YEAR(date_of_joining) = ?", EMP_MAPPER, year);
    }

    public Optional<Employee> findByUserId(Long userId) {
        List<Employee> list = jdbc.query("SELECT * FROM employees WHERE user_id = ?", EMP_MAPPER, userId);
        return list.stream().findFirst();
    }

    // New: return all employees
    public List<Employee> findAll() {
        return jdbc.query("SELECT * FROM employees", EMP_MAPPER);
    }

    // New: find by primary key id
    public Optional<Employee> findById(Long id) {
        List<Employee> list = jdbc.query("SELECT * FROM employees WHERE id = ?", EMP_MAPPER, id);
        return list.stream().findFirst();
    }

    // New: simple insert helper
    public Long save(Employee e) {
        jdbc.update("INSERT INTO employees (first_name, last_name, email, phone, date_of_joining, department, designation, about, user_id, company_id, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())",
                e.getFirstName(), e.getLastName(), e.getEmail(), e.getPhone(), e.getDateOfJoining(), e.getDepartment(), e.getDesignation(), e.getAbout(), e.getUser() == null ? null : e.getUser().getId(), e.getCompany() == null ? null : e.getCompany().getId(), e.getStatus() == null ? null : e.getStatus().name());
        Long id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        e.setId(id);
        return id;
    }
}
package com.odoo.hackathon.hrms.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.odoo.hackathon.hrms.entity.User;
import com.odoo.hackathon.hrms.enums.Role;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<User> USER_MAPPER = new RowMapper<>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User u = new User();
            u.setId(rs.getLong("id"));
            u.setLoginId(rs.getString("login_id"));
            u.setPassword(rs.getString("password"));
            String role = rs.getString("role");
            if (role != null) u.setRole(Role.valueOf(role));
            u.setIsFirstLogin(rs.getBoolean("is_first_login"));
            u.setIsActive(rs.getBoolean("is_active"));
            u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            u.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return u;
        }
    };

    public Optional<User> findByLoginId(String loginId) {
        List<User> list = jdbc.query("SELECT * FROM users WHERE login_id = ?", USER_MAPPER, loginId);
        return list.stream().findFirst();
    }

    public boolean existsByLoginId(String loginId) {
        Integer count = jdbc.queryForObject("SELECT COUNT(1) FROM users WHERE login_id = ?", Integer.class, loginId);
        return count != null && count > 0;
    }

    public List<User> findByCompanyId(Long companyId) {
        return jdbc.query("SELECT * FROM users WHERE company_id = ?", USER_MAPPER, companyId);
    }

    public List<User> findByRole(Role role) {
        return jdbc.query("SELECT * FROM users WHERE role = ?", USER_MAPPER, role.name());
    }

    public List<User> findByCompanyIdAndRole(Long companyId, Role role) {
        return jdbc.query("SELECT * FROM users WHERE company_id = ? AND role = ?", USER_MAPPER, companyId, role.name());
    }

    public long countByCompanyId(Long companyId) {
        Integer c = jdbc.queryForObject("SELECT COUNT(1) FROM users WHERE company_id = ?", Integer.class, companyId);
        return c == null ? 0L : c.longValue();
    }

    // Basic save (insert) helper
    public Long save(User u) {
        jdbc.update("INSERT INTO users (login_id, password, role, is_first_login, is_active, company_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())",
                u.getLoginId(), u.getPassword(), u.getRole() == null ? null : u.getRole().name(), u.getIsFirstLogin(), u.getIsActive(), u.getCompany() == null ? null : u.getCompany().getId());
        Long id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        u.setId(id);
        return id;
    }

    public Optional<User> findById(Long id) {
        List<User> list = jdbc.query("SELECT * FROM users WHERE id = ?", USER_MAPPER, id);
        return list.stream().findFirst();
    }
}
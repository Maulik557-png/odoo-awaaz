package com.odoo.hackathon.hrms.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.odoo.hackathon.hrms.entity.Company;

@Repository
public class CompanyRepository {

    private final JdbcTemplate jdbc;

    public CompanyRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Company> CMP_MAPPER = new RowMapper<>() {
        @Override
        public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
            Company c = new Company();
            c.setId(rs.getLong("id"));
            c.setName(rs.getString("name"));
            c.setCompanyCode(rs.getString("company_code"));
            c.setEmail(rs.getString("email"));
            c.setPhone(rs.getString("phone"));
            java.sql.Timestamp created = rs.getTimestamp("created_at");
            if (created != null) c.setCreatedAt(created.toLocalDateTime());
            java.sql.Timestamp updated = rs.getTimestamp("updated_at");
            if (updated != null) c.setUpdatedAt(updated.toLocalDateTime());
            return c;
        }
    };

    public Optional<Company> findByCompanyCode(String companyCode) {
        List<Company> list = jdbc.query("SELECT * FROM companies WHERE company_code = ?", CMP_MAPPER, companyCode);
        return list.stream().findFirst();
    }

    public Optional<Company> findByName(String name) {
        List<Company> list = jdbc.query("SELECT * FROM companies WHERE name = ?", CMP_MAPPER, name);
        return list.stream().findFirst();
    }

    public boolean existsByCompanyCode(String companyCode) {
        Integer c = jdbc.queryForObject("SELECT COUNT(1) FROM companies WHERE company_code = ?", Integer.class, companyCode);
        return c != null && c > 0;
    }

    public boolean existsByName(String name) {
        Integer c = jdbc.queryForObject("SELECT COUNT(1) FROM companies WHERE name = ?", Integer.class, name);
        return c != null && c > 0;
    }

    public Long save(Company c) {
        jdbc.update("INSERT INTO companies (name, company_code, email, phone, created_at, updated_at) VALUES (?, ?, ?, ?, NOW(), NOW())",
                c.getName(), c.getCompanyCode(), c.getEmail(), c.getPhone());
        Long id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        c.setId(id);
        return id;
    }

    public Optional<Company> findById(Long id) {
        List<Company> list = jdbc.query("SELECT * FROM companies WHERE id = ?", CMP_MAPPER, id);
        return list.stream().findFirst();
    }
}
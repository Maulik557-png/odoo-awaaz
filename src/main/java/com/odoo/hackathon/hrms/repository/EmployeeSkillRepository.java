package com.odoo.hackathon.hrms.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.odoo.hackathon.hrms.entity.EmployeeSkill;

@Repository
public class EmployeeSkillRepository {

    private final JdbcTemplate jdbc;

    public EmployeeSkillRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<EmployeeSkill> SKILL_MAPPER = new RowMapper<>() {
        @Override
        public EmployeeSkill mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeSkill es = new EmployeeSkill();
            es.setId(rs.getLong("id"));
            es.setEmployeeId(rs.getLong("employee_id"));
            es.setSkillName(rs.getString("skill_name"));
            es.setProficiency(rs.getString("proficiency"));
            java.sql.Timestamp created = rs.getTimestamp("created_at");
            if (created != null) es.setCreatedAt(created.toLocalDateTime());
            java.sql.Timestamp updated = rs.getTimestamp("updated_at");
            if (updated != null) es.setUpdatedAt(updated.toLocalDateTime());
            return es;
        }
    };

    public List<EmployeeSkill> findByEmployeeId(Long employeeId) {
        return jdbc.query("SELECT * FROM employee_skills WHERE employee_id = ?", SKILL_MAPPER, employeeId);
    }

    public void save(EmployeeSkill skill) {
        if (skill.getId() == null) {
            jdbc.update("INSERT INTO employee_skills (employee_id, skill_name, proficiency, created_at, updated_at) VALUES (?, ?, ?, NOW(), NOW())",
                    skill.getEmployeeId(), skill.getSkillName(), skill.getProficiency());
        } else {
            jdbc.update("UPDATE employee_skills SET skill_name = ?, proficiency = ?, updated_at = NOW() WHERE id = ?",
                    skill.getSkillName(), skill.getProficiency(), skill.getId());
        }
    }

    public void delete(Long id) {
        jdbc.update("DELETE FROM employee_skills WHERE id = ?", id);
    }
}

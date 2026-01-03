-- ============================================================================
-- STEP 2: SAMPLE DATA INSERTION (DML)
-- This script will:
-- 1. Insert sample company
-- 2. Insert sample users (Admin, HR Officer, Employees)
-- 3. Insert employee records
-- 4. Insert sample attendance records
-- 5. Insert sample leave requests
-- ============================================================================

-- Make sure we're using the correct database
USE odoo;

-- ============================================================================
-- COMPANY DATA
-- ============================================================================

-- Insert default company (Odoo India)
INSERT INTO companies (name, company_code, email, phone, created_at, updated_at) 
VALUES 
('Odoo India', 'OI', 'contact@odooindia.com', '+91-9876543210', NOW(), NOW());

-- Get the company_id
SET @company_id = LAST_INSERT_ID();

-- ============================================================================
-- ADMIN USER
-- ============================================================================

-- Insert default admin user
-- Password: Admin@123 (BCrypt hash: $2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi)
INSERT INTO users (login_id, password, role, is_first_login, is_active, company_id, created_at, updated_at)
VALUES 
('OIADMI20240001', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ADMIN', FALSE, TRUE, @company_id, NOW(), NOW());

SET @admin_user_id = LAST_INSERT_ID();

-- Insert admin employee record
INSERT INTO employees (first_name, last_name, email, phone, date_of_joining, department, designation, user_id, company_id, status, created_at, updated_at)
VALUES 
('Admin', 'User', 'admin@odooindia.com', '+91-9876543210', '2024-01-01', 'Administration', 'System Administrator', @admin_user_id, @company_id, 'ACTIVE', NOW(), NOW());

-- ============================================================================
-- HR OFFICER
-- ============================================================================

-- Insert HR Officer
INSERT INTO users (login_id, password, role, is_first_login, is_active, company_id, created_at, updated_at)
VALUES 
('OIHROF20240002', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'HR_OFFICER', TRUE, TRUE, @company_id, NOW(), NOW());

SET @hr_user_id = LAST_INSERT_ID();

INSERT INTO employees (first_name, last_name, email, phone, date_of_joining, department, designation, user_id, company_id, status, created_at, updated_at)
VALUES 
('HR', 'Officer', 'hr@odooindia.com', '+91-9876543211', '2024-01-02', 'Human Resources', 'HR Manager', @hr_user_id, @company_id, 'ACTIVE', NOW(), NOW());

-- ============================================================================
-- EMPLOYEE 1: John Doe
-- ============================================================================

INSERT INTO users (login_id, password, role, is_first_login, is_active, company_id, created_at, updated_at)
VALUES 
('OIJODO20220001', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'EMPLOYEE', TRUE, TRUE, @company_id, NOW(), NOW());

SET @employee1_user_id = LAST_INSERT_ID();

INSERT INTO employees (first_name, last_name, email, phone, date_of_joining, department, designation, user_id, company_id, status, created_at, updated_at)
VALUES 
('John', 'Doe', 'john.doe@odooindia.com', '+91-9876543212', '2022-03-15', 'Engineering', 'Software Engineer', @employee1_user_id, @company_id, 'ACTIVE', NOW(), NOW());

SET @employee1_id = LAST_INSERT_ID();

-- ============================================================================
-- EMPLOYEE 2: Alice Jackson
-- ============================================================================

INSERT INTO users (login_id, password, role, is_first_login, is_active, company_id, created_at, updated_at)
VALUES 
('OIALJA20220002', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'EMPLOYEE', TRUE, TRUE, @company_id, NOW(), NOW());

SET @employee2_user_id = LAST_INSERT_ID();

INSERT INTO employees (first_name, last_name, email, phone, date_of_joining, department, designation, user_id, company_id, status, created_at, updated_at)
VALUES 
('Alice', 'Jackson', 'alice.jackson@odooindia.com', '+91-9876543213', '2022-04-20', 'Engineering', 'Senior Developer', @employee2_user_id, @company_id, 'ACTIVE', NOW(), NOW());

SET @employee2_id = LAST_INSERT_ID();

-- ============================================================================
-- EMPLOYEE 3: Raj Patel
-- ============================================================================

INSERT INTO users (login_id, password, role, is_first_login, is_active, company_id, created_at, updated_at)
VALUES 
('OIRAPR20230001', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'EMPLOYEE', TRUE, TRUE, @company_id, NOW(), NOW());

SET @employee3_user_id = LAST_INSERT_ID();

INSERT INTO employees (first_name, last_name, email, phone, date_of_joining, department, designation, user_id, company_id, status, created_at, updated_at)
VALUES 
('Raj', 'Patel', 'raj.patel@odooindia.com', '+91-9876543214', '2023-01-10', 'Marketing', 'Marketing Manager', @employee3_user_id, @company_id, 'ACTIVE', NOW(), NOW());

SET @employee3_id = LAST_INSERT_ID();

-- ============================================================================
-- ATTENDANCE DATA (for John Doe - Last 5 days)
-- ============================================================================

INSERT INTO attendance (employee_id, attendance_date, check_in_time, check_out_time, status, remarks, created_at, updated_at)
VALUES 
(@employee1_id, DATE_SUB(CURDATE(), INTERVAL 4 DAY), '09:00:00', '18:00:00', 'PRESENT', 'On time', NOW(), NOW()),
(@employee1_id, DATE_SUB(CURDATE(), INTERVAL 3 DAY), '09:15:00', '18:30:00', 'LATE', 'Traffic delay', NOW(), NOW()),
(@employee1_id, DATE_SUB(CURDATE(), INTERVAL 2 DAY), '09:00:00', '13:00:00', 'HALF_DAY', 'Medical appointment', NOW(), NOW()),
(@employee1_id, DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:00:00', '18:00:00', 'PRESENT', NULL, NOW(), NOW()),
(@employee1_id, CURDATE(), '09:05:00', NULL, 'PRESENT', 'Currently working', NOW(), NOW());

-- ============================================================================
-- LEAVE REQUESTS
-- ============================================================================

-- Pending leave request for John Doe
INSERT INTO leave_requests (employee_id, leave_type, start_date, end_date, total_days, reason, status, created_at, updated_at)
VALUES 
(@employee1_id, 'SICK_LEAVE', DATE_ADD(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 6 DAY), 2, 'Medical appointment and recovery', 'PENDING', NOW(), NOW());

-- Approved leave request for Alice Jackson
INSERT INTO leave_requests (employee_id, leave_type, start_date, end_date, total_days, reason, status, approved_by, approved_at, created_at, updated_at)
VALUES 
(@employee2_id, 'PAID_LEAVE', DATE_ADD(CURDATE(), INTERVAL 10 DAY), DATE_ADD(CURDATE(), INTERVAL 14 DAY), 5, 'Family vacation', 'APPROVED', @admin_user_id, NOW(), NOW(), NOW());

-- Rejected leave request for Raj Patel
INSERT INTO leave_requests (employee_id, leave_type, start_date, end_date, total_days, reason, status, approved_by, rejection_reason, created_at, updated_at)
VALUES 
(@employee3_id, 'UNPAID_LEAVE', DATE_ADD(CURDATE(), INTERVAL 3 DAY), DATE_ADD(CURDATE(), INTERVAL 5 DAY), 3, 'Personal work', 'REJECTED', @hr_user_id, 'Critical project deadline approaching', NOW(), NOW());

-- ============================================================================
-- VERIFICATION
-- ============================================================================

SELECT '============================================' AS '';
SELECT 'SAMPLE DATA INSERTED SUCCESSFULLY!' AS 'STATUS';
SELECT '============================================' AS '';
SELECT '' AS '';
SELECT 'Data Summary:' AS '';
SELECT COUNT(*) AS 'Companies' FROM companies;
SELECT COUNT(*) AS 'Users' FROM users;
SELECT COUNT(*) AS 'Employees' FROM employees;
SELECT COUNT(*) AS 'Attendance Records' FROM attendance;
SELECT COUNT(*) AS 'Leave Requests' FROM leave_requests;
SELECT '' AS '';
SELECT 'User Breakdown by Role:' AS '';
SELECT role AS 'Role', COUNT(*) AS 'Count' FROM users GROUP BY role;
SELECT '' AS '';
SELECT 'Default Login Credentials:' AS '';
SELECT '--------------------------------------------' AS '';
SELECT 'ADMIN' AS 'Role', 'OIADMI20240001' AS 'Login ID', 'Admin@123' AS 'Password';
SELECT 'HR_OFFICER' AS 'Role', 'OIHROF20240002' AS 'Login ID', 'Admin@123' AS 'Password';
SELECT 'EMPLOYEE' AS 'Role', 'OIJODO20220001' AS 'Login ID', 'Admin@123' AS 'Password';
SELECT 'EMPLOYEE' AS 'Role', 'OIALJA20220002' AS 'Login ID', 'Admin@123' AS 'Password';
SELECT 'EMPLOYEE' AS 'Role', 'OIRAPR20230001' AS 'Login ID', 'Admin@123' AS 'Password';
SELECT '' AS '';
SELECT 'Database Connection Details:' AS '';
SELECT '--------------------------------------------' AS '';
SELECT 'Database' AS 'odoo', 'Username' AS 'odoo_admin', 'Password' AS 'Odoo@2024';
SELECT '============================================' AS '';
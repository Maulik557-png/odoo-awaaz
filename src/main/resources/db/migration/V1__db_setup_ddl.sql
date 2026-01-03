-- ============================================================================
-- STEP 1: USER AND DATABASE SETUP + DDL
-- This script will:
-- 1. Create a dedicated MySQL user
-- 2. Create the database
-- 3. Grant permissions
-- 4. Drop existing tables (if any)
-- 5. Create fresh tables
-- ============================================================================

-- Step 1: Create dedicated user for the application
DROP USER IF EXISTS 'odoo_admin'@'localhost';
CREATE USER 'odoo_admin'@'localhost' IDENTIFIED BY 'Odoo@2024';

-- Step 2: Drop database if exists and create fresh
DROP DATABASE IF EXISTS odoo;
CREATE DATABASE odoo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Step 3: Grant all privileges to the user
GRANT ALL PRIVILEGES ON odoo.* TO 'odoo_admin'@'localhost';
FLUSH PRIVILEGES;

-- Step 4: Use the database
USE odoo;

-- Step 5: Drop existing tables if any (in correct order due to foreign keys)
DROP TABLE IF EXISTS password_history;
DROP TABLE IF EXISTS leave_requests;
DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS companies;

-- ============================================================================
-- TABLE CREATION (DDL)
-- ============================================================================

-- Create companies table
CREATE TABLE companies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    company_code VARCHAR(10) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_company_code (company_code),
    INDEX idx_company_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_id VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'HR_OFFICER', 'EMPLOYEE') NOT NULL,
    is_first_login BOOLEAN NOT NULL DEFAULT TRUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    company_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE,
    INDEX idx_login_id (login_id),
    INDEX idx_company_id (company_id),
    INDEX idx_role (role),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create employees table
CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    date_of_joining DATE NOT NULL,
    department VARCHAR(100),
    designation VARCHAR(100),
    user_id BIGINT NOT NULL,
    company_id BIGINT,
    status ENUM('ACTIVE', 'INACTIVE', 'TERMINATED', 'ON_LEAVE') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_employees_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_employees_company FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE,
    INDEX idx_email (email),
    INDEX idx_user_id (user_id),
    INDEX idx_company_id (company_id),
    INDEX idx_date_of_joining (date_of_joining),
    INDEX idx_status (status),
    INDEX idx_department (department)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create attendance table
CREATE TABLE attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    check_in_time TIME,
    check_out_time TIME,
    status ENUM('PRESENT', 'ABSENT', 'HALF_DAY', 'LATE', 'LEAVE') NOT NULL,
    remarks TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_attendance_employee FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    CONSTRAINT unique_employee_date UNIQUE (employee_id, attendance_date),
    INDEX idx_employee_id (employee_id),
    INDEX idx_attendance_date (attendance_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create leave_requests table
CREATE TABLE leave_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    leave_type ENUM('PAID_LEAVE', 'SICK_LEAVE', 'UNPAID_LEAVE', 'CASUAL_LEAVE') NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_days INT NOT NULL,
    reason TEXT NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    approved_by BIGINT,
    approved_at TIMESTAMP NULL,
    rejection_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_leave_requests_employee FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    CONSTRAINT fk_leave_requests_approver FOREIGN KEY (approved_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_employee_id (employee_id),
    INDEX idx_status (status),
    INDEX idx_start_date (start_date),
    INDEX idx_end_date (end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create password_history table
CREATE TABLE password_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_password_history_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- VERIFICATION
-- ============================================================================

SELECT '============================================' AS '';
SELECT 'DDL SETUP COMPLETED SUCCESSFULLY!' AS 'STATUS';
SELECT '============================================' AS '';
SELECT '' AS '';
SELECT 'Database Information:' AS '';
SELECT DATABASE() AS 'Current Database';
SELECT COUNT(*) AS 'Total Tables Created' FROM information_schema.tables WHERE table_schema = 'odoo';
SELECT '' AS '';
SELECT 'Tables Created:' AS '';
SELECT table_name AS 'Table Name' FROM information_schema.tables WHERE table_schema = 'odoo' ORDER BY table_name;
SELECT '============================================' AS '';
SELECT 'Next Step: Run V2__sample_data_dml.sql to insert sample data' AS 'INFO';
SELECT '============================================' AS '';
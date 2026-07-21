# Human Resource Management System (HRMS)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> **Tagline:** Every workday, perfectly aligned.

A modern, full-stack Human Resource Management System built with Spring Boot and React, implementing Rapid Application Development (RAD) methodology.

---

## 📑 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [System Architecture](#-system-architecture)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Database Setup](#-database-setup)
- [Configuration](#-configuration)
- [Running the Application](#-running-the-application)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [Default Credentials](#-default-credentials)
- [Development Guidelines](#-development-guidelines)
- [Troubleshooting](#-troubleshooting)
- [Roadmap](#-roadmap)
- [License](#-license)

---

## 🎯 Overview

Dayflow HRMS is a comprehensive Human Resource Management System designed to digitize and streamline core HR operations. The system provides secure authentication, role-based access control, employee management, attendance tracking, leave management, and payroll processing capabilities.

### Project Scope

- ✅ Secure authentication (Sign Up / Sign In / Role-based Endpoints)
- ✅ Role-based access control (Admin, HR Officer, Employee)
- ✅ Employee profile management & editing
- ✅ Attendance tracking (Check-In / Check-Out & Work Hours calculation)
- ✅ Leave and time-off management (Submission, Approval & Rejection workflows)
- ✅ Payroll & Salary structure management
- ✅ Approval workflows for HR/Admin

---

## ✨ Features

### 🔐 Authentication & Authorization
- JWT-based authentication with `JwtAuthenticationFilter`
- Role-based access control (RBAC: `ADMIN`, `HR_OFFICER`, `EMPLOYEE`)
- Auto-generated Employee Login IDs (e.g. `OIJODO20260001`)
- Password change functionality (`/api/auth/change-password`)
- Dedicated registration endpoints: `/api/auth/signup/admin` and `/api/auth/signup/employee` (or `/api/auth/register`)

### 👥 Employee Management
- Complete employee profile viewing (`/api/employees/profile`, `/api/employees/{id}`)
- Profile editing capabilities (`PUT /api/employees/{id}`)
- Admin onboarding (`POST /api/employees`) with automatic Login ID & temp password generation
- Department, designation, and employment status tracking

### 📅 Attendance Tracking
- Daily check-in & check-out (`/api/attendance/check-in`, `/api/attendance/check-out`)
- Automatic work hours calculation (`"8h 30m"`)
- Attendance status tracking (Present, Absent, Late, Half-Day, Leave)
- Attendance history with date range filtering (`startDate`, `endDate`)

### 🏖️ Leave Management
- Multiple leave types (Paid Leave, Sick Leave, Unpaid Leave, Casual Leave)
- Leave request submission with automated total days calculation
- Approval & Rejection workflow with rejection reasons (`PUT /api/leave-requests/{id}/approve`, `/reject`)
- Request cancellation (`DELETE /api/leave-requests/{id}`)

### 💰 Payroll & Salary Management
- Employee read-only payroll view (`/api/payroll/my-payroll`)
- Admin company-wide payroll processing (`/api/payroll`)
- Salary structure creation and update (`POST /api/payroll/salary`)
- Automatic breakdown calculation (Basic Salary, Allowances, Deductions, Net Salary)

---

## 🛠️ Tech Stack

### Backend
- **Framework:** Spring Boot 3.5.9
- **Language:** Java 17
- **Security:** Spring Security + JWT (JJWT 0.11.5)
- **Database:** MySQL 8.0+
- **Data Access:** Spring JdbcTemplate
- **Build Tool:** Maven

### Frontend
- **Framework:** React 18
- **Styling:** Tailwind CSS
- **Routing:** React Router v6

---

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Client Layer                         │
│                     (React + Tailwind)                      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     API Gateway Layer                       │
│                  (Spring Boot REST APIs)                    │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    Security Layer                           │
│              (JWT Authentication + Spring Security)         │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    Service Layer                            │
│ (AuthService, EmployeeService, AttendanceService,           │
│  LeaveRequestService, PayrollService)                       │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                  Data Access Layer                          │
│     (Spring JdbcTemplate Repositories & SQL Mappers)        │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      Database Layer                         │
│                      (MySQL Database)                       │
└─────────────────────────────────────────────────────────────┘
```

---

## ⚙️ Configuration

### Backend Configuration (`src/main/resources/application-local.properties`):

```properties
spring.application.name=dayflow-hrms
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/odoo?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=odoo_admin
spring.datasource.password=Odoo@2024
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

jwt.secret=YOUR_JWT_SECRET_HERE
jwt.expiration=86400000
```

---

## 🚀 Running the Application

### Start Backend Server

```bash
# Run Maven wrapper from workspace root
.\mvnw spring-boot:run
```

Backend will start at: `http://localhost:8080`

---

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### 1. Authentication Endpoints

#### User Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "loginId": "OIJODO20260001",
  "password": "EmployeePassword@123"
}

Response (200 OK):
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "loginId": "OIJODO20260001"
}
```

#### Admin Signup
```http
POST /api/auth/signup/admin
Content-Type: application/json

{
  "companyName": "Odoo India",
  "name": "Admin User",
  "phone": "+919876543210",
  "email": "admin@odooindia.com",
  "password": "AdminPassword@123",
  "confirmPassword": "AdminPassword@123"
}

Response (200 OK):
"admin-created"
```

#### Employee Self-Registration
```http
POST /api/auth/signup/employee
Content-Type: application/json

{
  "companyName": "Odoo India",
  "name": "John Doe",
  "phone": "+919876543211",
  "email": "john.doe@odooindia.com",
  "password": "EmployeePassword@123",
  "confirmPassword": "EmployeePassword@123"
}

Response (200 OK):
"OIJODO20260001"
```

#### Change Password
```http
POST /api/auth/change-password
Authorization: Bearer {token}
Content-Type: application/json

{
  "currentPassword": "OldPassword@123",
  "newPassword": "NewPassword@123",
  "confirmPassword": "NewPassword@123"
}

Response (200 OK):
"Password changed successfully"
```

---

### 2. Employee Endpoints

#### Create Employee (Admin/HR)
```http
POST /api/employees
Authorization: Bearer {token}
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane.doe@odooindia.com",
  "phone": "+91-9876543219",
  "dateOfJoining": "2026-01-15",
  "department": "Engineering",
  "designation": "Software Developer",
  "role": "EMPLOYEE"
}
```

#### Get Current Employee Profile
```http
GET /api/employees/profile
Authorization: Bearer {token}
```

#### Update Employee Profile
```http
PUT /api/employees/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "phone": "+91-9999988888",
  "department": "R&D",
  "about": "Full-stack developer working on HRMS."
}
```

#### Get All Employees
```http
GET /api/employees
Authorization: Bearer {token}
```

---

### 3. Attendance Endpoints

#### Check-In
```http
POST /api/attendance/check-in
Authorization: Bearer {token}
```

#### Check-Out
```http
POST /api/attendance/check-out
Authorization: Bearer {token}
```

#### Get My Attendance
```http
GET /api/attendance/my-attendance?startDate=2026-01-01&endDate=2026-12-31
Authorization: Bearer {token}
```

#### Get All Attendance (Admin/HR)
```http
GET /api/attendance?startDate=2026-01-01&endDate=2026-12-31
Authorization: Bearer {token}
```

---

### 4. Leave Request Endpoints

#### Submit Leave Request
```http
POST /api/leave-requests
Authorization: Bearer {token}
Content-Type: application/json

{
  "leaveType": "PAID_LEAVE",
  "startDate": "2026-08-01",
  "endDate": "2026-08-05",
  "reason": "Annual vacation"
}
```

#### Get My Leave Requests
```http
GET /api/leave-requests/my-requests
Authorization: Bearer {token}
```

#### Approve Leave (Admin/HR)
```http
PUT /api/leave-requests/{id}/approve
Authorization: Bearer {token}
```

#### Reject Leave (Admin/HR)
```http
PUT /api/leave-requests/{id}/reject
Authorization: Bearer {token}
Content-Type: application/json

{
  "rejectionReason": "Project deadline conflict"
}
```

#### Delete Leave Request
```http
DELETE /api/leave-requests/{id}
Authorization: Bearer {token}
```

---

### 5. Payroll Endpoints

#### Get My Payroll
```http
GET /api/payroll/my-payroll
Authorization: Bearer {token}
```

#### Get All Payroll (Admin/HR)
```http
GET /api/payroll
Authorization: Bearer {token}
```

#### Update Salary Structure (Admin/HR)
```http
POST /api/payroll/salary
Authorization: Bearer {token}
Content-Type: application/json

{
  "employeeId": 1,
  "amount": 75000.00,
  "currency": "USD",
  "effectiveDate": "2026-01-01"
}
```

---

## 🔑 Default Credentials

### Database Access
```
Host: localhost
Port: 3306
Database: odoo
Username: odoo_admin
Password: Odoo@2024
```

### Application Users

| Role | Login ID | Password | Description |
|------|----------|----------|-------------|
| **Admin** | OIADMI20240001 | Admin@123 | System Administrator |
| **HR Officer** | OIHROF20240002 | Admin@123 | HR Manager |
| **Employee** | OIJODO20220001 | Admin@123 | Software Engineer |

---

## 🚀 Roadmap

### Phase 1 (Completed ✅)
- [x] Authentication & Authorization (Login, Admin/Employee Signup, Change Password)
- [x] Employee Management & Profile Editing
- [x] Attendance Tracking (Check-In, Check-Out, Work Hours)
- [x] Leave Management (Apply, Approve, Reject, Delete)
- [x] Payroll & Salary Management (View Payroll, Update Salary)

### Phase 2 (Future)
- [ ] Document Management
- [ ] Email & Notification Alerts
- [ ] Analytics & Performance Dashboard

---

*Last Updated: July 2026*

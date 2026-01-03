# Dayflow - Human Resource Management System (HRMS)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
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
- [Contributors](#-contributors)
- [License](#-license)

---

## 🎯 Overview

Dayflow HRMS is a comprehensive Human Resource Management System designed to digitize and streamline core HR operations. The system provides secure authentication, role-based access control, employee management, attendance tracking, and leave management capabilities.

### Project Scope

- ✅ Secure authentication (Sign Up / Sign In)
- ✅ Role-based access (Admin, HR Officer, Employee)
- ✅ Employee profile management
- ✅ Attendance tracking (daily/weekly view)
- ✅ Leave and time-off management
- ✅ Approval workflows for HR/Admin

---

## ✨ Features

### 🔐 Authentication & Authorization
- JWT-based authentication
- Role-based access control (RBAC)
- Auto-generated login IDs (Format: `OIJODO20220001`)
- System-generated passwords on first login
- Password change functionality

### 👥 Employee Management
- Complete employee profile management
- Department and designation tracking
- Employment status management
- Automated serial number generation

### 📅 Attendance Tracking
- Daily check-in/check-out
- Attendance status (Present, Absent, Late, Half-Day, Leave)
- Weekly and monthly attendance reports
- Remarks and notes

### 🏖️ Leave Management
- Multiple leave types (Paid, Sick, Unpaid, Casual)
- Leave request submission
- Approval/rejection workflow
- Leave balance tracking

### 👨‍💼 Admin Features
- Employee onboarding
- Attendance monitoring
- Leave request approvals
- System configuration

---

## 🛠️ Tech Stack

### Backend
- **Framework:** Spring Boot 3.2.1
- **Language:** Java 17
- **Security:** Spring Security + JWT
- **Database:** MySQL 8.0+
- **ORM:** Spring Data JPA (Hibernate)
- **Build Tool:** Maven
- **Authentication:** JSON Web Tokens (JJWT 0.11.5)

### Frontend
- **Framework:** React 18
- **Language:** TypeScript
- **Styling:** Tailwind CSS
- **Routing:** Next.js

### Development Tools
- **IDE:** IntelliJ IDEA / Eclipse / VS Code
- **API Testing:** Postman
- **Database Client:** MySQL Workbench / DBeaver
- **Version Control:** Git

---

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Client Layer                          │
│              (React + Next.js + TypeScript)                  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     API Gateway Layer                        │
│                  (Spring Boot REST APIs)                     │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    Security Layer                            │
│              (JWT Authentication + Spring Security)          │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    Service Layer                             │
│     (Business Logic + LoginID Generation + Validation)       │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                  Data Access Layer                           │
│                 (Spring Data JPA Repositories)               │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      Database Layer                          │
│                      (MySQL Database)                        │
└─────────────────────────────────────────────────────────────┘
```

---

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

| Software | Version | Download Link |
|----------|---------|---------------|
| Java JDK | 17 or higher | [Download](https://www.oracle.com/java/technologies/downloads/) |
| Maven | 3.8+ | [Download](https://maven.apache.org/download.cgi) |
| MySQL | 8.0+ | [Download](https://dev.mysql.com/downloads/mysql/) |
| Node.js | 18+ | [Download](https://nodejs.org/) |
| Git | Latest | [Download](https://git-scm.com/downloads) |

### Verify Installation

```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check MySQL version
mysql --version

# Check Node.js version
node --version

# Check Git version
git --version
```

---

## 📥 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/odoo-hrms.git
cd odoo-hrms
```

### 2. Backend Setup

```bash
# Navigate to backend directory
cd backend

# Install dependencies
mvn clean install

# Skip tests if needed
mvn clean install -DskipTests
```

### 3. Frontend Setup

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Or using yarn
yarn install
```

---

## 🗄️ Database Setup

### Step 1: Start MySQL Service

```bash
# On Linux/Mac
sudo systemctl start mysql

# On Windows (Command Prompt as Admin)
net start MySQL
```

### Step 2: Execute Database Scripts

The project includes two SQL scripts for database setup:

1. **V1__setup_and_ddl.sql** - Creates database, user, and tables
2. **V2__sample_data_dml.sql** - Inserts sample data

#### Method 1: Using MySQL Command Line

```bash
# Navigate to database scripts directory
cd src/main/resources/db/migration

# Execute DDL script (creates database and tables)
mysql -u root -p < V1__setup_and_ddl.sql

# Execute DML script (inserts sample data)
mysql -u root -p < V2__sample_data_dml.sql
```

#### Method 2: Using MySQL Workbench

1. Open MySQL Workbench
2. Connect to your MySQL server as `root`
3. File → Open SQL Script → Select `V1__setup_and_ddl.sql`
4. Execute (Lightning bolt icon ⚡)
5. Repeat for `V2__sample_data_dml.sql`

### Step 3: Verify Database Setup

```bash
mysql -u odoo_admin -p
# Password: Odoo@2024

mysql> USE odoo;
mysql> SHOW TABLES;
mysql> SELECT * FROM users;
mysql> SELECT * FROM employees;
```

Expected tables:
- `companies`
- `users`
- `employees`
- `attendance`
- `leave_requests`
- `password_history`

---

## ⚙️ Configuration

### Backend Configuration

Edit `src/main/resources/application.properties`:

```properties
# Application Configuration
spring.application.name=odoo-hrms
server.port=8080

# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/odoo?useSSL=false&serverTimezone=UTC
spring.datasource.username=odoo_admin
spring.datasource.password=Odoo@2024
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT Configuration
jwt.secret=YOUR_JWT_SECRET_HERE
jwt.expiration=86400000

# Logging
logging.level.com.odoo.hackathon.hrms=DEBUG
```

### Generate JWT Secret

```bash
# Generate a secure JWT secret
openssl rand -base64 64

# Copy the output and paste it in application.properties
```

### Frontend Configuration

Create `.env.local` in frontend directory:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api
NEXT_PUBLIC_APP_NAME=Dayflow HRMS
```

---

## 🚀 Running the Application

### Start Backend Server

```bash
# From backend directory
mvn spring-boot:run

# Or if using IDE, run the main application class:
# com.odoo.hackathon.hrms.HrmsApplication
```

Backend will start at: `http://localhost:8080`

### Start Frontend Server

```bash
# From frontend directory
npm run dev

# Or using yarn
yarn dev
```

Frontend will start at: `http://localhost:3000`

### Verify Application is Running

```bash
# Test backend health
curl http://localhost:8080/actuator/health

# Or open in browser
http://localhost:8080
```

---

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### 1. Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "loginId": "OIADMI20240001",
  "password": "Admin@123"
}

Response:
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "loginId": "OIADMI20240001",
  "name": "Admin User",
  "email": "admin@odooindia.com",
  "role": "ADMIN",
  "isFirstLogin": false,
  "message": "Login successful"
}
```

#### 2. Change Password
```http
POST /api/auth/change-password
Authorization: Bearer {token}
Content-Type: application/json

{
  "currentPassword": "OldPassword@123",
  "newPassword": "NewPassword@123",
  "confirmPassword": "NewPassword@123"
}
```

### Employee Endpoints

#### 1. Create Employee (Admin/HR Only)
```http
POST /api/employees
Authorization: Bearer {token}
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@odooindia.com",
  "phone": "+91-9876543212",
  "dateOfJoining": "2024-01-15",
  "department": "Engineering",
  "designation": "Software Engineer",
  "role": "EMPLOYEE"
}

Response:
{
  "loginId": "OIJODO20240003",
  "temporaryPassword": "Temp@1234XyZ",
  "message": "Employee created successfully"
}
```

#### 2. Get Employee Profile
```http
GET /api/employees/profile
Authorization: Bearer {token}
```

#### 3. Get All Employees (Admin/HR Only)
```http
GET /api/employees
Authorization: Bearer {token}
```

### Attendance Endpoints

#### 1. Check-In
```http
POST /api/attendance/check-in
Authorization: Bearer {token}
```

#### 2. Check-Out
```http
POST /api/attendance/check-out
Authorization: Bearer {token}
```

#### 3. Get My Attendance
```http
GET /api/attendance/my-attendance?startDate=2024-01-01&endDate=2024-01-31
Authorization: Bearer {token}
```

### Leave Request Endpoints

#### 1. Submit Leave Request
```http
POST /api/leave-requests
Authorization: Bearer {token}
Content-Type: application/json

{
  "leaveType": "PAID_LEAVE",
  "startDate": "2024-02-01",
  "endDate": "2024-02-03",
  "reason": "Family vacation"
}
```

#### 2. Get My Leave Requests
```http
GET /api/leave-requests/my-requests
Authorization: Bearer {token}
```

#### 3. Approve/Reject Leave (Admin/HR Only)
```http
PUT /api/leave-requests/{id}/approve
Authorization: Bearer {token}

PUT /api/leave-requests/{id}/reject
Authorization: Bearer {token}
Content-Type: application/json

{
  "rejectionReason": "Insufficient leave balance"
}
```

---

## 📁 Project Structure

```
odoo-hrms/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/odoo/hackathon/hrms/
│   │   │   │       ├── config/
│   │   │   │       │   ├── JpaConfig.java
│   │   │   │       │   ├── JwtConfig.java
│   │   │   │       │   ├── SecurityConfig.java
│   │   │   │       │   └── PasswordEncoderConfig.java
│   │   │   │       │
│   │   │   │       ├── controller/
│   │   │   │       │   ├── AuthController.java
│   │   │   │       │   ├── EmployeeController.java
│   │   │   │       │   ├── AttendanceController.java
│   │   │   │       │   └── LeaveRequestController.java
│   │   │   │       │
│   │   │   │       ├── dto/
│   │   │   │       │   ├── request/
│   │   │   │       │   │   ├── LoginRequest.java
│   │   │   │       │   │   ├── CreateEmployeeRequest.java
│   │   │   │       │   │   ├── ChangePasswordRequest.java
│   │   │   │       │   │   └── LeaveRequestDto.java
│   │   │   │       │   │
│   │   │   │       │   └── response/
│   │   │   │       │       ├── LoginResponse.java
│   │   │   │       │       ├── EmployeeResponse.java
│   │   │   │       │       └── ApiResponse.java
│   │   │   │       │
│   │   │   │       ├── entity/
│   │   │   │       │   ├── User.java
│   │   │   │       │   ├── Employee.java
│   │   │   │       │   ├── Company.java
│   │   │   │       │   ├── Attendance.java
│   │   │   │       │   ├── LeaveRequest.java
│   │   │   │       │   └── PasswordHistory.java
│   │   │   │       │
│   │   │   │       ├── repository/
│   │   │   │       │   ├── UserRepository.java
│   │   │   │       │   ├── EmployeeRepository.java
│   │   │   │       │   ├── CompanyRepository.java
│   │   │   │       │   ├── AttendanceRepository.java
│   │   │   │       │   └── LeaveRequestRepository.java
│   │   │   │       │
│   │   │   │       ├── service/
│   │   │   │       │   ├── AuthService.java
│   │   │   │       │   ├── EmployeeService.java
│   │   │   │       │   ├── AttendanceService.java
│   │   │   │       │   ├── LeaveRequestService.java
│   │   │   │       │   ├── LoginIdGeneratorService.java
│   │   │   │       │   └── PasswordGeneratorService.java
│   │   │   │       │
│   │   │   │       ├── security/
│   │   │   │       │   ├── JwtTokenProvider.java
│   │   │   │       │   ├── JwtAuthenticationFilter.java
│   │   │   │       │   └── UserDetailsServiceImpl.java
│   │   │   │       │
│   │   │   │       ├── enums/
│   │   │   │       │   ├── Role.java
│   │   │   │       │   ├── EmploymentStatus.java
│   │   │   │       │   ├── AttendanceStatus.java
│   │   │   │       │   └── LeaveType.java
│   │   │   │       │
│   │   │   │       ├── exception/
│   │   │   │       │   ├── GlobalExceptionHandler.java
│   │   │   │       │   ├── ResourceNotFoundException.java
│   │   │   │       │   ├── InvalidCredentialsException.java
│   │   │   │       │   └── DuplicateResourceException.java
│   │   │   │       │
│   │   │   │       ├── util/
│   │   │   │       │   ├── PasswordValidator.java
│   │   │   │       │   └── DateUtil.java
│   │   │   │       │
│   │   │   │       └── HrmsApplication.java
│   │   │   │
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── db/
│   │   │           └── migration/
│   │   │               ├── V1__setup_and_ddl.sql
│   │   │               └── V2__sample_data_dml.sql
│   │   │
│   │   └── test/
│   │       └── java/
│   │           └── com/odoo/hackathon/hrms/
│   │               └── HrmsApplicationTests.java
│   │
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   ├── components/
│   │   ├── lib/
│   │   └── styles/
│   │
│   ├── public/
│   ├── package.json
│   └── next.config.js
│
├── README.md
├── .gitignore
└── LICENSE
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
| **Employee** | OIALJA20220002 | Admin@123 | Senior Developer |
| **Employee** | OIRAPR20230001 | Admin@123 | Marketing Manager |

### Login ID Format

```
Format: [CompanyCode][FirstName2Letters][LastName2Letters][Year][SerialNumber]
Example: OIJODO20220001

Where:
- OI = Odoo India (Company Code)
- JO = John (First 2 letters of first name)
- DO = Doe (First 2 letters of last name)
- 2022 = Year of Joining
- 0001 = Serial number for that year
```

---

## 💻 Development Guidelines

### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Keep methods small and focused
- Use Lombok to reduce boilerplate code

### Git Workflow
```bash
# Create feature branch
git checkout -b feature/employee-management

# Make changes and commit
git add .
git commit -m "feat: Add employee creation endpoint"

# Push to remote
git push origin feature/employee-management

# Create Pull Request on GitHub
```

### Commit Message Convention
```
feat: Add new feature
fix: Fix bug
docs: Update documentation
style: Format code
refactor: Refactor code
test: Add tests
chore: Update dependencies
```

### Testing
```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=EmployeeServiceTest

# Run with coverage
mvn test jacoco:report
```

---

## 🐛 Troubleshooting

### Issue 1: Application Failed to Start - DataSource Error

**Error:**
```
Failed to configure a DataSource: 'url' attribute is not specified
```

**Solution:**
1. Check `application.properties` exists in `src/main/resources/`
2. Verify MySQL is running: `systemctl status mysql`
3. Verify database exists: `mysql -u odoo_admin -p odoo`
4. Check MySQL connector dependency in `pom.xml`

---

### Issue 2: JWT Token Errors

**Error:**
```
Cannot resolve symbol 'Jwts' or 'SignatureAlgorithm'
```

**Solution:**
1. Verify JWT dependencies in `pom.xml` (version 0.11.5)
2. Reload Maven: Right-click `pom.xml` → Maven → Reload Project
3. Clean and rebuild: `mvn clean install`

---

### Issue 3: Port Already in Use

**Error:**
```
Port 8080 was already in use
```

**Solution:**
```bash
# Find process using port 8080
lsof -i :8080  # Mac/Linux
netstat -ano | findstr :8080  # Windows

# Kill the process
kill -9 <PID>  # Mac/Linux
taskkill /PID <PID> /F  # Windows

# Or change port in application.properties
server.port=8081
```

---

### Issue 4: Lombok Not Working

**Solution:**

**IntelliJ IDEA:**
1. File → Settings → Plugins → Install "Lombok"
2. File → Settings → Build → Compiler → Annotation Processors
3. Enable "Enable annotation processing"

**Eclipse:**
1. Download lombok.jar from https://projectlombok.org/download
2. Run: `java -jar lombok.jar`
3. Select Eclipse installation and click Install

---

### Issue 5: Database Connection Refused

**Solution:**
```bash
# Check MySQL is running
sudo systemctl status mysql

# Start MySQL
sudo systemctl start mysql

# Check if database exists
mysql -u root -p
mysql> SHOW DATABASES;
mysql> USE odoo;
```

---

### Issue 6: CORS Error in Frontend

**Solution:**

Add CORS configuration in `SecurityConfig.java`:

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```

---

## 📊 Database Schema

### ER Diagram

```
┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│  companies   │         │    users     │         │  employees   │
├──────────────┤         ├──────────────┤         ├──────────────┤
│ id (PK)      │◄───┐    │ id (PK)      │◄───┐    │ id (PK)      │
│ name         │    └────│ company_id   │    └────│ user_id      │
│ company_code │         │ login_id     │         │ company_id   │
│ email        │         │ password     │         │ first_name   │
│ phone        │         │ role         │         │ last_name    │
└──────────────┘         │ is_first_login│        │ email        │
                         │ is_active    │         │ phone        │
                         └──────────────┘         │ date_of_joining│
                                                   │ department   │
                                                   │ designation  │
                                                   │ status       │
                                                   └──────────────┘
                                                          │
                              ┌───────────────────────────┴───────────────────────────┐
                              │                                                       │
                              ▼                                                       ▼
                    ┌──────────────┐                                        ┌──────────────┐
                    │ attendance   │                                        │leave_requests│
                    ├──────────────┤                                        ├──────────────┤
                    │ id (PK)      │                                        │ id (PK)      │
                    │ employee_id  │                                        │ employee_id  │
                    │ attendance_date│                                      │ leave_type   │
                    │ check_in_time│                                        │ start_date   │
                    │ check_out_time│                                       │ end_date     │
                    │ status       │                                        │ total_days   │
                    │ remarks      │                                        │ reason       │
                    └──────────────┘                                        │ status       │
                                                                            │ approved_by  │
                                                                            └──────────────┘
```

---

## 🤝 Contributors

- **Your Name** - *Initial work* - [GitHub Profile](https://github.com/yourusername)

See also the list of [contributors](https://github.com/yourusername/odoo-hrms/contributors) who participated in this project.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- Spring Boot Documentation
- React Documentation
- MySQL Documentation
- Tailwind CSS
- JWT.io
- Stack Overflow Community

---

## 📞 Support

For support, email support@dayflow.com or join our Slack channel.

---

## 🚀 Roadmap

### Phase 1 (Current)
- [x] Authentication & Authorization
- [x] Employee Management
- [x] Attendance Tracking
- [x] Leave Management

### Phase 2 (Future)
- [ ] Payroll Management
- [ ] Performance Reviews
- [ ] Document Management
- [ ] Email Notifications
- [ ] Mobile App

### Phase 3 (Future)
- [ ] Analytics Dashboard
- [ ] Report Generation
- [ ] Integration with third-party tools
- [ ] Multi-company support

---

## 📸 Screenshots

### Login Page
![Login Page](screenshots/login.png)

### Dashboard
![Dashboard](screenshots/dashboard.png)

### Employee Management
![Employee Management](screenshots/employees.png)

### Attendance Tracking
![Attendance](screenshots/attendance.png)

### Leave Requests
![Leave Requests](screenshots/leave-requests.png)

---

## 🔗 Related Documentation

- [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [JWT Introduction](https://jwt.io/introduction)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [React Documentation](https://react.dev/)
- [Next.js Documentation](https://nextjs.org/docs)

---

## ⚡ Quick Start Commands

```bash
# Clone repository
git clone https://github.com/yourusername/odoo-hrms.git && cd odoo-hrms

# Setup database
mysql -u root -p < src/main/resources/db/migration/V1__setup_and_ddl.sql
mysql -u root -p < src/main/resources/db/migration/V2__sample_data_dml.sql

# Build and run backend
cd backend && mvn spring-boot:run

# Install and run frontend (in new terminal)
cd frontend && npm install && npm run dev
```

Access application at: **http://localhost:3000**

---

*Last Updated: January 2026*

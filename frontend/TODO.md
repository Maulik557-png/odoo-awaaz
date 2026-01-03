# Dayflow HRMS Frontend Implementation TODO

## 1. Project Setup
- [x] Create React app in dayflow directory
- [x] Install dependencies: tailwindcss, postcss, autoprefixer, axios, react-router-dom, react-hot-toast
- [x] Configure Tailwind CSS with dark mode support
- [x] Create mandatory project structure (directories and empty files)

## 2. Core Infrastructure
- [x] Implement centralized API service (services/api.js)
- [x] Create authentication context for token management and role-based access
- [x] Create dark mode context
- [x] Implement ProtectedRoute component

## 3. Authentication Pages
- [x] Login.jsx: Form with validations, API call, redirect based on role
- [x] Register.jsx: Form with validations, API call, redirect to login

## 4. Components
- [x] Navbar.jsx: Project name, logout button
- [x] Sidebar.jsx: Role-based menu items

## 5. Dashboards
- [x] EmployeeDashboard.jsx: Cards for attendance, leave, activity
- [x] AdminDashboard.jsx: Overview cards

## 6. Module Pages
- [x] Attendance.jsx: Check-in/out for employee, view all for admin
- [x] ApplyLeave.jsx: Form to apply leave
- [x] LeaveApprovals.jsx: Admin view and approve/reject leaves
- [x] Profile.jsx: View/edit profile (limited for employee, full for admin)
- [x] Payroll.jsx: View salary (read-only for employee, manage for admin)

## 7. UI Enhancements
- [x] Add dark mode toggle
- [x] Implement loading indicators
- [x] Add toast messages for success/error
- [x] Add confirmation modals for delete actions
- [x] Ensure responsive design

## 8. Routing and App Setup
- [x] Update App.js with routes and context providers
- [x] Update index.js if needed

## 9. Testing and Finalization
- [x] Run the app and verify functionality
- [x] Ensure all validations and API calls are correct
- [x] Final UI/UX polish

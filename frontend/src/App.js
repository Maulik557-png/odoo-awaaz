import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './AuthContext';
import Login from './pages/Login';
import Register from './pages/Register';
import EmployeeDashboard from './pages/EmployeeDashboard';
import AdminDashboard from './pages/AdminDashboard';
import Attendance from './pages/Attendance';
import ApplyLeave from './pages/ApplyLeave';
import LeaveApprovals from './pages/LeaveApprovals';
import Profile from './pages/Profile';
import Payroll from './pages/Payroll';
import ProtectedRoute from './components/ProtectedRoute';
import Navbar from './components/Navbar';
import Sidebar from './components/Sidebar';
import Home from './pages/Home';

function App() {
  return (
    <AuthProvider>
      <Toaster position="top-right" />
      <Router>
      <div className="min-h-screen bg-gray-100 dark:bg-gray-900">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/employee-dashboard" element={
            <ProtectedRoute role="Employee">
              <div className="flex">
                <Sidebar role="Employee" />
                <div className="flex-1">
                  <Navbar />
                  <EmployeeDashboard />
                </div>
              </div>
            </ProtectedRoute>
          } />
          <Route path="/admin-dashboard" element={
            <ProtectedRoute role="Admin">
              <div className="flex">
                <Sidebar role="Admin" />
                <div className="flex-1">
                  <Navbar />
                  <AdminDashboard />
                </div>
              </div>
            </ProtectedRoute>
          } />
          <Route path="/attendance" element={
            <ProtectedRoute>
              <div className="flex">
                <Sidebar />
                <div className="flex-1">
                  <Navbar />
                  <Attendance />
                </div>
              </div>
            </ProtectedRoute>
          } />
          <Route path="/apply-leave" element={
            <ProtectedRoute role="Employee">
              <div className="flex">
                <Sidebar role="Employee" />
                <div className="flex-1">
                  <Navbar />
                  <ApplyLeave />
                </div>
              </div>
            </ProtectedRoute>
          } />
          <Route path="/leave-approvals" element={
            <ProtectedRoute role="Admin">
              <div className="flex">
                <Sidebar role="Admin" />
                <div className="flex-1">
                  <Navbar />
                  <LeaveApprovals />
                </div>
              </div>
            </ProtectedRoute>
          } />
          <Route path="/profile" element={
            <ProtectedRoute>
              <div className="flex">
                <Sidebar />
                <div className="flex-1">
                  <Navbar />
                  <Profile />
                </div>
              </div>
            </ProtectedRoute>
          } />
          <Route path="/payroll" element={
            <ProtectedRoute>
              <div className="flex">
                <Sidebar />
                <div className="flex-1">
                  <Navbar />
                  <Payroll />
                </div>
              </div>
            </ProtectedRoute>
          } />
          <Route path="/" element={<Home />} />
        </Routes>
      </div>
    </Router>
    </AuthProvider>
  );
}

export default App;

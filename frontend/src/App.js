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
import Layout from './components/Layout';
import Home from './pages/Home';

function App() {
  return (
    <AuthProvider>
      <Toaster position="top-right" />
      <Router>
        <div className="min-h-screen bg-gray-100 dark:bg-gray-900">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            {/* Authenticated Routes wrapped in Layout */}
            <Route element={<Layout />}>
              <Route path="/employee-dashboard" element={
                <ProtectedRoute role="Employee">
                  <EmployeeDashboard />
                </ProtectedRoute>
              } />
              <Route path="/admin-dashboard" element={
                <ProtectedRoute role="Admin">
                  <AdminDashboard />
                </ProtectedRoute>
              } />
              <Route path="/attendance" element={
                <ProtectedRoute>
                  <Attendance />
                </ProtectedRoute>
              } />
              <Route path="/apply-leave" element={
                <ProtectedRoute role="Employee">
                  <ApplyLeave />
                </ProtectedRoute>
              } />
              <Route path="/leave-approvals" element={
                <ProtectedRoute role="Admin">
                  <LeaveApprovals />
                </ProtectedRoute>
              } />
              <Route path="/profile" element={
                <ProtectedRoute>
                  <Profile />
                </ProtectedRoute>
              } />
              <Route path="/payroll" element={
                <ProtectedRoute>
                  <Payroll />
                </ProtectedRoute>
              } />
            </Route>
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;

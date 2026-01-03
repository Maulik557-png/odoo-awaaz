import React, { useState, useEffect } from 'react';
import { getEmployees, getAttendance, getLeaves, getPayroll } from '../services/api';
import toast from 'react-hot-toast';

const AdminDashboard = () => {
  const [stats, setStats] = useState({
    totalEmployees: 0,
    totalAttendance: 0,
    pendingLeaves: 0,
    totalPayroll: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [employeesRes, attendanceRes, leavesRes, payrollRes] = await Promise.all([
          getEmployees(),
          getAttendance(),
          getLeaves(),
          getPayroll()
        ]);
        setStats({
          totalEmployees: employeesRes.data.length,
          totalAttendance: attendanceRes.data.length,
          pendingLeaves: leavesRes.data.filter(l => l.status === 'Pending').length,
          totalPayroll: payrollRes.data.reduce((sum, p) => sum + p.salary, 0)
        });
      } catch (error) {
        toast.error('Failed to load dashboard stats');
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6 text-gray-800 dark:text-white">Admin Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold mb-4 text-gray-800 dark:text-white">Total Employees</h2>
          <p className="text-3xl font-bold text-blue-600">{stats.totalEmployees}</p>
        </div>
        <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold mb-4 text-gray-800 dark:text-white">Attendance Records</h2>
          <p className="text-3xl font-bold text-green-600">{stats.totalAttendance}</p>
        </div>
        <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold mb-4 text-gray-800 dark:text-white">Pending Leaves</h2>
          <p className="text-3xl font-bold text-yellow-600">{stats.pendingLeaves}</p>
        </div>
        <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold mb-4 text-gray-800 dark:text-white">Total Payroll</h2>
          <p className="text-3xl font-bold text-purple-600">${stats.totalPayroll}</p>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;

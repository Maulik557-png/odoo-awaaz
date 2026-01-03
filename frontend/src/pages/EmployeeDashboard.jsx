import React, { useState, useEffect } from 'react';
import { getAttendance, getLeaves, getPayroll } from '../services/api';
import toast from 'react-hot-toast';
import { useAuth } from '../AuthContext';

const EmployeeDashboard = () => {
  const [attendance, setAttendance] = useState([]);
  const [leaves, setLeaves] = useState([]);
  const [payroll, setPayroll] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [attendanceRes, leavesRes, payrollRes] = await Promise.all([
          getAttendance(),
          getLeaves(),
          getPayroll()
        ]);
        setAttendance(attendanceRes.data);
        setLeaves(leavesRes.data);
        setPayroll(payrollRes.data);
      } catch (error) {
        toast.error('Failed to load dashboard data');
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  const attendanceSummary = {
    total: attendance.length,
    present: attendance.filter(a => a.status === 'Present').length,
    absent: attendance.filter(a => a.status === 'Absent').length,
  };

  const leaveSummary = {
    total: leaves.length,
    approved: leaves.filter(l => l.status === 'Approved').length,
    pending: leaves.filter(l => l.status === 'Pending').length,
    rejected: leaves.filter(l => l.status === 'Rejected').length,
  };

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6 text-gray-800 dark:text-white">Employee Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
        <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold mb-4 text-gray-800 dark:text-white">Attendance Summary</h2>
          <p className="text-gray-600 dark:text-gray-300">Total Days: {attendanceSummary.total}</p>
          <p className="text-green-600">Present: {attendanceSummary.present}</p>
          <p className="text-red-600">Absent: {attendanceSummary.absent}</p>
        </div>
        <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold mb-4 text-gray-800 dark:text-white">Leave Status</h2>
          <p className="text-gray-600 dark:text-gray-300">Total Leaves: {leaveSummary.total}</p>
          <p className="text-green-600">Approved: {leaveSummary.approved}</p>
          <p className="text-yellow-600">Pending: {leaveSummary.pending}</p>
          <p className="text-red-600">Rejected: {leaveSummary.rejected}</p>
        </div>
        <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
          <h2 className="text-xl font-semibold mb-4 text-gray-800 dark:text-white">Payroll</h2>
          {(() => {
            const entry = Array.isArray(payroll)
              ? payroll.find(p => p.employeeId === user?.employeeId) || payroll[0]
              : payroll;
            return entry ? (
              <div className="text-gray-600 dark:text-gray-300 space-y-1">
                <p><span className="font-medium">Employee:</span> {entry.employeeName}</p>
                <p><span className="font-medium">Net Salary:</span> ${entry.netSalary ?? entry.salary ?? 0}</p>
                <p><span className="font-medium">Pay Date:</span> {entry.payDate || '-'}</p>
              </div>
            ) : (
              <p className="text-gray-600 dark:text-gray-300">No payroll data available</p>
            );
          })()}
        </div>
      </div>
      <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
        <h2 className="text-xl font-semibold mb-4 text-gray-800 dark:text-white">Recent Activity</h2>
        <ul className="space-y-2">
          {attendance.slice(-5).map((record, index) => (
            <li key={index} className="text-gray-600 dark:text-gray-300">
              {record.date}: {record.status}
            </li>
          ))}
          {leaves.slice(-5).map((leave, index) => (
            <li key={index} className="text-gray-600 dark:text-gray-300">
              Leave request: {leave.startDate} - {leave.endDate} ({leave.status})
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default EmployeeDashboard;

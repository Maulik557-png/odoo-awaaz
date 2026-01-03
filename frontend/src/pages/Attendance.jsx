import React, { useState, useEffect } from 'react';
import { getAttendance, postAttendance } from '../services/api';
import { useAuth } from '../AuthContext';
import toast from 'react-hot-toast';

const Attendance = () => {
  const [attendance, setAttendance] = useState([]);
  const [loading, setLoading] = useState(true);
  const [checkInLoading, setCheckInLoading] = useState(false);
  const { user } = useAuth();

  useEffect(() => {
    fetchAttendance();
  }, []);

  const fetchAttendance = async () => {
    try {
      const response = await getAttendance();
      let data = response.data;
      if (user.role === 'Employee') {
        data = data.filter(a => a.employeeId === user.employeeId);
      }
      setAttendance(data);
    } catch (error) {
      toast.error('Failed to load attendance records');
    } finally {
      setLoading(false);
    }
  };

  const handleCheckInOut = async () => {
    setCheckInLoading(true);
    try {
      const today = new Date().toISOString().split('T')[0];
      const existingRecord = attendance.find(a => a.date === today && a.employeeId === user.employeeId);

      if (existingRecord && existingRecord.checkOutTime) {
        toast.error('Already checked out for today');
        return;
      }

      const data = {
        employeeId: user.employeeId,
        date: today,
        checkInTime: existingRecord ? null : new Date().toTimeString().split(' ')[0],
        checkOutTime: existingRecord ? new Date().toTimeString().split(' ')[0] : null,
        status: 'Present'
      };

      await postAttendance(data);
      toast.success(existingRecord ? 'Checked out successfully' : 'Checked in successfully');
      fetchAttendance();
    } catch (error) {
      toast.error('Failed to update attendance');
    } finally {
      setCheckInLoading(false);
    }
  };

  const calculateWorkHours = (inTime, outTime) => {
    if (!inTime || !outTime) return '-';
    const [h1, m1] = inTime.split(':').map(Number);
    const [h2, m2] = outTime.split(':').map(Number);
    let diff = (h2 * 60 + m2) - (h1 * 60 + m1);
    const hours = Math.floor(diff / 60);
    const mins = diff % 60;
    return `${hours}h ${mins}m`;
  };

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  const today = new Date().toISOString().split('T')[0];
  const todayRecord = attendance.find(a => a.date === today && a.employeeId === user.employeeId);

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6 text-gray-800 dark:text-white">Attendance</h1>
      <h1 className="text-3xl font-bold mb-6 text-gray-800 dark:text-white">Attendance</h1>
      {/* Check In/Out moved to TopBar */}
      <div className="bg-white dark:bg-gray-800 rounded-lg shadow-md overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-50 dark:bg-gray-700">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">Date</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">Employee</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">Check In</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">Check Out</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">Work Hours</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-300 uppercase tracking-wider">Status</th>
            </tr>
          </thead>
          <tbody className="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
            {attendance.map((record, index) => (
              <tr key={index}>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">{record.date}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">{record.employeeName}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">{record.checkInTime || '-'}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">{record.checkOutTime || '-'}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">{calculateWorkHours(record.checkInTime, record.checkOutTime)}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-white">{record.status}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Attendance;

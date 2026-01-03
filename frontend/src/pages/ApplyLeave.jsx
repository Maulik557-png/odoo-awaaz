import React, { useState } from 'react';
import toast from 'react-hot-toast';
import { postLeave } from '../services/api';
import { useAuth } from '../AuthContext';

const ApplyLeave = () => {
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    leaveType: 'Paid',
    startDate: '',
    endDate: '',
    remarks: ''
  });
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.startDate || !formData.endDate) {
      toast.error('Please select start and end dates');
      return;
    }
    setLoading(true);
    try {
      await postLeave({
        employeeId: user?.employeeId || 'EMP001',
        type: formData.leaveType,
        startDate: formData.startDate,
        endDate: formData.endDate,
        remarks: formData.remarks,
      });
      toast.success('Leave application submitted successfully');
      setFormData({ leaveType: 'Paid', startDate: '', endDate: '', remarks: '' });
    } catch (error) {
      toast.error('Failed to apply for leave');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6 bg-gray-100 dark:bg-gray-900 min-h-screen">
      <h1 className="text-2xl font-bold mb-6 text-gray-900 dark:text-white">Apply for Leave</h1>
      <form onSubmit={handleSubmit} className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md max-w-md">
        <div className="mb-4">
          <label className="block text-gray-700 dark:text-gray-300 mb-2">Leave Type</label>
          <select
            name="leaveType"
            value={formData.leaveType}
            onChange={handleChange}
            className="w-full p-2 border border-gray-300 dark:border-gray-600 rounded dark:bg-gray-700 dark:text-white"
          >
            <option value="Paid">Paid Leave</option>
            <option value="Sick">Sick Leave</option>
            <option value="Unpaid">Unpaid Leave</option>
          </select>
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 dark:text-gray-300 mb-2">Start Date</label>
          <input
            type="date"
            name="startDate"
            value={formData.startDate}
            onChange={handleChange}
            className="w-full p-2 border border-gray-300 dark:border-gray-600 rounded dark:bg-gray-700 dark:text-white"
            required
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 dark:text-gray-300 mb-2">End Date</label>
          <input
            type="date"
            name="endDate"
            value={formData.endDate}
            onChange={handleChange}
            className="w-full p-2 border border-gray-300 dark:border-gray-600 rounded dark:bg-gray-700 dark:text-white"
            required
          />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 dark:text-gray-300 mb-2">Remarks</label>
          <textarea
            name="remarks"
            value={formData.remarks}
            onChange={handleChange}
            className="w-full p-2 border border-gray-300 dark:border-gray-600 rounded dark:bg-gray-700 dark:text-white"
            rows="3"
          />
        </div>
        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-500 text-white p-2 rounded hover:bg-blue-600 disabled:opacity-50"
        >
          {loading ? 'Submitting...' : 'Apply Leave'}
        </button>
      </form>
    </div>
  );
};

export default ApplyLeave;

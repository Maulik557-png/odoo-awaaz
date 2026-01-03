import React, { useState, useEffect } from 'react';
import { getEmployees } from '../services/api';
import { useAuth } from '../AuthContext';
import toast from 'react-hot-toast';

const Profile = () => {
  const [profile, setProfile] = useState(null);
  const [editing, setEditing] = useState(false);
  const [formData, setFormData] = useState({});
  const { user } = useAuth();

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await getEmployees();
        const userProfile = response.data.find(emp => emp.id === user.employeeId);
        if (userProfile) {
          setProfile(userProfile);
          setFormData(userProfile);
        }
      } catch (error) {
        toast.error('Failed to load profile');
      }
    };
    fetchProfile();
  }, [user.employeeId]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSave = () => {
    // In a real app, you'd call an update API here
    setProfile(formData);
    setEditing(false);
    toast.success('Profile updated successfully');
  };

  if (!profile) {
    return <div className="flex justify-center items-center h-screen">Loading...</div>;
  }

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6 text-gray-800 dark:text-white">Profile</h1>
      <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md max-w-2xl">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-xl font-semibold text-gray-800 dark:text-white">Personal Information</h2>
          {user.role === 'Admin' || editing ? (
            <button
              onClick={editing ? handleSave : () => setEditing(true)}
              className="bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded-md"
            >
              {editing ? 'Save' : 'Edit'}
            </button>
          ) : null}
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-gray-700 dark:text-gray-300 mb-2">Employee ID</label>
            <input
              type="text"
              value={formData.employeeId || ''}
              disabled
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md bg-gray-100 dark:bg-gray-700 dark:text-white"
            />
          </div>
          <div>
            <label className="block text-gray-700 dark:text-gray-300 mb-2">Full Name</label>
            <input
              type="text"
              name="fullName"
              value={formData.fullName || ''}
              onChange={handleChange}
              disabled={!editing}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-white disabled:bg-gray-100 dark:disabled:bg-gray-700"
            />
          </div>
          <div>
            <label className="block text-gray-700 dark:text-gray-300 mb-2">Email</label>
            <input
              type="email"
              name="email"
              value={formData.email || ''}
              onChange={handleChange}
              disabled={!editing}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-white disabled:bg-gray-100 dark:disabled:bg-gray-700"
            />
          </div>
          <div>
            <label className="block text-gray-700 dark:text-gray-300 mb-2">Phone</label>
            <input
              type="tel"
              name="phone"
              value={formData.phone || ''}
              onChange={handleChange}
              disabled={!editing}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-white disabled:bg-gray-100 dark:disabled:bg-gray-700"
            />
          </div>
          <div>
            <label className="block text-gray-700 dark:text-gray-300 mb-2">Address</label>
            <input
              type="text"
              name="address"
              value={formData.address || ''}
              onChange={handleChange}
              disabled={!editing}
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-white disabled:bg-gray-100 dark:disabled:bg-gray-700"
            />
          </div>
          <div>
            <label className="block text-gray-700 dark:text-gray-300 mb-2">Department</label>
            <input
              type="text"
              value={formData.department || ''}
              disabled
              className="w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-md bg-gray-100 dark:bg-gray-700 dark:text-white"
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;

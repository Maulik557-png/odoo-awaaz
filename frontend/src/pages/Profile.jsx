import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { getEmployee } from '../services/api'; // Ensure this is implemented
import { useAuth } from '../AuthContext';
import toast from 'react-hot-toast';

const Profile = () => {
  const { id } = useParams();
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        // If id is present in URL, fetch that user (verified by backend/protected route if needed)
        // Otherwise fetch current logged in user (user.employeeId)
        // Access control: Ensure backend or UI blocks non-admins from viewing others if strict. 
        // Here assuming Admin can view any ID, Employee views self.

        let targetId = id;
        if (!targetId) {
          // My Profile
          targetId = user.employeeId || user.id; // user object structure depends on auth response
          // If user.employeeId, it might be string/long.
        }

        if (!targetId) {
          // Fallback or error
          console.error("No user ID found to fetch profile");
          return;
        }

        const data = await getEmployee(targetId);
        setProfile(data);
      } catch (error) {
        toast.error('Failed to load profile');
        console.error(error);
      } finally {
        setLoading(false);
      }
    };
    fetchProfile();
  }, [id, user]);

  if (loading) return <div className="flex justify-center p-10"><div className="animate-spin rounded-full h-8 w-8 border-b-2 border-brand-primary"></div></div>;
  if (!profile) return <div className="p-10 text-center">Profile not found.</div>;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700 p-6">
        <div className="flex flex-col md:flex-row gap-6 items-center md:items-start">
          <div className="h-24 w-24 rounded-full bg-gradient-to-br from-brand-primary to-brand-secondary text-white grid place-items-center text-3xl font-bold shadow-lg">
            {profile.firstName?.[0]}{profile.lastName?.[0]}
          </div>
          <div className="flex-1 text-center md:text-left">
            <h1 className="text-2xl font-bold text-slate-900 dark:text-white">{profile.firstName} {profile.lastName}</h1>
            <p className="text-slate-500 dark:text-slate-400 font-medium">{profile.designation}</p>
            <div className="flex items-center justify-center md:justify-start gap-4 mt-3">
              <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-300">
                {profile.department}
              </span>
              <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${profile.status === 'ACTIVE' ? 'bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-300' : 'bg-gray-100 text-gray-800'
                }`}>
                {profile.status}
              </span>
            </div>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left Column: Info & Skills */}
        <div className="space-y-6 lg:col-span-2">

          {/* About Section */}
          <div className="bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700 p-6">
            <h2 className="text-lg font-semibold text-slate-900 dark:text-white mb-4">About</h2>
            <p className="text-slate-600 dark:text-slate-300 leading-relaxed">
              {profile.about || "No detailed information available."}
            </p>
          </div>

          {/* Personal Details */}
          <div className="bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700 p-6">
            <h2 className="text-lg font-semibold text-slate-900 dark:text-white mb-4">Personal Information</h2>
            <dl className="grid grid-cols-1 sm:grid-cols-2 gap-x-4 gap-y-6">
              <div>
                <dt className="text-sm font-medium text-slate-500 dark:text-slate-400">Email Address</dt>
                <dd className="mt-1 text-sm text-slate-900 dark:text-white">{profile.email}</dd>
              </div>
              <div>
                <dt className="text-sm font-medium text-slate-500 dark:text-slate-400">Phone Number</dt>
                <dd className="mt-1 text-sm text-slate-900 dark:text-white">{profile.phone || 'N/A'}</dd>
              </div>
              <div>
                <dt className="text-sm font-medium text-slate-500 dark:text-slate-400">Date of Joining</dt>
                <dd className="mt-1 text-sm text-slate-900 dark:text-white">{profile.dateOfJoining}</dd>
              </div>
              <div>
                <dt className="text-sm font-medium text-slate-500 dark:text-slate-400">Employee ID</dt>
                <dd className="mt-1 text-sm text-slate-900 dark:text-white">{profile.id}</dd>
              </div>
            </dl>
          </div>

          {/* Skills */}
          <div className="bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700 p-6">
            <h2 className="text-lg font-semibold text-slate-900 dark:text-white mb-4">Skills</h2>
            {profile.skills && profile.skills.length > 0 ? (
              <div className="flex flex-wrap gap-2">
                {profile.skills.map((skill, index) => (
                  <span key={index} className="inline-flex items-center px-3 py-1 rounded-lg text-sm font-medium bg-slate-100 text-slate-700 dark:bg-slate-700 dark:text-slate-300 border border-slate-200 dark:border-slate-600">
                    {skill.skillName} <span className="ml-2 text-xs opacity-60">({skill.proficiency})</span>
                  </span>
                ))}
              </div>
            ) : (
              <p className="text-sm text-slate-500">No skills listed.</p>
            )}
          </div>
        </div>

        {/* Right Column: Salary & Actions */}
        <div className="space-y-6">
          {/* Salary Card (Visible to Self or Admin) */}
          {/* Check role permissions strictly in real app. Here we assume profile.currentSalary is only populated if allowed. */}
          {(user.role === 'Admin' || user.employeeId === profile.id || String(user.employeeId) === String(profile.id)) && (
            <div className="bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700 p-6">
              <h2 className="text-lg font-semibold text-slate-900 dark:text-white mb-4">Current Salary</h2>
              {profile.currentSalary ? (
                <div>
                  <div className="text-3xl font-bold text-slate-900 dark:text-white">
                    {profile.currentSalary.amount?.toLocaleString()} <span className="text-lg font-normal text-slate-500">{profile.currentSalary.currency}</span>
                  </div>
                  <p className="text-sm text-slate-500 mt-1">Effective since {profile.currentSalary.effectiveDate}</p>
                </div>
              ) : (
                <p className="text-sm text-slate-500">Salary details not available.</p>
              )}
            </div>
          )}

          {/* Attendance Summary (Placeholder for now, could link to attendance page) */}
          <div className="bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700 p-6">
            <h2 className="text-lg font-semibold text-slate-900 dark:text-white mb-4">Attendance</h2>
            <p className="text-sm text-slate-500 mb-4">Check detailed attendance history and logs.</p>
            {/* Link to attendance filtered by this user could go here */}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;

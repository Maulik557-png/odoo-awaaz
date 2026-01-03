import React, { useState, useEffect } from 'react';
import { getEmployees, getAttendance, getLeaves } from '../services/api';
import { Link } from 'react-router-dom';

const AdminDashboard = () => {
  const [employees, setEmployees] = useState([]);
  const [stats, setStats] = useState({
    totalEmployees: 0,
    activeEmployees: 0,
    onLeaveToday: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [employeesRes, leavesRes] = await Promise.all([
          getEmployees(),
          getLeaves()
        ]);

        const empList = employeesRes.data || [];
        setEmployees(empList);

        // Simple stats calculation
        // Assuming leavesRes returns active leaves. Ideally backend should provide stats.
        const today = new Date().toISOString().split('T')[0];
        const onLeave = leavesRes.data ? leavesRes.data.filter(l => l.startDate <= today && l.endDate >= today && l.status === 'APPROVED').length : 0;

        setStats({
          totalEmployees: empList.length,
          activeEmployees: empList.filter(e => e.status === 'ACTIVE').length,
          onLeaveToday: onLeave
        });

      } catch (error) {
        console.error("Error fetching admin dashboard data:", error);
        toast.error('Failed to load dashboard data');
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-brand-primary"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-900 dark:text-white">Company Overview</h1>
        <div className="text-sm text-slate-500">
          {new Date().toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white dark:bg-slate-800 p-6 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700">
          <p className="text-sm font-medium text-slate-500 dark:text-slate-400">Total Employees</p>
          <p className="text-3xl font-bold text-slate-900 dark:text-white mt-2">{stats.totalEmployees}</p>
        </div>
        <div className="bg-white dark:bg-slate-800 p-6 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700">
          <p className="text-sm font-medium text-slate-500 dark:text-slate-400">Active Now</p>
          <p className="text-3xl font-bold text-emerald-600 mt-2">{stats.activeEmployees}</p>
        </div>
        <div className="bg-white dark:bg-slate-800 p-6 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700">
          <p className="text-sm font-medium text-slate-500 dark:text-slate-400">On Leave Today</p>
          <p className="text-3xl font-bold text-amber-500 mt-2">{stats.onLeaveToday}</p>
        </div>
      </div>

      {/* Employee Cards Grid */}
      <div>
        <h2 className="text-lg font-semibold text-slate-900 dark:text-white mb-4">Employees</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {employees.map((emp) => (
            <Link to={`/profile/${emp.id}`} key={emp.id} className="group relative bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700 p-6 hover:shadow-md transition-shadow">
              <div className="flex items-start justify-between">
                <div className="flex-1">
                  {/* Avatar Placeholder */}
                  <div className="h-12 w-12 rounded-full bg-gradient-to-br from-brand-primary/20 to-brand-secondary/20 text-brand-primary grid place-items-center font-bold text-lg mb-4">
                    {emp.firstName?.charAt(0)}{emp.lastName?.charAt(0)}
                  </div>
                  <h3 className="font-semibold text-slate-900 dark:text-white truncate" title={`${emp.firstName} ${emp.lastName}`}>
                    {emp.firstName} {emp.lastName}
                  </h3>
                  <p className="text-sm text-slate-500 dark:text-slate-400 truncate">{emp.designation || 'No Designation'}</p>

                  <div className="mt-4 space-y-2">
                    <div className="flex items-center text-xs text-slate-500">
                      <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                      </svg>
                      {emp.department || 'No Dept'}
                    </div>
                    <div className="flex items-center text-xs text-slate-500">
                      <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                      </svg>
                      <span className="truncate">{emp.email}</span>
                    </div>
                  </div>
                </div>
                <span className={`inline-flex items-center px-2 py-1 rounded-full text-xs font-medium ${emp.status === 'ACTIVE' ? 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400' :
                  'bg-gray-100 text-gray-700 dark:bg-gray-700 dark:text-gray-300'
                  }`}>
                  {emp.status}
                </span>
              </div>
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;

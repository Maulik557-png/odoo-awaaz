import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../AuthContext';

const Sidebar = ({ role }) => {
  const { user } = useAuth();
  const location = useLocation();

  const employeeMenu = [
    { path: '/employee-dashboard', label: 'Dashboard', icon: 'M3 12h18M12 3v18' },
    { path: '/profile', label: 'My Profile', icon: 'M12 14a5 5 0 100-10 5 5 0 000 10zm7 7a7 7 0 10-14 0' },
    { path: '/attendance', label: 'Attendance', icon: 'M8 7h8M8 11h8M8 15h6M5 4h14v16H5z' },
    { path: '/apply-leave', label: 'Apply Leave', icon: 'M5 12l4 4L19 6' },
    { path: '/payroll', label: 'Payroll', icon: 'M12 8c-2.5 0-3.5 3 0 3s2.5 3 0 3m0-9v12' },
  ];

  const adminMenu = [
    { path: '/admin-dashboard', label: 'Dashboard', icon: 'M3 12h18M12 3v18' },
    { path: '/profile', label: 'Employee Management', icon: 'M4 6h16v12H4z M8 10h8' },
    { path: '/attendance', label: 'Attendance Records', icon: 'M8 7h8M8 11h8M8 15h6M5 4h14v16H5z' },
    { path: '/leave-approvals', label: 'Leave Approvals', icon: 'M5 12l4 4L19 6' },
    { path: '/payroll', label: 'Payroll Management', icon: 'M12 8c-2.5 0-3.5 3 0 3s2.5 3 0 3m0-9v12' },
  ];

  const menu = role === 'Admin' ? adminMenu : employeeMenu;

  return (
    <aside className="w-64 shrink-0 h-screen sticky top-0 border-r border-slate-200/60 dark:border-slate-800 bg-white/60 dark:bg-slate-900/50 backdrop-blur px-3 py-4">
      <ul className="space-y-1">
        {menu.map((item) => {
          const active = location.pathname === item.path;
          return (
            <li key={item.path}>
              <Link
                to={item.path}
                className={`group flex items-center gap-3 px-3 py-2 rounded-lg border transition-colors ${
                  active
                    ? 'bg-gradient-to-r from-brand-primary/90 to-brand-secondary/90 text-white border-transparent shadow'
                    : 'text-slate-700 dark:text-slate-300 border-slate-200/60 dark:border-slate-800 hover:bg-slate-50 dark:hover:bg-slate-800/50'
                }`}
              >
                <span className={`grid place-items-center h-8 w-8 rounded-md ${active ? 'bg-white/20' : 'bg-slate-100 dark:bg-slate-800'}`}>
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" className={`h-4 w-4 ${active ? 'text-white' : 'text-slate-600 dark:text-slate-300'}`} fill="currentColor">
                    <path d={item.icon} />
                  </svg>
                </span>
                <span className="font-medium text-sm">{item.label}</span>
              </Link>
            </li>
          );
        })}
      </ul>
      <div className="mt-6 p-3 rounded-xl border border-slate-200/60 dark:border-slate-800 bg-gradient-to-br from-brand-primary/10 to-brand-secondary/10">
        <p className="text-xs text-slate-600 dark:text-slate-300">Signed in as</p>
        <p className="text-sm font-semibold text-slate-800 dark:text-white">{user?.role}</p>
      </div>
    </aside>
  );
};

export default Sidebar;

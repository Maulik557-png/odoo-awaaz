import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';

const ProtectedRoute = ({ children, role }) => {
  const { user, loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen grid place-items-center bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-950">
        <div className="flex flex-col items-center gap-4">
          <span className="relative inline-flex h-12 w-12">
            <span className="absolute inline-flex h-full w-full rounded-full bg-gradient-to-r from-brand-primary to-brand-secondary opacity-75 animate-ping"></span>
            <span className="relative inline-flex rounded-full h-12 w-12 bg-gradient-to-r from-brand-primary to-brand-secondary"></span>
          </span>
          <p className="text-slate-600 dark:text-slate-300 font-medium">Preparing your workspace...</p>
        </div>
      </div>
    );
  }

  if (!user) {
    return <Navigate to="/login" />;
  }

  if (role && user.role !== role) {
    return <Navigate to={user.role === 'Employee' ? '/employee-dashboard' : '/admin-dashboard'} />;
  }

  return children;
};

export default ProtectedRoute;

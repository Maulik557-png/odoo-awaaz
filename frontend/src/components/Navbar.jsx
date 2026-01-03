import React from 'react';
import { useAuth } from '../AuthContext';
import { Link, useNavigate } from 'react-router-dom';

const Navbar = () => {
  const { user, logout, darkMode, toggleDarkMode } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="sticky top-0 z-40 backdrop-blur bg-white/70 dark:bg-slate-900/60 border-b border-slate-200/60 dark:border-slate-800 px-4 md:px-6 py-3 flex justify-between items-center">
      <div className="flex items-center gap-3">
        <div className="h-9 w-9 rounded-xl bg-gradient-to-br from-brand-primary to-brand-secondary grid place-items-center shadow-card">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M6 12a6 6 0 1 1 12 0v3a2 2 0 0 1-2 2h-1a2 2 0 0 1-2-2v-3a2 2 0 1 0-4 0v3a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2v-3z" fill="#fff"/>
            <circle cx="12" cy="7" r="2" fill="#fff"/>
          </svg>
        </div>
        <div>
          <h1 className="text-lg md:text-xl font-bold tracking-tight text-slate-900 dark:text-white">Dayflow</h1>
          <p className="text-xs text-slate-500 dark:text-slate-400 hidden sm:block">Modern HRMS dashboard</p>
        </div>
      </div>
      <div className="flex items-center gap-2 md:gap-3">
        <button
          onClick={toggleDarkMode}
          aria-label="Toggle dark mode"
          className="inline-flex items-center gap-2 px-3 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white/60 dark:bg-slate-800/60 hover:bg-white dark:hover:bg-slate-800 transition-colors"
        >
          {darkMode ? (
            <span className="inline-flex items-center gap-2 text-amber-400">
              <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" viewBox="0 0 20 20" fill="currentColor"><path d="M10 15a5 5 0 110-10 5 5 0 010 10z"/><path fillRule="evenodd" d="M10 1a1 1 0 011 1v1a1 1 0 11-2 0V2a1 1 0 011-1zm0 14a1 1 0 011 1v1a1 1 0 11-2 0v-1a1 1 0 011-1zM3.22 4.22a1 1 0 011.42 0l.7.7A1 1 0 014.3 6.7l-.7-.7a1 1 0 010-1.42zM14.66 15.34a1 1 0 011.41 0l.7.7a1 1 0 11-1.41 1.41l-.7-.7a1 1 0 010-1.41zM1 10a1 1 0 011-1h1a1 1 0 110 2H2a1 1 0 01-1-1zm14 0a1 1 0 011-1h1a1 1 0 110 2h-1a1 1 0 01-1-1zM3.22 15.78a1 1 0 010-1.42l.7-.7a1 1 0 111.41 1.41l-.7.7a1 1 0 01-1.41 0zM14.66 4.66a1 1 0 010-1.41l.7-.7a1 1 0 111.41 1.41l-.7.7a1 1 0 01-1.41 0z" clipRule="evenodd"/></svg>
              Light
            </span>
          ) : (
            <span className="inline-flex items-center gap-2 text-slate-600 dark:text-slate-300">
              <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" viewBox="0 0 24 24" fill="currentColor"><path d="M21 12.79A9 9 0 1111.21 3 7 7 0 0021 12.79z"/></svg>
              Dark
            </span>
          )}
        </button>
        {user ? (
          <>
            <span className="hidden sm:inline-flex items-center px-3 py-1 rounded-full text-sm bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 border border-slate-200 dark:border-slate-700">{user?.role}</span>
            <button
              onClick={handleLogout}
              className="inline-flex items-center gap-2 bg-gradient-to-r from-brand-primary to-brand-secondary hover:opacity-90 text-white px-4 py-2 rounded-lg shadow"
            >
              <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" viewBox="0 0 24 24" fill="currentColor"><path d="M16 17l5-5-5-5v3H9v4h7v3z"/><path d="M4 5h7V3H4a2 2 0 00-2 2v14a2 2 0 002 2h7v-2H4V5z"/></svg>
              Logout
            </button>
          </>
        ) : (
          <div className="hidden sm:flex items-center gap-2">
            <Link to="/login" className="inline-flex items-center gap-2 px-4 py-2 rounded-lg border border-slate-200 dark:border-slate-700 bg-white/60 dark:bg-slate-800/60 hover:bg-white dark:hover:bg-slate-800">Login</Link>
            <Link to="/register" className="inline-flex items-center gap-2 bg-gradient-to-r from-brand-primary to-brand-secondary hover:opacity-90 text-white px-4 py-2 rounded-lg shadow">Sign up</Link>
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;

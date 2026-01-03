import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [darkMode, setDarkMode] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    const employeeId = localStorage.getItem('employeeId');
    const fullName = localStorage.getItem('fullName');
    if (token && role) {
      setUser({ token, role, employeeId, fullName });
    }
    const savedDarkMode = localStorage.getItem('darkMode') === 'true';
    setDarkMode(savedDarkMode);
    if (savedDarkMode) {
      document.documentElement.classList.add('dark');
    }
    setLoading(false);
  }, []);

  const login = (token, role, employeeId, fullName) => {
    localStorage.setItem('token', token);
    localStorage.setItem('role', role);
    if (employeeId) localStorage.setItem('employeeId', employeeId);
    if (fullName) localStorage.setItem('fullName', fullName);
    setUser({ token, role, employeeId, fullName });
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('employeeId');
    localStorage.removeItem('fullName');
    setUser(null);
  };

  const toggleDarkMode = () => {
    const newDarkMode = !darkMode;
    setDarkMode(newDarkMode);
    localStorage.setItem('darkMode', newDarkMode);
    if (newDarkMode) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, loading, darkMode, toggleDarkMode }}>
      {children}
    </AuthContext.Provider>
  );
};

import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL ? `${process.env.REACT_APP_API_URL}/api` : 'http://localhost:8080/api';
// Toggle mocks via env var REACT_APP_USE_MOCKS=true|false. Default to false (use real backend).
const USE_MOCKS = process.env.REACT_APP_USE_MOCKS === 'true';

const api = axios.create({
  baseURL: API_BASE_URL,
});

// Add request interceptor to include auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// ---------------- Mock Layer ----------------
// Minimal axios-like response helpers
const ok = (data) => Promise.resolve({ data });

// Seed demo data
let mockEmployees = [
  { id: 'EMP001', employeeId: 'EMP001', fullName: 'John Doe', email: 'employee@dayflow.com', phone: '+91 98765 43210', address: 'Bengaluru, IN', department: 'Engineering' },
  { id: 'ADM001', employeeId: 'ADM001', fullName: 'Alice Admin', email: 'admin@dayflow.com', phone: '+91 91234 56789', address: 'Mumbai, IN', department: 'Operations' },
];

let mockAttendance = [
  { date: '2026-01-02', employeeId: 'EMP001', employeeName: 'John Doe', checkInTime: '09:12:10', checkOutTime: '17:43:03', status: 'Present' },
  { date: '2026-01-01', employeeId: 'EMP001', employeeName: 'John Doe', checkInTime: '09:18:22', checkOutTime: '17:10:45', status: 'Present' },
  { date: '2025-12-31', employeeId: 'EMP001', employeeName: 'John Doe', checkInTime: null, checkOutTime: null, status: 'Absent' },
  { date: '2026-01-02', employeeId: 'ADM001', employeeName: 'Alice Admin', checkInTime: '08:55:00', checkOutTime: '16:30:00', status: 'Present' },
];

let mockLeaves = [
  { id: 1, employeeId: 'EMP001', employeeName: 'John Doe', startDate: '2026-01-10', endDate: '2026-01-12', type: 'Paid', remarks: 'Family function', status: 'Pending' },
  { id: 2, employeeId: 'EMP001', employeeName: 'John Doe', startDate: '2025-12-20', endDate: '2025-12-22', type: 'Sick', remarks: 'Flu', status: 'Approved' },
];

let mockPayroll = [
  { employeeId: 'EMP001', employeeName: 'John Doe', basicSalary: 52000, allowances: 6000, deductions: 1600, netSalary: 56400, payDate: '2025-12-31' },
  { employeeId: 'ADM001', employeeName: 'Alice Admin', basicSalary: 82000, allowances: 12000, deductions: 4200, netSalary: 89800, payDate: '2025-12-31' },
];

// Mock implementations
const mockApi = {
  // Auth endpoints still return success shapes if needed later
  login: (email, password) => ok({ token: 'mock-token', role: email.includes('admin') ? 'Admin' : 'Employee' }),
  register: (employeeId, fullName, email, password, role) => ok({ id: employeeId, fullName, email, role }),

  // Employees
  getEmployees: () => ok(mockEmployees),

  // Attendance
  getAttendance: () => ok(mockAttendance),
  postAttendance: (data) => {
    const name = mockEmployees.find(e => e.employeeId === data.employeeId)?.fullName || 'Unknown';
    mockAttendance = [
      ...mockAttendance.filter(a => !(a.date === data.date && a.employeeId === data.employeeId)),
      { employeeName: name, status: 'Present', ...data },
    ];
    return ok({ success: true });
  },

  // Leaves
  getLeaves: () => ok(mockLeaves),
  postLeave: (data) => {
    const id = mockLeaves.length ? Math.max(...mockLeaves.map(l => l.id)) + 1 : 1;
    const emp = mockEmployees.find(e => e.employeeId === data.employeeId) || {};
    mockLeaves = [...mockLeaves, { id, employeeId: data.employeeId || 'EMP001', employeeName: emp.fullName || 'John Doe', type: data.leaveType || data.type || 'Paid', remarks: data.remarks || '', startDate: data.startDate, endDate: data.endDate, status: 'Pending' }];
    return ok({ id });
  },
  putLeave: (id, data) => {
    mockLeaves = mockLeaves.map(l => (l.id === id ? { ...l, ...data } : l));
    return ok({ success: true });
  },
  deleteLeave: (id) => {
    mockLeaves = mockLeaves.filter(l => l.id !== id);
    return ok({ success: true });
  },

  // Payroll
  getPayroll: () => ok(mockPayroll),
};

// --------------- Exported API ---------------
// Auth APIs
export const login = (identifier, password) => {
  if (USE_MOCKS) return mockApi.login(identifier, password).then(r => r.data);
  // backend expects { loginId, password } — accept either email or loginId from UI
  return api.post('/auth/login', { loginId: identifier, password }).then(r => r.data);
};

export const signupAdmin = (payload) => {
  if (USE_MOCKS) return mockApi.register(payload.employeeId, payload.fullName, payload.email, payload.password, payload.role).then(r => r.data);
  return api.post('/auth/signup/admin', payload).then(r => r.data);
};

// Employee APIs
export const getEmployees = () => USE_MOCKS ? mockApi.getEmployees().then(r => r.data) : api.get('/employee').then(r => r.data);
export const getEmployee = (id) => USE_MOCKS ? Promise.resolve(mockEmployees.find(e => e.id === id || e.employeeId === id) || mockEmployees[0]) : api.get(`/employee/${id}`).then(r => r.data);

// Attendance APIs
export const getAttendance = () => USE_MOCKS ? mockApi.getAttendance().then(r => r.data) : api.get('/attendance').then(r => r.data);
export const postAttendance = (data) => USE_MOCKS ? mockApi.postAttendance(data).then(r => r.data) : api.post('/attendance', data).then(r => r.data);

// Leave APIs
export const getLeaves = () => USE_MOCKS ? mockApi.getLeaves().then(r => r.data) : api.get('/leaves').then(r => r.data);
export const postLeave = (data) => USE_MOCKS ? mockApi.postLeave(data).then(r => r.data) : api.post('/leaves', data).then(r => r.data);
export const putLeave = (id, data) => USE_MOCKS ? mockApi.putLeave(id, data).then(r => r.data) : api.put(`/leaves/${id}`, data).then(r => r.data);
export const deleteLeave = (id) => USE_MOCKS ? mockApi.deleteLeave(id).then(r => r.data) : api.delete(`/leaves/${id}`).then(r => r.data);

// Payroll APIs
export const getPayroll = () => USE_MOCKS ? mockApi.getPayroll().then(r => r.data) : api.get('/payroll').then(r => r.data);

// At the end of the file, add a backward-compatible alias for register
export const register = (employeeId, fullName, email, password, role) => {
  // For the backend we expect a signup payload; reuse signupAdmin which accepts a payload object
  const payload = { companyName: null, name: fullName, phone: null, email, password, confirmPassword: password };
  // If this app expects employee-level registration, signupAdmin is the closest available endpoint.
  // For compatibility with existing frontend usage, call signupAdmin with the constructed payload.
  return signupAdmin(payload);
};

export default api;
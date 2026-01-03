import React, { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import ProfileDropdown from './ProfileDropdown';
import { getAttendance, postAttendance } from '../services/api';
import toast from 'react-hot-toast';

const TopBar = () => {
    const { user } = useAuth();
    const location = useLocation();
    const [isCheckedIn, setIsCheckedIn] = useState(false);
    const [loading, setLoading] = useState(false);

    // Check status on mount
    React.useEffect(() => {
        if (user && user.role === 'Employee') {
            checkStatus();
        }
    }, [user]);

    const checkStatus = async () => {
        try {
            const today = new Date().toISOString().split('T')[0];
            const res = await getAttendance();
            const record = res.data.find(a => a.date === today && a.employeeId === user.employeeId);
            if (record && !record.checkOutTime) {
                setIsCheckedIn(true);
            } else {
                setIsCheckedIn(false);
            }
        } catch (e) {
            console.error(e);
        }
    };

    const handleCheckInToggle = async () => {
        setLoading(true);
        try {
            const today = new Date().toISOString().split('T')[0];
            const res = await getAttendance();
            const existingRecord = res.data.find(a => a.date === today && a.employeeId === user.employeeId);

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
            setIsCheckedIn(!existingRecord); // If existing record (checked in), we are checking out (false). Wait behavior logic:
            // If no record -> Check In -> isCheckedIn = true
            // If record (no checkout) -> Check Out -> isCheckedIn = false (Red)

            toast.success(existingRecord ? 'Checked out' : 'Checked in');
            checkStatus(); // Refresh status
        } catch (error) {
            toast.error('Failed to update status');
        } finally {
            setLoading(false);
        }
    };

    const employeeLinks = [
        { path: '/employee-dashboard', label: 'Dashboard' },
        { path: '/attendance', label: 'Attendance' },
        { path: '/apply-leave', label: 'Time Off' },
    ];

    const adminLinks = [
        { path: '/admin-dashboard', label: 'Dashboard' },
        { path: '/profile', label: 'Employees' }, // Repurposed for employee list as per request
        { path: '/leave-approvals', label: 'Leaves' },
        { path: '/attendance', label: 'Attendance' },
    ];

    const links = user?.role === 'ADMIN' ? adminLinks : employeeLinks;

    return (
        <nav className="sticky top-0 z-40 w-full backdrop-blur flex-none transition-colors duration-500 lg:z-50 lg:border-b lg:border-slate-900/10 dark:border-slate-50/[0.06] bg-white/95 dark:bg-slate-900/75 supports-backdrop-blur:bg-white/60">
            <div className="max-w-8xl mx-auto">
                <div className="py-4 border-b border-slate-900/10 lg:px-8 lg:border-0 dark:border-slate-300/10 px-4">
                    <div className="relative flex items-center justify-between">
                        {/* Logo / Company Name */}
                        <Link to="/" className="mr-3 flex-none w-[2.0625rem] overflow-hidden md:w-auto">
                            <span className="font-bold text-xl text-slate-900 dark:text-white">
                                {user?.role === 'ADMIN' ? 'TechCorp Inc.' : 'HRMS'}
                            </span>
                        </Link>

                        {/* Navigation Links */}
                        <div className="hidden lg:flex lg:items-center lg:gap-8 mx-8">
                            {links.map(link => (
                                <Link
                                    key={link.path}
                                    to={link.path}
                                    className={`text-sm font-medium hover:text-brand-primary transition-colors ${location.pathname === link.path ? 'text-brand-primary' : 'text-slate-600 dark:text-slate-300'
                                        }`}
                                >
                                    {link.label}
                                </Link>
                            ))}
                        </div>

                        {/* Right Side: Check In + Profile */}
                        <div className="flex items-center gap-4">
                            {user?.role === 'Employee' && (
                                <button
                                    onClick={handleCheckInToggle}
                                    disabled={loading}
                                    className={`relative inline-flex h-6 w-11 flex-shrink-0 cursor-pointer rounded-full border-2 border-transparent transition-colors duration-200 ease-in-out focus:outline-none ${isCheckedIn ? 'bg-green-500' : 'bg-red-500'
                                        }`}
                                    title={isCheckedIn ? "Checked In" : "Checked Out"}
                                >
                                    <span className="sr-only">Check in status</span>
                                    <span
                                        aria-hidden="true"
                                        className={`pointer-events-none inline-block h-5 w-5 transform rounded-full bg-white shadow ring-0 transition duration-200 ease-in-out ${isCheckedIn ? 'translate-x-5' : 'translate-x-0'
                                            }`}
                                    />
                                </button>
                            )}
                            <ProfileDropdown />
                        </div>
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default TopBar;

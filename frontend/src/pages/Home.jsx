import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';

const Feature = ({ icon, title, desc }) => (
  <div className="card p-6">
    <div className="h-10 w-10 rounded-lg bg-gradient-to-br from-brand-primary to-brand-secondary text-white grid place-items-center shadow mb-4">
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" className="h-5 w-5" fill="currentColor">
        <path d={icon} />
      </svg>
    </div>
    <h3 className="text-lg font-semibold mb-2">{title}</h3>
    <p className="text-slate-600 dark:text-slate-300 text-sm">{desc}</p>
  </div>
);

const Home = () => {
  const navigate = useNavigate();
  return (
    <div className="min-h-screen">
      <Navbar />
      <header className="relative overflow-hidden">
        <div className="absolute -top-24 -left-24 h-72 w-72 rounded-full bg-gradient-to-br from-brand-primary/20 to-brand-secondary/20 blur-3xl" />
        <div className="absolute -bottom-24 -right-24 h-72 w-72 rounded-full bg-gradient-to-br from-brand-secondary/20 to-brand-primary/20 blur-3xl" />
        <div className="relative mx-auto max-w-6xl px-4 md:px-6 py-16 md:py-24">
          <div className="grid md:grid-cols-2 gap-10 items-center">
            <div>
              <h1 className="text-3xl md:text-5xl font-extrabold tracking-tight text-slate-900 dark:text-white">
                Streamline your HR operations with Dayflow
              </h1>
              <p className="mt-4 text-slate-600 dark:text-slate-300 text-base md:text-lg">
                Manage attendance, leaves, payroll, and employee profiles in one elegant dashboard.
              </p>
              <div className="mt-6 flex flex-wrap gap-3">
                <button onClick={() => navigate('/login')} className="inline-flex items-center gap-2 bg-gradient-to-r from-brand-primary to-brand-secondary hover:opacity-90 text-white px-5 py-3 rounded-lg shadow">
                  Get Started
                </button>
                <Link to="/register" className="inline-flex items-center gap-2 px-5 py-3 rounded-lg border border-slate-200 dark:border-slate-700 bg-white/60 dark:bg-slate-800/60 hover:bg-white dark:hover:bg-slate-800">
                  Create an account
                </Link>
              </div>
              <div className="mt-6 text-xs text-slate-500 dark:text-slate-400">
                Demo accounts: admin@dayflow.com/admin • employee@dayflow.com/employee
              </div>
            </div>
            <div className="relative">
              <div className="card p-4 md:p-6">
                <div className="grid grid-cols-2 gap-4">
                  <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-900/40">
                    <p className="text-xs text-slate-500">Attendance</p>
                    <p className="text-2xl font-bold mt-2">On time</p>
                  </div>
                  <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-900/40">
                    <p className="text-xs text-slate-500">Leaves</p>
                    <p className="text-2xl font-bold mt-2">2 pending</p>
                  </div>
                  <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-900/40">
                    <p className="text-xs text-slate-500">Payroll</p>
                    <p className="text-2xl font-bold mt-2">₹ 58,400</p>
                  </div>
                  <div className="p-4 rounded-xl bg-slate-50 dark:bg-slate-900/40">
                    <p className="text-xs text-slate-500">Employees</p>
                    <p className="text-2xl font-bold mt-2">142</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </header>
      <main className="mx-auto max-w-6xl px-4 md:px-6 pb-16">
        <section className="grid md:grid-cols-3 gap-6">
          <Feature title="Attendance" desc="Quick check-in/out, real-time view for admins." icon="M8 7h8M8 11h8M8 15h6M5 4h14v16H5z" />
          <Feature title="Leaves" desc="Apply, approve, and track leaves effortlessly." icon="M5 12l4 4L19 6" />
          <Feature title="Payroll" desc="Transparent payroll overview and management." icon="M12 8c-2.5 0-3.5 3 0 3s2.5 3 0 3m0-9v12" />
        </section>
      </main>
    </div>
  );
};

export default Home;

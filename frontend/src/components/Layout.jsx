import React from 'react';
import { Outlet } from 'react-router-dom';
import TopBar from './TopBar';

const Layout = () => {
    return (
        <div className="min-h-screen bg-gray-50 dark:bg-gray-900 flex flex-col">
            <TopBar />
            <main className="flex-1 w-full max-w-8xl mx-auto p-4 lg:px-8">
                <Outlet />
            </main>
        </div>
    );
};

export default Layout;

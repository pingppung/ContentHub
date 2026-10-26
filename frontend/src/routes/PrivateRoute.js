import React, { useEffect, useState } from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import AuthService from '../services/AuthService';

const PrivateRoute = ({ redirectPath }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const location = useLocation();

    useEffect(() => {
        let active = true;
        setLoading(true);
        setUser(null);
        const token = AuthService.getToken();
        if (!token) {
            setLoading(false);
            return;
        }
        AuthService.getCurrentUser(token)
            .then(currentUser => { if (active) setUser(currentUser); })
            .catch(error => {
                if (active && error.response?.status === 401) AuthService.removeToken();
            })
            .finally(() => { if (active) setLoading(false); });
        return () => { active = false; };
    }, [location.pathname]);

    if (loading) return <div>Loading...</div>;
    const isAdminPage = location.pathname === '/admin' || location.pathname.startsWith('/admin/');
    const allowed = user && (!isAdminPage || user.role === 'ROLE_ADMIN');
    return allowed ? <Outlet /> : <Navigate to={redirectPath} replace />;
};

export default PrivateRoute;

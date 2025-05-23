import React, { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';

export default function Home() {
    const { user } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (user) {
            navigate('/dashboard');
        }
    }, [user, navigate]);

    return (
        <div style={{ padding: '2rem', textAlign: 'center' }}>
            <h1>Welcome to Our Booking Platform</h1>
            <p>Please log in or register to continue.</p>
            <div style={{ marginTop: '2rem' }}>
                <Link to="/login" style={{ marginRight: '1rem' }}>
                    Login
                </Link>
                <Link to="/register">Register</Link>
            </div>
        </div>
    );
}

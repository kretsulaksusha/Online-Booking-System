import React from 'react';
import { Routes, Route, Link, Navigate } from 'react-router-dom';
import Home from './pages/Home.jsx';
import Dashboard from './pages/Dashboard.jsx';
import Login from './pages/Login.jsx';
import Register from './pages/Register.jsx';
import Bookings from './pages/Bookings.jsx';
import NotFound from './pages/NotFound.jsx';
import { useAuth } from './context/AuthContext';
import './App.css';

export default function App() {
    const { user, logout, isAuthenticated } = useAuth();

    return (
        <div className="app-container">
            <nav className="navbar">
                <Link className="nav-link" to="/">Home</Link>
                <Link className="nav-link" to="/dashboard">Dashboard</Link>

                {isAuthenticated ? (
                    <>
                        <Link className="nav-link" to="/booking">Bookings</Link>
                        <button
                            className="nav-link logout-button"
                            onClick={logout}
                            style={{
                                background: 'none',
                                border: 'none',
                                cursor: 'pointer',
                                padding: 0,
                                font: 'inherit',
                                color: 'blue',
                                textDecoration: 'underline',
                            }}
                        >
                            Logout ({user || 'User'})
                        </button>
                    </>
                ) : (
                    <>
                        <Link className="nav-link" to="/login">Login</Link>
                        <Link className="nav-link" to="/register">Register</Link>
                    </>
                )}
            </nav>

            <main className="main-content">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/dashboard" element={<Dashboard />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route
                        path="/booking"
                        element={
                            isAuthenticated ? <Bookings /> : <Navigate to="/login" replace />
                        }
                    />
                    <Route path="*" element={<NotFound />} />
                </Routes>
            </main>
        </div>
    );
}

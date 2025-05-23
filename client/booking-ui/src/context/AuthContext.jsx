import React, { createContext, useContext, useState } from 'react';

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [token, setToken] = useState(() => localStorage.getItem('token'));

    // Decoding username from JWT token (simple base64 decode of payload part)
    const getUsernameFromToken = (token) => {
        if (!token) return null;
        try {
            const payload = token.split('.')[1];
            return JSON.parse(atob(payload)).sub; // assuming username stored in 'sub'
        } catch {
            return null;
        }
    };

    const [user, setUser] = useState(() => getUsernameFromToken(token));

    const login = async (credentials) => {
        try {
            const res = await fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(credentials),
            });

            if (!res.ok) {
                const message = await res.text();
                throw new Error(message || 'Login failed');
            }

            const data = await res.json(); // { token: "jwt-token" }

            localStorage.setItem('token', data.token);
            setToken(data.token);
            setUser(getUsernameFromToken(data.token));
            return { success: true };
        } catch (err) {
            console.error(err);
            return { success: false, message: err.message };
        }
    };

    const register = async (credentials) => {
        try {
            const res = await fetch('http://localhost:8080/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(credentials),
            });

            if (!res.ok) {
                const message = await res.text();
                throw new Error(message || 'Registration failed');
            }

            // Registration returns plain text success message
            // No token returned, so user must login after register
            return { success: true };
        } catch (err) {
            console.error(err);
            return { success: false, message: err.message };
        }
    };

    const logout = async () => {
        if (!token) return;

        try {
            await fetch('http://localhost:8080/auth/logout', {
                method: 'POST',
                headers: { Authorization: `Bearer ${token}` },
            });
        } catch (e) {
            console.error('Logout error', e);
        }

        localStorage.removeItem('token');
        setToken(null);
        setUser(null);
    };

    const isAuthenticated = !!token;

    return (
        <AuthContext.Provider value={{ user, token, login, register, logout, isAuthenticated }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}

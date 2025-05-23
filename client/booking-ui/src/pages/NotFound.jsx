import React from 'react';
import { Link } from 'react-router-dom';

export default function NotFound() {
    return (
        <div style={{ padding: '1rem', textAlign: 'center' }}>
            <h1>404 - Page Not Found</h1>
            <p>Sorry, the page you're looking for does not exist.</p>
            <Link to="/">Go back to Dashboard</Link>
        </div>
    );
}

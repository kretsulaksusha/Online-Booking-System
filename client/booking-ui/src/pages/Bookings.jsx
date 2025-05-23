import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';

export default function Bookings() {
    const { user } = useAuth();
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!user) {
            setLoading(false);
            return;
        }

        const fetchBookings = async () => {
            try {
                const res = await fetch(`http://localhost:8080/bookings`, {
                    headers: {
                        Authorization: `Bearer ${user.token}`,
                    },
                });
                if (!res.ok) throw new Error('Failed to fetch bookings');
                const data = await res.json();
                setBookings(data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchBookings();
    }, [user]);

    if (!user) return <div>Please log in to see your bookings.</div>;

    if (loading) return <div>Loading your bookings...</div>;

    if (error) return <div>Error: {error}</div>;

    if (bookings.length === 0) return <div>You have no bookings yet.</div>;

    return (
        <div>
            <h1>Your Bookings</h1>
            <table border="1" cellPadding="8" cellSpacing="0">
                <thead>
                <tr>
                    <th>Booking ID</th>
                    <th>Item ID</th>
                    <th>Quantity</th>
                    <th>Created At</th>
                </tr>
                </thead>
                <tbody>
                {bookings.map((b) => (
                    <tr key={b.id}>
                        <td>{b.id}</td>
                        <td>{b.itemId}</td>
                        <td>{b.quantity}</td>
                        <td>{new Date(b.createdAt).toLocaleString()}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';

export default function Dashboard() {
    const { isAuthenticated } = useAuth();
    const [inventory, setInventory] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [bookingState, setBookingState] = useState({}); // Tracks loading/error/success per item

    useEffect(() => {
        fetch('http://localhost:8080/inventory')
            .then(res => {
                if (!res.ok) throw new Error('Failed to fetch inventory');
                return res.json();
            })
            .then(data => {
                setInventory(data);
                setLoading(false);
            })
            .catch(err => {
                setError(err.message);
                setLoading(false);
            });
    }, []);

    const handleBook = async (itemId, quantity, currentAvailable, price) => {
        if (quantity <= 0 || quantity > currentAvailable) {
            setBookingState(prev => ({
                ...prev,
                [itemId]: { error: `Invalid quantity`, loading: false },
            }));
            return;
        }

        setBookingState(prev => ({
            ...prev,
            [itemId]: { loading: true, success: null, error: null },
        }));

        try {
            // 1. Create booking
            const bookingRes = await fetch('http://localhost:8080/bookings', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ itemId, quantity }),
            });

            if (!bookingRes.ok) throw new Error('Booking failed');

            // 2. Update inventory
            const newCount = currentAvailable - quantity;
            const patchRes = await fetch(`http://localhost:8080/inventory/${itemId}`, {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ availableCount: newCount, price: price }),
            });

            if (!patchRes.ok) throw new Error('Failed to update inventory');

            // 3. Refresh inventory
            const updatedInventory = await fetch('http://localhost:8080/inventory').then(res => res.json());
            setInventory(updatedInventory);

            setBookingState(prev => ({
                ...prev,
                [itemId]: { loading: false, success: `Booked ${quantity} item(s)`, error: null },
            }));
        } catch (err) {
            setBookingState(prev => ({
                ...prev,
                [itemId]: { loading: false, success: null, error: err.message },
            }));
        }
    };

    if (loading) return <div>Loading inventory...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div>
            <h1>Inventory Dashboard</h1>
            <table border="1" cellPadding="8" cellSpacing="0">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Type</th>
                    <th>Available Count</th>
                    <th>Price</th>
                    {isAuthenticated && <th>Action</th>}
                </tr>
                </thead>
                <tbody>
                {inventory
                    .filter(item => item.availableCount > 0)
                    .map(item => (
                        <InventoryRow
                            key={item.id}
                            item={item}
                            isAuthenticated={isAuthenticated}
                            bookingState={bookingState[item.id]}
                            onBook={handleBook}
                        />
                    ))}
                </tbody>
            </table>
        </div>
    );
}

function InventoryRow({ item, isAuthenticated, bookingState, onBook }) {
    const [quantity, setQuantity] = useState(1);

    return (
        <tr>
            <td>{item.id}</td>
            <td>{item.type}</td>
            <td>{item.availableCount}</td>
            <td>${item.price.toFixed(2)}</td>
            {isAuthenticated && (
                <td>
                    <input
                        type="number"
                        min="1"
                        max={item.availableCount}
                        value={quantity}
                        onChange={(e) => setQuantity(Number(e.target.value))}
                        style={{ width: '60px', marginRight: '8px' }}
                    />
                    <button
                        onClick={() => onBook(item.id, quantity, item.availableCount, item.price)}
                        disabled={bookingState?.loading}
                    >
                        {bookingState?.loading ? 'Booking...' : 'Book'}
                    </button>
                    {bookingState?.error && <div style={{ color: 'red' }}>{bookingState.error}</div>}
                    {bookingState?.success && <div style={{ color: 'green' }}>{bookingState.success}</div>}
                </td>
            )}
        </tr>
    );
}

import { useState } from 'react';

export default function Booking() {
    const [bookingDetails, setBookingDetails] = useState({
        date: '',
        time: '',
        service: '',
        userId: '',
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setBookingDetails({ ...bookingDetails, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const token = localStorage.getItem('token');
        if (!token) {
            alert('Please log in first.');
            return;
        }

        try {
            const res = await fetch('http://localhost:8082/bookings', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(bookingDetails),
            });

            const data = await res.json();

            if (res.ok) {
                alert('Booking successful!');
                setBookingDetails({ date: '', time: '', service: '', userId: '' });
            } else {
                alert(data.message || 'Booking failed.');
            }
        } catch (err) {
            console.error(err);
            alert('Network error.');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Book a Service</h2>

            <input
                type="date"
                name="date"
                value={bookingDetails.date}
                onChange={handleChange}
                required
            />
            <input
                type="time"
                name="time"
                value={bookingDetails.time}
                onChange={handleChange}
                required
            />
            <input
                type="text"
                name="service"
                placeholder="Service name"
                value={bookingDetails.service}
                onChange={handleChange}
                required
            />
            <input
                type="text"
                name="userId"
                placeholder="User ID"
                value={bookingDetails.userId}
                onChange={handleChange}
                required
            />

            <button type="submit">Book Now</button>
        </form>
    );
}

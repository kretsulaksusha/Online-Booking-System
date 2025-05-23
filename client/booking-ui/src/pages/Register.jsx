import { useState } from 'react';

export default function Register() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleRegister = async (e) => {
        e.preventDefault();

        const payload = {
            username,
            password
        };

        try {
            const res = await fetch('http://localhost:8085/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload),
            });

            console.log(res);

            const text = await res.text();

            if (res.ok) {
                alert('Registered successfully!');
            } else {
                alert(`Registration failed: ${text}`);
            }
        } catch (error) {
            alert('An error occurred during registration.');
            console.error(error);
        }
    };

    return (
        <form onSubmit={handleRegister}>
            <h2>Register</h2>
            <input
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
            />
            <button type="submit">Register</button>
        </form>
    );
}

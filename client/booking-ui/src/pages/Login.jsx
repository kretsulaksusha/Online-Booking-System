import { useState } from 'react';

export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async (e) => {
        const payload = {
            username,
            password
        };

        e.preventDefault();
        try {
            const res = await fetch('http://localhost:8085/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload),
            });

            const data = await res.json();

            if (res.ok) {
                localStorage.setItem('token', data.token);
                alert('Login successful!');
            } else {
                alert(data);
            }
        } catch (error) {
            alert('An error occurred during login. Please try again later.');
            console.error(error);
        }
    };

    return (
        <form onSubmit={handleLogin}>
            <h2>Login</h2>
            <input placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
            <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
            <button type="submit">Login</button>
        </form>
    );
}

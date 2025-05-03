package online.booking.userservice.user.service;

import lombok.RequiredArgsConstructor;
import online.booking.userservice.user.model.User;
import online.booking.userservice.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    @Override
    public User register(User user) {
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        user.setPasswordHash(encoder.encode(user.getPasswordHash()));
        return repo.save(user);
    }

    @Override
    public User login(String email, String password) {
        User u = repo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!encoder.matches(password, u.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return u;
    }

    @Override
    public User findById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }
}

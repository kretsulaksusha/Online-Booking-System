package online.booking.userservice.user.service;

import lombok.RequiredArgsConstructor;
import online.booking.userservice.user.dto.UserUpdateRequest;
import online.booking.userservice.user.exception.UserNotFoundException;
import online.booking.userservice.user.model.User;
import online.booking.userservice.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;

    @Override
    public User findById(UUID id) {
        return repo.findById(id)
                .filter(User::isActive)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
    }

    @Override
    public User updateProfile(UUID id, UserUpdateRequest req) {
        User user = findById(id);
        user.setEmail(req.getEmail());
        user.setFullName(req.getFullName());
        return repo.save(user);
    }

    @Override
    public void deactivate(UUID id) {
        User user = findById(id);
        user.setActive(false);
        repo.save(user);
    }
}


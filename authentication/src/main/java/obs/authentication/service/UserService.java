package obs.authentication.service;

import obs.authentication.model.User;
import obs.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public Optional<User> register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            log.debug("Username already exists: {}", user.getUsername());
            return Optional.empty();
        }
        user.setPassword(encoder.encode(user.getPassword()));
        log.info("Encoded password: {}", user.getPassword());
        return Optional.of(userRepository.save(user));
    }

    public Optional<User> authenticate(String username, String password) {
        log.debug("authenticate password: {}", password);

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Check password match (compare raw password with stored encoded password)
            if (encoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }

        // Return empty if user not found or the password doesn't match
        return Optional.empty();
    }

    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}

package obs.authentication.controller;

import obs.authentication.model.User;
import obs.authentication.security.JwtUtil;
import obs.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        Optional<User> registeredUser = userService.register(user);
        if (registeredUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists: " + user.getUsername());
        }
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        log.info("🔐 Attempting login for user: {}", user.getUsername());
        log.debug("Raw password received: {}", user.getPassword());

        Optional<User> authenticated = userService.authenticate(user.getUsername(), user.getPassword());

        if (authenticated.isPresent()) {
            String token = jwtUtil.generateToken(authenticated.get().getUsername());
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @GetMapping("/list-users")
    public ResponseEntity<List<User>> listUsers() {
        List<User> users = userService.listAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/delete-all-users")
    public ResponseEntity<String> deleteAllUsers() {
        userService.deleteAllUsers();
        return ResponseEntity.ok("All users deleted");
    }
}

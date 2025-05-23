package obs.authentication.controller;

import obs.authentication.model.User;
import obs.authentication.model.RevokedToken;
import obs.authentication.repository.RevokedTokenRepository;
import obs.authentication.security.JwtUtil;
import obs.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
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
    private final RevokedTokenRepository revokedRepo;

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

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        String token = authHeader.substring(7);
        String jti   = jwtUtil.extractJti(token);
        Date exp     = jwtUtil.extractExpiration(token);

        RevokedToken rt = new RevokedToken();
        rt.setJti(jti);
        rt.setExpiresAt(exp);
        revokedRepo.save(rt);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody String token) {
        try {
            log.debug("Received token: {}", token);

            String jti      = jwtUtil.extractJti(token);
            String username = jwtUtil.extractUsername(token);
            log.debug("Parsed jti: {}, username: {}", jti, username);

            // 1) signature & expiration check
            if (!jwtUtil.validateToken(token, username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }
            // 2) revocation check
            if (revokedRepo.existsById(jti)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has been revoked");
            }

            return ResponseEntity.ok(Collections.singletonMap("username", username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid: " + e.getMessage());
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

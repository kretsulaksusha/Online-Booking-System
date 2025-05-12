package obs.authentication.controller;

import obs.authentication.model.User;
import obs.authentication.security.JwtUtil;
import obs.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            User authenticated = userService.authenticate(user.getUsername(), user.getPassword());

            // Generate token if authenticated successfully
            String token = jwtUtil.generateToken(authenticated.getUsername());

            // Return token in a structured JSON response
            return ResponseEntity.ok().body(Collections.singletonMap("token", token));
        } catch (RuntimeException e) {
            // Unauthorized response if authentication fails
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}

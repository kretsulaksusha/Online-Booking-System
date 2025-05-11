package online.booking.userservice.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.booking.userservice.user.dto.LoginRequest;
import online.booking.userservice.user.dto.UserRequest;
import online.booking.userservice.user.dto.UserResponse;
import online.booking.userservice.user.model.User;
import online.booking.userservice.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody UserRequest request
    ) {
        User toCreate = toUser(request);
        User created  = userService.register(toCreate);
        return ResponseEntity.ok(toResponse(created));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        User loggedIn = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(toResponse(loggedIn));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable String id) {
        User user = userService.findById(UUID.fromString(id));
        return ResponseEntity.ok(toResponse(user));
    }

    private User toUser(UserRequest req) {
        return User.builder()
                .email(req.getEmail())
                .passwordHash(req.getPassword())
                .fullName(req.getFullName())
                .build();
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(u.getId(), u.getEmail(), u.getFullName());
    }
}

package online.booking.userservice.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.booking.userservice.user.dto.UserResponse;
import online.booking.userservice.user.dto.UserUpdateRequest;
import online.booking.userservice.user.model.User;
import online.booking.userservice.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;

    @GetMapping
    public UserResponse me(@RequestHeader("X-User-Id") UUID userId) {
        return toResponse(userService.findById(userId));
    }

    @PutMapping
    public UserResponse update(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody UserUpdateRequest req
    ) {
        return toResponse(userService.updateProfile(userId, req));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@RequestHeader("X-User-Id") UUID userId) {
        userService.deactivate(userId);
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(u.getId(), u.getEmail(), u.getFullName());
    }
}

package online.booking.userservice.user.service;

import online.booking.userservice.user.dto.UserUpdateRequest;
import online.booking.userservice.user.model.User;

import java.util.UUID;

public interface UserService {
    User findById(UUID userId);
    User updateProfile(UUID userId, UserUpdateRequest req);
    void deactivate(UUID userId);
}



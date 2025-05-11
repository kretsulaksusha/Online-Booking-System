package online.booking.userservice.user.service;

import online.booking.userservice.user.dto.LoginRequest;
import online.booking.userservice.user.dto.UserRequest;
import online.booking.userservice.user.model.User;

import java.util.UUID;

public interface UserService {
    User register(User user);
    User login(String email, String password);
    User findById(UUID id);
}


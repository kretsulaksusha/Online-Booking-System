package online.booking.userservice.user.dto;

import lombok.Value;
import java.util.UUID;

@Value
public class UserDto {
    UUID id;
    String email;
    String fullName;
}

package online.booking.userservice.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Builder
@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(name="full_name", nullable=false)
    private String fullName;

    @Column(nullable=false, updatable=false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(nullable=false)
    @Builder.Default
    private boolean active = true;
}


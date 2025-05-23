package obs.authentication.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "revoked_tokens")
public class RevokedToken {
    @Id
    private String jti;

    @Indexed(name = "expire_at_idx", expireAfterSeconds = 0)
    private Date expiresAt;
}

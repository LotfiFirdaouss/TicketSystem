package morocco.it.TicketSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Represents a request to login a user")
public class LoginRequest {
    private String username;
    private String password;
}

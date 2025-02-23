package morocco.it.TicketSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Represents a request to login a user")
public class LoginRequest {

    @Schema(description = "The username of the user", example = "john_doe", required = true)
    private String username;

    @Schema(description = "The password of the user", example = "securePassword123", required = true)
    private String password;
}

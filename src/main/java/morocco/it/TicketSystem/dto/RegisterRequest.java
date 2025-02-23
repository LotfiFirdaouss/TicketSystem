package morocco.it.TicketSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import morocco.it.TicketSystem.entities.enums.Role;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Represents a request to register a new user")
public class RegisterRequest {

    @Schema(description = "The username of the user", example = "john_doe", required = true)
    private String username;

    @Schema(description = "The password of the user", example = "securePassword123", required = true)
    private String password;

    @Schema(description = "The role of the user", example = "USER", required = true)
    private Role role;
}
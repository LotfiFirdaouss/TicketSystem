package morocco.it.TicketSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import morocco.it.TicketSystem.entities.enums.Role;

@Data
@Schema(description = "Represents a request to register a new user")
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}

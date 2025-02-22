package morocco.it.TicketSystem.dto;

import lombok.Data;
import morocco.it.TicketSystem.entities.enums.Role;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}

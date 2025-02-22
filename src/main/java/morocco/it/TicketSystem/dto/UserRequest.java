package morocco.it.TicketSystem.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    private int roleIndex;
}

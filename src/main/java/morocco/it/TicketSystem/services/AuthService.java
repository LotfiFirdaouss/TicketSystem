package morocco.it.TicketSystem.services;

import morocco.it.TicketSystem.dto.LoginRequest;
import morocco.it.TicketSystem.dto.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest registerRequest);

    String login(LoginRequest loginRequest);

    void logout(String token);
}

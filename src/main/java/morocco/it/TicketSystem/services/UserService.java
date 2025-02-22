package morocco.it.TicketSystem.services;

import morocco.it.TicketSystem.dto.UserRequest;
import morocco.it.TicketSystem.entities.User;

public interface UserService {
    User getUserById(Long id);
    User createUser(UserRequest userRequest);
}

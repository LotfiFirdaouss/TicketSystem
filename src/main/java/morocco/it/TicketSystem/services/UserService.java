package morocco.it.TicketSystem.services;

import morocco.it.TicketSystem.entities.User;

public interface UserService {
    User getUserById(Long id);
    User getUserByUsername(String username);
    User createUser(User user);
}

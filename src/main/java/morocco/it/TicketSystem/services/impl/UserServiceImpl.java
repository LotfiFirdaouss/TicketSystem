package morocco.it.TicketSystem.services.impl;

import lombok.AllArgsConstructor;
import morocco.it.TicketSystem.entities.User;
import morocco.it.TicketSystem.exceptions.ResourceNotFoundException;
import morocco.it.TicketSystem.repositories.UserRepository;
import morocco.it.TicketSystem.services.UserService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: "+id));
    }
    @Override
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Username: "+ username));
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

}

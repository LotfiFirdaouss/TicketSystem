package morocco.it.TicketSystem.services.impl;

import lombok.AllArgsConstructor;
import morocco.it.TicketSystem.dto.UserRequest;
import morocco.it.TicketSystem.entities.User;
import morocco.it.TicketSystem.entities.enums.Role;
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
    public User createUser(UserRequest userRequest){
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .role(Role.fromIndex(userRequest.getRoleIndex()))
                .build();

        return userRepository.save(user);
    }
}

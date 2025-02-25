package morocco.it.TicketSystem.mappers;

import morocco.it.TicketSystem.entities.User;
import morocco.it.TicketSystem.services.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected UserService userService;

    @Named("mapUserIdToUser")
    public User mapUserIdToUser(Long userId) {
        return (userId != null) ? userService.getUserById(userId) : null;
    }

}

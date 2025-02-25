package morocco.it.TicketSystem.mappers;

import morocco.it.TicketSystem.dto.TicketRequest;
import morocco.it.TicketSystem.dto.TicketResponse;
import morocco.it.TicketSystem.entities.Ticket;
import morocco.it.TicketSystem.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class})
public interface TicketMapper {

    @Mapping(source = "createdById", target = "createdBy", qualifiedByName = "mapUserIdToUser")
    @Mapping(source = "assignedToId", target = "assignedTo", qualifiedByName = "mapUserIdToUser")
    Ticket fromRequestToEntity(TicketRequest request);

    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "assignedTo", target = "assignedToId", qualifiedByName = "getAssignedToId")
    @Mapping(source = "comments", target = "comments", qualifiedByName = "mapCommentsToDto")
    TicketResponse fromEntityToResponse(Ticket ticket);

    @Named("getAssignedToId")
    static Long getAssignedToId(User assignedTo) {
        return (assignedTo != null) ? assignedTo.getId() : null;
    }

    List<TicketResponse> fromEntityToResponseList(List<Ticket> tickets);
}

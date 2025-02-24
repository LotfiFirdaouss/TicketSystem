package morocco.it.TicketSystem.services;

import morocco.it.TicketSystem.dto.CommentDto;
import morocco.it.TicketSystem.dto.TicketRequest;
import morocco.it.TicketSystem.dto.TicketRequestUpdate;
import morocco.it.TicketSystem.dto.TicketResponse;
import morocco.it.TicketSystem.entities.enums.Status;

import java.util.List;
import java.util.Optional;

public interface TicketService {

    // CREATE
    TicketResponse createTicket(TicketRequest ticketRequest);

    // UPDATE
    TicketResponse changeTicketStatus(TicketRequestUpdate ticketRequestUpdate, Long ticketId); // default=NEW , IN_PROGRESS , RESOLVED
    TicketResponse assignTicket(TicketRequestUpdate ticketRequestUpdate, Long ticketId);
    TicketResponse addCommentToTicket(CommentDto commentDto, Long ticketId);

    // READ
    List<TicketResponse> getAllTickets();

    List<TicketResponse> getTicketByEmployeeId(Long employeeId);

    TicketResponse getTicketById(Long id);
    List<TicketResponse> getTicketByStatus(Status status);

}

package morocco.it.TicketSystem.services;

import morocco.it.TicketSystem.dto.CommentRequest;
import morocco.it.TicketSystem.dto.TicketRequest;
import morocco.it.TicketSystem.dto.TicketRequestUpdate;
import morocco.it.TicketSystem.dto.TicketResponse;
import morocco.it.TicketSystem.entities.Comment;
import morocco.it.TicketSystem.entities.Ticket;

import java.util.List;

public interface TicketService {

    // CREATE
    TicketResponse createTicket(TicketRequest ticketRequest);

    // UPDATE
    TicketResponse changeTicketStatus(TicketRequestUpdate ticketRequestUpdate, Long ticketId); // default=NEW , IN_PROGRESS , RESOLVED
    TicketResponse assignTicket(TicketRequestUpdate ticketRequestUpdate, Long ticketId);
    TicketResponse reassignTicket(TicketRequestUpdate ticketRequestUpdate, Long ticketId);
    Comment addCommentToTicket(CommentRequest commentRequest, Long ticketId);

    // READ
    Ticket getTicketById(Long id);
    List<TicketResponse> getAllTickets();
    List<TicketResponse> getTicketByEmployeeId(Long employeeId);
    //  Search & filter by Ticket ID and status
    List<TicketResponse> getTicketByStatus(int statusIndex);


}

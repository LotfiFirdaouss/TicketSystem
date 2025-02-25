package morocco.it.TicketSystem.services.impl;

import lombok.AllArgsConstructor;
import morocco.it.TicketSystem.dto.*;
import morocco.it.TicketSystem.entities.Comment;
import morocco.it.TicketSystem.entities.Ticket;
import morocco.it.TicketSystem.entities.User;
import morocco.it.TicketSystem.entities.enums.Status;
import morocco.it.TicketSystem.entities.enums.TicketAction;
import morocco.it.TicketSystem.exceptions.ResourceNotFoundException;
import morocco.it.TicketSystem.mappers.CommentMapper;
import morocco.it.TicketSystem.mappers.TicketMapper;
import morocco.it.TicketSystem.repositories.CommentRepository;
import morocco.it.TicketSystem.repositories.TicketRepository;
import morocco.it.TicketSystem.services.AuditLogService;
import morocco.it.TicketSystem.services.TicketService;
import morocco.it.TicketSystem.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

    //private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketMapper ticketMapper;
    private final CommentMapper commentMapper;

    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final AuditLogService auditLogService;

    @Override
    public TicketResponse createTicket(TicketRequest ticketRequest){

        // Saving the ticket
        Ticket ticket = ticketMapper.fromRequestToEntity(ticketRequest);

        Ticket createdTicket = ticketRepository.save(ticket);

        // logging the action
        AuditLogRequest auditLogRequest = AuditLogRequest.builder()
                .ticket(createdTicket)
                .userId(createdTicket.getCreatedBy().getId())
                .ticketAction(TicketAction.CREATED)
                .build();

        auditLogService.createAuditLog(auditLogRequest);

        // Returning the result
        return ticketMapper.fromEntityToResponse(createdTicket);
    }

    @Override
    public TicketResponse changeTicketStatus(TicketRequestUpdate ticketRequestUpdate, Long ticketId) {
        // Get the ticket (throws exception if not found)
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: "+ticketId));

        // Validate the status index
        Status newStatus = ticketRequestUpdate.getStatus();

        // Log the old status before updating
        Status oldStatus = ticket.getStatus();

        // Update the ticket status
        ticket.setStatus(newStatus);

        // Save the updated ticket
        Ticket savedTicket = ticketRepository.save(ticket);

        // Log the status change action
        AuditLogRequest auditLogRequest = AuditLogRequest.builder()
                .ticket(ticket)
                .userId(ticket.getCreatedBy().getId())
                .ticketAction(TicketAction.STATUS_CHANGED)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .build();

        auditLogService.createAuditLog(auditLogRequest);

        // Return the updated ticket response
        return ticketMapper.fromEntityToResponse(savedTicket);
    }

    @Override
    public TicketResponse assignTicket(TicketRequestUpdate ticketRequestUpdate, Long ticketId) {
        // Get the ticket (throws exception if not found)
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: "+ticketId));


        // Check if it's first time assignement or reassignement
        TicketAction ticketAction = TicketAction.ASSIGNED;
        if(ticket.getAssignedTo() != null){
            ticketAction = TicketAction.REASSIGNED;
        }

        // Validate assignedToId
        Long assignedToId = ticketRequestUpdate.getAssignedToId();
        if (assignedToId == null) {
            throw new IllegalArgumentException("assignedToId cannot be null");
        }

        // Fetch the user (throws exception if not found)
        User itSupport = userService.getUserById(assignedToId);

        // Assign the ticket to IT support
        ticket.setAssignedTo(itSupport);

        // Saving the updated ticket
        Ticket savedTicket = ticketRepository.save(ticket);

        // logging the action
        AuditLogRequest auditLogRequest = AuditLogRequest.builder()
                .ticket(savedTicket)
                .userId(savedTicket.getCreatedBy().getId())
                .ticketAction(ticketAction)
                .assignedToUserId(ticketRequestUpdate.getAssignedToId())
                .build();

        auditLogService.createAuditLog(auditLogRequest);

        // returning the result
        return ticketMapper.fromEntityToResponse(savedTicket);
    }

    @Override
    public TicketResponse addCommentToTicket(CommentDto commentDto, Long ticketId) {
        // Get the ticket and user (throws exception if not found)
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: " + ticketId));

        // Map to Comment
        Comment comment = commentMapper.fromDtoCommentToComment(commentDto);

        // Associate the comment with the ticket
        comment.setTicket(ticket);

        // Save comment
        commentRepository.save(comment);
        Ticket updatedTicket = ticketRepository.getById(ticketId);

        // Log the action
        AuditLogRequest auditLogRequest = AuditLogRequest.builder()
                .ticket(ticket)
                .userId(commentDto.getCreatedById())
                .ticketAction(TicketAction.COMMENT_ADDED)
                .comment(commentDto.getContent())
                .build();

        auditLogService.createAuditLog(auditLogRequest);

        // Return the updated ticket response
        return ticketMapper.fromEntityToResponse(updatedTicket);
    }



    @Override
    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: "+id));
        return ticketMapper.fromEntityToResponse(ticket);
    }

    @Override
    public List<TicketResponse> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return ticketMapper.fromEntityToResponseList(tickets);
    }

    @Override
    public List<TicketResponse> getTicketByEmployeeId(Long employeeId) {
        List<Ticket> tickets = ticketRepository.findByCreatedBy_Id(employeeId);
        return ticketMapper.fromEntityToResponseList(tickets);
    }

    @Override
    public List<TicketResponse> getTicketByStatus(Status status) {
        List<Ticket> tickets = ticketRepository.findByStatus(status);
        return ticketMapper.fromEntityToResponseList(tickets);
    }

}

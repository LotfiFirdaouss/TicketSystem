package morocco.it.TicketSystem.services.impl;

import lombok.RequiredArgsConstructor;
import morocco.it.TicketSystem.dto.*;
import morocco.it.TicketSystem.entities.Comment;
import morocco.it.TicketSystem.entities.Ticket;
import morocco.it.TicketSystem.entities.User;
import morocco.it.TicketSystem.entities.enums.Status;
import morocco.it.TicketSystem.entities.enums.TicketAction;
import morocco.it.TicketSystem.exceptions.ResourceNotFoundException;
import morocco.it.TicketSystem.repositories.CommentRepository;
import morocco.it.TicketSystem.repositories.TicketRepository;
import morocco.it.TicketSystem.services.AuditLogService;
import morocco.it.TicketSystem.services.TicketService;
import morocco.it.TicketSystem.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final AuditLogService auditLogService;

    @Override
    public TicketResponse createTicket(TicketRequest ticketRequest){
        log.info("Received request to create ticket: {}", ticketRequest);

        // Saving the ticket
        Ticket ticket = fromRequestToEntity(ticketRequest);
        log.debug("Converted DTO to entity: {}", ticket);

        Ticket createdTicket = ticketRepository.save(ticket);
        log.info("Ticket successfully saved with ID: {}", createdTicket.getId());

        // logging the action
        AuditLogRequest auditLogRequest = AuditLogRequest.builder()
                .ticket(createdTicket)
                .userId(createdTicket.getCreatedBy().getId())
                .ticketAction(TicketAction.CREATED)
                .build();

        auditLogService.createAuditLog(auditLogRequest);

        // Returning the result
        return fromEntityToResponse(createdTicket);
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
        return fromEntityToResponse(ticket);
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
        return fromEntityToResponse(savedTicket);
    }

    @Override
    public TicketResponse addCommentToTicket(CommentDto commentRequest, Long ticketId) {
        // Get the ticket and user (throws exception if not found)
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: "+ticketId));
        User user = userService.getUserById(commentRequest.getCreatedById());

        // map to Comment
        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .ticket(ticket)
                .createdBy(user)
                .build();

        // save comment
        Comment savedComment = commentRepository.save(comment);

        // logging the action
        AuditLogRequest auditLogRequest = AuditLogRequest.builder()
                .ticket(ticket)
                .userId(commentRequest.getCreatedById())
                .ticketAction(TicketAction.COMMENT_ADDED)
                .build();

        auditLogService.createAuditLog(auditLogRequest);

        // Get the ticket
        TicketResponse ticketResponse = fromEntityToResponse(ticketRepository.getById(ticketId));

        // returning the result
        return ticketResponse;
    }



    @Override
    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: "+id));
        return fromEntityToResponse(ticket);
    }

    @Override
    public List<TicketResponse> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(this::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponse> getTicketByEmployeeId(Long employeeId) {
        List<Ticket> tickets = ticketRepository.findByCreatedBy_Id(employeeId);
        return tickets.stream()
                .map(this::fromEntityToResponse)
                .collect(Collectors.toList());
    }

//    @Override
//    public Optional<TicketResponse> getEmployeeTicketsById(Long employeeId, Long ticketId) {
//        Optional<Ticket> ticket = ticketRepository.findByCreatedBy_IdAndId(employeeId, ticketId);
//        return ticket.map(this::fromEntityToResponse); // Correctly handling Optional
//    }
//
//    @Override
//    public List<TicketResponse> getEmployeeTicketsByStatus(Long employeeId, Status status) {
//        return null;
//    }

    @Override
    public List<TicketResponse> getTicketByStatus(Status status) {
        List<Ticket> tickets = ticketRepository.findByStatus(status);
        return tickets.stream()
                .map(this::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    private Ticket fromRequestToEntity(TicketRequest ticketRequest){
        Long createdById = ticketRequest.getCreatedById();
        User user = userService.getUserById(createdById);

        Ticket ticket = new Ticket();  // Ensure a new instance (no ID set)
        ticket.setTitle(ticketRequest.getTitle());
        ticket.setDescription(ticketRequest.getDescription());
        ticket.setPriority(ticketRequest.getPriority());
        ticket.setCategory(ticketRequest.getCategory());
        ticket.setCreatedBy(user);

        Status status = ticketRequest.getStatus();
        if(status != null) { // If status is provided
            ticket.setStatus(status);
        }

        return ticket;
    }

    private TicketResponse fromEntityToResponse(Ticket ticket){
        return TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .priority(ticket.getPriority())
                .category(ticket.getCategory())
                .status(ticket.getStatus())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .createdById(ticket.getCreatedBy().getId())
                .assignedToId(getAssignedToId(ticket.getAssignedTo()))
                .comments(getCommentRequests(ticket.getComments()))
                .build();
    }

    private Long getAssignedToId(User assignedTo) {
        return Optional.ofNullable(assignedTo)
                .map(User::getId)
                .orElse(null); // or use a default value like -1L
    }

    private List<CommentDto> getCommentRequests(List<Comment> comments) {
        return Optional.ofNullable(comments)
                .map(commentList -> commentList.stream()
                        .map(this::fromEntityToDtoComment) // Map each comment to DTO
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList()); // Return empty list instead of null
    }

    private CommentDto fromEntityToDtoComment(Comment comment){
        return CommentDto.builder()
                .content(comment.getContent())
                .createdById(comment.getCreatedBy().getId())
                .build();
    }

}

package morocco.it.TicketSystem.services.impl;

import lombok.RequiredArgsConstructor;
import morocco.it.TicketSystem.dto.AuditLogRequest;
import morocco.it.TicketSystem.dto.CommentRequest;
import morocco.it.TicketSystem.dto.TicketRequest;
import morocco.it.TicketSystem.dto.TicketResponse;
import morocco.it.TicketSystem.entities.Comment;
import morocco.it.TicketSystem.entities.Ticket;
import morocco.it.TicketSystem.entities.User;
import morocco.it.TicketSystem.entities.enums.Category;
import morocco.it.TicketSystem.entities.enums.Priority;
import morocco.it.TicketSystem.entities.enums.Status;
import morocco.it.TicketSystem.exceptions.ResourceNotFoundException;
import morocco.it.TicketSystem.repositories.CommentRepository;
import morocco.it.TicketSystem.repositories.TicketRepository;
import morocco.it.TicketSystem.services.AuditLogService;
import morocco.it.TicketSystem.services.TicketService;
import morocco.it.TicketSystem.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final AuditLogService auditLogService;

    @Override
    public TicketResponse createTicket(TicketRequest ticketRequest){
        // Saving the ticket
        Ticket ticket = fromRequestToEntity(ticketRequest);
        Ticket createdTicket = ticketRepository.save(ticket);

        // logging the action
        AuditLogRequest auditLogRequest = AuditLogRequest.builder()
                .ticket(createdTicket)
                .userId(createdTicket.getCreatedBy().getId())
                .ticketActionIndex(1) // CREATED
                .build();

        auditLogService.createAuditLog(auditLogRequest);

        // Returning the result
        return fromEntityToResponse(createdTicket);
    }

    @Override
    public TicketResponse changeTicketStatus(TicketRequest ticketRequest) {
        // Get the ticket (throws exception if not found)
        Ticket ticket = getTicketById(ticketRequest.getId());

        // Setting the new status
        Status status = Status.fromIndex(ticketRequest.getStatusIndex());
        ticket.setStatus(status);

        // Saving the updated ticket
        Ticket savedTicket = ticketRepository.save(ticket);

        // logging the action
        AuditLogRequest auditLogRequest = AuditLogRequest.builder()
                .ticket(savedTicket)
                .userId(savedTicket.getCreatedBy().getId())
                .ticketActionIndex(0) // STATUS_CHANGED
                .oldStatusIndex(ticket.getStatus().ordinal())
                .newStatusIndex(ticketRequest.getStatusIndex())
                .build();

        auditLogService.createAuditLog(auditLogRequest);

        // returning the result
        return fromEntityToResponse(savedTicket);
    }

    @Override
    public TicketResponse assignTicket(TicketRequest ticketRequest) {
        return assignTicketHelper(ticketRequest,2); // ASSIGNED
    }

    @Override
    public TicketResponse reassignTicket(TicketRequest ticketRequest) {
        return assignTicketHelper(ticketRequest,3); // REASSIGNED
    }

    private TicketResponse assignTicketHelper(TicketRequest ticketRequest, int ticketActionIndex) {
        // Get the ticket (throws exception if not found)
        Ticket ticket = getTicketById(ticketRequest.getId());

        // Assign the ticket to IT support
        User itSupport = userService.getUserById(ticketRequest.getAssignedToId());
        ticket.setAssignedTo(itSupport);

        // Saving the updated ticket
        Ticket savedTicket = ticketRepository.save(ticket);

        // logging the action
        AuditLogRequest auditLogRequest = AuditLogRequest.builder()
                .ticket(savedTicket)
                .userId(savedTicket.getCreatedBy().getId())
                .ticketActionIndex(ticketActionIndex) // ASSIGNED
                .assignedToUserId(ticketRequest.getAssignedToId())
                .build();

        auditLogService.createAuditLog(auditLogRequest);

        // returning the result
        return fromEntityToResponse(savedTicket);
    }



    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ID: "+id));
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

    @Override
    public List<TicketResponse> getTicketByStatus(int statusIndex) {
        Status status = Status.fromIndex(statusIndex);
        List<Ticket> tickets = ticketRepository.findByStatus(status);
        return tickets.stream()
                .map(this::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    private Ticket fromRequestToEntity(TicketRequest ticketRequest){
        Long createdById = ticketRequest.getCreatedById();
        User user = userService.getUserById(createdById);

        Ticket ticket = Ticket.builder()
                .title(ticketRequest.getTitle())
                .description(ticketRequest.getDescription())
                .priority(Priority.fromIndex(ticketRequest.getPriorityIndex()))
                .category(Category.fromIndex(ticketRequest.getCategoryIndex()))
                .createdBy(user)
                .build();

        int statusIndex = ticketRequest.getStatusIndex();
        if(statusIndex != -1) { // the status is set on the request
            ticket.setStatus(Status.fromIndex(statusIndex));
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
                .assignedToUsername(ticket.getAssignedTo().getUsername())
                .build();
    }

}

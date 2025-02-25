package morocco.it.TicketSystem.services.impl;

import morocco.it.TicketSystem.dto.*;
import morocco.it.TicketSystem.entities.Comment;
import morocco.it.TicketSystem.entities.Ticket;
import morocco.it.TicketSystem.entities.User;
import morocco.it.TicketSystem.entities.enums.Category;
import morocco.it.TicketSystem.entities.enums.Priority;
import morocco.it.TicketSystem.entities.enums.Role;
import morocco.it.TicketSystem.entities.enums.Status;
import morocco.it.TicketSystem.mappers.TicketMapper;
import morocco.it.TicketSystem.repositories.CommentRepository;
import morocco.it.TicketSystem.repositories.TicketRepository;
import morocco.it.TicketSystem.services.AuditLogService;
import morocco.it.TicketSystem.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTicket() {
        // Given
        TicketRequest ticketRequest = new TicketRequest();
        ticketRequest.setTitle("Test Ticket");
        ticketRequest.setDescription("Test Description");
        ticketRequest.setPriority(Priority.MEDIUM);
        ticketRequest.setCategory(Category.SOFTWARE);
        ticketRequest.setCreatedById(1L);

        User user = User.builder()
                .id(1L)
                .username("testUser")
                .role(Role.EMPLOYEE)
                .build();

        Ticket ticket = Ticket.builder()
                .id(1L)
                .title(ticketRequest.getTitle())
                .description(ticketRequest.getDescription())
                .priority(ticketRequest.getPriority())
                .category(ticketRequest.getCategory())
                .createdBy(user)
                .build();

        TicketResponse ticketResponse = TicketResponse.builder()
                .id(1L)
                .title(ticketRequest.getTitle())
                .description(ticketRequest.getDescription())
                .priority(ticketRequest.getPriority())
                .category(ticketRequest.getCategory())
                .build();

        // Mock behavior of dependencies
        when(userService.getUserById(1L)).thenReturn(user);
        when(ticketMapper.fromRequestToEntity(ticketRequest)).thenReturn(ticket);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(ticketMapper.fromEntityToResponse(ticket)).thenReturn(ticketResponse);

        // When
        TicketResponse response = ticketService.createTicket(ticketRequest);

        // Then
        assertNotNull(response);  // Check if response is not null
        assertEquals(1L, response.getId());
        assertEquals("Test Ticket", response.getTitle());
        assertEquals("Test Description", response.getDescription());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(auditLogService, times(1)).createAuditLog(any(AuditLogRequest.class));
    }



    @Test
    void testChangeTicketStatus() {
        // Given
        Long ticketId = 1L;
        TicketRequestUpdate ticketRequestUpdate = new TicketRequestUpdate();
        ticketRequestUpdate.setStatus(Status.IN_PROGRESS);

        User createdBy = User.builder()
                .id(1L)
                .username("employeeUser")
                .role(Role.EMPLOYEE)
                .build();

        Ticket ticket = Ticket.builder()
                .id(ticketId)
                .title("Test Ticket")
                .description("Test Description")
                .status(Status.NEW)
                .createdBy(createdBy)
                .build();

        TicketResponse ticketResponse = TicketResponse.builder()
                .id(ticketId)
                .status(Status.IN_PROGRESS)
                .build();

        // Mock behaviors
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(ticketMapper.fromEntityToResponse(ticket)).thenReturn(ticketResponse);

        // When
        TicketResponse response = ticketService.changeTicketStatus(ticketRequestUpdate, ticketId);

        // Then
        assertNotNull(response);  // Ensure response is not null
        assertEquals(Status.IN_PROGRESS, response.getStatus());  // Check status update
        verify(ticketRepository, times(1)).save(any(Ticket.class));  // Ensure save was called
        verify(auditLogService, times(1)).createAuditLog(any(AuditLogRequest.class));  // Verify audit log creation
    }

    @Test
    void testAssignTicket() {
        // Given
        Long ticketId = 1L;
        TicketRequestUpdate ticketRequestUpdate = new TicketRequestUpdate();
        ticketRequestUpdate.setAssignedToId(2L);

        User createdBy = User.builder()
                .id(1L)
                .username("employeeUser")
                .role(Role.EMPLOYEE)
                .build();

        Ticket ticket = Ticket.builder()
                .id(ticketId)
                .title("Test Ticket")
                .description("Test Description")
                .createdBy(createdBy)
                .build();

        User itSupport = User.builder()
                .id(2L)
                .username("itSupportUser")
                .role(Role.IT_SUPPORT)
                .build();

        TicketResponse ticketResponse = TicketResponse.builder()
                .id(ticketId)
                .assignedToId(2L)  // Ensure the assignedToId is correctly set
                .build();

        // Mock behaviors
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(userService.getUserById(2L)).thenReturn(itSupport);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(ticketMapper.fromEntityToResponse(ticket)).thenReturn(ticketResponse);

        // When
        TicketResponse response = ticketService.assignTicket(ticketRequestUpdate, ticketId);

        // Then
        assertNotNull(response);  // Ensure response is not null
        assertEquals(2L, response.getAssignedToId());  // Ensure assignedToId is correct
        verify(ticketRepository, times(1)).save(any(Ticket.class));  // Ensure save was called
        verify(auditLogService, times(1)).createAuditLog(any(AuditLogRequest.class));  // Verify audit log creation
    }


}
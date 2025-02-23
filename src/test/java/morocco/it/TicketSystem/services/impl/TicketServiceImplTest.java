package morocco.it.TicketSystem.services.impl;

import morocco.it.TicketSystem.dto.*;
import morocco.it.TicketSystem.entities.Comment;
import morocco.it.TicketSystem.entities.Ticket;
import morocco.it.TicketSystem.entities.User;
import morocco.it.TicketSystem.entities.enums.Category;
import morocco.it.TicketSystem.entities.enums.Priority;
import morocco.it.TicketSystem.entities.enums.Role;
import morocco.it.TicketSystem.entities.enums.Status;
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

        when(userService.getUserById(1L)).thenReturn(user);
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket ticket = invocation.getArgument(0);
            ticket.setId(1L); // Simulate saving to the database
            return ticket;
        });

        // When
        TicketResponse response = ticketService.createTicket(ticketRequest);

        // Then
        assertNotNull(response);
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
                .createdBy(createdBy) // Ensure createdBy is set
                .build();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // When
        TicketResponse response = ticketService.changeTicketStatus(ticketRequestUpdate, ticketId);

        // Then
        assertNotNull(response);
        assertEquals(Status.IN_PROGRESS, response.getStatus());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(auditLogService, times(1)).createAuditLog(any(AuditLogRequest.class));
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
                .createdBy(createdBy) // Ensure createdBy is set
                .build();

        User itSupport = User.builder()
                .id(2L)
                .username("itSupportUser")
                .role(Role.IT_SUPPORT)
                .build();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(userService.getUserById(2L)).thenReturn(itSupport);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // When
        TicketResponse response = ticketService.assignTicket(ticketRequestUpdate, ticketId);

        // Then
        assertNotNull(response);
        assertEquals(2L, response.getAssignedToId());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(auditLogService, times(1)).createAuditLog(any(AuditLogRequest.class));
    }

}
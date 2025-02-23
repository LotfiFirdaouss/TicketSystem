package morocco.it.TicketSystem.controllers;

import morocco.it.TicketSystem.dto.*;
import morocco.it.TicketSystem.entities.enums.*;
import morocco.it.TicketSystem.services.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for createTicket
    @Test
    void testCreateTicket() {
        // Given
        TicketRequest ticketRequest = new TicketRequest();
        ticketRequest.setTitle("Test Ticket");
        ticketRequest.setDescription("Test Description");
        ticketRequest.setPriority(Priority.MEDIUM);
        ticketRequest.setCategory(Category.SOFTWARE);
        ticketRequest.setCreatedById(1L);

        TicketResponse ticketResponse = TicketResponse.builder()
                .id(1L)
                .title("Test Ticket")
                .description("Test Description")
                .priority(Priority.MEDIUM)
                .category(Category.SOFTWARE)
                .status(Status.NEW)
                .createdById(1L)
                .createdAt(Instant.now())
                .build();

        when(ticketService.createTicket(ticketRequest)).thenReturn(ticketResponse);

        // When
        ResponseEntity<TicketResponse> response = ticketController.createTicket(ticketRequest);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ticketResponse, response.getBody());
        verify(ticketService, times(1)).createTicket(ticketRequest);
    }

    // Test for updateTicketStatus
    @Test
    void testUpdateTicketStatus() {
        // Given
        Long ticketId = 1L;
        TicketRequestUpdate ticketRequestUpdate = new TicketRequestUpdate();
        ticketRequestUpdate.setStatus(Status.IN_PROGRESS);

        TicketResponse ticketResponse = TicketResponse.builder()
                .id(ticketId)
                .title("Test Ticket")
                .description("Test Description")
                .status(Status.IN_PROGRESS)
                .build();

        when(ticketService.changeTicketStatus(ticketRequestUpdate, ticketId)).thenReturn(ticketResponse);

        // When
        ResponseEntity<TicketResponse> response = ticketController.updateTicketStatus(ticketRequestUpdate, ticketId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(ticketResponse, response.getBody());
        verify(ticketService, times(1)).changeTicketStatus(ticketRequestUpdate, ticketId);
    }

    // Test for assignTicket
    @Test
    void testAssignTicket() {
        // Given
        Long ticketId = 1L;
        TicketRequestUpdate ticketRequestUpdate = new TicketRequestUpdate();
        ticketRequestUpdate.setAssignedToId(2L);

        TicketResponse ticketResponse = TicketResponse.builder()
                .id(ticketId)
                .title("Test Ticket")
                .description("Test Description")
                .assignedToId(2L)
                .build();

        when(ticketService.assignTicket(ticketRequestUpdate, ticketId)).thenReturn(ticketResponse);

        // When
        ResponseEntity<TicketResponse> response = ticketController.assignTicket(ticketRequestUpdate, ticketId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(ticketResponse, response.getBody());
        verify(ticketService, times(1)).assignTicket(ticketRequestUpdate, ticketId);
    }

    // Test for addCommentToTicket
    @Test
    void testAddCommentToTicket() {
        // Given
        Long ticketId = 1L;
        CommentDto commentRequest = new CommentDto();
        commentRequest.setContent("Test Comment");
        commentRequest.setCreatedById(1L);

        TicketResponse ticketResponse = TicketResponse.builder()
                .id(ticketId)
                .title("Test Ticket")
                .description("Test Description")
                .comments(Collections.singletonList(commentRequest))
                .build();

        when(ticketService.addCommentToTicket(commentRequest, ticketId)).thenReturn(ticketResponse);

        // When
        ResponseEntity<TicketResponse> response = ticketController.addCommentToTicket(commentRequest, ticketId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(ticketResponse, response.getBody());
        verify(ticketService, times(1)).addCommentToTicket(commentRequest, ticketId);
    }

    // Test for getAllTickets
    @Test
    void testGetAllTickets() {
        // Given
        TicketResponse ticketResponse = TicketResponse.builder()
                .id(1L)
                .title("Test Ticket")
                .description("Test Description")
                .status(Status.NEW)
                .build();

        List<TicketResponse> ticketResponses = Collections.singletonList(ticketResponse);

        when(ticketService.getAllTickets()).thenReturn(ticketResponses);

        // When
        ResponseEntity<List<TicketResponse>> response = ticketController.getAllTickets();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketResponses, response.getBody());
        verify(ticketService, times(1)).getAllTickets();
    }

    // Test for getTicketByEmployeeId
    @Test
    void testGetTicketByEmployeeId() {
        // Given
        Long employeeId = 1L;
        TicketResponse ticketResponse = TicketResponse.builder()
                .id(1L)
                .title("Test Ticket")
                .description("Test Description")
                .createdById(employeeId)
                .build();

        List<TicketResponse> ticketResponses = Collections.singletonList(ticketResponse);

        when(ticketService.getTicketByEmployeeId(employeeId)).thenReturn(ticketResponses);

        // When
        ResponseEntity<List<TicketResponse>> response = ticketController.getTicketByEmployeeId(employeeId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketResponses, response.getBody());
        verify(ticketService, times(1)).getTicketByEmployeeId(employeeId);
    }

    // Test for getTicketById
    @Test
    void testGetTicketById() {
        // Given
        Long ticketId = 1L;
        TicketResponse ticketResponse = TicketResponse.builder()
                .id(ticketId)
                .title("Test Ticket")
                .description("Test Description")
                .status(Status.NEW)
                .build();

        when(ticketService.getTicketById(ticketId)).thenReturn(ticketResponse);

        // When
        ResponseEntity<TicketResponse> response = ticketController.getTicketById(ticketId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketResponse, response.getBody());
        verify(ticketService, times(1)).getTicketById(ticketId);
    }

    // Test for getTicketByStatus
    @Test
    void testGetTicketByStatus() {
        // Given
        Status status = Status.NEW;
        TicketResponse ticketResponse = TicketResponse.builder()
                .id(1L)
                .title("Test Ticket")
                .description("Test Description")
                .status(status)
                .build();

        List<TicketResponse> ticketResponses = Collections.singletonList(ticketResponse);

        when(ticketService.getTicketByStatus(status)).thenReturn(ticketResponses);

        // When
        ResponseEntity<List<TicketResponse>> response = ticketController.getTicketByStatus(status);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketResponses, response.getBody());
        verify(ticketService, times(1)).getTicketByStatus(status);
    }

}

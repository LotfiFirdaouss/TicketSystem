package morocco.it.TicketSystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import morocco.it.TicketSystem.dto.*;
import morocco.it.TicketSystem.entities.enums.*;
import morocco.it.TicketSystem.security.JwtUtil;
import morocco.it.TicketSystem.services.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TicketControllerIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUpAuthentication() {
        // By default, set the authenticated user to EMPLOYEE
        switchToEmployeeUser();
    }

    private void switchToEmployeeUser() {
        UserDetails employeeUser = User.withUsername("employeeUser")
                .password("password")
                .authorities(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(employeeUser, null, employeeUser.getAuthorities())
        );
    }

    private void switchToItSupportUser() {
        UserDetails itSupportUser = User.withUsername("itSupportUser")
                .password("password")
                .authorities(new SimpleGrantedAuthority("ROLE_IT_SUPPORT"))
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(itSupportUser, null, itSupportUser.getAuthorities())
        );
    }

    @Test
    void testCreateTicketAsEmployee() throws Exception {
        // Generate a JWT token for an EMPLOYEE user
        String jwtToken = jwtUtil.generateToken("employeeUser", "EMPLOYEE");

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

        when(ticketService.createTicket(any(TicketRequest.class))).thenReturn(ticketResponse);

        // When & Then
        mockMvc.perform(post("/tickets/create-ticket")
                        .header("Authorization", "Bearer " + jwtToken) // Add JWT token to headers
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Ticket"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    void testGetAllTicketsAsItSupport() throws Exception {
        // Generate a JWT token for an IT_SUPPORT user
        String jwtToken = jwtUtil.generateToken("itSupportUser", "IT_SUPPORT");

        // Given
        TicketResponse ticketResponse = TicketResponse.builder()
                .id(1L)
                .title("Test Ticket")
                .description("Test Description")
                .status(Status.NEW)
                .build();

        List<TicketResponse> ticketResponses = Collections.singletonList(ticketResponse);

        when(ticketService.getAllTickets()).thenReturn(ticketResponses);

        // When & Then
        mockMvc.perform(get("/tickets/all")
                        .header("Authorization", "Bearer " + jwtToken)) // Add JWT token to headers
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Ticket"))
                .andExpect(jsonPath("$[0].description").value("Test Description"));
    }


    @Test
    void testGetAllTicketsAsEmployee() throws Exception {
        // Switch to EMPLOYEE user
        switchToEmployeeUser();

        // Log the authenticated user and roles
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Authenticated user: " + authentication.getName());
        logger.info("User roles: " + authentication.getAuthorities());

        // When & Then
        mockMvc.perform(get("/tickets/all"))
                .andExpect(status().isForbidden()); // EMPLOYEE should not have access
    }

    @Test
    void testCreateTicketAsItSupport() throws Exception {
        // Switch to IT_SUPPORT user
        switchToItSupportUser();

        // Given
        TicketRequest ticketRequest = new TicketRequest();
        ticketRequest.setTitle("Test Ticket");
        ticketRequest.setDescription("Test Description");
        ticketRequest.setPriority(Priority.MEDIUM);
        ticketRequest.setCategory(Category.SOFTWARE);
        ticketRequest.setCreatedById(1L);

        // When & Then
        mockMvc.perform(post("/tickets/create-ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketRequest)))
                .andExpect(status().isForbidden()); // IT_SUPPORT should not have access
    }
}
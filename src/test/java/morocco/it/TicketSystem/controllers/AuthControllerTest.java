package morocco.it.TicketSystem.controllers;

import morocco.it.TicketSystem.dto.LoginRequest;
import morocco.it.TicketSystem.dto.RegisterRequest;
import morocco.it.TicketSystem.entities.enums.Role;
import morocco.it.TicketSystem.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testUser");
        registerRequest.setPassword("testPassword");
        registerRequest.setRole(Role.EMPLOYEE); // Include the role

        when(authService.register(registerRequest)).thenReturn("User registered successfully");

        // When
        String response = authController.register(registerRequest);

        // Then
        assertEquals("User registered successfully", response);
        verify(authService, times(1)).register(registerRequest);
    }

    @Test
    void testLogin() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        when(authService.login(loginRequest)).thenReturn("Login successful");

        // When
        String response = authController.login(loginRequest);

        // Then
        assertEquals("Login successful", response);
        verify(authService, times(1)).login(loginRequest);
    }

    @Test
    void testLogout() {
        // Given
        String token = "Bearer testToken";

        doNothing().when(authService).logout(token);

        // When
        String response = authController.logout(token);

        // Then
        assertEquals("Logged out successfully", response);
        verify(authService, times(1)).logout(token);
    }

}

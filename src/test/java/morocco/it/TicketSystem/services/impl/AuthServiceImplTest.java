package morocco.it.TicketSystem.services.impl;

import morocco.it.TicketSystem.dto.LoginRequest;
import morocco.it.TicketSystem.dto.RegisterRequest;
import morocco.it.TicketSystem.entities.User;
import morocco.it.TicketSystem.entities.enums.Role;
import morocco.it.TicketSystem.repositories.UserRepository;
import morocco.it.TicketSystem.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

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
        registerRequest.setRole(Role.EMPLOYEE);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");

        // When
        String response = authService.register(registerRequest);

        // Then
        assertEquals("User registered successfully", response);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testUser");
        registerRequest.setPassword("testPassword");
        registerRequest.setRole(Role.EMPLOYEE);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(new User()));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(registerRequest);
        });
        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void testLogin() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        User user = User.builder()
                .username("testUser")
                .password("encodedPassword")
                .role(Role.EMPLOYEE)
                .build();

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("testPassword", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("testUser", "EMPLOYEE")).thenReturn("jwtToken");

        // When
        String response = authService.login(loginRequest);

        // Then
        assertEquals("jwtToken", response);
    }

    @Test
    void testLogin_UserNotFound() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testLogin_InvalidCredentials() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        User user = User.builder()
                .username("testUser")
                .password("encodedPassword")
                .role(Role.EMPLOYEE)
                .build();

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("testPassword", "encodedPassword")).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void testLogout() {
        // Given
        String token = "jwtToken";

        // When
        authService.logout(token);

        // Then
        // No exception is thrown, and the method completes successfully
    }
}
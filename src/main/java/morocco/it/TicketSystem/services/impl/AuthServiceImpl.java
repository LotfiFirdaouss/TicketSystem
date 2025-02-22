package morocco.it.TicketSystem.services.impl;

import lombok.AllArgsConstructor;
import morocco.it.TicketSystem.dto.LoginRequest;
import morocco.it.TicketSystem.dto.RegisterRequest;
import morocco.it.TicketSystem.entities.User;
import morocco.it.TicketSystem.repositories.UserRepository;
import morocco.it.TicketSystem.security.JwtUtil;
import morocco.it.TicketSystem.services.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String register(RegisterRequest registerRequest) {
        // Check if the user already exists
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Create a new user
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();

        // Save the user
        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public String login(LoginRequest loginRequest) {
        // Find the user
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify the password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate a JWT token
        return jwtUtil.generateToken(user.getUsername(), user.getRole().name());
    }

    @Override
    public void logout(String token) {
        // Invalidate the token (optional, since JWTs are stateless)
        // You can implement a token blacklist if needed
    }
}

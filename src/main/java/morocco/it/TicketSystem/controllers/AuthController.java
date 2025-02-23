package morocco.it.TicketSystem.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import morocco.it.TicketSystem.dto.LoginRequest;
import morocco.it.TicketSystem.dto.RegisterRequest;
import morocco.it.TicketSystem.services.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Authentication Controller", description = "APIs for managing user authentication")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user", description = "Creates a new user account in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }


    @Operation(summary = "User login", description = "Authenticates a user and returns a token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }


    @Operation(summary = "User logout", description = "Logs out the user and invalidates the token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged out successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping("/logout")
    public String logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return "Logged out successfully";
    }
}

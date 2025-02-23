package morocco.it.TicketSystem.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class) // Add JWT filter
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // Allow access to H2 console
                        .requestMatchers("/auth/**").permitAll() // Allow authentication endpoints
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll() // Allow Swagger resources

                        // EMPLOYEE-ONLY Endpoints
                        .requestMatchers("/tickets/createTicket").hasRole("EMPLOYEE")
                        .requestMatchers("/tickets/by-employee-id/*").hasRole("EMPLOYEE")

                        // IT_SUPPORT-ONLY Endpoints
                        .requestMatchers("/tickets/all").hasRole("IT_SUPPORT")
                        .requestMatchers("/tickets/by-status/*").hasRole("IT_SUPPORT")
                        .requestMatchers("/tickets/status-update/*").hasRole("IT_SUPPORT")
                        .requestMatchers("/tickets/assign-update/*").hasRole("IT_SUPPORT")
                        .requestMatchers("/tickets/comment-update/*").hasRole("IT_SUPPORT")

                        // General ticket access (only authenticated users)
                        .requestMatchers("/tickets/**").authenticated()
                )
                .headers(headers -> headers // Allow frames for H2 console
                        .frameOptions(frameOptions -> frameOptions.disable())
                );

        return http.build();
    }
}

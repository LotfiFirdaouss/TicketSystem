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
                        .requestMatchers("/h2-console/**").permitAll() // Allow access to H2 console without authentication
                        .requestMatchers("/auth/**").permitAll() // Allow access to /auth/** without authentication
                        .requestMatchers("/employee/**").hasRole("EMPLOYEE") // Restrict /employee/** to EMPLOYEE role
                        .requestMatchers("/it-support/**").hasRole("IT_SUPPORT") // Restrict /it-support/** to IT_SUPPORT role
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .headers(headers -> headers // Allow frames for H2 console
                        .frameOptions(frameOptions -> frameOptions.disable())
                );

        return http.build();
    }
}

package morocco.it.TicketSystem.entities;

import jakarta.persistence.*;
import morocco.it.TicketSystem.entities.enums.Role;

import java.time.Instant;

@Entity
@Table(name= "users")
@SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
}

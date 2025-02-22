package morocco.it.TicketSystem.entities;

import jakarta.persistence.*;
import lombok.*;
import morocco.it.TicketSystem.entities.enums.Role;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name= "users")
@SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
@Data
@Builder
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
    private Instant createdAt;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> createdTickets;

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> assignedTickets;

    @OneToMany(mappedBy = "user")
    private List<AuditLog> auditLogs;

    @OneToMany(mappedBy = "createdBy")
    List<Comment> comments;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now(); // set the timestamp only if it's not already set
        }
    }
}

package morocco.it.TicketSystem.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "audit_logs")
@SequenceGenerator(name = "audit_seq", sequenceName = "audit_seq", allocationSize = 1)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_seq")
    private Long id;

    private String action;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private Instant timestamp;

    @PrePersist
    public void prePersist() {
        if (timestamp == null) {
            timestamp = Instant.now(); // set the timestamp only if it's not already set
        }
    }
}

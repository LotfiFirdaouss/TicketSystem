package morocco.it.TicketSystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import morocco.it.TicketSystem.entities.enums.Category;
import morocco.it.TicketSystem.entities.enums.Priority;
import morocco.it.TicketSystem.entities.enums.Status;

import java.time.Instant;

@Entity
@Table(name = "tickets")
@SequenceGenerator(name = "ticket_seq", sequenceName = "ticket_seq", allocationSize = 1)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_seq")
    private Long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.NEW;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to", nullable = false)
    private User assignedTo;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now(); // Update the updated_at field
    }
}

package morocco.it.TicketSystem.entities;

import jakarta.persistence.*;
import lombok.*;
import morocco.it.TicketSystem.entities.enums.Category;
import morocco.it.TicketSystem.entities.enums.Priority;
import morocco.it.TicketSystem.entities.enums.Status;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "tickets")
@SequenceGenerator(name = "ticket_seq", sequenceName = "ticket_seq", allocationSize = 1)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY)
    private List<AuditLog> auditLogs;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now(); // set the timestamp only if it's not already set
        }
        if(status == null){
            status = Status.NEW;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now(); // Update the updated_at field
    }

}

package morocco.it.TicketSystem.dto;

import lombok.Builder;
import lombok.Data;
import morocco.it.TicketSystem.entities.enums.Category;
import morocco.it.TicketSystem.entities.enums.Priority;
import morocco.it.TicketSystem.entities.enums.Status;

import java.time.Instant;

@Data
@Builder
public class TicketResponse {
    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Category category;
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;
    private Long createdById;
    private String assignedToUsername;
}

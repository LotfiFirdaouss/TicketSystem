package morocco.it.TicketSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import morocco.it.TicketSystem.entities.enums.Category;
import morocco.it.TicketSystem.entities.enums.Priority;
import morocco.it.TicketSystem.entities.enums.Status;

@Data
@Schema(description = "Represents a request to update an existing ticket")
public class TicketRequestUpdate {
    private String title;
    private String description;
    private Priority priority;
    private Category category;
    private Status status;
    private Long assignedToId;
}

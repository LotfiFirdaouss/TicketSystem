package morocco.it.TicketSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import morocco.it.TicketSystem.entities.enums.Category;
import morocco.it.TicketSystem.entities.enums.Priority;
import morocco.it.TicketSystem.entities.enums.Status;

@Data
@Schema(description = "Represents a request to update an existing ticket")
public class TicketRequestUpdate {

    @Schema(description = "The updated title of the ticket", example = "Fix login issue")
    private String title;

    @Schema(description = "The updated description of the ticket", example = "Users cannot log in to the system.")
    private String description;

    @Schema(description = "The updated priority of the ticket", example = "HIGH")
    private Priority priority;

    @Schema(description = "The updated category of the ticket", example = "BUG")
    private Category category;

    @Schema(description = "The updated status of the ticket", example = "IN_PROGRESS")
    private Status status;

    @Schema(description = "The ID of the user to whom the ticket is assigned", example = "2")
    private Long assignedToId;
}
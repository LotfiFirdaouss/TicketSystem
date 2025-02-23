package morocco.it.TicketSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import morocco.it.TicketSystem.entities.enums.Category;
import morocco.it.TicketSystem.entities.enums.Priority;
import morocco.it.TicketSystem.entities.enums.Status;

@Data
@Schema(description = "Represents a request to create a new ticket")
public class TicketRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    @Schema(description = "The title of the ticket", example = "Fix login issue", required = true)
    private String title;

    @NotBlank(message = "Description is required")
    @Schema(description = "The description of the ticket", example = "Users cannot log in to the system.", required = true)
    private String description;

    @Min(value = 0, message = "Priority is required")
    @Schema(description = "The priority of the ticket", example = "HIGH", required = true)
    private Priority priority;

    @Min(value = 0, message = "Category is required")
    @Schema(description = "The category of the ticket", example = "BUG", required = true)
    private Category category;

    @Schema(description = "The status of the ticket", example = "NEW")
    private Status status = Status.NEW; // Optional with default value

    @NotNull(message = "CreatedById is required")
    @Schema(description = "The ID of the user who created the ticket", example = "1", required = true)
    private Long createdById;

    @Schema(description = "The ID of the user to whom the ticket is assigned", example = "2")
    private Long assignedToId; // Optional
}

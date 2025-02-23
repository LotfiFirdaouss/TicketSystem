package morocco.it.TicketSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import morocco.it.TicketSystem.entities.enums.Category;
import morocco.it.TicketSystem.entities.enums.Priority;
import morocco.it.TicketSystem.entities.enums.Status;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@Schema(description = "Represents the response for a ticket")
public class TicketResponse {

    @Schema(description = "The ID of the ticket", example = "1")
    private Long id;

    @Schema(description = "The title of the ticket", example = "Fix login issue")
    private String title;

    @Schema(description = "The description of the ticket", example = "Users cannot log in to the system.")
    private String description;

    @Schema(description = "The priority of the ticket", example = "HIGH")
    private Priority priority;

    @Schema(description = "The category of the ticket", example = "BUG")
    private Category category;

    @Schema(description = "The status of the ticket", example = "OPEN")
    private Status status;

    @Schema(description = "The timestamp when the ticket was created", example = "2023-10-01T12:00:00Z")
    private Instant createdAt;

    @Schema(description = "The timestamp when the ticket was last updated", example = "2023-10-01T12:30:00Z")
    private Instant updatedAt;

    @Schema(description = "The ID of the user who created the ticket", example = "1")
    private Long createdById;

    @Schema(description = "The ID of the user to whom the ticket is assigned", example = "2")
    private Long assignedToId;

    @Schema(description = "List of comments associated with the ticket")
    private List<CommentDto> comments;
}
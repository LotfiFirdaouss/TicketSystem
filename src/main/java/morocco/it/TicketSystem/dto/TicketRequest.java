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
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 0, message = "Priority is required")
    private Priority priority;

    @Min(value = 0, message = "Category is required")
    private Category category;

    private Status status = Status.NEW; // Optional with default value

    @NotNull(message = "CreatedById is required")
    private Long createdById;

    private Long assignedToId; // Optional
}

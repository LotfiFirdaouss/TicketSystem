package morocco.it.TicketSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Represents a request to add a new comment to an existing ticket")
public class CommentDto {

    @Schema(description = "The content of the comment", example = "This is a sample comment.", required = true)
    private String content;

    @Schema(description = "The ID of the user who created the comment", example = "1", required = true)
    private Long createdById;
}

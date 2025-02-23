package morocco.it.TicketSystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Represents a request to add a new comment to an existing ticket")
public class CommentDto {
    private String content;
    private Long createdById;
}

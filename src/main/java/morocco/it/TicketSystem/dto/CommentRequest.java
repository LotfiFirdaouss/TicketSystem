package morocco.it.TicketSystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentRequest {
    private String content;
    private Long ticketId;
    private Long createdById;
}

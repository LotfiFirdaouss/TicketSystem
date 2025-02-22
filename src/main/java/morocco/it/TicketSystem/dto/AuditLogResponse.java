package morocco.it.TicketSystem.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class AuditLogResponse {
    private String action;
    private Long ticketId;
    private Long userId;
    private Instant timestamp;
}

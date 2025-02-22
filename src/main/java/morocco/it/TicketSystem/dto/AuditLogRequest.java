package morocco.it.TicketSystem.dto;

import lombok.Builder;
import lombok.Data;
import morocco.it.TicketSystem.entities.Ticket;

@Data
@Builder
public class AuditLogRequest {
    private Ticket ticket; // on which ticket
    private Long userId; // who perfmors the action

    // to build the action string we need :
    private int ticketActionIndex;
    private int oldStatusIndex;
    private int newStatusIndex;
    private Long assignedToUserId;
    private String comment;

}

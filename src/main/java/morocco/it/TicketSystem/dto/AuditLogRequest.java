package morocco.it.TicketSystem.dto;

import lombok.Builder;
import lombok.Data;
import morocco.it.TicketSystem.entities.Ticket;
import morocco.it.TicketSystem.entities.enums.Status;
import morocco.it.TicketSystem.entities.enums.TicketAction;

@Data
@Builder
public class AuditLogRequest {
    private Ticket ticket; // on which ticket
    private Long userId; // who perfmors the action

    // to build the action string we need :
    private TicketAction ticketAction;
    private Status oldStatus;
    private Status newStatus;
    private Long assignedToUserId;
    private String comment;

}

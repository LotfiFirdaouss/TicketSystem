package morocco.it.TicketSystem.dto;

import lombok.Data;

@Data
public class TicketRequest {
    private Long id;
    private String title;
    private String description;
    private int priorityIndex;
    private int categoryIndex;
    private int statusIndex;
    private Long createdById; // user performing the action
    private Long assignedToId; // user to whom the ticked was assigned
}

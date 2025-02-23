package morocco.it.TicketSystem.dto;

import lombok.Data;
import morocco.it.TicketSystem.entities.enums.Category;
import morocco.it.TicketSystem.entities.enums.Priority;
import morocco.it.TicketSystem.entities.enums.Status;

@Data
public class TicketRequestUpdate {
    private String title;
    private String description;
    private Priority priority;
    private Category category;
    private Status status;
    //private Long createdById; // will never change
    private Long assignedToId;
}

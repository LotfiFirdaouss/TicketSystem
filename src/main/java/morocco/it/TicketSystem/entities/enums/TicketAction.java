package morocco.it.TicketSystem.entities.enums;

public enum TicketAction {
    STATUS_CHANGED, // 0
    CREATED, // 1
    ASSIGNED, // 2
    REASSIGNED, // 3
    COMMENT_ADDED; // 4

    public static String getActionMessage(TicketAction action, String... details) {
        switch (action) {
            case STATUS_CHANGED:
                return "Status changed from " + details[0] + " to " + details[1];
            case CREATED:
                return "Ticket created";
            case ASSIGNED:
                return "Ticket assigned to it support with username : " + details[0];
            case REASSIGNED:
                return "Ticket reassigned to it support with username : " + details[0];
            case COMMENT_ADDED:
                return "Commment added to ticket : " + details[0];
            default:
                return "Action performed";
        }
    }
}

package morocco.it.TicketSystem.entities.enums;

public enum Priority {
    LOW, // 0
    MEDIUM, // 1
    HIGH; // 2

    public static Priority fromIndex(int index) {
        Priority[] values = Priority.values();
        if (index >= 0 && index < values.length) {
            return values[index];
        }
        throw new IllegalArgumentException("Invalid index: " + index);
    }
}

package morocco.it.TicketSystem.entities.enums;

public enum Status {
    NEW, // 0
    IN_PROGRESS, // 1
    RESOLVED; // 2

    public static Status fromIndex(int index) {
        Status[] values = Status.values();
        if (index >= 0 && index < values.length) {
            return values[index];
        }
        throw new IllegalArgumentException("Invalid index: " + index);
    }
}

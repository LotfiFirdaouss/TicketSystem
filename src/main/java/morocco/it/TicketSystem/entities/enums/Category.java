package morocco.it.TicketSystem.entities.enums;

public enum Category {
    NETWORK, // 0
    HARDWARE, // 1
    SOFTWARE, // 2
    OTHER; // 3

    public static Category fromIndex(int index) {
        Category[] values = Category.values();
        if (index >= 0 && index < values.length) {
            return values[index];
        }
        throw new IllegalArgumentException("Invalid index: " + index);
    }
}

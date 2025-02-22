package morocco.it.TicketSystem.entities.enums;

public enum Role {
    EMPLOYEE, // 0
    IT_SUPPORT; // 1

    public static Role fromIndex(int index) {
        Role[] values = Role.values();
        if (index >= 0 && index < values.length) {
            return values[index];
        }
        throw new IllegalArgumentException("Invalid index: " + index);
    }
}

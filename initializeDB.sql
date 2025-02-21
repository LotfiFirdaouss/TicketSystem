CREATE TABLE users (
    id            NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username      VARCHAR2(100) UNIQUE NOT NULL,
    password      VARCHAR2(255) NOT NULL, -- Store hashed passwords
    role          VARCHAR2(20) CHECK (LOWER(role) IN ('employee', 'it_support')) NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tickets (
    id             NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title          VARCHAR2(255) NOT NULL,
    description    CLOB NOT NULL,
    priority       VARCHAR2(10) CHECK (LOWER(priority) IN ('low', 'medium', 'high')) NOT NULL,
    category       VARCHAR2(20) CHECK (LOWER(category) IN ('network', 'hardware', 'software', 'other')) NOT NULL,
    status         VARCHAR2(20) CHECK (LOWER(status) IN ('new', 'in_progress', 'resolved')) DEFAULT 'New',
    created_by     NUMBER NOT NULL, -- Employee who created the ticket
    assigned_to    NUMBER, -- IT support handling the ticket (can be NULL initially)
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_to) REFERENCES users(id) ON DELETE SET NULL
);

-- Trigger to update 'updated_at' field
CREATE OR REPLACE TRIGGER update_ticket_timestamp
BEFORE UPDATE ON tickets
FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE TABLE audit_logs (
    id            NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ticket_id     NUMBER NOT NULL,
    user_id       NUMBER NOT NULL, -- Who made the change
    action        VARCHAR2(255) NOT NULL, -- e.g., "Status changed to In Progress"
    timestamp     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes on foreign keys
CREATE INDEX idx_tickets_created_by ON tickets (created_by);
CREATE INDEX idx_tickets_assigned_to ON tickets (assigned_to);
CREATE INDEX idx_audit_ticket_id ON audit_logs (ticket_id);
CREATE INDEX idx_audit_user_id ON audit_logs (user_id);
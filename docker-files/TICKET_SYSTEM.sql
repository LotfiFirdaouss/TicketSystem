-- Create the ticket_system user
CREATE USER ticket_system IDENTIFIED BY ticket_password_123;
GRANT CONNECT, RESOURCE TO ticket_system;
ALTER USER ticket_system QUOTA UNLIMITED ON USERS;

-- Switch to the ticket_system user
ALTER SESSION SET CURRENT_SCHEMA = ticket_system;

-- Create sequences for ID generation
CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE ticket_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE comment_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE audit_seq START WITH 1 INCREMENT BY 1;

-- Create the users table
CREATE TABLE users (
    id NUMBER DEFAULT user_seq.NEXTVAL PRIMARY KEY,
    username VARCHAR2(255) UNIQUE NOT NULL,
    password VARCHAR2(255) NOT NULL,
    role VARCHAR2(50) NOT NULL CHECK (role IN ('EMPLOYEE', 'IT_SUPPORT')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tickets (
    id NUMBER DEFAULT ticket_seq.NEXTVAL PRIMARY KEY,
    title VARCHAR2(255) NOT NULL,
    description VARCHAR2(255 CHAR),
    priority VARCHAR2(50) NOT NULL CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    category VARCHAR2(50) NOT NULL CHECK (category IN ('NETWORK', 'HARDWARE', 'SOFTWARE', 'OTHER')),
    status VARCHAR2(50) DEFAULT 'NEW' CHECK (status IN ('NEW', 'IN_PROGRESS', 'RESOLVED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    created_by NUMBER NOT NULL REFERENCES users(id),
    assigned_to NUMBER REFERENCES users(id)
);

CREATE TABLE comments (
    id NUMBER DEFAULT comment_seq.NEXTVAL PRIMARY KEY,
    content VARCHAR2(255 CHAR) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ticket_id NUMBER NOT NULL REFERENCES tickets(id),
    user_id NUMBER NOT NULL REFERENCES users(id)
);

-- Create the audit_logs table
CREATE TABLE audit_logs (
    id NUMBER DEFAULT audit_seq.NEXTVAL PRIMARY KEY,
    action VARCHAR2(255) NOT NULL, -- Allow any string value
    ticket_id NUMBER NOT NULL REFERENCES tickets(id),
    user_id NUMBER NOT NULL REFERENCES users(id),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

# **Ticket System API**

This is a backend application for managing tickets, users, comments, and audit logs. It is built using **Spring Boot** and uses an **Oracle Database** for data storage. The application is containerized using **Docker** and **Docker Compose** for easy setup and deployment.

---

## **Features**
- **User Management**: Register, login, and manage users.
- **Ticket Management**: Create, update, and resolve tickets.
- **Comments**: Add comments to tickets.
- **Audit Logs**: Track actions performed on tickets.
- **Swagger UI**: Interactive API documentation.

---

## **Prerequisites**
Before running the application, ensure you have the following installed:
- **Docker**: [Install Docker](https://docs.docker.com/get-docker/)
- **Docker Compose**: [Install Docker Compose](https://docs.docker.com/compose/install/)
- **Java 17**: [Install Java](https://openjdk.org/projects/jdk/17/) (optional, for local development)

---

## **Getting Started**

### **1. Clone the Repository**
```bash
git clone https://github.com/LotfiFirdaouss/TicketSystem.git
cd TicketSystem
```

### **2. Build and Run the Application**
The application and database are containerized using Docker Compose. Run the following command to start everything:

```bash
docker-compose up --build
```

This will:
1. Build the Docker images for the Oracle database and the Spring Boot application.
2. Start the Oracle database container and initialize it with the `TICKET_SYSTEM` schema and tables.
3. Start the Spring Boot application container.

---

### **3. Access the Application**
- **Swagger UI**: Open `http://localhost:8080/TS-api/swagger-ui/index.html` in your browser to interact with the API.
- **Database**: Connect to the Oracle database using the following credentials:
  - **Host**: `localhost`
  - **Port**: `1521`
  - **Database/SID**: `XEPDB1`
  - **Username**: `SYSTEM`
  - **Password**: `ts_password_123`

---

## **Project Structure**
```
TicketSystem/
├── src/                  # Spring Boot application source code
├── database-startup/     # SQL scripts for database initialization
│   └── TICKET_SYSTEM.sql
├── Dockerfile            # Dockerfile for building the Spring Boot application
├── docker-compose.yml    # Docker Compose configuration
├── pom.xml               # Maven build configuration
└── README.md             # Project documentation
```

---

## **Configuration**
### **Database Connection**
The application connects to the Oracle database using the following credentials:
- **Username**: `SYSTEM`
- **Password**: `ts_password_123`

These credentials are hardcoded in the `application.properties` file.

### **Database Initialization**
The `TICKET_SYSTEM.sql` script is automatically executed when the Oracle database container starts. It creates the `TICKET_SYSTEM` user, schema, and tables.

---

## **API Endpoints**
The following endpoints are available:

### **Authentication Controller**
- **POST `/auth/register`**: Register a new user.
- **POST `/auth/login`**: User login.
- **POST `/auth/logout`**: User logout.

### **Ticket Controller**
- **POST `/tickets/create-ticket`**: Create a new ticket.
- **PUT `/tickets/status-update/{ticketId}`**: Update the ticket status.
- **PUT `/tickets/comment-update/{ticketId}`**: Add a comment to a ticket.
- **PUT `/tickets/assign-update/{ticketId}`**: Assign a ticket to IT support.
- **GET `/tickets/{ticketId}`**: Get a ticket by ID.
- **GET `/tickets/by-status/{status}`**: Get tickets by status.
- **GET `/tickets/by-employee-id/{employeeId}`**: Get tickets by employee ID.
- **GET `/tickets/all`**: Get all tickets.

For detailed API documentation, use the **Swagger UI** at `http://localhost:8080/TS-api/swagger-ui/index.html`.

---

## **Troubleshooting**
### **Common Issues**
1. **Database Not Initialized**:
   - Ensure the `TICKET_SYSTEM.sql` script is correct and mounted properly in the `docker-compose.yml` file.
   - Check the Oracle DB logs for errors:
     ```bash
     docker logs oracle-db
     ```

2. **Application Fails to Start**:
   - Check the Spring Boot application logs:
     ```bash
     docker logs ts-api
     ```

3. **Port Conflicts**:
   - Ensure port `8080` (Spring Boot) and `1521` (Oracle DB) are not in use by other applications.

---

## **Contributing**
Contributions are welcome! If you'd like to contribute, please follow these steps:
1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Submit a pull request with a detailed description of your changes.

---

## **License**
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## **Acknowledgments**
- Thanks to [gvenzl/oracle-xe](https://hub.docker.com/r/gvenzl/oracle-xe) for the Oracle database Docker image.
- Thanks to the Spring Boot and Docker communities for their excellent documentation and tools.

---

## **Contact**
If you have any questions or feedback, feel free to reach out:
- **Email**: [lotf.firdaouss@gmail.com](mailto:lotf.firdaouss@gmail.com)
- **GitHub**: [LotfiFirdaouss](https://github.com/LotfiFirdaouss)

---

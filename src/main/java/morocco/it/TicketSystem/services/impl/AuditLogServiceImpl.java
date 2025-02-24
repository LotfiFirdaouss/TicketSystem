package morocco.it.TicketSystem.services.impl;

import lombok.AllArgsConstructor;
import morocco.it.TicketSystem.dto.AuditLogRequest;
import morocco.it.TicketSystem.dto.AuditLogResponse;
import morocco.it.TicketSystem.entities.AuditLog;
import morocco.it.TicketSystem.entities.Ticket;
import morocco.it.TicketSystem.entities.User;
import morocco.it.TicketSystem.entities.enums.Status;
import morocco.it.TicketSystem.entities.enums.TicketAction;
import morocco.it.TicketSystem.repositories.AuditLogRepository;
import morocco.it.TicketSystem.services.AuditLogService;
import morocco.it.TicketSystem.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserService userService;

    @Override
    public AuditLogResponse createAuditLog(AuditLogRequest auditLogRequest) {
        AuditLog auditLog = fromRequestToEntity(auditLogRequest);
        AuditLog savedAuditLog = auditLogRepository.save(auditLog);
        return fromEntityToResponse(savedAuditLog);
    }

    @Override
    public List<AuditLogResponse> getAllAuditLogs() {
        List<AuditLog> auditLogs = auditLogRepository.findAll();
        return auditLogs.stream()
                .map(this::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    private AuditLog fromRequestToEntity(AuditLogRequest auditLogRequest){
        // Getting the appropriate ticket
        Ticket ticket = auditLogRequest.getTicket();

        // Getting the appropriate user (who performs the action)
        User user = userService.getUserById(auditLogRequest.getUserId());

        // Building the action string
        TicketAction ticketAction = auditLogRequest.getTicketAction();
        String action = "";
        switch (ticketAction) {
            case STATUS_CHANGED -> {
                Status oldStatus = auditLogRequest.getOldStatus();
                Status newStatus = auditLogRequest.getNewStatus();
                action = TicketAction.getActionMessage(ticketAction, oldStatus.name(), newStatus.name());
            }
            case ASSIGNED, REASSIGNED -> {
                User assignedToUser = userService.getUserById(auditLogRequest.getAssignedToUserId());
                String assignedToUsername = assignedToUser.getUsername().toUpperCase();
                action = TicketAction.getActionMessage(ticketAction, assignedToUsername);
            }
            case COMMENT_ADDED -> {
                String comment = auditLogRequest.getComment();
                action = TicketAction.getActionMessage(ticketAction, comment);
            }
            default -> action = TicketAction.getActionMessage(ticketAction);
        }

        // Building the audit log Object
        return AuditLog.builder()
                .action(action)
                .ticket(ticket)
                .user(user)
                .build();
    }

    private AuditLogResponse fromEntityToResponse(AuditLog auditLog){
        return AuditLogResponse.builder()
                .action(auditLog.getAction())
                .ticketId(auditLog.getTicket().getId())
                .userId(auditLog.getUser().getId())
                .build();
    }
}

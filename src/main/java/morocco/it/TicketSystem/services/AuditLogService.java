package morocco.it.TicketSystem.services;

import morocco.it.TicketSystem.dto.AuditLogRequest;
import morocco.it.TicketSystem.dto.AuditLogResponse;

import java.util.List;

public interface AuditLogService {
    AuditLogResponse createAuditLog(AuditLogRequest auditLogRequest);
    List<AuditLogResponse> getAllAuditLogs();
}

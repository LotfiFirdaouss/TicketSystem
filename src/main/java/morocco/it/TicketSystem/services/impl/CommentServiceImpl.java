package morocco.it.TicketSystem.services.impl;

import lombok.AllArgsConstructor;
import morocco.it.TicketSystem.dto.AuditLogRequest;
import morocco.it.TicketSystem.dto.CommentRequest;
import morocco.it.TicketSystem.entities.Comment;
import morocco.it.TicketSystem.entities.Ticket;
import morocco.it.TicketSystem.entities.User;
import morocco.it.TicketSystem.repositories.CommentRepository;
import morocco.it.TicketSystem.services.AuditLogService;
import morocco.it.TicketSystem.services.CommentService;
import morocco.it.TicketSystem.services.TicketService;
import morocco.it.TicketSystem.services.UserService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final TicketService ticketService;
    private final UserService userService;
    private final AuditLogService auditLogService;
    private final CommentRepository commentRepository;

    @Override
    public Comment addCommentToTicket(CommentRequest commentRequest) {
        // Get the ticket and user (throws exception if not found)
        Ticket ticket = ticketService.getTicketById(commentRequest.getTicketId());
        User user = userService.getUserById(commentRequest.getCreatedById());

        // map to Comment
        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .ticket(ticket)
                .createdBy(user)
                .build();

        // save comment
        Comment savedComment = commentRepository.save(comment);

        // logging the action
        AuditLogRequest auditLogRequest = AuditLogRequest.builder()
                .ticket(ticket)
                .userId(commentRequest.getCreatedById())
                .ticketActionIndex(4) // COMMENT_ADDED
                .build();

        auditLogService.createAuditLog(auditLogRequest);

        // returning the result
        return savedComment;
    }

}

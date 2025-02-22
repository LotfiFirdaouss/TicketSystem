package morocco.it.TicketSystem.services;

import morocco.it.TicketSystem.dto.CommentRequest;
import morocco.it.TicketSystem.entities.Comment;

public interface CommentService {
    Comment addCommentToTicket(CommentRequest commentRequest);
}

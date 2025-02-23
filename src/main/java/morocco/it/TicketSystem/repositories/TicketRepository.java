package morocco.it.TicketSystem.repositories;

import morocco.it.TicketSystem.entities.Ticket;
import morocco.it.TicketSystem.entities.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCreatedBy_Id(Long id);
    List<Ticket> findByStatus(Status status);
    Optional<Ticket> findByCreatedBy_IdAndId(Long createdBy_Id,Long id);
}

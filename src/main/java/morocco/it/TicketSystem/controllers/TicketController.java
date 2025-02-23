package morocco.it.TicketSystem.controllers;

import lombok.AllArgsConstructor;
import morocco.it.TicketSystem.dto.TicketRequest;
import morocco.it.TicketSystem.dto.TicketResponse;
import morocco.it.TicketSystem.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest ticketRequest){
        TicketResponse ticketResponse = ticketService.createTicket(ticketRequest);
        return new ResponseEntity<>(ticketResponse, HttpStatus.CREATED);
    }

}

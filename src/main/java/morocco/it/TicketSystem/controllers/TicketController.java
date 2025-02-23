package morocco.it.TicketSystem.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import morocco.it.TicketSystem.dto.CommentDto;
import morocco.it.TicketSystem.dto.TicketRequest;
import morocco.it.TicketSystem.dto.TicketRequestUpdate;
import morocco.it.TicketSystem.dto.TicketResponse;
import morocco.it.TicketSystem.entities.enums.Status;
import morocco.it.TicketSystem.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Create a new ticket")
    @PostMapping("/create-ticket")
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody TicketRequest ticketRequest){
        TicketResponse ticketResponse = ticketService.createTicket(ticketRequest);
        return new ResponseEntity<>(ticketResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Update the ticket status")
    @PutMapping("/status-update/{ticketId}")
    public ResponseEntity<TicketResponse> updateTicketStatus(@RequestBody TicketRequestUpdate ticketRequestUpdate, @PathVariable("ticketId") Long ticketId){
        TicketResponse ticketResponse = ticketService.changeTicketStatus(ticketRequestUpdate, ticketId);
        return new ResponseEntity<>(ticketResponse, HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Assign ticket to IT support")
    @PutMapping("/assign-update/{ticketId}")
    public ResponseEntity<TicketResponse> assignTicket(@RequestBody TicketRequestUpdate ticketRequestUpdate, @PathVariable("ticketId") Long ticketId){
        TicketResponse ticketResponse = ticketService.assignTicket(ticketRequestUpdate, ticketId);
        return new ResponseEntity<>(ticketResponse, HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Add commment on ticket")
    @PutMapping("/comment-update/{ticketId}")
    public ResponseEntity<TicketResponse> addCommentToTicket(@RequestBody CommentDto commentRequest, @PathVariable("ticketId") Long ticketId) {
        TicketResponse ticketResponse = ticketService.addCommentToTicket(commentRequest, ticketId);
        return new ResponseEntity<>(ticketResponse, HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get all tickets")
    @GetMapping("/all")
    public ResponseEntity<List<TicketResponse>> getAllTickets(){
        List<TicketResponse> ticketResponses = ticketService.getAllTickets();
        return new ResponseEntity<>(ticketResponses, HttpStatus.OK);
    }

    @Operation(summary = "Fetch all tickets for an employee.")
    @GetMapping("/by-employee-id/{employeeId}")
    public ResponseEntity<List<TicketResponse>> getTicketByEmployeeId(@PathVariable Long employeeId){
        List<TicketResponse> ticketResponses = ticketService.getTicketByEmployeeId(employeeId);
        return new ResponseEntity<>(ticketResponses, HttpStatus.OK);
    }

//    @Operation(summary = "Fetch a specific ticket for an employee.")
//    @GetMapping("/by-employee-id/{employeeId}/by-ticket-id/{ticketId}")
//    public ResponseEntity<Optional<TicketResponse>> getEmployeeTicketById(@PathVariable Long employeeId, @PathVariable Long ticketId){
//        Optional<TicketResponse> ticketResponseOptional = ticketService.getEmployeeTicketsById(employeeId, ticketId);
//        return new ResponseEntity<>(ticketResponseOptional, HttpStatus.OK);
//    }

    //@Operation(summary = "Fetch all tickets for an employee with a specific status.")
    //@GetMapping("/by-employee-id/{employeeId}/by-status/{status}")
    //public getEmployeeTicketByStatus

    @Operation(summary = "Get a ticket by ID")
    @GetMapping("{ticketId}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long ticketId){
        TicketResponse ticketResponse = ticketService.getTicketById(ticketId);
        return new ResponseEntity<>(ticketResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get tickets by status")
    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<TicketResponse>> getTicketByStatus(@PathVariable Status status){
        List<TicketResponse> ticketResponses = ticketService.getTicketByStatus(status);
        return new ResponseEntity<>(ticketResponses, HttpStatus.OK);
    }



}

package morocco.it.TicketSystem.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@RequestMapping("/tickets")
@AllArgsConstructor
@Tag(name = "Ticket Controller", description = "APIs for managing tickets")
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Create a new ticket", description = "Creates a new ticket in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PostMapping("/create-ticket")
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody TicketRequest ticketRequest){
        TicketResponse ticketResponse = ticketService.createTicket(ticketRequest);
        return new ResponseEntity<>(ticketResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Update the ticket status", description = "Updates the status of an existing ticket.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ticket status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @PutMapping("/status-update/{ticketId}")
    public ResponseEntity<TicketResponse> updateTicketStatus(@RequestBody TicketRequestUpdate ticketRequestUpdate, @PathVariable("ticketId") Long ticketId){
        TicketResponse ticketResponse = ticketService.changeTicketStatus(ticketRequestUpdate, ticketId);
        return new ResponseEntity<>(ticketResponse, HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Assign ticket to IT support", description = "Assigns a ticket to an IT support team member.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ticket assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @PutMapping("/assign-update/{ticketId}")
    public ResponseEntity<TicketResponse> assignTicket(@RequestBody TicketRequestUpdate ticketRequestUpdate, @PathVariable("ticketId") Long ticketId){
        TicketResponse ticketResponse = ticketService.assignTicket(ticketRequestUpdate, ticketId);
        return new ResponseEntity<>(ticketResponse, HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Add comment to ticket", description = "Adds a comment to an existing ticket.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment added successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @PutMapping("/comment-update/{ticketId}")
    public ResponseEntity<TicketResponse> addCommentToTicket(@RequestBody CommentDto commentRequest, @PathVariable("ticketId") Long ticketId) {
        TicketResponse ticketResponse = ticketService.addCommentToTicket(commentRequest, ticketId);
        return new ResponseEntity<>(ticketResponse, HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get all tickets", description = "Retrieves a list of all tickets in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets retrieved successfully")
    })
    @GetMapping("/all")
    public ResponseEntity<List<TicketResponse>> getAllTickets(){
        List<TicketResponse> ticketResponses = ticketService.getAllTickets();
        return new ResponseEntity<>(ticketResponses, HttpStatus.OK);
    }

    @Operation(summary = "Get tickets by employee ID", description = "Retrieves all tickets created by a specific employee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })    @GetMapping("/by-employee-id/{employeeId}")
    public ResponseEntity<List<TicketResponse>> getTicketByEmployeeId(@PathVariable Long employeeId){
        List<TicketResponse> ticketResponses = ticketService.getTicketByEmployeeId(employeeId);
        return new ResponseEntity<>(ticketResponses, HttpStatus.OK);
    }

    @Operation(summary = "Get ticket by ID", description = "Retrieves a specific ticket by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    @GetMapping("{ticketId}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long ticketId){
        TicketResponse ticketResponse = ticketService.getTicketById(ticketId);
        return new ResponseEntity<>(ticketResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get tickets by status", description = "Retrieves all tickets with a specific status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets retrieved successfully")
    })
    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<TicketResponse>> getTicketByStatus(@PathVariable Status status){
        List<TicketResponse> ticketResponses = ticketService.getTicketByStatus(status);
        return new ResponseEntity<>(ticketResponses, HttpStatus.OK);
    }



}

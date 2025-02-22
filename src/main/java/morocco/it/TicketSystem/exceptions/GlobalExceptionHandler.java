package morocco.it.TicketSystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception){
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException exception){
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception exception){
        return buildErrorResponse("An unexpected error occured: "+exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", message);

        return new ResponseEntity<>(errorDetails, status);
    }
}

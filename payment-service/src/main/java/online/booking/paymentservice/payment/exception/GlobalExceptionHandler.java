package online.booking.paymentservice.payment.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ IllegalArgumentException.class, PaymentNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {
        ErrorResponse err = new ErrorResponse(
                LocalDateTime.now(),
                400,
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(err);
    }
}
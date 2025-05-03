package online.booking.paymentservice.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.booking.paymentservice.payment.dto.PaymentRequest;
import online.booking.paymentservice.payment.dto.PaymentResponse;
import online.booking.paymentservice.payment.model.Payment;
import online.booking.paymentservice.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService svc;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(
            @Valid @RequestBody PaymentRequest request
    ) {
        Payment p = svc.initiate(request);
        return ResponseEntity.ok(toResponse(p));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable UUID id) {
        Payment p = svc.getById(id);
        return ResponseEntity.ok(toResponse(p));
    }

    private PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(
                p.getId(),
                p.getUserId(),
                p.getBookingId(),
                p.getAmount(),
                p.getCurrency(),
                p.getStatus().name()
        );
    }
}
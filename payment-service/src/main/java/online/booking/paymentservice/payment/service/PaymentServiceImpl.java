package online.booking.paymentservice.payment.service;

import lombok.RequiredArgsConstructor;
import online.booking.paymentservice.payment.dto.PaymentRequest;
import online.booking.paymentservice.payment.exception.PaymentNotFoundException;
import online.booking.paymentservice.payment.model.Payment;
import online.booking.paymentservice.payment.model.PaymentStatus;
import online.booking.paymentservice.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository repo;

    @Override
    public Payment initiate(PaymentRequest request) {
        Payment p = Payment.builder()
                .userId(request.getUserId())
                .bookingId(request.getBookingId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(PaymentStatus.PENDING)
                .build();

        Payment saved = repo.save(p);
        saved.setStatus(PaymentStatus.SUCCESS);
        return repo.save(saved);
    }

    @Override
    public Payment getById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id.toString()));
    }
}
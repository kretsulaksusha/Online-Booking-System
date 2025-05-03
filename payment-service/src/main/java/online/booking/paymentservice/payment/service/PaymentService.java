package online.booking.paymentservice.payment.service;

import online.booking.paymentservice.payment.dto.PaymentRequest;
import online.booking.paymentservice.payment.model.Payment;

import java.util.UUID;

public interface PaymentService {
    Payment initiate(PaymentRequest request);
    Payment getById(UUID id);
}
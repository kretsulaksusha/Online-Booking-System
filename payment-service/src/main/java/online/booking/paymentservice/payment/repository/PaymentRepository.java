package online.booking.paymentservice.payment.repository;

import online.booking.paymentservice.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
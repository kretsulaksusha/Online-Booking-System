package online.booking.paymentservice.payment.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class PaymentResponse {
    UUID id;
    UUID userId;
    UUID bookingId;
    BigDecimal amount;
    String currency;
    String status;
}

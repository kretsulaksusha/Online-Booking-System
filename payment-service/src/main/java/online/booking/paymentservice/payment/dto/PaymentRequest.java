package online.booking.paymentservice.payment.dto;

import jakarta.validation.constraints.*;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class PaymentRequest {
    @NotNull
    UUID userId;

    @NotNull
    UUID bookingId;

    @NotNull
    @DecimalMin("0.01")
    BigDecimal amount;

    @NotBlank
    @Size(min = 3, max = 3)
    String currency;
}
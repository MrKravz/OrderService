package by.ares.orderservice.dto.request;

import by.ares.orderservice.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusRequest {
    private Long id;
    private PaymentStatus status;
}

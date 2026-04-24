package by.ares.orderservice.dto.request;

import by.ares.orderservice.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusRequest {
    private Long id;
    private PaymentStatus status;
}

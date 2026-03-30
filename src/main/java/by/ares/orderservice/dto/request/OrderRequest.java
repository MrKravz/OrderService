package by.ares.orderservice.dto.request;

import by.ares.orderservice.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Status status = Status.CREATED;
    private Long userId;
    private List<OrderItemRequest> items = new ArrayList<>();
}

package by.ares.orderservice.dto.request;

import by.ares.orderservice.model.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotNull
    private Status status;

    @NotNull
    @Positive
    private Long userId;
}

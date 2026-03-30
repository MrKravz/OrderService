package by.ares.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {

    @NotBlank(message = "Item name must not be empty")
    @Size(min = 5, max = 15, message = "Item name number must be in range from 5 to 16 characters")
    private String name;

    @NotNull(message = "Item price must not be null")
    @Positive(message = "Item price must be greater than 0")
    private Long price;

}

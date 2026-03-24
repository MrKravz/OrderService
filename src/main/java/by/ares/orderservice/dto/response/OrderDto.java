package by.ares.orderservice.dto.response;

import by.ares.orderservice.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private Status status;
    private Float totalPrice;
    private Boolean deleted;
    private UserDto userDto;
    private List<ItemDto> items;
}

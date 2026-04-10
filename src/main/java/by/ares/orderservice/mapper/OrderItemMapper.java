package by.ares.orderservice.mapper;

import by.ares.orderservice.dto.request.OrderItemRequest;
import by.ares.orderservice.dto.response.OrderItemDto;
import by.ares.orderservice.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ItemMapper.class, OrderMapper.class})
public interface OrderItemMapper extends SimpleMapper<OrderItemRequest, OrderItemDto, OrderItem> {
    @Override
    @Mapping(source = "item", target = "itemDto")
    OrderItemDto toDto(OrderItem entity);
}

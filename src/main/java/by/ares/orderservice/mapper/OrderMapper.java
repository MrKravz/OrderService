package by.ares.orderservice.mapper;

import by.ares.orderservice.dto.request.OrderRequest;
import by.ares.orderservice.dto.response.OrderDto;
import by.ares.orderservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ItemMapper.class})
public interface OrderMapper extends SimpleMapper<OrderRequest, OrderDto, Order> {
}

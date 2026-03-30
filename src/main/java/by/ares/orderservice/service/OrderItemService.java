package by.ares.orderservice.service;

import by.ares.orderservice.dto.response.OrderDto;

public interface OrderItemService {
    OrderDto addItemToOrder(Long orderId, Long itemId);
    OrderDto removeItemFromOrder(Long orderId, Long itemId);
}

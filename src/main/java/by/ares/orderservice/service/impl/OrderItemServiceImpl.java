package by.ares.orderservice.service.impl;

import by.ares.orderservice.dto.response.OrderDto;
import by.ares.orderservice.dto.response.UserDto;
import by.ares.orderservice.exception.ItemNotFoundException;
import by.ares.orderservice.exception.OrderItemNotFoundException;
import by.ares.orderservice.exception.OrderNotFoundException;
import by.ares.orderservice.mapper.OrderMapper;
import by.ares.orderservice.model.Item;
import by.ares.orderservice.model.Order;
import by.ares.orderservice.model.OrderItem;
import by.ares.orderservice.repository.ItemRepository;
import by.ares.orderservice.repository.OrderItemRepository;
import by.ares.orderservice.repository.OrderRepository;
import by.ares.orderservice.service.ApiClientService;
import by.ares.orderservice.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static by.ares.orderservice.util.OrderServiceConstants.*;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;
    private final ApiClientService apiClientService;
    private final OrderMapper orderMapper;

    @Override
    public OrderDto addItemToOrder(Long orderId, Long itemId) {
        var resultOrderItem = orderItemRepository.findByOrderIdAndItemId(orderId, itemId);
        if (resultOrderItem.isPresent()) {
            OrderItem result = incrementOrderItem(resultOrderItem.get());
            orderItemRepository.save(result);
            return mapOrderDto(result.getOrder());
        }
        OrderItem resultOrder = createOrderItem(orderId, itemId);
        orderItemRepository.save(resultOrder);
        return mapOrderDto(resultOrder.getOrder());
    }

    @Override
    public OrderDto removeItemFromOrder(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByOrderIdAndItemId(orderId, itemId)
                .orElseThrow(() -> new OrderItemNotFoundException(ORDER_ITEM_NOT_FOUND));
        if (orderItem.getQuantity() > 1) {
            decrementOrderItem(orderItem);
            Order resultOrder = orderItemRepository.save(orderItem).getOrder();
            return mapOrderDto(resultOrder);
        }
        Order order = orderItem.getOrder();
        order.removeOrderItem(orderItem);
        orderItemRepository.delete(orderItem);
        return mapOrderDto(order);
    }

    private OrderItem incrementOrderItem(OrderItem resultOrderItem) {
        final int incrementValue = 1;
        resultOrderItem.setQuantity(resultOrderItem.getQuantity() + incrementValue);
        return resultOrderItem;
    }

    private void decrementOrderItem(OrderItem orderItem) {
        final int decrementValue = 1;
        orderItem.setQuantity(orderItem.getQuantity() - decrementValue);
    }

    private OrderItem createOrderItem(Long orderId, Long itemId) {
        final int defaultValue = 1;
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(ITEM_NOT_FOUND));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
        OrderItem orderItem = new OrderItem()
                .setOrder(order)
                .setItem(item)
                .setQuantity(defaultValue);
        order.addOrderItem(orderItem);
        return orderItem;
    }

    private OrderDto mapOrderDto(Order order) {
        OrderDto orderDto = orderMapper.toDto(order);
        UserDto userDto = apiClientService.findUserById(order.getUserId());
        orderDto.setUserDto(userDto);
        return orderDto;
    }

}

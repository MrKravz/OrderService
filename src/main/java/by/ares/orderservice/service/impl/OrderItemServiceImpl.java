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

import java.util.Optional;

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
        OrderItem resultOrderItem = incrementOrCreate(
                orderItemRepository.findByOrderIdAndItemId(orderId, itemId), orderId, itemId
        );

        return saveAndMapOrderItem(resultOrderItem);
    }

    @Override
    public OrderDto removeItemFromOrder(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByOrderIdAndItemId(orderId, itemId)
                .orElseThrow(() -> new OrderItemNotFoundException(ORDER_ITEM_NOT_FOUND));
        final int decrementValue = 1;
        if (orderItem.getQuantity() > 1) {
            orderItem.setQuantity(orderItem.getQuantity() - decrementValue);
            return saveAndMapOrderItem(orderItem);
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
        order.removeOrderItem(orderItem);
        return deleteAndMapOrderItem(orderItem);
    }

    private OrderItem incrementOrCreate(Optional<OrderItem> resultOrderItem, Long orderId, Long itemId) {
        final int incrementValue = 1;
        if (resultOrderItem.isPresent()) {
            OrderItem orderItem = resultOrderItem.get();
            orderItem.setQuantity(orderItem.getQuantity() + incrementValue);
            return orderItem;
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(ITEM_NOT_FOUND));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
        OrderItem orderItem = new OrderItem()
                .setOrder(order)
                .setItem(item)
                .setQuantity(incrementValue);
        order.addOrderItem(orderItem);
        return orderItem;
    }

    private OrderDto saveAndMapOrderItem(OrderItem orderItem) {
        Order resultOrder = orderItemRepository.save(orderItem).getOrder();
        OrderDto orderDto = orderMapper.toDto(resultOrder);
        UserDto userDto = apiClientService.findUserById(resultOrder.getUserId());
        orderDto.setUserDto(userDto);
        return orderDto;
    }

    private OrderDto deleteAndMapOrderItem(OrderItem orderItem) {
        orderItemRepository.delete(orderItem);
        OrderDto orderDto = orderMapper.toDto(orderItem.getOrder());
        UserDto userDto = apiClientService.findUserById(orderItem.getOrder().getUserId());
        orderDto.setUserDto(userDto);
        return orderDto;
    }

}

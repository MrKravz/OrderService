package by.ares.orderservice.unit.service;

import by.ares.orderservice.dto.response.OrderDto;
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
import by.ares.orderservice.service.impl.OrderItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static by.ares.orderservice.util.TestConstants.ITEM_ID;
import static by.ares.orderservice.util.TestConstants.ORDER_ID;
import static by.ares.orderservice.util.TestModelBuilder.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    private Order order;
    private Item item;
    private OrderItem orderItem;
    private OrderDto orderDto;


    @BeforeEach
    void setUp() {
        order = buildOrder();
        item = buildItem();
        orderItem = buildOrderItem(order, item, 1);
        orderDto = buildOrderDto();
    }

    @Test
    void addItemToOrder_shouldIncrementQuantity_whenItemExists() {
        when(orderItemRepository.findByOrderIdAndItemId(ORDER_ID, ITEM_ID))
                .thenReturn(Optional.of(orderItem));
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        OrderDto result = orderItemService.addItemToOrder(ORDER_ID, ITEM_ID);
        assertEquals(2, orderItem.getQuantity());
        assertEquals(orderDto, result);
        verify(orderItemRepository).save(orderItem);
        verify(orderMapper).toDto(order);
        verifyNoInteractions(itemRepository, orderRepository);
    }

    @Test
    void addItemToOrder_shouldCreateNewOrderItem_whenNotExists() {
        when(orderItemRepository.findByOrderIdAndItemId(ORDER_ID, ITEM_ID))
                .thenReturn(Optional.empty());
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(orderItemRepository.save(any(OrderItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        OrderDto result = orderItemService.addItemToOrder(ORDER_ID, ITEM_ID);
        assertEquals(orderDto, result);
        verify(itemRepository).findById(ITEM_ID);
        verify(orderRepository).findById(ORDER_ID);
        verify(orderItemRepository).save(any(OrderItem.class));
        verify(orderMapper).toDto(order);
    }

    @Test
    void addItemToOrder_shouldThrow_whenItemNotFound() {
        when(orderItemRepository.findByOrderIdAndItemId(ORDER_ID, ITEM_ID))
                .thenReturn(Optional.empty());
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class,
                () -> orderItemService.addItemToOrder(ORDER_ID, ITEM_ID));
        verify(itemRepository).findById(ITEM_ID);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void addItemToOrder_shouldThrow_whenOrderNotFound() {
        when(orderItemRepository.findByOrderIdAndItemId(ORDER_ID, ITEM_ID))
                .thenReturn(Optional.empty());
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class,
                () -> orderItemService.addItemToOrder(ORDER_ID, ITEM_ID));
        verify(orderRepository).findById(ORDER_ID);
    }

    @Test
    void removeItemFromOrder_shouldDecrementQuantity_whenMoreThanOne() {
        orderItem.setQuantity(2);
        when(orderItemRepository.findByOrderIdAndItemId(ORDER_ID, ITEM_ID))
                .thenReturn(Optional.of(orderItem));
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        OrderDto result = orderItemService.removeItemFromOrder(ORDER_ID, ITEM_ID);
        assertEquals(1, orderItem.getQuantity());
        assertEquals(orderDto, result);
        verify(orderItemRepository).save(orderItem);
        verify(orderMapper).toDto(order);
    }

    @Test
    void removeItemFromOrder_shouldDelete_whenQuantityIsOne() {
        orderItem.setQuantity(1);
        when(orderItemRepository.findByOrderIdAndItemId(ORDER_ID, ITEM_ID))
                .thenReturn(Optional.of(orderItem));
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        OrderDto result = orderItemService.removeItemFromOrder(ORDER_ID, ITEM_ID);
        assertEquals(orderDto, result);
        verify(orderItemRepository).delete(orderItem);
        verify(orderMapper).toDto(order);
    }

    @Test
    void removeItemFromOrder_shouldThrow_whenNotFound() {
        when(orderItemRepository.findByOrderIdAndItemId(ORDER_ID, ITEM_ID))
                .thenReturn(Optional.empty());
        assertThrows(OrderItemNotFoundException.class,
                () -> orderItemService.removeItemFromOrder(ORDER_ID, ITEM_ID));
    }

}
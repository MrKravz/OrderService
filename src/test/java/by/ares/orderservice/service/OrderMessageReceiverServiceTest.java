package by.ares.orderservice.service;

import by.ares.orderservice.dto.request.OrderStatusRequest;
import by.ares.orderservice.model.Order;
import by.ares.orderservice.repository.OrderRepository;
import by.ares.orderservice.service.impl.OrderMessageReceiverService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static by.ares.orderservice.util.TestConstants.*;
import static by.ares.orderservice.util.TestModelBuilder.buildOrder;
import static by.ares.orderservice.util.TestModelBuilder.buildOrderStatusRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderMessageReceiverServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderMessageReceiverService service;

    @Test
    void shouldReturnEarlyWhenPaymentFailed() {
        OrderStatusRequest request = buildOrderStatusRequest();
        request.setStatus(FAILED);
        service.listen(request);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void shouldConfirmOrderWhenPaymentSuccess() {
        OrderStatusRequest request = buildOrderStatusRequest();
        Order order = buildOrder();
        order.setId(ORDER_ID);
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        service.listen(request);
        assertEquals(CONFIRMED, order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void shouldHandleOrderNotFoundException() {
        OrderStatusRequest request = buildOrderStatusRequest();
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());
        service.listen(request);
        verify(orderRepository, never()).save(any());
    }

}
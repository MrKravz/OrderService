package by.ares.orderservice.service.impl;

import by.ares.orderservice.dto.request.OrderStatusRequest;
import by.ares.orderservice.exception.OrderNotFoundException;
import by.ares.orderservice.model.Order;
import by.ares.orderservice.model.PaymentStatus;
import by.ares.orderservice.model.Status;
import by.ares.orderservice.repository.OrderRepository;
import by.ares.orderservice.service.MessageReceiverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static by.ares.orderservice.util.OrderServiceConstants.ORDER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderMessageReceiverService implements MessageReceiverService<OrderStatusRequest> {

    private final OrderRepository orderRepository;

    @Override
    @KafkaListener(id = "order-group-id", topics = "${TOPIC_NAME}")
    public void listen(OrderStatusRequest request) {
        try {
            if (request.getStatus() == PaymentStatus.FAILED) {
                log.warn("Payment failed for orderId={}", request.getId());
                return;
            }
            Order order = orderRepository.findById(request.getId())
                    .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
            order.setStatus(Status.CONFIRMED);
            orderRepository.save(order);
            log.info("Order confirmed for orderId={}", order.getId());
        } catch (OrderNotFoundException ex) {
            log.error("Order not found for orderId={}", request.getId(), ex);
        }
    }

}

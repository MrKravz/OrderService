package by.ares.orderservice.service;

import by.ares.orderservice.dto.request.OrderRequest;
import by.ares.orderservice.dto.request.StatusRequest;
import by.ares.orderservice.dto.response.OrderDto;

import java.util.List;

public interface OrderService extends SpecificationService<OrderDto>,
        StatusChangerService<Long, StatusRequest> {
    List<OrderDto> findAllByUserId(Long userId);
    OrderDto findById(Long id);
    Long save(OrderRequest orderRequest);
    void delete(Long id);
}

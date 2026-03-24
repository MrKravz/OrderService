package by.ares.orderservice.service.impl;

import by.ares.orderservice.dto.request.OrderRequest;
import by.ares.orderservice.dto.request.SpecificationRequest;
import by.ares.orderservice.dto.request.StatusRequest;
import by.ares.orderservice.dto.response.OrderDto;
import by.ares.orderservice.dto.response.UserDto;
import by.ares.orderservice.exception.OrderNotFoundException;
import by.ares.orderservice.mapper.OrderMapper;
import by.ares.orderservice.model.Order;
import by.ares.orderservice.model.Status;
import by.ares.orderservice.repository.OrderRepository;
import by.ares.orderservice.service.ApiClientService;
import by.ares.orderservice.service.OrderService;
import by.ares.orderservice.service.SpecificationBuilderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static by.ares.orderservice.util.OrderServiceConstants.ORDER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ApiClientService apiClientService;
    private final SpecificationBuilderService<Order> specificationBuilderService;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDto> findAllByUserId(Long userId) {
        List<OrderDto> orders = orderRepository.findAllByUserId(userId)
                .stream()
                .map(orderMapper::toDto)
                .toList();
        UserDto userDto = apiClientService.findUserById(userId);
        orders.forEach(x -> x.setUserDto(userDto));
        return orders;
    }

    @Override
    public Page<OrderDto> findAll(SpecificationRequest specificationRequest, Pageable pageable) {
        if (specificationRequest == null) {
            return orderRepository.findAll(pageable)
                    .map(orderMapper::toDto);
        }
        Specification<Order> specification = specificationBuilderService.configure(specificationRequest);
        return orderRepository.findAll(specification, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderDto findById(Long id) {
        OrderDto orderDto = orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
        orderDto.setUserDto(apiClientService.findUserById(id));
        return orderDto;
    }

    @Override
    @Transactional
    public Long save(OrderRequest orderRequest) {
        return orderRepository.save(orderMapper.toModel(orderRequest))
                .getId();
    }

    @Override
    @Transactional
    public Long update(OrderRequest orderRequest, Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
        return id;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
        order.setDeleted(true);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public Long changeStatus(Long id, StatusRequest status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND));
        order.setStatus(status.getStatus());
        if (status.getStatus().equals(Status.DONE)) {
            delete(id);
            return id;
        }
        return orderRepository.save(order).getId();
    }

}

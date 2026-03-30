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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static by.ares.orderservice.util.OrderServiceConstants.ORDER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
        Page<Order> resultPage = findOrders(specificationRequest, pageable);
        Map<Long, UserDto> userMap = findUsers(resultPage);
        var pageContent = resultPage.getContent()
                .stream()
                .map(order -> mapToDto(order, userMap))
                .toList();
        return new PageImpl<>(pageContent, pageable, resultPage.getTotalElements());
    }
    private Page<Order> findOrders(SpecificationRequest request, Pageable pageable) {
        if (request == null) {
            return orderRepository.findAll(pageable);
        }
        Specification<Order> specification = specificationBuilderService.configure(request);
        return orderRepository.findAll(specification, pageable);
    }
    private Map<Long, UserDto> findUsers(Page<Order> orderPage) {
        List<Long> idList = orderPage.stream()
                .map(Order::getUserId)
                .distinct()
                .toList();
        if (idList.isEmpty()) {
            return Collections.emptyMap();
        }
        return apiClientService.findAllByIdList(idList)
                .stream()
                .collect(Collectors.toMap(UserDto::getId, x -> x));
    }
    private OrderDto mapToDto(Order order, Map<Long, UserDto> userMap) {
        OrderDto dto = orderMapper.toDto(order);
        dto.setUserDto(userMap.get(order.getUserId()));
        return dto;
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

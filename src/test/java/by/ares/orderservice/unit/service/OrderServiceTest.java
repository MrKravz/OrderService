package by.ares.orderservice.unit.service;

import by.ares.orderservice.dto.request.OrderRequest;
import by.ares.orderservice.dto.request.SpecificationRequest;
import by.ares.orderservice.dto.response.OrderDto;
import by.ares.orderservice.exception.OrderNotFoundException;
import by.ares.orderservice.mapper.OrderMapper;
import by.ares.orderservice.model.Order;
import by.ares.orderservice.repository.OrderRepository;
import by.ares.orderservice.service.ApiClientService;
import by.ares.orderservice.service.SpecificationBuilderService;
import by.ares.orderservice.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static by.ares.orderservice.util.TestConstants.ORDER_ID;
import static by.ares.orderservice.util.TestConstants.USER_ID;
import static by.ares.orderservice.util.TestModelBuilder.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private SpecificationBuilderService<Order> specificationBuilderService;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private ApiClientService apiClientService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private OrderDto orderDto;


    @BeforeEach
    void setUp() {
        order = buildOrder();
        orderDto = buildOrderDto();
    }

    @Test
    void findAllByUserId_shouldReturnMappedList() {
        when(orderRepository.findAllByUserId(USER_ID)).thenReturn(List.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        List<OrderDto> result = orderService.findAllByUserId(USER_ID);
        assertEquals(1, result.size());
        assertEquals(orderDto, result.get(0));
        verify(orderRepository).findAllByUserId(USER_ID);
        verify(orderMapper).toDto(order);
    }

    @Test
    void findAllByUserId_shouldReturnEmptyList() {
        when(orderRepository.findAllByUserId(USER_ID)).thenReturn(Collections.emptyList());
        List<OrderDto> result = orderService.findAllByUserId(USER_ID);
        assertTrue(result.isEmpty());
        verify(orderRepository).findAllByUserId(USER_ID);
        verifyNoInteractions(orderMapper);
    }

    @Test
    void findAll_shouldUseSimpleFindAll_whenSpecificationIsNull() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(List.of(order));
        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        Page<OrderDto> result = orderService.findAll(null, pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(orderDto, result.getContent().get(0));
        verify(orderRepository).findAll(pageable);
        verify(orderMapper).toDto(order);
        verifyNoInteractions(specificationBuilderService);
    }

    @Test
    void findAll_shouldUseSpecification_whenProvided() {
        Pageable pageable = PageRequest.of(0, 10);
        SpecificationRequest request = new SpecificationRequest();
        Specification<Order> specification = mock(Specification.class);
        Page<Order> orderPage = new PageImpl<>(List.of(order));
        when(specificationBuilderService.configure(request)).thenReturn(specification);
        when(orderRepository.findAll(specification, pageable)).thenReturn(orderPage);
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        Page<OrderDto> result = orderService.findAll(request, pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(orderDto, result.getContent().get(0));
        verify(specificationBuilderService).configure(request);
        verify(orderRepository).findAll(specification, pageable);
        verify(orderMapper).toDto(order);
    }

    @Test
    void findAll_shouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        SpecificationRequest request = new SpecificationRequest();
        Specification<Order> specification = mock(Specification.class);
        Page<Order> emptyPage = Page.empty();
        when(specificationBuilderService.configure(request)).thenReturn(specification);
        when(orderRepository.findAll(specification, pageable)).thenReturn(emptyPage);
        Page<OrderDto> result = orderService.findAll(request, pageable);
        assertTrue(result.isEmpty());
        verify(specificationBuilderService).configure(request);
        verify(orderRepository).findAll(specification, pageable);
        verifyNoInteractions(orderMapper);
    }

    @Test
    void findById_shouldReturnOrder() {
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        OrderDto result = orderService.findById(ORDER_ID);
        assertEquals(orderDto, result);
    }

    @Test
    void findById_shouldThrowException() {
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.findById(ORDER_ID));
    }

    @Test
    void save_shouldReturnOrder() {
        OrderRequest request = buildOrderRequest();
        when(orderMapper.toModel(request)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        OrderDto result = orderService.save(request);
        assertEquals(result, orderDto);
        verify(orderRepository).save(order);
    }

    @Test
    void update_shouldReturnOrder() {
        OrderRequest request = buildOrderRequest();
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        OrderDto result = orderService.update(request, ORDER_ID);
        assertEquals(result, orderDto);
        verify(orderRepository).save(order);
    }

    @Test
    void delete_shouldDelete() {
        orderService.delete(ORDER_ID);
        verify(orderRepository).deleteById(any());
    }

}

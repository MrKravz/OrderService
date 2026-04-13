package by.ares.orderservice.unit.service;

import by.ares.orderservice.exception.OrderNotFoundException;
import by.ares.orderservice.model.Order;
import by.ares.orderservice.repository.OrderRepository;
import by.ares.orderservice.service.impl.SecurityValidationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static by.ares.orderservice.util.TestConstants.*;
import static by.ares.orderservice.util.TestModelBuilder.buildOrder;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityValidationServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private SecurityValidationServiceImpl service;

    private Order order;

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    @BeforeEach
    void setUp() {
        order = buildOrder();
    }

    @Test
    void shouldAllowAccess_whenAdmin() {
        assertDoesNotThrow(() ->
                service.validateAccess(USER_ID, USER_ID_2, ADMIN)
        );
    }

    @Test
    void shouldAllowAccess_whenOwner() {
        assertDoesNotThrow(() ->
                service.validateAccess(USER_ID, USER_ID, USER)
        );
    }

    @Test
    void shouldDenyAccess_whenNotOwner() {
        assertThrows(AccessDeniedException.class, () ->
                service.validateAccess(USER_ID, USER_ID_2, USER)
        );
    }

    @Test
    void shouldAllowAccess_whenUserOwnsOrder() {
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        assertDoesNotThrow(() ->
                service.validateOrderAccess(ORDER_ID, USER_ID, USER)
        );
    }

    @Test
    void shouldDenyAccess_whenUserNotOwnsOrder() {
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order.setUserId(USER_ID_2)));
        assertThrows(AccessDeniedException.class, () ->
                service.validateOrderAccess(ORDER_ID, USER_ID, USER)
        );
    }

    @Test
    void shouldAllowAccess_whenAdminNotOwnsOrder() {
        assertDoesNotThrow(() ->
                service.validateOrderAccess(ORDER_ID, USER_ID, ADMIN)
        );
    }

    @Test
    void shouldDenyAccess_whenOrderNotFound() {
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () ->
                service.validateOrderAccess(ORDER_ID, USER_ID, USER)
        );
    }

}

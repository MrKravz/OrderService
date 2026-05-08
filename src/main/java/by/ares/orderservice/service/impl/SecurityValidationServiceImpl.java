package by.ares.orderservice.service.impl;

import by.ares.orderservice.exception.OrderNotFoundException;
import by.ares.orderservice.model.Role;
import by.ares.orderservice.repository.OrderRepository;
import by.ares.orderservice.service.SecurityValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityValidationServiceImpl implements SecurityValidationService {

    private final OrderRepository orderRepository;

    @Override
    public void validateAccess(Long id, Long userId, String role) {
        if (isAdmin(Role.valueOf(role)) || isResourceOwner(id, userId)) {
            return;
        }
        throw new AccessDeniedException("Access denied");
    }

    @Override
    public void validateOrderAccess(Long orderId, Long userId, String role) {
        if (isAdmin(Role.valueOf(role)) || isOrderOwner(orderId, userId)) {
            return;
        }
        throw new AccessDeniedException("Access denied");
    }

    private boolean isResourceOwner(Long id, Long userId) {
        return id.equals(userId);
    }

    private boolean isAdmin(Role role) {
        return role.equals(Role.ADMIN);
    }

    private boolean isOrderOwner(Long orderId, Long userId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"))
                .getUserId()
                .equals(userId);
    }

}

package by.ares.orderservice.service;

public interface SecurityValidationService {
    void validateAccess(Long id, Long userId, String role);
    void validateOrderAccess(Long orderId, Long userId, String role);
}

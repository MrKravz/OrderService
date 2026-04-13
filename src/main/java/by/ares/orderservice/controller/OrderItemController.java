package by.ares.orderservice.controller;

import by.ares.orderservice.dto.response.OrderDto;
import by.ares.orderservice.service.OrderItemService;
import by.ares.orderservice.service.SecurityValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/{orderId}")
public class OrderItemController {

    private final OrderItemService orderItemService;
    private final SecurityValidationService securityValidationService;

    @PutMapping("/items/{itemId}")
    public ResponseEntity<OrderDto> addItem(@PathVariable Long orderId,
                                            @PathVariable Long itemId,
                                            @RequestHeader("X-User-Id") Long userId,
                                            @RequestHeader("X-User-Role") String role) {
        securityValidationService.validateOrderAccess(orderId, userId, role);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderItemService.addItemToOrder(orderId, itemId));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<OrderDto> removeItem(@PathVariable Long orderId,
                                               @PathVariable Long itemId,
                                               @RequestHeader("X-User-Id") Long userId,
                                               @RequestHeader("X-User-Role") String role) {
        securityValidationService.validateOrderAccess(orderId, userId, role);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderItemService.removeItemFromOrder(orderId, itemId));
    }

}

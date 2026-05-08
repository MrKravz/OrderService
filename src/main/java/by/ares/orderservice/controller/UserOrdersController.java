package by.ares.orderservice.controller;

import by.ares.orderservice.dto.response.OrderDto;
import by.ares.orderservice.service.OrderService;
import by.ares.orderservice.service.SecurityValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/users/{userId}")
public class UserOrdersController {

    private final OrderService orderService;
    private final SecurityValidationService securityValidationService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAllById(@PathVariable(name = "userId") Long id,
                                                      @RequestHeader("X-User-Id") Long userId,
                                                      @RequestHeader("X-User-Role") String role) {
        securityValidationService.validateAccess(id, userId, role);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.findAllByUserId(id));
    }

}

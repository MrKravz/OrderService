package by.ares.orderservice.controller;

import by.ares.orderservice.dto.response.OrderDto;
import by.ares.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/orders")
public class UserOrdersController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAllById(@PathVariable Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.findAllByUserId(userId));
    }

}

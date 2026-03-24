package by.ares.orderservice.controller;

import by.ares.orderservice.dto.response.OrderDto;
import by.ares.orderservice.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders/{orderId}")
public class OrderItemController {

    private final OrderItemService orderItemService;


    @PostMapping("/add-item/{itemId}")
    public ResponseEntity<OrderDto> addItem(@PathVariable Long orderId,
                                             @PathVariable Long itemId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderItemService.addItemToOrder(orderId, itemId));
    }

    @PostMapping("/remove-item/{itemId}")
    public ResponseEntity<OrderDto> removeItem(@PathVariable Long orderId,
                                             @PathVariable Long itemId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderItemService.removeItemFromOrder(orderId, itemId));
    }

}

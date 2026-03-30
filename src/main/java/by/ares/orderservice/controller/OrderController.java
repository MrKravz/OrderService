package by.ares.orderservice.controller;

import by.ares.orderservice.dto.request.OrderRequest;
import by.ares.orderservice.dto.request.SpecificationRequest;
import by.ares.orderservice.dto.response.OrderDto;
import by.ares.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<Page<OrderDto>> findAll(@ModelAttribute SpecificationRequest specificationRequest,
                                                  @PageableDefault Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.findAll(specificationRequest, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.findById(id));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDto>> findAllById(@PathVariable Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.findAllByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.save(orderRequest));
    }

    @DeleteMapping("{/id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}

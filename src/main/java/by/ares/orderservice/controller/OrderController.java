package by.ares.orderservice.controller;

import by.ares.orderservice.dto.request.OrderRequest;
import by.ares.orderservice.dto.request.SpecificationRequest;
import by.ares.orderservice.dto.response.OrderDto;
import by.ares.orderservice.service.OrderService;
import by.ares.orderservice.service.SecurityValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final SecurityValidationService securityValidationService;


    @GetMapping
    public ResponseEntity<Page<OrderDto>> findAll(@ModelAttribute SpecificationRequest specificationRequest,
                                                  @PageableDefault Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.findAll(specificationRequest, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable Long id,
                                             @RequestHeader("X-User-Id") Long userId,
                                             @RequestHeader("X-User-Role") String role) {
        securityValidationService.validateOrderAccess(id, userId, role);
        OrderDto orderDto = orderService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderDto);
    }

    @PostMapping
    public ResponseEntity<OrderDto> save(@Valid @RequestBody OrderRequest orderRequest,
                                         @RequestHeader("X-User-Id") Long userId,
                                         @RequestHeader("X-User-Role") String role) {
        securityValidationService.validateAccess(orderRequest.getUserId(), userId, role);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.save(orderRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderDto> update(@PathVariable Long id, @RequestHeader("X-User-Id") Long userId,
                                           @RequestHeader("X-User-Role") String role,
                                           @Valid @RequestBody OrderRequest orderRequest) {
        securityValidationService.validateAccess(orderRequest.getUserId(), userId, role);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.update(orderRequest, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}

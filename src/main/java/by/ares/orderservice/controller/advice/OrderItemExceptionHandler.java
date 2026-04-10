package by.ares.orderservice.controller.advice;

import by.ares.orderservice.exception.ExceptionResponse;
import by.ares.orderservice.exception.OrderItemNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderItemExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(OrderItemExceptionHandler.class);

    @ExceptionHandler(OrderItemNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleOrderItemNotFoundException(OrderItemNotFoundException ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(ex.getMessage(), System.currentTimeMillis()));
    }

}

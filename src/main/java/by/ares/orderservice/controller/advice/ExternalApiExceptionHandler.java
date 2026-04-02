package by.ares.orderservice.controller.advice;

import by.ares.orderservice.exception.ExceptionResponse;
import by.ares.orderservice.exception.ExternalApiException;
import by.ares.orderservice.exception.ResponseParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExternalApiExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ExternalApiExceptionHandler.class);

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ExceptionResponse> handleExternalApiException(ExternalApiException ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ExceptionResponse(ex.getMessage(), System.currentTimeMillis()));
    }

    @ExceptionHandler(ResponseParseException.class)
    public ResponseEntity<ExceptionResponse> handleResponseParseException(ResponseParseException ex) {
        logger.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(ex.getMessage(), System.currentTimeMillis()));
    }

}

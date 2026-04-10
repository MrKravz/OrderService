package by.ares.orderservice.service;

import by.ares.orderservice.dto.request.SpecificationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SpecificationService<T> {
    Page<T> findAll(SpecificationRequest specificationRequest, Pageable pageable);
}

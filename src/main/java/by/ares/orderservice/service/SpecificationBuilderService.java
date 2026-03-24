package by.ares.orderservice.service;

import by.ares.orderservice.dto.request.SpecificationRequest;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilderService<T> {
    Specification<T> configure(SpecificationRequest specificationRequest);
}

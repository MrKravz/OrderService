package by.ares.orderservice.service.impl;

import by.ares.orderservice.dto.request.SpecificationRequest;
import by.ares.orderservice.model.Order;
import by.ares.orderservice.service.SpecificationBuilderService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderSpecificationBuilderService implements SpecificationBuilderService<Order> {

    @Override
    public Specification<Order> configure(SpecificationRequest specificationRequest) {
        List<Specification<Order>> spec = new ArrayList<>();
        if (specificationRequest.getMinDate() != null && specificationRequest.getMaxDate() != null) {
            spec.add((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("createdAt"), specificationRequest.getMinDate(),
                            specificationRequest.getMaxDate()));
        }
        if (specificationRequest.getStatuses() != null && !specificationRequest.getStatuses().isEmpty()) {
            spec.add((root, query, criteriaBuilder) ->
                    root.get("status").in(specificationRequest.getStatuses())
            );
        }
        return Specification.allOf(spec);
    }

}

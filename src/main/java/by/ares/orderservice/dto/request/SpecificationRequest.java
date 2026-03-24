package by.ares.orderservice.dto.request;

import by.ares.orderservice.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SpecificationRequest {
    private LocalDate minDate;
    private LocalDate maxDate;
    private Status status;
}

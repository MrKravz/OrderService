package by.ares.orderservice.util;

import by.ares.orderservice.dto.request.OrderRequest;
import by.ares.orderservice.dto.request.StatusRequest;
import by.ares.orderservice.dto.response.OrderDto;
import by.ares.orderservice.model.Order;
import by.ares.orderservice.model.Status;

import static by.ares.orderservice.util.TestConstants.*;

public class TestModelBuilder {

    public static Order buildOrder() {
        return new Order()
                .setId(ORDER_ID)
                .setStatus(CREATED)
                .setUserId(USER_ID);
    }

    public static OrderDto buildOrderDto() {
        return OrderDto.builder()
                .id(ORDER_ID)
                .status(CREATED)
                .deleted(false)
                .build();
    }

    public static OrderRequest buildOrderRequest() {
        return OrderRequest.builder()
                .status(Status.CREATED)
                .userId(10L)
                .build();
    }

    public static StatusRequest buildStatusRequest() {
        return StatusRequest.builder()
                .status(AWAITED)
                .build();
    }

}

package by.ares.orderservice.util;

import by.ares.orderservice.dto.request.OrderRequest;
import by.ares.orderservice.dto.request.StatusRequest;
import by.ares.orderservice.dto.response.OrderDto;
import by.ares.orderservice.dto.response.UserDto;
import by.ares.orderservice.model.Item;
import by.ares.orderservice.model.Order;
import by.ares.orderservice.model.OrderItem;
import by.ares.orderservice.model.Status;

import static by.ares.orderservice.util.TestConstants.*;

public class TestModelBuilder {

    public static Order buildOrder() {
        return new Order()
                .setId(ORDER_ID)
                .setStatus(CREATED)
                .setUserId(USER_ID);
    }

    public static Item buildItem() {
        return new Item()
                .setId(ITEM_ID)
                .setName(ITEM_NAME)
                .setPrice(PRICE);
    }

    public static OrderItem buildOrderItem(Order order, Item item, int quantity) {
        return new OrderItem()
                .setId(ORDER_ITEM_ID)
                .setItem(item)
                .setOrder(order)
                .setQuantity(quantity);
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

    public static UserDto buildUserDto() {
        return UserDto.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .active(ACTIVATION_STATUS)
                .build();
    }

    public static UserDto buildChangedUserDto() {
        return UserDto.builder()
                .id(USER_ID_2)
                .name(USER_NAME)
                .active(ACTIVATION_STATUS)
                .build();
    }

    public static StatusRequest buildStatusRequest() {
        return StatusRequest.builder()
                .status(AWAITED)
                .build();
    }

}

package by.ares.orderservice.util;

import by.ares.orderservice.dto.request.ItemRequest;
import by.ares.orderservice.dto.request.OrderRequest;
import by.ares.orderservice.dto.request.StatusRequest;
import by.ares.orderservice.dto.response.ItemDto;
import by.ares.orderservice.dto.response.OrderDto;
import by.ares.orderservice.dto.response.UserDto;
import by.ares.orderservice.model.Item;
import by.ares.orderservice.model.Order;
import by.ares.orderservice.model.OrderItem;

import static by.ares.orderservice.util.TestConstants.*;

public class TestModelBuilder {

    public static Order buildOrder() {
        return new Order()
                .setStatus(AWAITED)
                .setUserId(USER_ID);
    }

    public static Item buildItem() {
        return new Item()
                .setName(ITEM_NAME)
                .setPrice(PRICE);
    }

    public static ItemDto buildItemDto() {
        return new ItemDto()
                .setId(ITEM_ID)
                .setName(ITEM_NAME)
                .setPrice(PRICE);
    }

    public static ItemRequest buildItemRequest() {
        return new ItemRequest()
                .setName(ITEM_NAME)
                .setPrice(PRICE);
    }

    public static OrderItem buildOrderItem(Order order, Item item, int quantity) {
        var result = new OrderItem()
                .setItem(item)
                .setOrder(order)
                .setQuantity(quantity);
        item.getOrders().add(result);
        order.getItems().add(result);
        return result;
    }

    public static OrderDto buildOrderDto() {
        return OrderDto.builder()
                .id(ORDER_ID)
                .status(AWAITED)
                .deleted(false)
                .build();
    }

    public static OrderRequest buildOrderRequest() {
        return OrderRequest.builder()
                .status(AWAITED)
                .userId(USER_ID)
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

package by.ares.orderservice.integration.controller;

import by.ares.orderservice.integration.controller.abstraction.AbstractIntegrationTest;
import by.ares.orderservice.model.Item;
import by.ares.orderservice.model.Order;
import by.ares.orderservice.model.OrderItem;
import by.ares.orderservice.repository.ItemRepository;
import by.ares.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.ares.orderservice.util.TestConstants.AWAITED;
import static by.ares.orderservice.util.TestModelBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserOrdersControllerTest extends AbstractIntegrationTest {

    @Autowired
    public ItemRepository itemRepository;
    @Autowired
    public OrderRepository orderRepository;

    private Order order;

    @BeforeEach
    void init() {
        orderRepository.deleteAll();
        order = saveTestOrder();
    }

    private Order saveTestOrder() {
        Item item = buildItem();
        itemRepository.save(item);
        order = buildOrder();
        OrderItem orderItem = buildOrderItem(order, item, 1);
        order.addOrderItem(orderItem);
        return orderRepository.save(order);
    }

    @Test
    void findAll_ShouldReturnAllOrderByUserId() throws Exception {
        stubFindUserById();
        mockMvc.perform(get("/users/{userId}/orders", order.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].status").value(AWAITED.toString()));
    }

}
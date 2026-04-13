package by.ares.orderservice.integration.controller;

import by.ares.orderservice.integration.controller.abstraction.AbstractIntegrationTest;
import by.ares.orderservice.model.Item;
import by.ares.orderservice.model.Order;
import by.ares.orderservice.model.OrderItem;
import by.ares.orderservice.repository.ItemRepository;
import by.ares.orderservice.repository.OrderRepository;
import by.ares.orderservice.service.SecurityValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static by.ares.orderservice.util.TestConstants.AWAITED;
import static by.ares.orderservice.util.TestModelBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserOrdersControllerTest extends AbstractIntegrationTest {

    @MockitoBean
    private SecurityValidationService securityValidationService;
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
        mockMvc.perform(get("/users/{userId}/orders", order.getUserId())
                        .header("X-User-Id", 1L)
                        .header("X-User-Role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].status").value(AWAITED.toString()));
    }

}
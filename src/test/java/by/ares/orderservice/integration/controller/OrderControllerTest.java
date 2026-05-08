package by.ares.orderservice.integration.controller;

import by.ares.orderservice.dto.request.OrderRequest;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static by.ares.orderservice.util.TestConstants.AWAITED;
import static by.ares.orderservice.util.TestConstants.CONFIRMED;
import static by.ares.orderservice.util.TestModelBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class OrderControllerTest extends AbstractIntegrationTest {

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
    void findById_shouldReturnOrder() throws Exception {
        stubFindUserById();
        mockMvc.perform(get("/orders/{id}", order.getId())
                        .header("X-User-Id", 1L)
                        .header("X-User-Role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value(AWAITED.toString()));
    }

    @Test
    void save_shouldReturnOrder() throws Exception {
        OrderRequest request = buildOrderRequest();
        stubFindUserById();
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-User-Id", 1L)
                        .header("X-User-Role", "ADMIN"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value(AWAITED.toString()));
    }

    @Test
    void update_shouldReturnOrder() throws Exception {
        OrderRequest request = buildOrderRequest();
        request.setStatus(CONFIRMED);
        stubFindUserById();
        mockMvc.perform(patch("/orders/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-User-Id", 1L)
                        .header("X-User-Role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value(CONFIRMED.toString()));
    }


    @Test
    void delete_shouldDelete() throws Exception {
        stubFindUserById();
        mockMvc.perform(delete("/orders/{id}", order.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void findAll_shouldReturnPage() throws Exception {
        saveTestOrder();
        saveTestOrder();
        stubFindAllByIdList();
        mockMvc.perform(get("/items?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

}
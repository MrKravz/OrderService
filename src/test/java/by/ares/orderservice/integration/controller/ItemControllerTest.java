package by.ares.orderservice.integration.controller;

import by.ares.orderservice.dto.request.ItemRequest;
import by.ares.orderservice.integration.controller.abstraction.AbstractIntegrationTest;
import by.ares.orderservice.model.Item;
import by.ares.orderservice.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static by.ares.orderservice.util.TestConstants.*;
import static by.ares.orderservice.util.TestModelBuilder.buildItem;
import static by.ares.orderservice.util.TestModelBuilder.buildItemRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemControllerTest extends AbstractIntegrationTest {

    @Autowired
    public ItemRepository itemRepository;

    private Item item;

    @BeforeEach
    void init() {
        item = saveTestItem();
    }

    private Item saveTestItem() {
        return itemRepository.save(buildItem());
    }

    @Test
    void shouldFindPaymentCardById() throws Exception {
        mockMvc.perform(get("/items/{id}", item.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(ITEM_NAME))
                .andExpect(jsonPath("$.price").value(String.valueOf(PRICE)));
    }

    @Test
    void shouldCreatePaymentCard() throws Exception {
        ItemRequest request = buildItemRequest();
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(ITEM_NAME))
                .andExpect(jsonPath("$.price").value(String.valueOf(PRICE)));
    }

    @Test
    void shouldUpdatePaymentCard() throws Exception {
        ItemRequest request = buildItemRequest();
        request.setName(UPDATED_ITEM_NAME);
        mockMvc.perform(put("/items/{id}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(UPDATED_ITEM_NAME))
                .andExpect(jsonPath("$.price").value(String.valueOf(PRICE)));
    }


    @Test
    void shouldDeletePaymentCard() throws Exception {
        mockMvc.perform(delete("/items/{id}", item.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnPage() throws Exception {
        saveTestItem();
        saveTestItem();
        mockMvc.perform(get("/items?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

}

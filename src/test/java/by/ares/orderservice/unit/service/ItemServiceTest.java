package by.ares.orderservice.unit.service;

import by.ares.orderservice.dto.request.ItemRequest;
import by.ares.orderservice.dto.response.ItemDto;
import by.ares.orderservice.exception.ItemNotFoundException;
import by.ares.orderservice.mapper.ItemMapper;
import by.ares.orderservice.model.Item;
import by.ares.orderservice.repository.ItemRepository;
import by.ares.orderservice.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static by.ares.orderservice.util.TestConstants.ITEM_ID;
import static by.ares.orderservice.util.TestModelBuilder.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;
    private ItemDto itemDto;


    @BeforeEach
    void setUp() {
        item = buildItem();
        itemDto = buildItemDto();
    }

    @Test
    void findAll_shouldReturnPage_whenElementsExist() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> itemPage = new PageImpl<>(List.of(item), pageable, 1);
        when(itemRepository.findAll(pageable)).thenReturn(itemPage);
        Page<ItemDto> result = itemService.findAll(pageable);
        assertEquals(1, result.getTotalElements());
        verify(itemRepository).findAll(pageable);
    }

    @Test
    void findAll_shouldReturnEmptyPage_whenElementsNotExist() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> emptyPage = Page.empty();
        when(itemRepository.findAll(pageable)).thenReturn(emptyPage);
        Page<ItemDto> result = itemService.findAll(pageable);
        assertTrue(result.isEmpty());
        verify(itemRepository).findAll(pageable);
        verifyNoInteractions(itemMapper);
    }

    @Test
    void findById_shouldReturnItem_whenElementExist() {
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
        when(itemMapper.toDto(item)).thenReturn(itemDto);
        ItemDto result = itemService.findById(ITEM_ID);
        assertEquals(itemDto, result);
    }

    @Test
    void findById_shouldThrowException_whenElementNotExist() {
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> itemService.findById(ITEM_ID));
    }

    @Test
    void save_shouldReturnItem() {
        ItemRequest request = buildItemRequest();
        when(itemMapper.toModel(request)).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toDto(item)).thenReturn(itemDto);
        ItemDto result = itemService.save(request);
        assertEquals(result, this.itemDto);
        verify(itemRepository).save(item);
    }

    @Test
    void update_shouldReturnItem() {
        ItemRequest request = buildItemRequest();
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toDto(item)).thenReturn(itemDto);
        ItemDto result = itemService.update(request, ITEM_ID);
        assertEquals(result, this.itemDto);
        verify(itemRepository).save(item);
    }

    @Test
    void delete_shouldDeleteItem() {
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
        itemService.delete(ITEM_ID);
        verify(itemRepository).deleteById(any());
    }

}

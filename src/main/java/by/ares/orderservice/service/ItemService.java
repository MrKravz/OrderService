package by.ares.orderservice.service;

import by.ares.orderservice.dto.request.ItemRequest;
import by.ares.orderservice.dto.response.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService extends CrudService<ItemDto, ItemRequest, Long> {
    Page<ItemDto> findAll(Pageable pageable);

}

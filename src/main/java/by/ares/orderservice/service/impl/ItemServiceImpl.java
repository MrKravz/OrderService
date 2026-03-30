package by.ares.orderservice.service.impl;

import by.ares.orderservice.dto.request.ItemRequest;
import by.ares.orderservice.dto.response.ItemDto;
import by.ares.orderservice.exception.ItemNotFoundException;
import by.ares.orderservice.mapper.ItemMapper;
import by.ares.orderservice.repository.ItemRepository;
import by.ares.orderservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static by.ares.orderservice.util.OrderServiceConstants.ITEM_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public Page<ItemDto> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable)
                .map(itemMapper::toDto);
    }

    @Override
    public ItemDto findById(Long id) {
        return itemRepository.findById(id)
                .map(itemMapper::toDto)
                .orElseThrow( () -> new ItemNotFoundException(ITEM_NOT_FOUND_EXCEPTION));
    }

    @Override
    @Transactional
    public Long save(ItemRequest itemRequest) {
        return itemRepository.save(itemMapper.toModel(itemRequest)).getId();
    }

    @Override
    @Transactional
    public Long update(ItemRequest itemRequest, Long id) {
        var item = itemRepository.findById(id)
                .orElseThrow( () -> new ItemNotFoundException(ITEM_NOT_FOUND_EXCEPTION));
        item.setName(itemRequest.getName())
                .setPrice(item.getPrice());
        return itemRepository.save(item).getId();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

}

package by.ares.orderservice.mapper;

import by.ares.orderservice.dto.request.ItemRequest;
import by.ares.orderservice.dto.response.ItemDto;
import by.ares.orderservice.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {OrderItemMapper.class})
public interface ItemMapper extends SimpleMapper<ItemRequest, ItemDto, Item> {
}

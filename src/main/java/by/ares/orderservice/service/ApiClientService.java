package by.ares.orderservice.service;

import by.ares.orderservice.dto.response.UserDto;

import java.util.List;

public interface ApiClientService {
    UserDto findUserById(Long id);
    List<UserDto> findAllByIdList(List<Long> idList);
}

package by.ares.orderservice.service;

import by.ares.orderservice.dto.response.UserDto;

public interface ApiClientService {

    UserDto findUserById(Long id);

}

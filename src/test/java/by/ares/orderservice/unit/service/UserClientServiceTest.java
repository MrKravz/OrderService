package by.ares.orderservice.unit.service;

import by.ares.orderservice.dto.response.UserDto;
import by.ares.orderservice.service.impl.UserClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;

import static by.ares.orderservice.util.TestConstants.*;
import static by.ares.orderservice.util.TestModelBuilder.buildChangedUserDto;
import static by.ares.orderservice.util.TestModelBuilder.buildUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserClientServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private UserClientServiceImpl userClientService;

    private UserDto userDto;
    private UserDto changedUserDto;

    @BeforeEach
    void init() {
        userDto = buildUserDto();
        changedUserDto = buildChangedUserDto();
        ReflectionTestUtils.setField(userClientService, "uri", URI);
    }

    @Test
    void shouldReturnUserListSuccessfully() {
        List<Long> ids = List.of(USER_ID, USER_ID_2);
        List<UserDto> expected = List.of(userDto, changedUserDto);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(expected);
        List<UserDto> result = userClientService.findAllByIdList(ids);
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnUserByIdSuccessfully() {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.body(UserDto.class)).thenReturn(userDto);
        UserDto result = userClientService.findUserById(USER_ID);
        assertEquals(userDto, result);
    }

}

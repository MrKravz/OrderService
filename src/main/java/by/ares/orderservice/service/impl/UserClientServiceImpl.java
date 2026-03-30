package by.ares.orderservice.service.impl;

import by.ares.orderservice.dto.response.UserDto;
import by.ares.orderservice.exception.ExceptionResponse;
import by.ares.orderservice.exception.ExternalApiException;
import by.ares.orderservice.exception.ResponseParseException;
import by.ares.orderservice.service.ApiClientService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static by.ares.orderservice.util.OrderServiceConstants.RESPONSE_PARSE_MESSAGE;
import static by.ares.orderservice.util.OrderServiceConstants.USER_URI;

@Service
@RequiredArgsConstructor
public class UserClientServiceImpl implements ApiClientService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    @Value("${USER_URI:}")
    private String uri;

    @PostConstruct
    public void validateUri() {
        if (uri == null || uri.isBlank()) {
            throw new IllegalStateException("USER_URI environment variable is not set or empty");
        }
    }

    @Override
    public List<UserDto> findAllByIdList(List<Long> idList) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(USER_URI)
                        .queryParam("id", idList.toArray())
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    ExceptionResponse error;
                    try (InputStream is = res.getBody()) {
                        error = objectMapper.readValue(is, ExceptionResponse.class);
                    } catch (IOException e) {
                        throw new ResponseParseException(RESPONSE_PARSE_MESSAGE);
                    }
                    throw new ExternalApiException(error.getMessage());
                })
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public UserDto findUserById(Long id) {
        return restClient.get()
                .uri(USER_URI + "/" + id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    ExceptionResponse error;
                    try (InputStream is = res.getBody()) {
                        error = objectMapper.readValue(is, ExceptionResponse.class);
                    } catch (IOException e) {
                        throw new ResponseParseException(RESPONSE_PARSE_MESSAGE);
                    }
                    throw new ExternalApiException(error.getMessage());
                })
                .body(UserDto.class);
    }

}

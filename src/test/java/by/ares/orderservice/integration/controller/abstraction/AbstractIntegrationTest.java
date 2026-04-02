package by.ares.orderservice.integration.controller.abstraction;

import by.ares.orderservice.util.TestcontainersConfiguration;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

import static by.ares.orderservice.util.TestConstants.USER_ID;
import static by.ares.orderservice.util.TestModelBuilder.buildUserDto;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;


@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected static WireMockServer wireMockServer = new WireMockServer(options().port(8081));

    @BeforeAll
    static void startWireMock() {
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @DynamicPropertySource
    static void setUserUri(DynamicPropertyRegistry registry) {
        registry.add("api.user.uri", () -> wireMockServer.baseUrl() + "/users");
    }

    protected void stubFindUserById() {
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/users/" + USER_ID))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(buildUserDto()))));
    }

    protected void stubFindAllByIdList() {
        List<Long> idList = List.of(USER_ID);
        String queryString = idList.stream()
                .map(id -> "id=" + id)
                .collect(Collectors.joining("&"));
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/users/list?" + queryString))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(buildUserDto()))));
    }

}

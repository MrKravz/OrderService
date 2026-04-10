package by.ares.orderservice;

import by.ares.orderservice.util.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
@Import(TestcontainersConfiguration.class)
@TestPropertySource(properties = "USER_URI=http://dummy:8080")
class OrderServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}

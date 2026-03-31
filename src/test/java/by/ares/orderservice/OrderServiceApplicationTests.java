package by.ares.orderservice;

import by.ares.orderservice.util.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


@SpringBootTest
@Import(TestcontainersConfiguration.class)
class OrderServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}

package by.ares.orderservice.util;

import by.ares.orderservice.OrderServiceApplication;
import org.springframework.boot.SpringApplication;

public class ManualTestRunner {
    public static void main(String[] args) {
        SpringApplication.from(OrderServiceApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}

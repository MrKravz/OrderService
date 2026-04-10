package by.ares.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestClient;

@Configuration
@EnableJpaAuditing
public class ApplicationConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }

}

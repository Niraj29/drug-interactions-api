package com.example.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

  @Bean
  public OpenAPI drugInteractionsAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Drug Interactions API")
                .description(
                    "API for managing drug interactions and analyzing OpenFDA adverse event signals")
                .version("1.0.0")
                .contact(new Contact().name("API Support").email("support@example.com")))
        .addServersItem(new Server().url("/").description("Local server"));
  }
}

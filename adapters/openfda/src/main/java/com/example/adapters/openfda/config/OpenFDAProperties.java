package com.example.adapters.openfda.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "openfda")
public class OpenFDAProperties {
  private String baseUrl;
  private String apiKey;
  private int defaultLimit = 100;
  private int timeout = 5000;
}

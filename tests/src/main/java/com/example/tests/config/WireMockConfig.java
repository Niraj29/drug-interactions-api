package com.example.tests.config;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@ActiveProfiles("test")
public class WireMockConfig {

  @Bean(initMethod = "start", destroyMethod = "stop")
  public WireMockServer wireMockServer() {
    return new WireMockServer(options().dynamicPort());
  }
}

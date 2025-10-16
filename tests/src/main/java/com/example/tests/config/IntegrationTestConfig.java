package com.example.tests.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@ActiveProfiles("test")
public class IntegrationTestConfig {

  @Bean(initMethod = "start", destroyMethod = "stop")
  public WireMockServer mockOpenFDAServer() {
    return new WireMockServer(
        WireMockConfiguration.options()
            .dynamicPort()
            .usingFilesUnderDirectory("src/test/resources/wiremock"));
  }

  @Bean
  @Primary
  public String openFDABaseUrl(WireMockServer mockOpenFDAServer) {
    return String.format("http://localhost:%s", mockOpenFDAServer.port());
  }
}

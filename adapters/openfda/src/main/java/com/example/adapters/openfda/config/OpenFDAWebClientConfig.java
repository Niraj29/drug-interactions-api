package com.example.adapters.openfda.config;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.netty.http.client.HttpClient;

@Configuration
public class OpenFDAWebClientConfig {

  @Bean
  public WebClient openFDAWebClient(OpenFDAProperties properties) {
    HttpClient httpClient =
        HttpClient.create()
            .responseTimeout(Duration.ofMillis(properties.getTimeout()))
            .doOnConnected(
                conn ->
                    conn.addHandlerLast(
                            new ReadTimeoutHandler(properties.getTimeout(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(
                            new WriteTimeoutHandler(
                                properties.getTimeout(), TimeUnit.MILLISECONDS)));

    return WebClient.builder()
        .baseUrl(properties.getBaseUrl())
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .filter(apiKeyFilter(properties))
        .build();
  }

  private ExchangeFilterFunction apiKeyFilter(OpenFDAProperties properties) {
    return (request, next) -> {
      if (properties.getApiKey() != null && !properties.getApiKey().isEmpty()) {
        // Safely append the api_key query parameter using UriComponentsBuilder
        URI uri = request.url();
        URI newUri =
            UriComponentsBuilder.fromUri(uri)
                .queryParam("api_key", properties.getApiKey())
                .build(true)
                .toUri();

        ClientRequest newRequest = ClientRequest.from(request).url(newUri).build();

        return next.exchange(newRequest);
      }
      return next.exchange(request);
    };
  }
}

package com.example.adapters.openfda.config;

import com.example.adapters.openfda.exception.OpenFDAException;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
@EnableConfigurationProperties(OpenFDAProperties.class)
public class OpenFDAClientConfig {

  @Bean
  @Primary
  public WebClient openFDAWebClient(OpenFDAProperties properties) {
    ConnectionProvider provider =
        ConnectionProvider.builder("openfda-connection-pool")
            .maxConnections(50)
            .maxIdleTime(Duration.ofSeconds(20))
            .maxLifeTime(Duration.ofMinutes(5))
            .pendingAcquireTimeout(Duration.ofSeconds(60))
            .build();

    HttpClient httpClient =
        HttpClient.create(provider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getTimeout())
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
        .filter(errorHandler())
        .filter(apiKeyFilter(properties))
        .build();
  }

  private ExchangeFilterFunction errorHandler() {
    return ExchangeFilterFunction.ofResponseProcessor(
        clientResponse -> {
          if (clientResponse.statusCode().is4xxClientError()) {
            return clientResponse
                .bodyToMono(String.class)
                .flatMap(
                    error ->
                        Mono.error(
                            new OpenFDAException(
                                "OpenFDA client error: " + error, clientResponse.statusCode())));
          }
          if (clientResponse.statusCode().is5xxServerError()) {
            return clientResponse
                .bodyToMono(String.class)
                .flatMap(
                    error ->
                        Mono.error(
                            new OpenFDAException(
                                "OpenFDA server error: " + error, HttpStatus.BAD_GATEWAY)));
          }
          return Mono.just(clientResponse);
        });
  }

  private ExchangeFilterFunction apiKeyFilter(OpenFDAProperties properties) {
    return ExchangeFilterFunction.ofRequestProcessor(
        request -> {
          if (properties.getApiKey() != null && !properties.getApiKey().isEmpty()) {
            // Build a new URI with the api_key query parameter appended
            var newUri =
                UriComponentsBuilder.fromUri(request.url())
                    .queryParam("api_key", properties.getApiKey())
                    .build()
                    .toUri();

            var newRequest = ClientRequest.from(request).url(newUri).build();

            return Mono.just(newRequest);
          }
          return Mono.just(request);
        });
  }
}

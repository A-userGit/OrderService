package com.innowise.orderservice.integration.config;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@RequiredArgsConstructor
public class WireMockConfig {

  private final WireMockProperties properties;

  @Bean
  public WireMockServer mockUserService() {
    return new WireMockServer(options().port(properties.getPort()));
  }
}

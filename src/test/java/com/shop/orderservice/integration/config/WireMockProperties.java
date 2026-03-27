package com.innowise.orderservice.integration.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "feign.user.service")
public class WireMockProperties {

  private String mock;
  private String name;
  private int port;

}

package com.innowise.orderservice.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka.topic")
public class KafkaTopicProperties {
  private String paymentResultTopic;
  private String orderCreatedTopic;
}

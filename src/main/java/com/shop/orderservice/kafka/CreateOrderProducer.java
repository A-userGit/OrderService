package com.innowise.orderservice.kafka;

import com.innowise.orderservice.config.kafka.KafkaTopicProperties;
import com.innowise.external.dto.kafka.CreatePaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrderProducer {

  private final KafkaTemplate<String, CreatePaymentDto> kafkaTemplate;
  private final KafkaTopicProperties topicProperties;

  public void sendMessage(CreatePaymentDto message) {
    kafkaTemplate.send(topicProperties.getOrderCreatedTopic(), message);
  }
}

package com.shop.orderservice.kafka;

import com.shop.orderservice.config.kafka.KafkaTopicProperties;
import com.shop.external.dto.kafka.CreatePaymentDto;
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

package com.innowise.orderservice.kafka;

import com.innowise.orderservice.dto.UpdateOrderDto;
import com.innowise.external.dto.kafka.PaymentResult;
import com.innowise.orderservice.enums.PaymentStatus;
import com.innowise.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentResultConsumer {

  private final static String CONSUMER_GROUP = "group-1";

  private final OrderService orderService;

  @KafkaListener(topics = "${kafka.topic.payment-result-topic}", groupId = CONSUMER_GROUP)

  public void listenOrderCreation(PaymentResult message) {
    if(message.getResult() == PaymentStatus.REJECTED) {
      orderService.updateOrder(new UpdateOrderDto("REJECTED", message.getOrderId()));
    }else {
      orderService.updateOrder(new UpdateOrderDto("PAID", message.getOrderId()));
    }
  }

}

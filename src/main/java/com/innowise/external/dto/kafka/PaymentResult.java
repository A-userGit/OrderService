package com.innowise.external.dto.kafka;

import com.innowise.orderservice.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResult {
  private long orderId;
  private PaymentStatus result;
}

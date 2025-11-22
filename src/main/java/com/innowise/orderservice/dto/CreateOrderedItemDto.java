package com.innowise.orderservice.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateOrderedItemDto {

  @Min(0)
  private int quantity;
  private long itemId;
  private long orderId;
}

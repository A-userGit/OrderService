package com.innowise.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderedItemDto {
  private long itemId;
  private String itemName;
  private int amount;
  private long orderId;
  private long id;
}

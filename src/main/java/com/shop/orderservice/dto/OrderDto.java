package com.innowise.orderservice.dto;

import com.innowise.orderservice.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderDto {
  private long id;
  private OrderStatus status;
  private LocalDateTime createdAt;
  private List<OrderedItemDto> items;
  private long userId;

  public OrderDto() {
    items = new ArrayList<>();
  }
}

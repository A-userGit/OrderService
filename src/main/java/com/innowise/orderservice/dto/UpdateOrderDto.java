package com.innowise.orderservice.dto;

import com.innowise.orderservice.enums.OrderStatus;
import com.innowise.orderservice.validation.annotation.EnumValid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateOrderDto {
  @EnumValid(enumClass = OrderStatus.class)
  private String status;
  private long id;
}

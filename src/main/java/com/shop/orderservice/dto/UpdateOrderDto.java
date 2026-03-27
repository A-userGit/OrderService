package com.shop.orderservice.dto;

import com.shop.orderservice.enums.OrderStatus;
import com.shop.orderservice.validation.annotation.EnumValid;
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

package com.shop.orderservice.dto;

import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateOrderDto {
  private String email;
  private HashMap<Long, Integer> items;

  public CreateOrderDto() {
    items = new HashMap<>();
  }
}

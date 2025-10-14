package com.innowise.orderservice.dto;

import com.innowise.orderservice.validation.annotation.LongValid;
import jakarta.validation.constraints.Email;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateOrderDto {

  @Email
  private String email;
  private HashMap<@LongValid String, Integer> items;

  public CreateOrderDto() {
    items = new HashMap<>();
  }
}

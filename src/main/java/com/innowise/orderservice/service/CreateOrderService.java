package com.innowise.orderservice.service;

import com.innowise.orderservice.dto.CreateOrderDto;
import com.innowise.orderservice.dto.CreateOrderedItemDto;
import com.innowise.orderservice.dto.OrderDto;

public interface CreateOrderService {

  OrderDto addOrder(CreateOrderDto createOrderDto);

  OrderDto addItemToOrder(CreateOrderedItemDto createOrderedItemDto);
}

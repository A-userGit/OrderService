package com.shop.orderservice.service;

import com.shop.orderservice.dto.CreateOrderDto;
import com.shop.orderservice.dto.CreateOrderedItemDto;
import com.shop.orderservice.dto.OrderDto;

public interface CreateOrderService {

  OrderDto addOrder(CreateOrderDto createOrderDto);

  OrderDto addItemToOrder(CreateOrderedItemDto createOrderedItemDto);
}

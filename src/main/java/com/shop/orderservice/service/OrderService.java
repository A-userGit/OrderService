package com.shop.orderservice.service;

import com.shop.orderservice.dto.CreateOrderDto;
import com.shop.orderservice.dto.OrderDto;
import com.shop.orderservice.dto.UpdateOrderDto;
import com.shop.orderservice.entity.Order;
import java.util.List;

public interface OrderService {

  Order createOrder(CreateOrderDto createOrderDto, long userId);

  OrderDto updateOrder(UpdateOrderDto updateOrderDto);

  OrderDto updateOrder(Order order);

  void deleteOrder(long id);

  List<OrderDto> getOrdersByIds(List<Long> ids);

  OrderDto getById(long id);

  Order getDBOrderById(long id);

  List<OrderDto> getOrdersByStatuses(List<String> statuses);

  List<OrderDto> getOrdersForUser(String email);

}

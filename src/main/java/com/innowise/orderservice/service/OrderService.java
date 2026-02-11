package com.innowise.orderservice.service;

import com.innowise.orderservice.dto.CreateOrderDto;
import com.innowise.orderservice.dto.OrderDto;
import com.innowise.orderservice.dto.UpdateOrderDto;
import com.innowise.orderservice.entity.Order;
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

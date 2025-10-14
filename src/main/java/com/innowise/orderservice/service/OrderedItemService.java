package com.innowise.orderservice.service;

import com.innowise.orderservice.dto.CreateOrderedItemDto;
import com.innowise.orderservice.dto.OrderedItemDto;
import com.innowise.orderservice.dto.UpdateOrderedItemDto;
import com.innowise.orderservice.entity.Item;
import com.innowise.orderservice.entity.Order;
import com.innowise.orderservice.entity.OrderedItem;
import java.util.List;
import java.util.Map;

public interface OrderedItemService {
  List<OrderedItem> createOrderedItems(Map<Item, Integer> items, Order order);

  OrderedItem createOrderedItem(CreateOrderedItemDto createOrderedItemDto, Order order, Item item);

  OrderedItemDto getById(long id);

  List<OrderedItemDto> getByIds(List<Long> ids);

  void deleteById(long id);

  OrderedItemDto updateOrderedItem(UpdateOrderedItemDto updateOrderedItemDto);
}

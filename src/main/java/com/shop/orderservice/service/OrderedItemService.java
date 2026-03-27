package com.shop.orderservice.service;

import com.shop.orderservice.dto.CreateOrderedItemDto;
import com.shop.orderservice.dto.OrderedItemDto;
import com.shop.orderservice.dto.UpdateOrderedItemDto;
import com.shop.orderservice.entity.Item;
import com.shop.orderservice.entity.Order;
import com.shop.orderservice.entity.OrderedItem;
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

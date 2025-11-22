package com.innowise.orderservice.service;

import com.innowise.orderservice.dto.CreateItemDto;
import com.innowise.orderservice.dto.ItemDto;
import com.innowise.orderservice.entity.Item;
import java.util.List;

public interface ItemService {

  ItemDto createItem(CreateItemDto createItemDto);

  ItemDto updateItem(ItemDto updatingItemDto);

  void softDeleteItem(long id);

  List<ItemDto> getActiveItemsByIds(List<Long> ids);

  List<Item> getActiveDBItemsByIds(List<Long> ids);

  ItemDto getActiveById(long id);

  Item getActiveDBById(long id);
}

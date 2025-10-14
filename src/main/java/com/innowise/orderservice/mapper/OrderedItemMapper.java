package com.innowise.orderservice.mapper;

import com.innowise.orderservice.dto.CreateOrderedItemDto;
import com.innowise.orderservice.dto.OrderedItemDto;
import com.innowise.orderservice.entity.OrderedItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderedItemMapper {

  @Mapping(target = "orderId", source = "source.order.id")
  @Mapping(target = "itemName", source = "source.item.name")
  @Mapping(target = "itemId", source = "source.item.id")
  @Mapping(target = "amount", source = "source.quantity")
  OrderedItemDto toOrderedItemDto(OrderedItem source);

  @Mapping(target = "order", ignore = true)
  @Mapping(target = "item", ignore = true)
  @Mapping(target = "id", ignore = true)
  OrderedItem toOrderedItem(CreateOrderedItemDto source);
}

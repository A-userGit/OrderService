package com.shop.orderservice.mapper;

import com.shop.orderservice.dto.CreateOrderDto;
import com.shop.orderservice.dto.CreateOrderRequestDto;
import com.shop.orderservice.dto.OrderItemRequestDto;
import java.util.HashMap;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CreateOrderRequestMapper {

  @Mapping(source = "items", target = "items", qualifiedByName = "orderItemsToHashMap")
  CreateOrderDto toCreateOrderDto(CreateOrderRequestDto source);

  @Named("orderItemsToHashMap")
  static HashMap<Long, Integer> hashMapOrderItems(List<OrderItemRequestDto> items) {
    HashMap<Long, Integer> result = new HashMap<>();
    for (OrderItemRequestDto item: items){
      result.put(item.getId(), item.getAmount());
    }
    return result;
  }

}

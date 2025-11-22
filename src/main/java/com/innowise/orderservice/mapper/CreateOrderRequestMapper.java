package com.innowise.orderservice.mapper;

import com.innowise.orderservice.dto.CreateOrderDto;
import com.innowise.orderservice.dto.CreateOrderRequestDto;
import com.innowise.orderservice.dto.OrderItemRequestDto;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
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

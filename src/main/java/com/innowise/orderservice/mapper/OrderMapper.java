package com.innowise.orderservice.mapper;

import com.innowise.orderservice.dto.CreateOrderDto;
import com.innowise.orderservice.dto.OrderDto;
import com.innowise.orderservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderedItemMapper.class)
public interface OrderMapper {

  @Mapping(target = "userId", ignore = true)
  @Mapping(target = "status", expression = "java(com.innowise.orderservice.enums.OrderStatus.CREATED)")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
  @Mapping(target = "items", ignore = true)
  Order toOrder(CreateOrderDto source);

  @Mapping(target = "items", source = "items")
  @Mapping(target = "createdAt", source = "creationDate", dateFormat = "dd-MM-yyyy HH:mm:ss")
  OrderDto toOrderDto(Order source);

}

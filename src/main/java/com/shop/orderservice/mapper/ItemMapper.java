package com.innowise.orderservice.mapper;

import com.innowise.orderservice.dto.CreateItemDto;
import com.innowise.orderservice.dto.ItemDto;
import com.innowise.orderservice.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

  ItemDto toItemDto(Item source);

  Item toItem(ItemDto source);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "deleted", expression = "java(false)")
  Item toItem(CreateItemDto createItemDto);

}

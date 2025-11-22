package com.innowise.orderservice.service.impl;

import com.innowise.orderservice.dto.CreateOrderedItemDto;
import com.innowise.orderservice.dto.OrderedItemDto;
import com.innowise.orderservice.dto.UpdateOrderedItemDto;
import com.innowise.orderservice.entity.Item;
import com.innowise.orderservice.entity.Order;
import com.innowise.orderservice.entity.OrderedItem;
import com.innowise.orderservice.exception.ObjectNotFoundException;
import com.innowise.orderservice.mapper.OrderedItemMapper;
import com.innowise.orderservice.repository.OrderedItemRepository;
import com.innowise.orderservice.service.OrderedItemService;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderedItemServiceImpl implements OrderedItemService {

  private final static String OBJECT_NAME = "ordered item";
  private final OrderedItemRepository orderedItemRepository;
  private final OrderedItemMapper mapper;

  @Override
  @Transactional
  public List<OrderedItem> createOrderedItems(Map<Item, Integer> items, Order order) {
    List<OrderedItem> orderedItemList = new ArrayList<>();
    for (var entry : items.entrySet()) {
      OrderedItem orderedItem = new OrderedItem();
      orderedItem.setItem(entry.getKey());
      orderedItem.setOrder(order);
      orderedItem.setQuantity(entry.getValue());
      orderedItemList.add(orderedItem);
    }
    return orderedItemRepository.saveAll(orderedItemList);
  }

  @Override
  @Transactional
  public OrderedItem createOrderedItem(CreateOrderedItemDto createOrderedItemDto, Order order,
      Item item) {
    OrderedItem orderedItem = mapper.toOrderedItem(createOrderedItemDto);
    orderedItem.setOrder(order);
    orderedItem.setItem(item);
    return orderedItemRepository.save(orderedItem);
  }

  @Override
  public OrderedItemDto getById(long id) {
    Optional<OrderedItem> byId = orderedItemRepository.findById(id);
    if (byId.isEmpty()) {
      throw ObjectNotFoundException.entityNotFound(OBJECT_NAME, "id", id);
    }
    return mapper.toOrderedItemDto(byId.get());
  }

  @Override
  public List<OrderedItemDto> getByIds(List<Long> ids) {
    return orderedItemRepository.findAllById(ids).stream().map(mapper::toOrderedItemDto)
        .toList();
  }

  @Override
  @Transactional
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  public void deleteById(long id) {
    Optional<OrderedItem> byId = orderedItemRepository.findById(id);
    if (byId.isEmpty()) {
      throw ObjectNotFoundException.entityNotFound(OBJECT_NAME, "id", id);
    }
    orderedItemRepository.delete(byId.get());
  }

  @Transactional
  @Override
  public OrderedItemDto updateOrderedItem(UpdateOrderedItemDto updateOrderedItemDto) {
    Optional<OrderedItem> byId = orderedItemRepository.findById(updateOrderedItemDto.getId());
    if (byId.isEmpty()) {
      throw ObjectNotFoundException.entityNotFound(OBJECT_NAME, "id",
          updateOrderedItemDto.getId());
    }
    OrderedItem orderedItem = byId.get();
    orderedItem.setQuantity(updateOrderedItemDto.getAmount());
    OrderedItem saved = orderedItemRepository.save(orderedItem);
    return mapper.toOrderedItemDto(saved);
  }
}

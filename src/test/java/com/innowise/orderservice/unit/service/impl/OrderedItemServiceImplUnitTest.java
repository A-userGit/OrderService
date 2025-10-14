package com.innowise.orderservice.unit.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innowise.orderservice.dto.CreateOrderedItemDto;
import com.innowise.orderservice.dto.UpdateOrderedItemDto;
import com.innowise.orderservice.entity.Item;
import com.innowise.orderservice.entity.Order;
import com.innowise.orderservice.entity.OrderedItem;
import com.innowise.orderservice.exception.ObjectNotFoundException;
import com.innowise.orderservice.mapper.OrderedItemMapper;
import com.innowise.orderservice.repository.OrderedItemRepository;
import com.innowise.orderservice.service.impl.OrderedItemServiceImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderedItemServiceImplUnitTest {

  @Mock
  private OrderedItemRepository orderedItemRepository;

  @Mock
  private OrderedItemMapper mapper;

  @InjectMocks
  private OrderedItemServiceImpl orderedItemService;

  @Test
  @DisplayName("Test save multiple ordered items")
  public void createOrderedItemsTest() {
    orderedItemService.createOrderedItems(new HashMap<>(), new Order());
    verify(orderedItemRepository, times(1)).saveAll(any());
  }

  @Test
  @DisplayName("Test save ordered item")
  public void createOrderedItemTest() {
    when(mapper.toOrderedItem(any())).thenReturn(new OrderedItem());
    orderedItemService.createOrderedItem(new CreateOrderedItemDto(1, 1, 1), new Order(),
        new Item());
    verify(orderedItemRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("Test get ordered item that doesn't exist by id")
  public void getByIdNotExistsTest() {
    when(orderedItemRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ObjectNotFoundException.class, () -> orderedItemService.getById(1));
  }

  @Test
  @DisplayName("Test get ordered item by id")
  public void getByIdSuccessTest() {
    when(orderedItemRepository.findById(any(Long.class))).thenReturn(
        Optional.of(new OrderedItem()));
    orderedItemService.getById(1);
    verify(mapper, times(1)).toOrderedItemDto(any());
  }

  @Test
  @DisplayName("Test get ordered items by ids")
  public void getByIdsTest() {
    List<Long> ids = List.of(1L, 2L);
    when(orderedItemRepository.findAllById(any())).thenReturn(
        List.of(new OrderedItem(), new OrderedItem()));
    orderedItemService.getByIds(ids);
    verify(mapper, times(2)).toOrderedItemDto(any());
  }

  @Test
  @DisplayName("Test delete ordered item by id that doesn't exist")
  public void deleteByIdNotExistsTest() {
    when(orderedItemRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ObjectNotFoundException.class, () -> orderedItemService.deleteById(1));
  }

  @Test
  @DisplayName("Test delete ordered item by id")
  public void deleteByIdSuccessTest() {
    when(orderedItemRepository.findById(any(Long.class))).thenReturn(
        Optional.of(new OrderedItem()));
    orderedItemService.deleteById(1);
    verify(orderedItemRepository, times(1)).delete(any());
  }

  @Test
  @DisplayName("Test update ordered item that doesn't exist by id")
  public void updateOrderedItemNotExistsTest() {
    when(orderedItemRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ObjectNotFoundException.class,
        () -> orderedItemService.updateOrderedItem(new UpdateOrderedItemDto(12, 1)));
  }

  @Test
  @DisplayName("Test delete ordered item by id")
  public void updateOrderedItemSuccessTest() {
    when(orderedItemRepository.findById(any(Long.class))).thenReturn(
        Optional.of(new OrderedItem()));
    orderedItemService.updateOrderedItem(new UpdateOrderedItemDto(12, 1));
    verify(orderedItemRepository, times(1)).save(any());
  }
}

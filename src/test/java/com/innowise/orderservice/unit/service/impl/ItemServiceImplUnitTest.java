package com.innowise.orderservice.unit.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innowise.orderservice.dto.CreateItemDto;
import com.innowise.orderservice.dto.ItemDto;
import com.innowise.orderservice.entity.Item;
import com.innowise.orderservice.exception.ObjectAlreadyExistsException;
import com.innowise.orderservice.exception.ObjectNotFoundException;
import com.innowise.orderservice.mapper.ItemMapper;
import com.innowise.orderservice.repository.ItemRepository;
import com.innowise.orderservice.service.impl.ItemServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplUnitTest {

  @Mock
  private ItemRepository itemRepository;

  @Mock
  private ItemMapper mapper;

  @InjectMocks
  private ItemServiceImpl itemService;

  @Test
  @DisplayName("Test add item with name that already exists")
  public void createItemAlreadyExistsTest() {
    when(itemRepository.findByName(any(String.class))).thenReturn(Optional.of(new Item()));
    assertThrows(ObjectAlreadyExistsException.class,
        () -> itemService.createItem(new CreateItemDto("test", 12)));
  }

  @Test
  @DisplayName("Test add item")
  public void createItemSuccessTest() {
    when(itemRepository.findByName(any(String.class))).thenReturn(Optional.empty());
    itemService.createItem(new CreateItemDto("test", 12));
    verify(itemRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("Test update item with that doesn't exist")
  public void updateItemNotExistsTest() {
    when(itemRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ObjectNotFoundException.class,
        () -> itemService.updateItem(new ItemDto("test", 12, 1, false)));
  }

  @Test
  @DisplayName("Test update item")
  public void updateItemSuccessTest() {
    when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(new Item(1, "test", 12, false)));
    itemService.updateItem(new ItemDto("test", 12, 1, false));
    verify(itemRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("Test soft delete item that doesn't exist")
  public void softDeleteItemNotExistTest() {
    when(itemRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ObjectNotFoundException.class,
        () -> itemService.softDeleteItem(1));
  }

  @Test
  @DisplayName("Test soft delete item")
  public void softDeleteItemSuccessTest() {
    Item item = new Item(1, "test", 12 , true);
    Item spiedItem = spy(item);
    when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(spiedItem));
    itemService.softDeleteItem(1);
    verify(spiedItem).setDeleted(true);
    verify(itemRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("Test get active items by ids")
  public void getActiveItemsByIdsTest() {
    List<Long> ids = List.of(1L,2L);
    when(itemRepository.findAllActiveByIds(any())).thenReturn(List.of(new Item(), new Item()));
    itemService.getActiveItemsByIds(ids);
    verify(mapper, times(2)).toItemDto(any());
  }

  @Test
  @DisplayName("Test get active db items  by ids")
  public void getActiveDBItemsByIdsTest() {
    List<Long> ids = List.of(1L,2L);
    when(itemRepository.findAllActiveByIds(any())).thenReturn(List.of(new Item(), new Item()));
    List<ItemDto> activeItemsByIds = itemService.getActiveItemsByIds(ids);
    assertEquals(2, activeItemsByIds.size());
  }

  @Test
  @DisplayName("Test get active item  by id")
  public void getActiveByIdTest() {
    when(itemRepository.findActiveById(any())).thenReturn(Optional.of(new Item()));
    itemService.getActiveById(1);
    verify(mapper, times(1)).toItemDto(any());
  }

  @Test
  @DisplayName("Test get active db item  by id that doesn't exist")
  public void getActiveDBByIdNotExistTest() {
    when(itemRepository.findActiveById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ObjectNotFoundException.class,
        () -> itemService.getActiveDBById(1));
  }

  @Test
  @DisplayName("Test get active db item  by id")
  public void getActiveDBByIdSuccessTest() {
    when(itemRepository.findActiveById(any(Long.class))).thenReturn(Optional.of(new Item()));
    Item activeDBById = itemService.getActiveDBById(1);
    assertFalse(activeDBById.isDeleted());
  }

}

package com.innowise.orderservice.unit.service.impl;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innowise.orderservice.config.feign.UserInfoFeignClient;
import com.innowise.orderservice.dto.CreateOrderDto;
import com.innowise.orderservice.dto.CreateOrderedItemDto;
import com.innowise.orderservice.dto.OrderDto;
import com.innowise.orderservice.dto.external.UserDto;
import com.innowise.orderservice.entity.Item;
import com.innowise.orderservice.entity.Order;
import com.innowise.orderservice.enums.OrderStatus;
import com.innowise.orderservice.exception.ObjectNotFoundException;
import com.innowise.orderservice.kafka.CreateOrderProducer;
import com.innowise.orderservice.service.ItemService;
import com.innowise.orderservice.service.OrderService;
import com.innowise.orderservice.service.OrderedItemService;
import com.innowise.orderservice.service.impl.CreateOrderServiceImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class CreateOrderServiceImplUnitTest {

  @Mock
  private OrderService orderService;
  @Mock
  private ItemService itemService;
  @Mock
  private OrderedItemService orderedItemService;
  @Mock
  private UserInfoFeignClient feignClient;
  @Mock
  private CreateOrderProducer createOrderProducer;

  @InjectMocks
  private CreateOrderServiceImpl createOrderService;

  @Test
  @DisplayName("Test add order with user that doesn't exist")
  public void addOrderNoUserTest() {
    UserDto user = null;
    when(feignClient.getUserInfoFromUserService(any(String.class))).thenReturn(
        new ResponseEntity<>(
            user, HttpStatusCode.valueOf(404)));
    assertThrows(
        ObjectNotFoundException.class,
        () -> createOrderService.addOrder(new CreateOrderDto("test@mail.com", new HashMap<>())));
  }

  @Test
  @DisplayName("Test add order with item that doesn't exist")
  public void addOrderNoItemTest() {
    UserDto user = new UserDto(1, "test", "testS");
    HashMap<Long, Integer> items = new HashMap<>();
    items.put(1L, 10);
    when(feignClient.getUserInfoFromUserService(any(String.class))).thenReturn(
        new ResponseEntity<>(
            user, HttpStatusCode.valueOf(404)));
    when(itemService.getActiveDBItemsByIds(any())).thenReturn(new ArrayList<>());
    ObjectNotFoundException exception = assertThrows(
        ObjectNotFoundException.class,
        () -> createOrderService.addOrder(new CreateOrderDto("test@mail.com", items)));
    Assertions.assertEquals("item with field id and values  1 not found", exception.getMessage());
  }

  @Test
  @DisplayName("Test add order")
  public void addOrderSuccessTest() {
    UserDto user = new UserDto(1, "test", "testS");
    List<Item> items = new ArrayList<>();
    items.add(new Item(1, "testI", 1200, false));
    HashMap<Long, Integer> itemIds = new HashMap<>();
    itemIds.put(1L, 10);
    CreateOrderDto createOrderDto = new CreateOrderDto("test@mail.com", itemIds);
    Order order = new Order(1, 1, OrderStatus.CREATED, LocalDateTime.now(), new ArrayList<>());
    when(feignClient.getUserInfoFromUserService(any(String.class))).thenReturn(
        new ResponseEntity<>(
            user, HttpStatusCode.valueOf(404)));
    when(itemService.getActiveDBItemsByIds(any())).thenReturn(items);
    when(orderService.createOrder(createOrderDto, 1)).thenReturn(order);
    when(orderService.updateOrder(any(Order.class))).thenReturn(new OrderDto());
    createOrderService.addOrder(createOrderDto);
    verify(orderService, times(1)).updateOrder(any(Order.class));
  }

  @Test
  @DisplayName("Test add item to order")
  public void addItemToOrderSuccessTest() {
    Order order = new Order(1, 1, OrderStatus.CREATED, LocalDateTime.now(), new ArrayList<>());
    Item item = new Item(1, "testI", 1200, false);
    CreateOrderedItemDto createOrderedItemDto = new CreateOrderedItemDto(1, 1, 1);
    when(orderService.getDBOrderById(1)).thenReturn(order);
    when(itemService.getActiveDBById(1)).thenReturn(item);
    createOrderService.addItemToOrder(createOrderedItemDto);
    verify(orderedItemService, times(1)).createOrderedItem(createOrderedItemDto, order, item);
    verify(orderService, times(1)).updateOrder(any(Order.class));
  }


}

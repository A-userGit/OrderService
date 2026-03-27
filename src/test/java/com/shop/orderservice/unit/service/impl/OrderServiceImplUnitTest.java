package com.innowise.orderservice.unit.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innowise.orderservice.dto.CreateOrderDto;
import com.innowise.orderservice.dto.UpdateOrderDto;
import com.innowise.orderservice.entity.Order;
import com.innowise.orderservice.enums.OrderStatus;
import com.innowise.orderservice.exception.ObjectNotFoundException;
import com.innowise.orderservice.mapper.OrderMapper;
import com.innowise.orderservice.repository.OrderRepository;
import com.innowise.orderservice.service.impl.OrderServiceImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class OrderServiceImplUnitTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private OrderMapper mapper;

  @InjectMocks
  private OrderServiceImpl orderService;

  @Test
  @DisplayName("Test create order")
  public void createOrderTest() {
    when(mapper.toOrder(any())).thenReturn(new Order());
    orderService.createOrder(new CreateOrderDto("", new HashMap<>()), 1);
    verify(orderRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("Test update order that doesn't exist")
  public void updateOrderNotExistsTest() {
    when(orderRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ObjectNotFoundException.class,
        () -> orderService.updateOrder(new UpdateOrderDto("COMPLETED", 1)));
  }

  @Test
  @DisplayName("Test update order")
  public void updateOrderSuccessTest() {
    when(orderRepository.findById(any(Long.class))).thenReturn(Optional.of(new Order()));
    orderService.updateOrder(new UpdateOrderDto("COMPLETED", 1));
    verify(mapper, times(1)).toOrderDto(any());
    verify(orderRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("Test update db order")
  public void updateOrderTest() {
    orderService.updateOrder(new Order());
    verify(orderRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("Test delete order that doesn't exist")
  public void deleteOrderNotExistsTest() {
    when(orderRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ObjectNotFoundException.class,
        () -> orderService.deleteOrder(1));
  }

  @Test
  @DisplayName("Test delete order that doesn't exist")
  public void deleteOrderSuccessTest() {
    when(orderRepository.findById(any(Long.class))).thenReturn(Optional.of(new Order()));
    orderService.deleteOrder(1);
    verify(orderRepository, times(1)).deleteById(any());
  }

  @Test
  @DisplayName("Test get orders by ids")
  public void getOrdersByIdsTest() {
    List<Long> ids = List.of(1L, 2L);
    when(orderRepository.findAllById(any())).thenReturn(List.of(new Order(), new Order()));
    orderService.getOrdersByIds(ids);
    verify(mapper, times(2)).toOrderDto(any());
  }

  @Test
  @DisplayName("Test get order by id")
  public void getByIdTest() {
    when(orderRepository.findById(any())).thenReturn(Optional.of(new Order()));
    orderService.getById(1);
    verify(mapper, times(1)).toOrderDto(any());
  }

  @Test
  @DisplayName("Test get db order that doesn't exist by id")
  public void getDBOrderByIdNotExistsTest() {
    when(orderRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ObjectNotFoundException.class, () -> orderService.getDBOrderById(1));
  }

  @Test
  @DisplayName("Test get db order by id")
  public void getDBOrderByIdSuccessTest() {
    when(orderRepository.findById(any(Long.class))).thenReturn(
        Optional.of(new Order(1, 1, OrderStatus.CREATED, LocalDateTime.now(), new ArrayList<>())));
    Order dbOrderById = orderService.getDBOrderById(1);
    assertEquals(1, dbOrderById.getId());
  }

  @Test
  @DisplayName("Test get orders by statuses")
  public void getOrdersByStatusesTest() {
    List<String> statuses = List.of("PENDING", "COMPLETED");
    when(orderRepository.getByStatuses(any())).thenReturn(
        List.of(new Order(), new Order(), new Order()));
    orderService.getOrdersByStatuses(statuses);
    verify(mapper, times(3)).toOrderDto(any());
  }
}

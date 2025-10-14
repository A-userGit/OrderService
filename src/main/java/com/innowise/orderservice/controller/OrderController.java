package com.innowise.orderservice.controller;

import com.innowise.orderservice.dto.CreateOrderDto;
import com.innowise.orderservice.dto.CreateOrderedItemDto;
import com.innowise.orderservice.dto.OrderDto;
import com.innowise.orderservice.dto.UpdateOrderDto;
import com.innowise.orderservice.enums.OrderStatus;
import com.innowise.orderservice.service.CreateOrderService;
import com.innowise.orderservice.service.OrderService;
import com.innowise.orderservice.validation.annotation.EnumValid;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "order_security")
@RestController
@RequestMapping("/api/v1/orders/")
@RequiredArgsConstructor
public class OrderController {

  private final CreateOrderService createOrderService;
  private final OrderService orderService;

  @PostMapping("create")
  ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderDto createOrderDto) {
    OrderDto createdOrder = createOrderService.addOrder(createOrderDto);
    return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
  }

  @PatchMapping("update")
  ResponseEntity<OrderDto> updateOrder(@Valid @RequestBody UpdateOrderDto data) {
    OrderDto orderDto = orderService.updateOrder(data);
    return new ResponseEntity<>(orderDto, HttpStatus.OK);
  }

  @GetMapping("order")
  ResponseEntity<OrderDto> getById(@RequestParam(name = "id") Long id) {
    OrderDto orderById = orderService.getById(id);
    return new ResponseEntity<>(orderById, HttpStatus.OK);
  }

  @PostMapping("search")
  ResponseEntity<List<OrderDto>> getByIds(@RequestBody List<Long> ids) {
    List<OrderDto> orders = orderService.getOrdersByIds(ids);
    return new ResponseEntity<>(orders, HttpStatus.OK);
  }

  @DeleteMapping("delete")
  ResponseEntity<?> deleteById(@RequestParam(name = "id") Long id) {
    orderService.deleteOrder(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("order/item")
  ResponseEntity<OrderDto> addItem(@Valid @RequestBody CreateOrderedItemDto createOrderedItemDto) {
    OrderDto order = createOrderService.addItemToOrder(createOrderedItemDto);
    return new ResponseEntity<>(order, HttpStatus.CREATED);
  }

  @PostMapping("statuses/search")
  ResponseEntity<List<OrderDto>> getByStatuses(
      @Valid @RequestBody List<@EnumValid(enumClass = OrderStatus.class) String> statuses) {
    List<OrderDto> ordersByStatuses = orderService.getOrdersByStatuses(statuses);
    return new ResponseEntity<>(ordersByStatuses, HttpStatus.OK);
  }

}

package com.shop.orderservice.service.impl;

import com.shop.orderservice.config.feign.UserInfoFeignClient;
import com.shop.orderservice.dto.CreateOrderDto;
import com.shop.orderservice.dto.CreateOrderedItemDto;
import com.shop.orderservice.dto.OrderDto;
import com.shop.orderservice.dto.external.UserDto;
import com.shop.external.dto.kafka.CreatePaymentDto;
import com.shop.orderservice.entity.Item;
import com.shop.orderservice.entity.Order;
import com.shop.orderservice.entity.OrderedItem;
import com.shop.orderservice.exception.ObjectNotFoundException;
import com.shop.orderservice.kafka.CreateOrderProducer;
import com.shop.orderservice.service.CreateOrderService;
import com.shop.orderservice.service.ItemService;
import com.shop.orderservice.service.OrderService;
import com.shop.orderservice.service.OrderedItemService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateOrderServiceImpl implements CreateOrderService {

  private final OrderService orderService;
  private final ItemService itemService;
  private final OrderedItemService orderedItemService;
  private final UserInfoFeignClient feignClient;
  private final CreateOrderProducer createOrderProducer;

  @Override
  @Transactional
  public OrderDto addOrder(CreateOrderDto createOrderDto) {
    ResponseEntity<UserDto> userInfoFromUserService = feignClient.getUserInfoFromUserService(
        createOrderDto.getEmail());
    if (userInfoFromUserService.getBody() == null) {
      throw ObjectNotFoundException.entityNotFound("User", "email", createOrderDto.getEmail());
    }
    List<Item> activeItemsByIds = getItemsForOrder(createOrderDto);
    long id = userInfoFromUserService.getBody().getId();
    Order order = orderService.createOrder(createOrderDto, id);
    Map<Item, Integer> itemsToOrder = new HashMap<>();
    for (Item item : activeItemsByIds) {
      itemsToOrder.put(item, createOrderDto.getItems().get(item.getId()));
    }
    List<OrderedItem> orderedItems = orderedItemService.createOrderedItems(itemsToOrder, order);
    order.setItems(new ArrayList<>(orderedItems));
    sendPaymentRequest(order, orderedItems);
    return orderService.updateOrder(order);
  }

  @Override
  @Transactional
  public OrderDto addItemToOrder(CreateOrderedItemDto createOrderedItemDto) {
    Order order = orderService.getDBOrderById(createOrderedItemDto.getOrderId());
    Item item = itemService.getActiveDBById(createOrderedItemDto.getItemId());
    OrderedItem orderedItem = orderedItemService.createOrderedItem(createOrderedItemDto, order,
        item);
    order.getItems().add(orderedItem);
    return orderService.updateOrder(order);
  }

  private List<Item> getItemsForOrder(CreateOrderDto createOrderDto) {
    List<Item> activeItemsByIds = itemService.getActiveDBItemsByIds(
        createOrderDto.getItems().keySet().stream().toList());
    if (activeItemsByIds.size() != createOrderDto.getItems().size()) {
      createOrderDto.getItems().keySet()
          .removeAll(activeItemsByIds.stream().map(Item::getId).collect(
              Collectors.toSet()));
      throw ObjectNotFoundException.entitiesNotFound("item", "id",
          Arrays.asList(createOrderDto.getItems().keySet().toArray()));
    }
    return activeItemsByIds;
  }

  private void sendPaymentRequest(Order order, List<OrderedItem> items) {
    double sum = items.stream().mapToDouble(item -> item.getQuantity() * item.getItem().getPrice())
        .sum();
    CreatePaymentDto paymentDto = new CreatePaymentDto(order.getUserId(), order.getId(), sum);
    createOrderProducer.sendMessage(paymentDto);
  }
}

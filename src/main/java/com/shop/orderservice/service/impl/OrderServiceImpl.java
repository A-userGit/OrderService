package com.shop.orderservice.service.impl;

import com.shop.orderservice.config.feign.UserInfoFeignClient;
import com.shop.orderservice.dto.CreateOrderDto;
import com.shop.orderservice.dto.OrderDto;
import com.shop.orderservice.dto.UpdateOrderDto;
import com.shop.orderservice.dto.external.UserDto;
import com.shop.orderservice.entity.Order;
import com.shop.orderservice.enums.OrderStatus;
import com.shop.orderservice.exception.ObjectNotFoundException;
import com.shop.orderservice.mapper.OrderMapper;
import com.shop.orderservice.repository.OrderRepository;
import com.shop.orderservice.service.OrderService;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final static String OBJECT_NAME = "order";
  private final OrderRepository orderRepository;
  private final OrderMapper mapper;
  private final UserInfoFeignClient userInfoFeignClient;

  @Override
  @Transactional
  public Order createOrder(CreateOrderDto createOrderDto, long userId) {
    Order order = mapper.toOrder(createOrderDto);
    order.setUserId(userId);
    return orderRepository.save(order);
  }

  @Override
  @Transactional
  public OrderDto updateOrder(UpdateOrderDto updateOrderDto) {
    Optional<Order> byId = orderRepository.findById(updateOrderDto.getId());
    if(byId.isEmpty()){
      throw ObjectNotFoundException.entityNotFound(OBJECT_NAME, "id", updateOrderDto.getId());
    }
    Order order = byId.get();
    order.setStatus(OrderStatus.valueOf(updateOrderDto.getStatus()));
    Order saved = orderRepository.save(order);
    return mapper.toOrderDto(saved);
  }

  @Override
  public OrderDto updateOrder(Order order) {
    orderRepository.save(order);
    return mapper.toOrderDto(order);
  }

  @Override
  @Transactional
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  public void deleteOrder(long id) {
    Optional<Order> byId = orderRepository.findById(id);
    if(byId.isEmpty()){
      throw ObjectNotFoundException.entityNotFound(OBJECT_NAME, "id", id);
    }
    orderRepository.deleteById(id);
  }

  @Override
  public List<OrderDto> getOrdersByIds(List<Long> ids) {
    return orderRepository.findAllById(ids).stream().map(mapper::toOrderDto).toList();
  }

  @Override
  public OrderDto getById(long id) {
    return mapper.toOrderDto(getDBOrderById(id));
  }

  @Override
  public Order getDBOrderById(long id) {
    Optional<Order> byId = orderRepository.findById(id);
    if(byId.isEmpty()){
      throw ObjectNotFoundException.entityNotFound(OBJECT_NAME, "id", id);
    }
    return byId.get();
  }

  @Override
  public List<OrderDto> getOrdersByStatuses(List<String> statuses) {
    List<OrderStatus> orderStatuses = statuses.stream().map(OrderStatus::valueOf).toList();
    return orderRepository.getByStatuses(orderStatuses).stream().map(mapper::toOrderDto).toList();
  }

  @Override
  public List<OrderDto> getOrdersForUser(String email) {
    ResponseEntity<UserDto> userInfoFromUserService = userInfoFeignClient.getUserInfoFromUserService(email);
    if (userInfoFromUserService.getBody() == null) {
      throw ObjectNotFoundException.entityNotFound("User", "email", email);
    }
    return orderRepository.getByUserId(userInfoFromUserService.getBody().getId())
            .stream().map(mapper::toOrderDto).toList();
  }
}

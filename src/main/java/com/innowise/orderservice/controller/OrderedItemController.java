package com.innowise.orderservice.controller;

import com.innowise.orderservice.dto.OrderedItemDto;
import com.innowise.orderservice.dto.UpdateOrderedItemDto;
import com.innowise.orderservice.service.OrderService;
import com.innowise.orderservice.service.OrderedItemService;
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
@RequestMapping("/api/v1/ordered-items/")
@RequiredArgsConstructor
public class OrderedItemController {

  private final OrderedItemService orderedItemService;

  @PatchMapping("update")
  ResponseEntity<OrderedItemDto> updateOrderedItem(@Valid @RequestBody UpdateOrderedItemDto data) {
    OrderedItemDto orderedItemDto = orderedItemService.updateOrderedItem(data);
    return new ResponseEntity<>(orderedItemDto, HttpStatus.OK);
  }

  @GetMapping("ordered-item")
  ResponseEntity<OrderedItemDto> getById(@RequestParam(name = "id") Long id) {
    OrderedItemDto orderedItem = orderedItemService.getById(id);
    return new ResponseEntity<>(orderedItem, HttpStatus.OK);
  }

  @PostMapping("search")
  ResponseEntity<List<OrderedItemDto>> getByIds(@RequestBody List<Long> ids) {
    List<OrderedItemDto> orderedItems = orderedItemService.getByIds(ids);
    return new ResponseEntity<List<OrderedItemDto>>(orderedItems, HttpStatus.OK);
  }

  @DeleteMapping("delete")
  ResponseEntity<?> deleteById(@RequestParam(name = "id") Long id) {
    orderedItemService.deleteById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}

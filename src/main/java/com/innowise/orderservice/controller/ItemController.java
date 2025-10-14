package com.innowise.orderservice.controller;

import com.innowise.orderservice.dto.CreateItemDto;
import com.innowise.orderservice.dto.ItemDto;
import com.innowise.orderservice.service.ItemService;
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
@RequestMapping("/api/v1/items/")
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @PostMapping("create")
  ResponseEntity<ItemDto> createItem(@Valid @RequestBody CreateItemDto createItemDto) {
    ItemDto createdItem = itemService.createItem(createItemDto);
    return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
  }

  @PatchMapping("update")
  ResponseEntity<ItemDto> updateItem(@Valid @RequestBody ItemDto data) {
    ItemDto item = itemService.updateItem(data);
    return new ResponseEntity<>(item, HttpStatus.OK);
  }

  @GetMapping("item")
  ResponseEntity<ItemDto> getById(@RequestParam(name = "id") Long id) {
    ItemDto item = itemService.getActiveById(id);
    return new ResponseEntity<>(item, HttpStatus.OK);
  }

  @PostMapping("search")
  ResponseEntity<List<ItemDto>> getByIds(@RequestBody List<Long> ids) {
    List<ItemDto> items = itemService.getActiveItemsByIds(ids);
    return new ResponseEntity<>(items, HttpStatus.OK);
  }

  @DeleteMapping("delete")
  ResponseEntity<?> deleteById(@RequestParam(name = "id") Long id) {
    itemService.softDeleteItem(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}

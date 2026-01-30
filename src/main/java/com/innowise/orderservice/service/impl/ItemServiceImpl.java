package com.innowise.orderservice.service.impl;

import com.innowise.orderservice.dto.CreateItemDto;
import com.innowise.orderservice.dto.ItemDto;
import com.innowise.orderservice.entity.Item;
import com.innowise.orderservice.exception.ObjectAlreadyExistsException;
import com.innowise.orderservice.exception.ObjectNotFoundException;
import com.innowise.orderservice.mapper.ItemMapper;
import com.innowise.orderservice.repository.ItemRepository;
import com.innowise.orderservice.service.ItemService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

  private final ItemRepository itemRepository;
  private final ItemMapper mapper;

  @Override
  @Transactional
  public ItemDto createItem(CreateItemDto createItemDto) {
    Optional<Item> byName = itemRepository.findByName(createItemDto.getName());
    if (byName.isPresent()) {
      throw ObjectAlreadyExistsException.entityAlreadyExists("item", "name",
          createItemDto.getName());
    }
    Item saved = itemRepository.save(mapper.toItem(createItemDto));
    return mapper.toItemDto(saved);
  }

  @Override
  @Transactional
  public ItemDto updateItem(ItemDto updatingItemDto) {
    Optional<Item> byId = itemRepository.findById(updatingItemDto.getId());
    if (byId.isEmpty()) {
      throw ObjectNotFoundException.entityNotFound("item", "id", updatingItemDto.getId());
    }
    Item saved = itemRepository.save(mapper.toItem(updatingItemDto));
    return mapper.toItemDto(saved);
  }

  @Override
  @Transactional
  public void softDeleteItem(long id) {
    Optional<Item> byId = itemRepository.findById(id);
    if (byId.isEmpty()) {
      throw ObjectNotFoundException.entityNotFound("item", "id", id);
    }
    byId.get().setDeleted(true);
    itemRepository.save(byId.get());
  }

  @Override
  public List<ItemDto> getActiveItemsByIds(List<Long> ids) {
    return itemRepository.findAllActiveByIds(ids).stream().map(mapper::toItemDto).toList();
  }

  @Override
  public List<Item> getActiveDBItemsByIds(List<Long> ids) {
    return itemRepository.findAllActiveByIds(ids);
  }

  @Override
  public ItemDto getActiveById(long id) {
    return mapper.toItemDto(getActiveDBById(id));
  }

  @Override
  public Item getActiveDBById(long id) {
    Optional<Item> byId = itemRepository.findActiveById(id);
    if (byId.isEmpty()) {
      throw ObjectNotFoundException.entityNotFound("active item", "id", id);
    }
    return byId.get();
  }

  @Override
  public List<ItemDto> getItemsByAvailability(boolean isDeleted) {
    return itemRepository.findAllByAvailability(isDeleted).stream().map(mapper::toItemDto).toList();
  }
}

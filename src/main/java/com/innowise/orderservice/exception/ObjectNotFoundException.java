package com.innowise.orderservice.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ObjectNotFoundException extends RuntimeException {

  private final List<Object> missingObjectsIds;

  public ObjectNotFoundException(String message) {
    super(message);
    missingObjectsIds = new ArrayList<>();
  }

  public ObjectNotFoundException(String message, List<Object> ids) {
    super(message);
    missingObjectsIds = new ArrayList<>(ids);
  }

  public static ObjectNotFoundException entityNotFound(String type, String fieldName, Object id) {
    String message = String.format("%s with field %s and value %s not found", type, fieldName, id);
    return new ObjectNotFoundException(message);
  }

  public static ObjectNotFoundException entitiesNotFound(String type, String fieldName,
      List<Object> ids) {
    String message = String.format("%s with field %s and values %s not found", type, fieldName,
        ids.stream().reduce("", (partialString, id) -> partialString + " " + id));
    return new ObjectNotFoundException(message);
  }
}

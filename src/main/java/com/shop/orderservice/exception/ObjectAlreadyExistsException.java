package com.innowise.orderservice.exception;

public class ObjectAlreadyExistsException extends RuntimeException {

  public ObjectAlreadyExistsException(String message) {
    super(message);
  }

  public static ObjectAlreadyExistsException entityAlreadyExists(String type, String fieldName,
      Object id) {
    String message = String.format("%s with field %s and value %s already exists", type, fieldName, id);
    return new ObjectAlreadyExistsException(message);
  }

}

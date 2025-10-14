package com.innowise.orderservice.exception;

import feign.Response;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FeignClientException extends RuntimeException {

  private final Response.Body resposeBody;

  public FeignClientException(String message, Response.Body resposeBody) {
    super(message);
    this.resposeBody = resposeBody;
  }

  public static FeignClientException getServerException(String client, String uri,
      HttpStatus errorCode, Response.Body resposeBody) {
    String message = String.format("Server error %s during feign client %s call to uri %s",
        errorCode, client, uri);
    return new FeignClientException(message, resposeBody);
  }

  public static FeignClientException getClientException(String client, String uri,
      HttpStatus errorCode, Response.Body resposeBody) {
    String message = String.format("Client error %s during feign client %s call to uri %s",
        errorCode, client, uri);
    return new FeignClientException(message, resposeBody);
  }

  public static FeignClientException getGeneralException(String client, String uri,
      HttpStatus errorCode, Response.Body resposeBody) {
    String message = String.format("Unknown error %s during feign client %s call to uri %s",
        errorCode, client, uri);
    return new FeignClientException(message, resposeBody);
  }
}

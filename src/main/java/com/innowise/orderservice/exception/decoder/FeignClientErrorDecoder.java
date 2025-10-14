package com.innowise.orderservice.exception.decoder;

import com.innowise.orderservice.exception.FeignClientException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class FeignClientErrorDecoder implements ErrorDecoder {

  private final String clientName;

  @Override
  public Exception decode(String methodKey, Response response) {
    String requestUrl = response.request().url();
    Response.Body responseBody = response.body();
    HttpStatus responseStatus = HttpStatus.valueOf(response.status());
    if (responseStatus.is5xxServerError()) {
      return FeignClientException.getServerException(clientName, requestUrl, responseStatus, responseBody);
    } else if (responseStatus.is4xxClientError()) {
      return FeignClientException.getClientException(clientName, requestUrl, responseStatus, responseBody);
    } else {
      return FeignClientException.getGeneralException(clientName, requestUrl, responseStatus, responseBody);
    }
  }
}

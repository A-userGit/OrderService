package com.innowise.orderservice.unit.config.feign;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.innowise.orderservice.config.feign.AuthFeignInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(MockitoExtension.class)
public class AuthFeignInterceptorUnitTest {

  private AuthFeignInterceptor authFeignInterceptor = new AuthFeignInterceptor();

  @Test
  public void applyTest() {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("TestToken");
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    RequestTemplate template = new RequestTemplate();
    authFeignInterceptor.apply(template);
    Collection<String> strings = template.headers().get(HttpHeaders.AUTHORIZATION);
    assertTrue(strings.stream().anyMatch(header -> header.equals("TestToken")));
  }

  @Test
  public void applyNoAuthorizationTest() {
    RequestContextHolder.setRequestAttributes(null);
    RequestTemplate template = new RequestTemplate();
    authFeignInterceptor.apply(template);
    Collection<String> strings = template.headers().get(HttpHeaders.AUTHORIZATION);
    assertNull(strings);
  }
}
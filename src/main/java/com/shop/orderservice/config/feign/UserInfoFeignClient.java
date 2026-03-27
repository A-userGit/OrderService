package com.innowise.orderservice.config.feign;

import com.innowise.orderservice.dto.external.UserDto;
import jakarta.validation.constraints.Email;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.user.service.name}", url = "${feign.user.service.url}",
    configuration = FeignClientConfig.class)
public interface UserInfoFeignClient {

  @GetMapping(value = "${feign.user.service.info-endpoint}")
  ResponseEntity<UserDto> getUserInfoFromUserService(
      @RequestParam(name = "email") @Email String email);
}

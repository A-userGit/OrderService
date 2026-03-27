package com.shop.orderservice.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final SecurityProperties properties;

  private static final String[] AUTH_WHITE_LIST = {
          "/actuator/health/**",
          "/swagger-resources/**",
          "/swagger-ui/**",
          "/swagger-ui.html",
          "/v3/api-docs/**",
          "/v3/api-docs",
          "/webjars/**",
          "/favicon.ico",
          "/oauth2/**",
          "/login/**",
          "/api/v1/items/all/available"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(AUTH_WHITE_LIST).permitAll()
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer((oauth2) ->
            oauth2.jwt(withDefaults()));
    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return JwtDecoders.fromIssuerLocation(properties.getIssuerUri());
  }
}

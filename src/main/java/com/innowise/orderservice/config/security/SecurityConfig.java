package com.innowise.orderservice.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
      "/login/**"
  };

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.addAllowedHeader("Access-Control-Allow-Origin");
    config.addAllowedHeader("Authorization");
    config.addAllowedHeader("Content-Type");
    config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PATCH", "HEAD", "PUT", "DELETE"));
    config.setAllowedOriginPatterns(List.of("http://localhost:8080*", "http://localhost:8082*", "http://auth-service:8082*",
         "http://user-service:8080*", "http://order-service:8083*", "http://localhost:8083*"));
    config.setAllowCredentials(true);
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}

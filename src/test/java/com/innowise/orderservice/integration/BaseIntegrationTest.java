package com.innowise.orderservice.integration;

import com.innowise.orderservice.OrderServiceApplication;
import com.innowise.orderservice.integration.config.WireMockConfig;
import java.net.InetAddress;
import java.net.UnknownHostException;
import no.nav.security.mock.oauth2.MockOAuth2Server;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@Sql(
    scripts = {"classpath:sql/data.sql"},
    executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@SpringBootTest(classes = OrderServiceApplication.class)
public abstract class BaseIntegrationTest {

  @Container
  private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
      DockerImageName.parse("postgres:latest")).withReuse(true);

  private static MockOAuth2Server server;

  static {
    postgres.start();
    InetAddress authHost = null;
    try {
      authHost = InetAddress.getByName("auth-service");
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
    server = new MockOAuth2Server();
    server.start(authHost, 8082);
  }

  @DynamicPropertySource
  static void registerDBProperties(DynamicPropertyRegistry propertyRegistry) {
    propertyRegistry.add("integration-tests-db", postgres::getDatabaseName);
    propertyRegistry.add("spring.datasource.username", postgres::getUsername);
    propertyRegistry.add("spring.datasource.password", postgres::getPassword);
    propertyRegistry.add("spring.datasource.url", postgres::getJdbcUrl);
    propertyRegistry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
        () -> server.issuerUrl(".well-known/openid-configuration").toString());
    propertyRegistry.add("spring.security.oauth2.client.provider.my-oidc-provider.issuer-uri",
        () -> server.issuerUrl(".well-known/openid-configuration").toString());
  }
}

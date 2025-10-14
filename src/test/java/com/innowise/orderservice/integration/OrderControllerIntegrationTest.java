package com.innowise.orderservice.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.anyOf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.innowise.orderservice.dto.CreateOrderDto;
import com.innowise.orderservice.dto.CreateOrderedItemDto;
import com.innowise.orderservice.dto.UpdateOrderDto;
import com.innowise.orderservice.dto.external.UserDto;
import com.innowise.orderservice.enums.OrderStatus;
import com.innowise.orderservice.integration.config.WireMockProperties;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
@EnableFeignClients
public class OrderControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  WireMockServer server;

  @Autowired
  WireMockProperties wireMockProperties;

  @Test
  @WithMockUser
  @DisplayName("Integrational order get by id mvc test")
  public void getByIDTest() {
    try {
      mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders/order?id={id}", -1)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.status").value(OrderStatus.CREATED.name()));
    } catch (Exception e) {
      fail("Exception during order get by id mvc test " + e.getMessage());
    }
  }

  @Test
  @WithMockUser
  @DisplayName("Integrational orders get by statuses mvc test")
  public void getByStatusesTest() {
    try {
      List<String> statuses = List.of("CREATED", "PENDING");
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String listJSON = ow.writeValueAsString(statuses);
      mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/statuses/search")
              .with(csrf())
              .content(listJSON)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.[0].status", anyOf(is("CREATED"), is("PENDING"))))
          .andExpect(jsonPath("$.[1].status", anyOf(is("CREATED"), is("PENDING"))))
          .andExpect(jsonPath("$.[2].status", anyOf(is("CREATED"), is("PENDING"))));
    } catch (Exception e) {
      fail("Exception during orders get by statuses mvc test " + e.getMessage());
    }
  }

  @Test
  @WithMockUser
  @DisplayName("Integrational order create mvc test")
  public void createTest() {
    try {
      server.start();
      WireMock.configureFor(wireMockProperties.getName(), wireMockProperties.getPort());
      setupMockFeign();
      CreateOrderDto order = new CreateOrderDto();
      order.setEmail("test@mail.com");
      HashMap<String, Integer> items = new HashMap<>();
      items.put("-1", 10);
      items.put("-2", 12);
      order.setItems(items);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String orderJSON = ow.writeValueAsString(order);
      mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/create")
              .with(csrf())
              .content(orderJSON)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.userId").value(1));
    } catch (Exception e) {
      fail("Exception during order create mvc test " + e.getMessage());
    } finally {
      if (server.isRunning()) {
        server.stop();
      }
    }
  }

  @Test
  @WithMockUser
  @DisplayName("Integrational order update mvc test")
  public void updateTest() {
    try {
      UpdateOrderDto orderDto = new UpdateOrderDto("COMPLETED", -1);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String orderJSON = ow.writeValueAsString(orderDto);
      mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/orders/update")
              .with(csrf())
              .content(orderJSON)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.status").value("COMPLETED"))
          .andExpect(jsonPath("$.id").value(-1));
    } catch (Exception e) {
      fail("Exception during order update mvc test " + e.getMessage());
    }
  }

  @Test
  @WithMockUser
  @DisplayName("Integrational order delete mvc test")
  public void deleteTest() {
    try {
      mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/orders/delete")
              .with(csrf())
              .param("id", "-2")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().is(204));
    } catch (Exception e) {
      fail("Exception during order delete mvc test " + e.getMessage());
    }
  }

  @Test
  @WithMockUser
  @DisplayName("Integrational order get by id list mvc test")
  public void getByIdsTest() {
    try {
      List<Long> ids = List.of(-1L, -3L, -10L);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String idsJSON = ow.writeValueAsString(ids);
      mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/search")
              .with(csrf())
              .content(idsJSON)
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.[0].id").value(-1))
          .andExpect(jsonPath("$.[1].id").value(-3));
    } catch (Exception e) {
      fail("Exception during order get by id mvc test " + e.getMessage());
    }
  }

  @Test
  @WithMockUser
  @DisplayName("Integrational order add item mvc test")
  public void addItemToOrderTest() {
    try {
      CreateOrderedItemDto orderedItem = new CreateOrderedItemDto(12, -1, -1);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String orderedItemJSON = ow.writeValueAsString(orderedItem);
      mockMvc.perform(
              MockMvcRequestBuilders.post("/api/v1/orders/order/item")
                  .with(csrf())
                  .content(orderedItemJSON)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id").value(-1))
          .andExpect(jsonPath("$.items", hasSize(5)));
    } catch (Exception e) {
      fail("Exception during order add item mvc test " + e.getMessage());
    }
  }

  private void setupMockFeign() throws JsonProcessingException {
    UserDto userDto = new UserDto(1, "name", "surname");
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String userJSON = ow.writeValueAsString(userDto);
    server.stubFor(WireMock.get(WireMock.urlPathMatching(wireMockProperties.getMock()))
        .willReturn(WireMock.aResponse()
            .withStatus(HttpStatus.OK.value())
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withBody(userJSON)));
  }
}

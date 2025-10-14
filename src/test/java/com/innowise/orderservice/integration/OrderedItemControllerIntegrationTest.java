package com.innowise.orderservice.integration;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.innowise.orderservice.dto.CreateItemDto;
import com.innowise.orderservice.dto.ItemDto;
import com.innowise.orderservice.dto.UpdateOrderedItemDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
public class OrderedItemControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @WithMockUser
  @DisplayName("Integrational ordered item get by id mvc test")
  public void getByIDTest() {
    try {
      mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/ordered-items/ordered-item?id={id}", -1)
              .with(csrf())
              .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
          .andExpect(jsonPath("$.itemName").value("test1"));
    } catch (Exception e) {
      fail("Exception during ordered item get by id mvc test " + e.getMessage());
    }
  }

  @Test
  @WithMockUser
  @DisplayName("Integrational ordered item update mvc test")
  public void updateTest() {
    try {
      UpdateOrderedItemDto updateOrderedItemDto = new UpdateOrderedItemDto(600, -2);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String updJSON = ow.writeValueAsString(updateOrderedItemDto);
      mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/ordered-items/update").content(updJSON)
              .with(csrf())
              .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
          .andExpect(jsonPath("$.itemName").value("test2"))
          .andExpect(jsonPath("$.amount").value(600));
    } catch (Exception e) {
      fail("Exception during ordered item update mvc test " + e.getMessage());
    }
  }

  @Test
  @WithMockUser
  @DisplayName("Integrational ordered item delete mvc test")
  public void deleteTest() {
    try {
      mockMvc.perform(
          MockMvcRequestBuilders.delete("/api/v1/ordered-items/delete").param("id", "-4")
              .with(csrf())
              .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(204));
    } catch (Exception e) {
      fail("Exception during item delete mvc test " + e.getMessage());
    }
  }

  @Test
  @WithMockUser
  @DisplayName("Integrational ordered item get by id list mvc test")
  public void getByIdsTest() {
    try {
      List<Long> ids = List.of(-1L, -3L, -10L);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String idsJSON = ow.writeValueAsString(ids);
      mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/ordered-items/search").content(idsJSON)
              .with(csrf())
              .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
          .andExpect(jsonPath("$.[0].id").value(-1)).andExpect(jsonPath("$.[1].id").value(-3));
    } catch (Exception e) {
      fail("Exception during ordered item get by ids mvc test " + e.getMessage());
    }
  }
}

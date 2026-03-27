package com.innowise.orderservice.integration;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.innowise.orderservice.dto.CreateItemDto;
import com.innowise.orderservice.dto.ItemDto;
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
public class ItemControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @WithMockUser
  @DisplayName("Integrational item get by id mvc test")
  public void getByIDTest() {
    try {
      mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/items/item?id={id}", -1)
              .with(csrf())
              .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
          .andExpect(jsonPath("$.name").value("test1"));
    } catch (Exception e) {
      fail("Exception during item get by id mvc test " + e.getMessage());
    }
  }

  @Test
  @WithMockUser
  @DisplayName("Integrational item create mvc test")
  public void createTest() {
    try {
      CreateItemDto itemDto = new CreateItemDto("test12", 2300);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String itemJSON = ow.writeValueAsString(itemDto);
      mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/items/create").content(itemJSON)
              .with(csrf())
              .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
          .andExpect(jsonPath("$.name").value("test12")).andExpect(jsonPath("$.id").value(1));
    } catch (Exception e) {
      fail("Exception during item create mvc test " + e.getMessage());
    }
  }

  @Test
  @WithMockUser
  @DisplayName("Integrational item update mvc test")
  public void updateTest() {
    try {
      ItemDto itemDto = new ItemDto("updTest", 700, -3, false);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String itemJSON = ow.writeValueAsString(itemDto);
      mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/items/update").content(itemJSON)
              .with(csrf())
              .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
          .andExpect(jsonPath("$.name").value("updTest"));
    } catch (Exception e) {
      fail("Exception during item update mvc test " + e.getMessage());
    }
  }

  @Test
  @WithMockUser
  @DisplayName("Integrational item delete mvc test")
  public void deleteTest() {
    try {
      mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/items/delete").param("id", "-4")
          .with(csrf())
          .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(204));
    } catch (Exception e) {
      fail("Exception during item delete mvc test " + e.getMessage());
    }
  }

  @Test
  @WithMockUser
  @DisplayName("Integrational item get by id list mvc test")
  public void getByIdsTest() {
    try {
      List<Long> ids = List.of(-1L, -3L, -10L);
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
      String idsJSON = ow.writeValueAsString(ids);
      mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/items/search").content(idsJSON)
              .with(csrf())
              .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
          .andExpect(jsonPath("$.[0].id").value(-1)).andExpect(jsonPath("$.[1].id").value(-3));
    } catch (Exception e) {
      fail("Exception during item get by ids mvc test " + e.getMessage());
    }
  }
}

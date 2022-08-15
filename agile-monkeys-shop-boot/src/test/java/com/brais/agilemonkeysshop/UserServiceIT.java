package com.brais.agilemonkeysshop;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.agilemonkeys.shop.model.UserDTO;
import com.agilemonkeys.shop.model.UserStatusDTO;
import com.agilemonkeys.shop.model.UserTypeDTO;
import com.brais.agilemonkeysshop.rest.controller.user.mapper.UserApiMapper;
import com.brais.agilemonkeysshop.user.entity.User;
import com.brais.agilemonkeysshop.user.port.UserServicePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class UserServiceIT {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private UserServicePort userServicePort;

  @Autowired
  private UserApiMapper userApiMapper;

  @Autowired
  private MongoOperations mongoOperations;

  private String jwtToken;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeAll
  void setup() {
    setupDatabase();
  }

  @AfterAll
  void backup() {
    mongoOperations.remove(new Query(), "user");
    List<User> userList = mongoOperations.findAll(User.class, "userBackup");
    userList.forEach(user -> mongoOperations.insert(user, "user"));
  }

  public void setupDatabase() {
    if (mongoOperations.findAll(User.class, "user").isEmpty() && mongoOperations.collectionExists("userBackup")) {
      User adminUser = mongoOperations.findById("1", User.class, "userBackup");
      mongoOperations.insert(adminUser, "user");
    }
    if (mongoOperations.collectionExists("userBackup")) {
      mongoOperations.dropCollection("userBackup");
    }
    mongoOperations.createCollection("userBackup");
    List<User> userList = mongoOperations.findAll(User.class, "user");
    userList.forEach(user -> mongoOperations.insert(user, "userBackup"));
  }

  @Test
  public void when_callFindAllWithAuthentication_expect_returnSuccessfull() throws Exception {

    MvcResult mvcTokenResult = mvc.perform(post("/agile-monkeys/v1/public/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(User.builder().username("admin").password("agilemonkeysadmin").build())))
        .andExpect(status().isOk())
        .andReturn();
    String jsonTokenResult = mvcTokenResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
    jwtToken = JsonPath.parse(jsonTokenResult).read("$.jwtToken");

    MvcResult mvcResult = mvc.perform(get("/agile-monkeys/v1/user/list").header("Authorization",
                "Bearer " + jwtToken)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    String jsonResult = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
    List<UserDTO> userDTOList = JsonPath.parse(jsonResult).read("$.userList");

    assertFalse(userDTOList.isEmpty());
    assertEquals("1", JsonPath.parse(jsonResult).read("$.userList[0].id"));
    assertEquals("admin", JsonPath.parse(jsonResult).read("$.userList[0].username"));
    assertEquals("Admin", JsonPath.parse(jsonResult).read("$.userList[0].name"));
    assertEquals("Agile monkeys", JsonPath.parse(jsonResult).read("$.userList[0].surname"));
    assertEquals(UserTypeDTO.ADMIN.getValue(), JsonPath.parse(jsonResult).read("$.userList[0].userType"));
    assertEquals(UserStatusDTO.ACTIVE.getValue(), JsonPath.parse(jsonResult).read("$.userList[0].userStatus"));

  }

  @Test
  public void when_callCreateAndDeleteUserWithAuthentication_expect_returnSuccessful() throws Exception {

    User userToCreate = User.builder()
        .id("2")
        .username("testUser")
        .password("testPassword")
        .name("agile")
        .surname("monkeys")
        .build();

    MvcResult mvcTokenResult = mvc.perform(post("/agile-monkeys/v1/public/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(User.builder().username("admin").password("agilemonkeysadmin").build())))
        .andExpect(status().isOk())
        .andReturn();
    String jsonTokenResult = mvcTokenResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
    jwtToken = JsonPath.parse(jsonTokenResult).read("$.jwtToken");

    MvcResult mvcCreateResult = mvc.perform(post("/agile-monkeys/v1/user/create").header("Authorization",
                "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userToCreate)))
        .andExpect(status().isOk())
        .andReturn();
    String jsonCreateResult = mvcCreateResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
    String createdUserId = JsonPath.parse(jsonCreateResult).read("$.id");

    User createdUser = mongoOperations.findById(createdUserId, User.class, "user");

    mvc.perform(delete("/agile-monkeys/v1/user/delete/" + createdUserId).header("Authorization", "Bearer " + jwtToken)
        .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    User deletedUser = mongoOperations.findById(createdUserId, User.class, "user");

    assertNotNull(createdUser);
    assertEquals("2", createdUserId);
    assertNull(deletedUser);
  }
}

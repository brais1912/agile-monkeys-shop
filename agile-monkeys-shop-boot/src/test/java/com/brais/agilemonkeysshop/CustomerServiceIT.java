package com.brais.agilemonkeysshop;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

import com.agilemonkeys.shop.model.LiteCustomerDTO;
import com.brais.agilemonkeysshop.customer.entity.Customer;
import com.brais.agilemonkeysshop.customer.port.CustomerServicePort;
import com.brais.agilemonkeysshop.rest.controller.customer.mapper.CustomerApiMapper;
import com.brais.agilemonkeysshop.user.entity.User;
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
public class CustomerServiceIT {

  private static final String CUSTOMER_ID = "111";

  private static final String NAME = "testName";

  private static final String SURNAME = "testSurname";

  private static final String PHOTO = "./test/photo.jpg";

  private static final String CREATED_BY = "admin";

  private static final String UPDATED_BY = "admin";

  @Autowired
  private MockMvc mvc;

  @Autowired
  private CustomerServicePort customerServicePort;

  @Autowired
  private CustomerApiMapper customerApiMapper;

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
    mongoOperations.remove(new Query(), "customer");
    List<Customer> customerList = mongoOperations.findAll(Customer.class, "customerBackup");
    customerList.forEach(customer -> mongoOperations.insert(customer, "customer"));
  }

  public void setupDatabase() {
    if (mongoOperations.collectionExists("customerBackup")) {
      mongoOperations.dropCollection("customerBackup");
    }
    mongoOperations.createCollection("customerBackup");
    List<Customer> customerList = mongoOperations.findAll(Customer.class, "customer");
    customerList.forEach(customer -> mongoOperations.insert(customer, "customerBackup"));
    mongoOperations.remove(new Query(), "customer");

    mongoOperations.insert(Customer.builder()
            .id(CUSTOMER_ID)
            .name(NAME)
            .surname(SURNAME)
            .photo(PHOTO)
            .createdBy(CREATED_BY)
            .updatedBy(UPDATED_BY)
        .build(), "customer");
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

    MvcResult mvcResult = mvc.perform(get("/agile-monkeys/v1/customer/list").header("Authorization",
                "Bearer " + jwtToken)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    String jsonResult = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
    List<LiteCustomerDTO> customerDTOList = JsonPath.parse(jsonResult).read("$.customerList");

    assertFalse(customerDTOList.isEmpty());
    assertEquals(CUSTOMER_ID, JsonPath.parse(jsonResult).read("$.customerList[0].id"));
    assertEquals(NAME, JsonPath.parse(jsonResult).read("$.customerList[0].name"));
    assertEquals(SURNAME, JsonPath.parse(jsonResult).read("$.customerList[0].surname"));

  }
}

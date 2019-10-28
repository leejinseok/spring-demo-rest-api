package com.example.demo.events;

import com.example.demo.accounts.Account;
import com.example.demo.accounts.AccountRepository;
import com.example.demo.accounts.AccountRole;
import com.example.demo.accounts.AccountService;
import com.example.demo.common.AppProperties;
import com.example.demo.common.BaseController;
import com.example.demo.common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTest extends BaseController {

  @Autowired
  EventRepository eventRepository;

  @Autowired
  AccountService accountService;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  AppProperties appProperties;

  @Before
  public void setUp() {
    this.eventRepository.deleteAll();
    this.accountRepository.deleteAll();
  }

  @Test
  @TestDescription("정상적으로 이벤트를 생성하는 테스트")
  public void createEvent() throws Exception {
    EventDto event = EventDto.builder()
      .name("Spring")
      .description("REST API Development with Spring")
      .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
      .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
      .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
      .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
      .basePrice(100)
      .maxPrice(200)
      .limitOfEnrollment(100)
      .location("강남역 D2 스타텁 팩토리")
      .build();

    mockMvc.perform(post("/api/events/")
      .header(HttpHeaders.AUTHORIZATION, getBearerToken(true))
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaTypes.HAL_JSON)
      .content(objectMapper.writeValueAsString(event)))
      .andDo(print())
      .andExpect(status().isCreated())
      .andExpect(jsonPath("id").exists())
      .andExpect(header().exists(HttpHeaders.LOCATION))
      .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
      .andExpect(jsonPath("free").value(false))
      .andExpect(jsonPath("offline").value(true))
      .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));
  }
  private String getBearerToken(boolean needToCreateAccount) throws Exception {
    return "Bearer " + getAccessToken(needToCreateAccount);
  }

  private String getAccessToken(boolean needToCreateAccount) throws Exception {
    if (needToCreateAccount) {
      createAccount();
    }

    ResultActions perform = this.mockMvc.perform(post("/oauth/token")
      .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
      .param("username", appProperties.getUserUsername())
      .param("password", appProperties.getUserPassword())
      .param("grant_type", "password"));

    var responseBody = perform.andReturn().getResponse().getContentAsString();
    Jackson2JsonParser parser = new Jackson2JsonParser();
    return parser.parseMap(responseBody).get("access_token").toString();
  }

  private Account createAccount() {
    Account keesun = Account.builder()
      .email(appProperties.getUserUsername())
      .password(appProperties.getUserPassword())
      .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
      .build();
    return this.accountService.saveAccount(keesun);
  }
}

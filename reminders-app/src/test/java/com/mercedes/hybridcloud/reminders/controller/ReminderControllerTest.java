package com.mercedes.hybridcloud.reminders.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import com.mercedes.hybridcloud.reminders.domain.MessageResponse;
import com.mercedes.hybridcloud.reminders.domain.Reminder;
import com.mercedes.hybridcloud.reminders.exception.NoReminderFoundException;
import com.mercedes.hybridcloud.reminders.exception.ValidationException;
import com.mercedes.hybridcloud.reminders.factory.ReminderFactory;
import com.mercedes.hybridcloud.reminders.service.ReminderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@WebMvcTest(controllers = ReminderController.class)
class ReminderControllerTest {

  private static final String DUMMIES_PATH = "/dummies";
  private static final String FOO_ENDPOINT = DUMMIES_PATH + "/foo";
  private static final String ADD_ENDPOINT = "/api/addReminders";
  private static final String FETCH_ENDPOINT = "/api/fetchReminders";
  private static final String DELETE_ENDPOINT = "/api/deleteReminders";
  private static final String REQUEST_BODY_POST = "{\"date\": \"2021-10-03\",\"message\": \"First Reminder\", \"reminderId\":\"1\"}";
  private static final String REQUEST_BODY_INVALID_POST = "{\"date\": \"2021-10-03\",\"message\": \"First Reminder\"}";
  private static final String EXPECTED_RESPONSE = "src/test/resources/sample-response.json";


  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ReminderService reminderService;

  @Test
  void foo_with_valid_bar_returns_text_with_bar() throws Exception {
    final String expectedJson = "{'message': 'foo 1234'}";

    final MessageResponse mockResponse = new MessageResponse();
    mockResponse.setMessage("foo 1234");
    when(reminderService.foo(eq(1234))).thenReturn(mockResponse);

    mockMvc
        .perform(get(FOO_ENDPOINT).param("bar", "1234"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().json(expectedJson));
  }

  @Test
  void foo_without_bar_returns_text_without_bar() throws Exception {
    final String expectedJson = "{'message': 'foo'}";

    final MessageResponse mockResponse = new MessageResponse();
    mockResponse.setMessage("foo");
    when(reminderService.foo(eq(null))).thenReturn(mockResponse);

    mockMvc
        .perform(get(FOO_ENDPOINT))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().json(expectedJson));
  }

  @Test
  void foo_with_invalid_bar_returns_400() throws Exception {
    when(reminderService.foo(any())).thenThrow(new RuntimeException("invalid call to foo()"));

    mockMvc
        .perform(get(FOO_ENDPOINT).param("bar", "invalid input"))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldAddReminderForValidInput() throws Exception {
    final String expectedMessage = "Ok I have added <First Reminder> to your reminders for the date: 2021-10-03";

    Reminder r1 = ReminderFactory.getReminders().get(0);

    final MessageResponse mockResponse = new MessageResponse();
    mockResponse.setMessage(expectedMessage);
    when(reminderService.addReminders(r1)).thenReturn(mockResponse);

    mockMvc
            .perform(post(ADD_ENDPOINT).contentType("application/json").content(REQUEST_BODY_POST))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json("{'message': '" + expectedMessage + "'}"));
  }

  @Test
  void shouldThrow400ValidationExceptionForInvalidInput() throws Exception {

    when(reminderService.addReminders(any())).thenThrow(new ValidationException("Please include reminderId in the request body"));

    mockMvc
            .perform(post(ADD_ENDPOINT).contentType("application/json").content(REQUEST_BODY_INVALID_POST))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFetchReminderForValidInput() throws Exception {

    String expectedJson = Files.readString(Paths.get(EXPECTED_RESPONSE));
    List<Reminder> reminders = ReminderFactory.getReminders().subList(0,1);
    Page<Reminder> remindersPage = new PageImpl(reminders);

    when(reminderService.getReminders(any(),any(), any())).thenReturn(remindersPage);

    mockMvc
            .perform(get(FETCH_ENDPOINT).contentType("application/json").queryParam("date", "2021-10-03"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(expectedJson));
  }

  @Test
  void shouldThrow404NoReminderFoundExceptionForDataNotPresent() throws Exception {

    when(reminderService.getReminders(any(),any(), any())).thenThrow(new NoReminderFoundException("No Reminders found!"));

    mockMvc
            .perform(get(FETCH_ENDPOINT).contentType("application/json").queryParam("date", "2022-10-03"))
            .andDo(print())
            .andExpect(status().isNotFound());
  }

  @Test
  void shouldDeleteReminderForValidDate() throws Exception {
    final String expectedMessage = "Deleted the reminders set for the date: 2021-10-03";

    final MessageResponse mockResponse = new MessageResponse();
    mockResponse.setMessage(expectedMessage);
    when(reminderService.deleteReminders(any())).thenReturn(mockResponse);

    mockMvc
            .perform(delete(DELETE_ENDPOINT).contentType("application/json").queryParam("date", "2021-10-03"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json("{'message': '" + expectedMessage + "'}"));
  }

  @Test
  void shouldDeleteAllRemindersForNoParam() throws Exception {
    final String expectedMessage = "Ok,I have cleared all your reminders";

    final MessageResponse mockResponse = new MessageResponse();
    mockResponse.setMessage(expectedMessage);
    when(reminderService.deleteReminders(any())).thenReturn(mockResponse);

    mockMvc
            .perform(delete(DELETE_ENDPOINT).contentType("application/json"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json("{'message': '" + expectedMessage + "'}"));
  }

}

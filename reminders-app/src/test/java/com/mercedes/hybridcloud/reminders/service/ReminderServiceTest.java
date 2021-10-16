package com.mercedes.hybridcloud.reminders.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


import com.mercedes.hybridcloud.reminders.domain.MessageResponse;
import com.mercedes.hybridcloud.reminders.domain.Reminder;
import com.mercedes.hybridcloud.reminders.factory.ReminderFactory;
import com.mercedes.hybridcloud.reminders.repository.ReminderListingRepository;
import com.mercedes.hybridcloud.reminders.repository.ReminderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class ReminderServiceTest {


  @Mock
  private ReminderRepository reminderRepository;

  @Mock
  private ReminderListingRepository reminderListingRepository;

  @Mock
  private Pageable pageable;

  @InjectMocks
  private ReminderService reminderService;

  @Test
  void foo_with_valid_bar_returns_text_with_bar() {
    final MessageResponse foo = reminderService.foo(123);

    assertThat(foo.getMessage()).isEqualTo("foo 123");
  }

  @Test
  void foo_without_bar_returns_text_without_bar() {
    final MessageResponse foo = reminderService.foo(null);

    assertThat(foo.getMessage()).isEqualTo("foo");
  }

  @Test
  public void shouldGetAllRemindersForRequestWithoutParams() {

    List<Reminder> reminders = ReminderFactory.getReminders();
    Page<Reminder> remindersPage = new PageImpl(reminders);
    when(reminderRepository.findAll(pageable)).thenReturn(remindersPage);

    final Page<Reminder> reminderPage = reminderService.getReminders(null,null, pageable);
    assertEquals(2, reminderPage.getTotalElements());

  }

  @Test
  public void shouldGetRemindersForRequestWithOnlyKeyword() {

    String expectedReminder = "First Reminder";
    List<Reminder> reminders = ReminderFactory.getReminders().subList(0,1);
    Page<Reminder> remindersPage = new PageImpl(reminders);
    when(reminderListingRepository.findByKeywordSearch("first", pageable)).thenReturn(remindersPage);

    final Page<Reminder> reminderPage = reminderService.getReminders(null,"first", pageable);
    assertEquals(1, reminderPage.getTotalElements());
    List<String> message = reminderPage.get().map(Reminder::getMessage).collect(Collectors.toList());
    assertEquals(expectedReminder,message.get(0));
  }

  @Test
  public void shouldGetRemindersForRequestWithOnlyDate() {

    String expectedReminder = "Second Reminder";
    List<Reminder> reminders = ReminderFactory.getReminders().subList(1,2);
    LocalDate date = LocalDate.of(2021, 10, 4);
    when(reminderRepository.findByDate(date)).thenReturn(reminders);

    final Page<Reminder> reminderPage = reminderService.getReminders(date,null, pageable);
    assertEquals(1, reminderPage.getTotalElements());
    List<String> message = reminderPage.get().map(Reminder::getMessage).collect(Collectors.toList());
    assertEquals(expectedReminder,message.get(0));
  }

  @Test
  public void shouldGetRemindersForRequestWithDateAndKeyword() {

    String expectedReminder = "Second Reminder";
    List<Reminder> reminders = ReminderFactory.getReminders().subList(1,2);
    Page<Reminder> remindersPage = new PageImpl(reminders);
    LocalDate date = LocalDate.of(2021, 10, 4);
    when(reminderListingRepository.findReminderByDateAndKeyword(date, "second", pageable)).thenReturn(remindersPage);

    final Page<Reminder> reminderPage = reminderService.getReminders(date,"second", pageable);
    assertEquals(1, reminderPage.getTotalElements());
    List<String> message = reminderPage.get().map(Reminder::getMessage).collect(Collectors.toList());
    assertEquals(expectedReminder,message.get(0));
  }

}

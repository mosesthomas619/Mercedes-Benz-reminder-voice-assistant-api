package com.mercedes.hybridcloud.reminders.controller;

import com.mercedes.hybridcloud.reminders.domain.MessageResponse;
import com.mercedes.hybridcloud.reminders.domain.Reminder;
import com.mercedes.hybridcloud.reminders.domain.ReminderListWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface ReminderApi {

    public ResponseEntity<MessageResponse> foo(Integer bar);

    public ResponseEntity<MessageResponse> addReminder(Reminder reminder);

    public ResponseEntity<ReminderListWrapper> getReminder(LocalDate date, String keyword, Pageable pageable);

    public ResponseEntity<MessageResponse> deleteReminders(LocalDate date);


}

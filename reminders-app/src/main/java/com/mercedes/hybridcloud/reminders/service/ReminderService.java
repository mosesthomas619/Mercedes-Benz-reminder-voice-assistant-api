package com.mercedes.hybridcloud.reminders.service;


import com.mercedes.hybridcloud.reminders.domain.MessageResponse;
import com.mercedes.hybridcloud.reminders.domain.Reminder;
import com.mercedes.hybridcloud.reminders.exception.NoReminderFoundException;
import com.mercedes.hybridcloud.reminders.exception.ValidationException;
import com.mercedes.hybridcloud.reminders.repository.ReminderListingRepository;
import com.mercedes.hybridcloud.reminders.repository.ReminderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReminderService {

    /**
     * Returns a dummy response with the text "foo <bar>" or "foo" if {@code bar} is {@code null}.
     *
     * @param bar the number to be appended to foo.
     * @return a dummy response.
     */

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private ReminderListingRepository reminderListingRepository;


    public MessageResponse foo(Integer bar) {
        log.debug("Determining response for foo('{}').", bar);

        String message = "foo";
        if (bar != null) {
            message += " " + bar;
        }

        log.debug("Determined foo('{}') to be '{}'.", bar, message);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(message);

        return messageResponse;
    }


    public Page<Reminder> getReminders(LocalDate date, String keyword, Pageable pageable) {
        Page<Reminder> pageResult;
        if (null == date && (null == keyword || keyword.isEmpty())) {
            pageResult = reminderRepository.findAll(pageable);
        } else if (null == date) {
            pageResult = reminderListingRepository.findByKeywordSearch(keyword, pageable);
        } else if (null == keyword || keyword.isEmpty()) {
            pageResult = new PageImpl<>(reminderRepository.findByDate(date), pageable, reminderRepository.findByDate(date).size());
        } else {
            pageResult = reminderListingRepository.findReminderByDateAndKeyword(date, keyword, pageable);
        }
        if (pageResult.isEmpty()) {
            throw new NoReminderFoundException("No Reminders found!");
        }
        return pageResult;
    }

    public MessageResponse addReminders(Reminder reminder) {
        Optional<Reminder> existingReminder = reminderRepository.findById(reminder.getReminderId());
        reminderRepository.save(reminder);
        if (existingReminder.isPresent()) {
            return new MessageResponse("Existing Reminder with id: " + reminder.getReminderId() + " replaced with new one.");
        }
        return new MessageResponse("Ok I have added <" + reminder.getMessage() + "> to your reminders for the date: " + reminder.getDate().toString());
    }

    public MessageResponse deleteReminders(LocalDate date) {
        if (null == date) {
            reminderRepository.deleteAll();
            return new MessageResponse("Ok,I have cleared all your reminders");
        } else {
            List<Reminder> reminderList = reminderRepository.findByDate(date);
            if (reminderList.isEmpty()) {
                throw new NoReminderFoundException("No Reminders found to delete for the date: " + date.toString());
            }
            reminderList.forEach(reminder -> reminderRepository.delete(reminder));
            return new MessageResponse("Deleted the reminders set for the date:" + date.toString());
        }
    }

    public void validateReminder(Reminder reminder) {

        if (null == reminder.getReminderId() || reminder.getReminderId().isEmpty()) {
            throw new ValidationException("Please include reminderId in the request body");
        }
        if (null == reminder.getMessage() || reminder.getMessage().isEmpty()) {
            throw new ValidationException("Please include message in the request body");
        }
        if (null == reminder.getDate()) {
            throw new ValidationException("Please include date in the request body");
        }
    }

}

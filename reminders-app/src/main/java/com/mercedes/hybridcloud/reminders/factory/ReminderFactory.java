package com.mercedes.hybridcloud.reminders.factory;

import com.mercedes.hybridcloud.reminders.domain.Reminder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReminderFactory {

    public static List<Reminder> getReminders() {
        List<Reminder> reminders = new ArrayList<>();

        Reminder r1 = new Reminder();
        r1.setReminderId("1");
        LocalDate date1 = LocalDate.of(2021, 10, 3);
        r1.setDate(date1);
        r1.setMessage("First Reminder");

        Reminder r2 = new Reminder();
        r2.setReminderId("2");
        LocalDate date2 = LocalDate.of(2021, 10, 4);
        r2.setDate(date2);
        r2.setMessage("Second Reminder");

        reminders.add(r1);
        reminders.add(r2);

        return reminders;

    }

}

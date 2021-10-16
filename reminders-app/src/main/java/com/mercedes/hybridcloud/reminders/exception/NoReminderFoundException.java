package com.mercedes.hybridcloud.reminders.exception;

public class NoReminderFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoReminderFoundException(final String message) {
        super(message);
    }

}

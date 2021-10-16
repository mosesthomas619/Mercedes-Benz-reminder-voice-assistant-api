package com.mercedes.hybridcloud.reminders.controller;



import com.mercedes.hybridcloud.reminders.domain.MessageResponse;
import com.mercedes.hybridcloud.reminders.exception.NoReminderFoundException;
import com.mercedes.hybridcloud.reminders.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;



import java.util.UUID;



@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String SEMICOLON = "  ;  ";

	private static final String COLON = "  :  ";

	@ExceptionHandler(Exception.class)
	public ResponseEntity<MessageResponse> handleException(final Exception ex) {
		String errorRef = logError(ex);

		return new ResponseEntity<>(new MessageResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + COLON + errorRef), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ValidationException.class)
	protected ResponseEntity<MessageResponse> validationException(ValidationException ex) {
		String errorRef = logError(ex);
		return new ResponseEntity<>(new MessageResponse(ex.getMessage() + COLON + errorRef + ". Request body format : {date: yyyy-MM-dd, message: string, reminderId: string}"), HttpStatus.BAD_REQUEST);
	}




	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
															 HttpHeaders headers, HttpStatus status, WebRequest request) {
		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
		}
		String errorRef = logError(ex);
		return new ResponseEntity<Object>(new MessageResponse(status.getReasonPhrase() + COLON + errorRef), headers, status);
	}



	@ExceptionHandler(NoReminderFoundException.class)
	public ResponseEntity<MessageResponse> NoReminderFoundException(final NoReminderFoundException ex) {
		String errorRef = logError(ex);
		return new ResponseEntity<>(new MessageResponse(ex.getMessage() + COLON + errorRef), HttpStatus.NOT_FOUND);
	}


	private String logError(Exception ex) {
		String errorRef = "Error Reference: " + UUID.randomUUID();
		logger.error(errorRef, ex);
		return errorRef;
	}

}

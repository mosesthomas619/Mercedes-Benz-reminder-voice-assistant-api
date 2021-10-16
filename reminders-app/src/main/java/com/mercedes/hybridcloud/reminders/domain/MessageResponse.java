package com.mercedes.hybridcloud.reminders.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Used to make consistent response for json or xml.
 */

@JsonRootName(value = "message")
public class MessageResponse {

	/**
	 * The message.
	 */
	@JsonProperty
	private String message;

	public MessageResponse() {
	}

	/**
	 * Constructor to create a message to send in a controller response.
	 *
	 * @param message
	 * 		string representation of message to pass
	 */
	public MessageResponse(final String message) {
		setMessage(message);
	}

	//Getters and Setters

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MessageResponse{"
				+ "message='" + message + '\''
				+ '}';
	}

}

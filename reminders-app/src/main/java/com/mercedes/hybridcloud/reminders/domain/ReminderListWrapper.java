package com.mercedes.hybridcloud.reminders.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.data.domain.PageImpl;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder( {
		"listing"
		})
@JsonRootName(value = "Reminders")
// the below two lines put a wrapper around the json
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonTypeName("Reminders")
public class ReminderListWrapper {

	@JsonProperty("listing")
	private PageImpl<Reminder> listing;

	public ReminderListWrapper() {
	}

	public ReminderListWrapper(final PageImpl<Reminder> listing) {
		this.listing = listing;
	}

	public PageImpl<Reminder> getListing() {
		return listing;
	}

	public void setListing(final PageImpl<Reminder> listing) {
		this.listing = listing;
	}

	@Override
	public String toString() {
		return "ReminderListWrapper{"
				+ "listing="
				+ listing.toString()
				+ '}';
	}
}

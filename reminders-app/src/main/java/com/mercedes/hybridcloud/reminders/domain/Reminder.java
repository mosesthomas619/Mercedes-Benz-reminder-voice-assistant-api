package com.mercedes.hybridcloud.reminders.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@JsonPropertyOrder( {
        "id",
        "date",
        "message"
})
@Document(collection = "reminder")
public class Reminder {

    @Id
    @JsonProperty
    @NotNull(message = "Id cannot be null")
    @Field(value = "_id")
    private String reminderId;

    @NotNull(message = "date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("date")
    @JsonPropertyDescription("date of the reminder in YYYY-MM-DD format")
    private LocalDate date;

    @NotNull(message = "reminder cannot be null")
    private String message;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return Objects.equals(reminderId, reminder.reminderId) && Objects.equals(date, reminder.date) && Objects.equals(message, reminder.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reminderId, date, message);
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "reminderId='" + reminderId + '\'' +
                ", date=" + date +
                ", message='" + message + '\'' +
                '}';
    }
}

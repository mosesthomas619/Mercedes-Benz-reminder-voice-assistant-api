package com.mercedes.hybridcloud.reminders.controller;


import com.mercedes.hybridcloud.reminders.domain.MessageResponse;
import com.mercedes.hybridcloud.reminders.domain.Reminder;
import com.mercedes.hybridcloud.reminders.domain.ReminderListWrapper;
import com.mercedes.hybridcloud.reminders.service.ReminderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;

@Slf4j
@RestController
@Api(tags = "ReminderAPI")
public class ReminderController implements ReminderApi {

  private final ReminderService reminderService;

  @Autowired
  public ReminderController(ReminderService reminderService) {
    this.reminderService = reminderService;
  }



  @Override
  @RequestMapping(value = "/dummies/foo", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<MessageResponse> foo(@RequestParam(required = false, value = "bar") Integer bar) {
    log.debug("Received a request on path '/dummies/foo' with parameter bar='{}'.", bar);
    MessageResponse response = reminderService.foo(bar);
    return new ResponseEntity<>(
        response,
        HttpStatus.OK
    );
  }



  /*add reminders using a post call which accepts the Reminder object with id,date and message fields.
  All fields are mandatory */
  @Override
  @RequestMapping(value = "/api/addReminders", method = RequestMethod.POST, consumes = "application/json")
  public ResponseEntity<MessageResponse> addReminder(@Valid @RequestBody Reminder reminder) {
    reminderService.validateReminder(reminder);
    MessageResponse messageResponse = reminderService.addReminders(reminder);
    if(messageResponse.getMessage().contains("replaced")) {
      return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
    return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
  }




  /* Get reminders using a GET call. GET without any params give the entire list.
  GET with a keyword param gives the reminders which include that keyword(case insensitive).
  GET with date param gives the reminders for that particular day. Date format - yyyy-MM-dd
  GET with both the params return the reminders matching both keyword(case insensitive) and date.
  All request params are optional. Returns response as page. */
  @Override
  @RequestMapping(value = "/api/fetchReminders", method = RequestMethod.GET, produces = "application/json")
  @ApiImplicitParams( {
          @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                  value = "Results page you want to retrieve (0..N)"),
          @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                  value = "Number of records per page."),
          @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                  value = "Sorting criteria in the format: property(,asc|desc). "
                          + "Default sort order is ascending. "
                          + "Multiple sort criteria are supported.")
  })
  public ResponseEntity<ReminderListWrapper> getReminder(@RequestParam(required = false, value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date,
                                                         @RequestParam(required = false, value = "keyword") final String keyword,
                                                         final Pageable pageable) {

    return new ResponseEntity<>(new ReminderListWrapper((PageImpl<Reminder>) reminderService.getReminders(date, keyword, pageable)), HttpStatus.OK);

  }




  /* Deletes all reminders if date is not specified. Else deletes reminders only for that date. */
  @Override
  @RequestMapping(value = "/api/deleteReminders", method = RequestMethod.DELETE, produces = "application/json")
  public ResponseEntity<MessageResponse> deleteReminders(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date) {
    return new ResponseEntity<>(reminderService.deleteReminders(date), HttpStatus.OK);
  }

}

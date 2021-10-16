package com.mercedes.hybridcloud.reminders.repository;


import com.mercedes.hybridcloud.reminders.domain.Reminder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * This class is the list of mongo queries that return reminder objects.
 * NOTE: The @Query annotations will stop working if the attribute name changes in the database.
 */
@Repository
public interface ReminderRepository extends MongoRepository<Reminder, String> {

    List<Reminder> findByDate(LocalDate date);

}

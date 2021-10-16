package com.mercedes.hybridcloud.reminders.repository;


import com.mercedes.hybridcloud.reminders.domain.Reminder;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


@Component
public class ReminderListingRepository {

    private MongoTemplate mongoTemplate;


	@Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


	public Page<Reminder> findByKeywordSearch(String keyword, Pageable pageable ) {

		Criteria criteria = new Criteria();
		criteria = criteria.and("message").regex(keyword, "i");
		Query query = new Query().addCriteria(criteria).with(pageable);

		List<Reminder> reminders = mongoTemplate.find(query, Reminder.class);

		long total = mongoTemplate.count(query.skip(-1).limit(-1), Reminder.class);

		return new PageImpl<>(reminders, pageable, total);
	}





	public Page<Reminder> findReminderByDateAndKeyword(LocalDate date, String keyword, Pageable pageable ) {

		Criteria criteria = new Criteria();
		criteria = criteria.and("date").is(date);
		criteria = criteria.and("message").regex(keyword, "i");

		Query query = new Query().addCriteria(criteria).with(pageable);

		List<Reminder> reminders = mongoTemplate.find(query, Reminder.class);

		long total = mongoTemplate.count(query.skip(-1).limit(-1), Reminder.class);

		return new PageImpl<>(reminders, pageable, total);
	}


}

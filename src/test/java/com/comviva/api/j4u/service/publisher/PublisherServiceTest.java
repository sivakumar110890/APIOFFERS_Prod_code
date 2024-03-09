package com.comviva.api.j4u.service.publisher;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.comviva.api.j4u.model.TopicMessage;

public class PublisherServiceTest {

	J4UOfferEventPublisher eventPublisher;

	@Before
	public void setUp() throws Exception {
		eventPublisher = new J4UOfferEventPublisher();
		J4UOfferEventPublisher.startPublisher();
	}

	@After
	public void tearDown() throws Exception {
		J4UOfferEventPublisher.stopExecutorThread();
	}

	@Test
	public void testPublishEvent() throws Exception {
		TopicMessage message = new TopicMessage();
		message.setMessage(
				"{'MSISDN':'243123456789','RECH_VAL':'284500','TARIFF_ID':'12345','TRIGGER':'RECHARGE','RULE_TYPE':'DD','TRANSACTION_ID':'dsvsdvsdvdvdvdfgverg','TRIGGER_DATE':'2019-12-10'}");
		message.setTopic("queueName");
		eventPublisher.addEvent(message.getTopic(), message.getMessage());
	}
	
//	@Test
//	public void testPollQueue(){
//		eventPublisher.pollQueue();
//	}

}

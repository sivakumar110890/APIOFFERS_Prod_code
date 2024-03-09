package com.comviva.api.j4u.service.consumer;

import org.junit.Before;
import org.junit.Test;

public class J4UOfferConsumerTest {

	J4UOfferConsumer j4UOfferConsumer;
	private String groupId = "";
	private String topic = "";
	private int threads = 2;
	private int threadId = 0;
	@Before
	public void setUp() throws Exception {
		j4UOfferConsumer = new J4UOfferConsumer(groupId, topic, threads);
	}

	@Test
	public void onReceiveMPESA() {
		String message = "{\"SOURCE\":\"MPESA_QUARY_BAL\"}";
		j4UOfferConsumer.onReceive(message, threadId );
	}
	
	@Test
	public void onReceive() {
		String message = "{\"SOURCE\":\" \"}";
		j4UOfferConsumer.onReceive(message, threadId );
	}
	
	@Test
	public void onReceiveError() {
		String message = "";
		j4UOfferConsumer.onReceive(message, threadId );
	}

}

package com.comviva.api.j4u.service.consumer;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class ConsumerServiceTest {
	
	private static final Logger LOGGER = Logger.getLogger(ConsumerServiceTest.class);

    private ConsumerService consumerService;

    @Before
    public void setUp() throws Exception {
        consumerService = new ConsumerService();

    }

    @Test
    public void testStartJ4UConsumer() {
        try {
            consumerService.startConsumer();
            consumerService.stopConsumer();
        } catch (Exception e) {
        	LOGGER.error("Error in test :: ", e);
        }
    }

    @Test
    public void testStopJ4UConsumer() {
        try {
            consumerService.stopConsumer();
        } catch (Exception e) {
        	LOGGER.error("Error in test :: ", e);
        }
    }
    
    private Method forceStopAllConsumersMethod() throws NoSuchMethodException {
		Method method = ConsumerService.class.getDeclaredMethod("forceStopAllConsumers");
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testForceStopAllConsumers() {
		try {
			forceStopAllConsumersMethod().invoke(consumerService);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

}

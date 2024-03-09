package com.comviva.api.async;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class AppAsyncListenerTest {
	
	public static final Logger LOGGER = Logger.getLogger(AppAsyncListenerTest.class);

	private AppAsyncListener appAsyncListener;

	@Before
	public void setUp() throws Exception {
		appAsyncListener = new AppAsyncListener();
	}

	@Test
	public void testOnComplete() {
		try {
			appAsyncListener.onComplete(null);
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void testOnError() {
		try {
			appAsyncListener.onError(null);
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void testOnStartAsync() {
		try {
			appAsyncListener.onStartAsync(null);
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void testOnTimeout() {
		try {
			appAsyncListener.onTimeout(null);
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}

}

package com.comviva.api.wrapper;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class ServletOutputStreamWrapperTest {
	private static final Logger LOGGER = Logger.getLogger(ServletOutputStreamWrapperTest.class);

	private ServletOutputStreamWrapper servletOutputStreamWrapper;

	@Before
	public void setUp() throws Exception {
		servletOutputStreamWrapper = new ServletOutputStreamWrapper(null);
	}

	@Test
	public void testWrite() {
		try {
			servletOutputStreamWrapper.write(0);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetCopy() {
		servletOutputStreamWrapper.getCopy();
	}
	@Test
	public void testIsReady() {
		servletOutputStreamWrapper.isReady();
	}
	@Test
	public void testSetWriteListener() {
		servletOutputStreamWrapper.setWriteListener(null);
	}

}

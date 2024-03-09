package com.comviva.api.wrapper;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;



public class ResponseWrapperTest {
	
	private static final Logger LOGGER = Logger.getLogger(ResponseWrapperTest.class);
	
	private ResponseWrapper responseWrapper;
	
	@Before
	public void setUp() throws Exception {
		MockHttpServletResponse response = new MockHttpServletResponse();	
		HttpServletResponse res = (HttpServletResponse) response ;
		responseWrapper = new ResponseWrapper(res);
	}

	@Test
	public void testGetWriter() {
		try {
			responseWrapper.getWriter();
		} catch (IOException e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetOutputStream() {
		try {
			responseWrapper.getOutputStream();
		} catch (IOException e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testFlushBuffer() {
		try {
			responseWrapper.flushBuffer();
		} catch (IOException e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetCopy() {
		responseWrapper.getCopy();
	}

}

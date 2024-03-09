package com.comviva.api.utils;

import java.io.PrintWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.comviva.api.filters.RequestLoggingFilter;

public class ApiUtilsTest {

	ApiUtils apiUtils;
	@Before
	public void setUp() throws Exception {
		apiUtils = new ApiUtilsImpl();
	}

	@Test
	public void ResponseTypeText() {
		PrintWriter out = new PrintWriter(System.out);
		int errorCode = 2;
		String errorMsg = "Test error message";
		RequestLoggingFilter.setResponseType("text");
		apiUtils.returnResponse(out, errorCode, errorMsg);
	}
	
	@Test
	public void ResponseTypeXML() {
		PrintWriter out = new PrintWriter(System.out);
		int errorCode = 2;
		String errorMsg = "Test error message";
		RequestLoggingFilter.setResponseType("xml");
		apiUtils.returnResponse(out, errorCode, errorMsg);
	}
	
	@Test
	public void ResponseTypeJSON() {
		PrintWriter out = new PrintWriter(System.out);
		int errorCode = 2;
		String errorMsg = "Test error message";
		RequestLoggingFilter.setResponseType("json");
		apiUtils.returnResponse(out, errorCode, errorMsg);
	}
	
	@After
	public void reset(){
		RequestLoggingFilter.setResponseType("xml");
	}

}

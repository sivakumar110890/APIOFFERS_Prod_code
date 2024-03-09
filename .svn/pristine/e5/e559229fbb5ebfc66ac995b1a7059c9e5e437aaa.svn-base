package com.comviva.api.j4u.utils;

import static org.junit.Assert.*;
import static com.comviva.api.j4u.utils.J4UOfferConstants.*;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.comviva.api.j4u.config.PropertiesLoader;
import com.comviva.api.j4u.model.HTTPResponse;

public class HTTPSclientTest {

	private static final Logger LOGGER = Logger.getLogger(HTTPSclientTest.class);

	private static HTTPSclient httpSclient;

	@Before
	public void setup() {
		httpSclient = HTTPSclient.getInstance();
	}
	private String body ="<DistanceQueryReq>\r\n"
							+ "	<SessionId>24381140444120221411111024656</SessionId>\r\n"
							+ "	<SessionTime>20221411111024656</SessionTime>\r\n"
							+ "	<MSISDNA>243811404441</MSISDNA>\r\n"
							+ "	<MSISDNB>243811404441</MSISDNB>\r\n"
						+ "</DistanceQueryReq>";

	@Test
	public void sendRequestTestForPostRequest() throws Exception {
		try {
			String url = PropertiesLoader.getValue(URL_GET_CELL_ID);
			HTTPResponse actual = httpSclient.sendRequest(body, url, "POST");
			String expected = "HTTPResponse [status=-1, response=LOCATION_FAILURE, output=null, responseTime=0, sentTime=null, recvTime=null, responseJSON=null]";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}

	}


}

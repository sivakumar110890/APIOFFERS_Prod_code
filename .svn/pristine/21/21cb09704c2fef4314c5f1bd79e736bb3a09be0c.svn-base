package com.comviva.api.j4u.dao;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;

public class USSDDAOCallbackTest {

	private static final Logger LOGGER = Logger.getLogger(USSDDAOCallbackTest.class);
	private USSDDAOCallback ussddaoCallback;

	@Before
	public void setUp() throws Exception {
		ussddaoCallback = new USSDDAOCallback();
	}

	@Test
	public void testClientCallback() {
		ClientResponse clientResponse = new ClientResponse() {

			@Override
			public String getStatusString() {
				return null;
			}

			@Override
			public byte getStatus() {
				return 0;
			}

			@Override
			public VoltTable[] getResults() {
				return null;
			}

			@Override
			public int getClusterRoundtrip() {
				return 0;
			}

			@Override
			public long getClientRoundtripNanos() {
				return 0;
			}

			@Override
			public int getClientRoundtrip() {
				return 0;
			}

			@Override
			public String getAppStatusString() {
				return null;
			}

			@Override
			public byte getAppStatus() {
				return 0;
			}
		};
		try {
			ussddaoCallback.clientCallback(clientResponse);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

}

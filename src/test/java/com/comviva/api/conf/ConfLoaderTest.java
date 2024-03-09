package com.comviva.api.conf;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.comviva.api.exception.ConfigException;

public class ConfLoaderTest {
	
	public final static Logger LOGGER = Logger.getLogger(ConfLoaderTest.class);
	private ConfLoader confLoader = new ConfLoader("/APIOffers/config/ApiOffers.properties");

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void loadConfigurations() {
		try {
			confLoader.loadConfigurations();
		} catch (ConfigException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void loadConfigurations_Error() {
		ConfLoader confLoader = new ConfLoader("/APIOffers/config/voltdb.properties");
		try {
			confLoader.loadConfigurations();
		} catch (ConfigException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void loadConfigurations_Exception() {
		ConfLoader confLoader = new ConfLoader("Test");
		try {
			confLoader.loadConfigurations();
		} catch (ConfigException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void reLoadConfigurations() {
		try {
			confLoader.reLoadConfigurations();
		} catch (ConfigException e) {
			LOGGER.error(e);
		}
	}

}

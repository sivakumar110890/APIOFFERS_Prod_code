package com.comviva.api.j4u.config;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.comviva.api.exception.ConfigException;

public class PropertiesLoaderTest {

    private static final Logger LOGGER = Logger.getLogger(PropertiesLoaderTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testLoadEventProperties() {
		 try {
			PropertiesLoader.loadJ4UOfferProperty();
		} catch (ConfigException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void testGetValue() {
		 try {
				PropertiesLoader.getValue("");
		 } catch (Exception e) {
			 LOGGER.error(e);
		 }
	}

	@Test
	public void testGetIntValue() {
		try {
			PropertiesLoader.getIntValue("");
		 } catch (Exception e) {
			 LOGGER.error(e);
		 }
	}
	
	@Test
	public void getBooleanValue() {
		try {
			PropertiesLoader.getBooleanValue("");
		 } catch (Exception e) {
			 LOGGER.error(e);
		 }
	}

	@Test
	public void reloadProperties() {
		try {
			PropertiesLoader.reloadProperties();
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

}

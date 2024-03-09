package com.comviva.api.j4u.utils;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class TemplateCacheTest {

	public final static Logger LOGGER = Logger.getLogger(TemplateCacheTest.class);
	TemplateCache templateCache;
	@Before
	public void setUp() throws Exception {
		templateCache = TemplateCache.instance();
	}

//	@Test
//	public void get() {
//		String key = "TEMP_SW_J4UINELIGIBLE";
//		int actual = templateCache.get(key).getLangCd();
//		int expected = 4;
//		Assert.assertEquals(expected , actual);
//	}
//	
//	@Test
//	public void getMLMenu() {
//		String key = "ML_HOURLY_DATA_1OFFER_MENU_2";
//		int actual = templateCache.getMLMenu(key).getLangCd();
//		int expected = 2;
//		Assert.assertEquals(expected , actual);
//	}
	
	@Test
	public void init() {
		try {
			templateCache.init();
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

}

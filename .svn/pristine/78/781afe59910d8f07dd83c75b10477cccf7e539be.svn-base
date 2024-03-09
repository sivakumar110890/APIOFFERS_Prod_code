package com.comviva.api.j4u.utils;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.comviva.api.j4u.model.ProductInfo;

public class ProductInfoCacheTest {

	public final static Logger LOGGER = Logger.getLogger(ProductInfoCacheTest.class);
	ProductInfoCache productInfoCache;

	@Before
	public void setUp() throws Exception {
		productInfoCache = ProductInfoCache.instance();
	}

	@Test
	public void get() {
		String key = "611249_2";
		String expected = "4h6min + 100%Bonus night at $15,Vdc-Vdc,15D";
		ProductInfo actual = productInfoCache.get(key);
		// Assert.assertEquals(expected , actual.getProductDesc());
	}

	@Test
	public void getProductDesciption() {
		String key = "612135_2";
		String expected = "25 GB for 30 days at $50";
		ProductInfo actual = productInfoCache.get(key);
		// Assert.assertEquals(expected , actual.getProductDesc());

	}

	@Test
	public void getProductIdsList() {
		List<String> actual = productInfoCache.getProductIdsList("J4U Data");
		boolean expected = true;
		// Assert.assertEquals(expected , !actual.isEmpty());
	}

	@Test
	public void init() {
		ProductInfoCache productInfoCache = ProductInfoCache.instance();
	}

	@Test
	public void getLocationProdIds() {
		String category = "Data";
		String prodSubType = "J4U Data";
		String poolId = "PL_KIN_008";
		int langCode = 2;
		List<String> actual = productInfoCache.getLocationProdIds(category, prodSubType, poolId, langCode);
		boolean expected = true;
		//Assert.assertEquals(expected , !actual.isEmpty());
		
	}

	@Test
	public void testToString() {
		String actual = productInfoCache.toString();
		String expected = LocalDateTime.now().toString().substring(0, 10);
		assertTrue(expected.equals(actual.substring(20, 30)));
	}

}

package com.comviva.api.j4u.utils;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

   private Utils utils;
   
//   UtilsTest() throws Exception{
//	   utils = new Utils();
//   }

	@Test
	public void isNullOrEmpty_Empty() {
		Assert.assertEquals(true, Utils.isNullOrEmpty(""));
	}
	
	@Test
	public void isNullOrEmpty_Null() {
		Assert.assertEquals(true, Utils.isNullOrEmpty(null));
	}
	
	@Test
	public void isNullOrEmpty() {
		Assert.assertEquals(false, Utils.isNullOrEmpty("str"));
	}
	
	@Test
	public void convertToDBString() {
		Utils.convertToDBString("Test comment for utils testing ' test ' test 'test"
				+ "/Test comment for utils testing ' test ' test 'test/Test comment for utils testing ' test ' test 'test"
				+ "/Test comment for utils testing ' test ' test 'test/Test comment for utils testing ' test ' test 'test"
				+ "/Test comment for utils testing ' test ' test 'test/Test comment for utils testing ' test ' test 'test");
	}
	
	@Test
	public void getCurrentTimeStamp_Null() {
		Utils.convertToDBString(null);
	}
	
	@Test
	public void getCurrentTimeStamp1() {
		Utils.convertToDBString("comment");
	}

	@Test
	public void getCurrentTimeStamp() {
		Utils.getCurrentTimeStamp(); 
	}
	
	@Test
	public void getDateAsString() {
		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		Date date = new Date();
		Utils.getDateAsString(date, dateFormat);
	}
	
//	@Test
//	public void getTransactionID() {
//		utils.getTransactionID();
//	}
	
	@Test
	public void getProductCategory() {
		Utils.getProductCategory();
	}
	
	@Test
	public void setProductCategory() {
		Map<String, String> productCategory = new HashMap<String, String>();
		Utils.setProductCategory(productCategory);
	}
	
	@Test
	public void setMlRefreshFlagMap() {
		Map<String, String> mlRefreshFlagMap = new HashMap<String, String>();
		Utils.setMlRefreshFlagMap(mlRefreshFlagMap);
	}
	
	@Test
	public void getMlRefreshFlagMap() {
		Utils.getMlRefreshFlagMap();
	}
	@Test
	public void getSubCategoryType() throws Exception {
		String actual = Utils.getSubCategoryType("J4U Data");
		String expected = "J4U Data";
		assertTrue(actual.equals(expected));
	}
	
}

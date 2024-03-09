package com.comviva.api.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class DateFunctionTest {
	
	private static final Logger LOGGER = Logger.getLogger(DateFunctionTest.class);
	private String format = "yyyy-MM-dd";

	@Test
	public void getCurrentDateInFormat() {
		try {
			String actual = DateFunction.getCurrentDateInFormat(format);
			Calendar cal = Calendar.getInstance();
	        DateFormat dateFormat = new SimpleDateFormat(format);
	        String expected = dateFormat.format(cal.getTime());
			Assert.assertEquals(expected, actual);
			} catch (FunctionException e) {
			LOGGER.error(e);
		}
		
	}
	
	@Test
	public void getCurrentDateInFormat_Null() {
		try {
			DateFunction.getCurrentDateInFormat(null);
			} catch (FunctionException e) {
			LOGGER.error(e);
		}
		
	}
	
	@Test
	public void getDateInFormat_Null(){
		try {
			String actual = DateFunction.getDateInFormat(null, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getDateInFormat_FormatNull(){
		Date date = new Date();
		try {
			DateFunction.getDateInFormat(date, null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	@Test
	public void getDateInFormat_Empty(){
		Date date = new Date();
		String format = "";
		try {
			DateFunction.getDateInFormat(date, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getDateInFormat(){
		Date date = new Date();
		try {
			String actual = DateFunction.getDateInFormat(date, format);
			final Calendar cal = Calendar.getInstance();
		    cal.setTime(date);
		    final DateFormat dateFormat = new SimpleDateFormat(format.trim());
		    String expected = dateFormat.format(cal.getTime());
			Assert.assertEquals(expected, actual);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getDateInFormat_StartDateNull(){
		String expected = "0000-00-00";
		try {
			String actual = DateFunction.getDateInFormat(null, format, format);
			Assert.assertEquals(expected, actual);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getDateInFormat_inFormatNull(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getDateInFormat(strDate, null, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getDateInFormat_outFormatNull(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getDateInFormat(strDate, format, null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getDateInFormat_2(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getDateInFormat(strDate, format, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getDateInFormat_Error(){
		String strDate = "Date Error";
		try {
			DateFunction.getDateInFormat(strDate, format, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getDateInFormat_NoOfDays(){
		int noOfDays = 2;
		try {
			DateFunction.getDateInFormat(noOfDays, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getDateInFormat_NoOfDaysNull(){
		int noOfDays = 2;
		try {
			DateFunction.getDateInFormat(noOfDays, null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getPreviousDateInFormat(){
		try {
			DateFunction.getPreviousDateInFormat(format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getPreviousDateInFormat_null(){
		try {
			DateFunction.getPreviousDateInFormat(null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getPreviousDateInFormat_length(){
		String format = "";
		try {
			DateFunction.getPreviousDateInFormat(format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getPreviousDateInFormat_StartDateNull(){
		String expected = "0000-00-00";
		try {
			String actual = DateFunction.getPreviousDateInFormat(null, format, format);
			Assert.assertEquals(expected, actual);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getPreviousDateInFormat_inFormatNull(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getPreviousDateInFormat(strDate, null, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getPreviousDateInFormat_outFormatNull(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getPreviousDateInFormat(strDate, format, null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getPreviousDateInFormat_2(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getPreviousDateInFormat(strDate, format, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getPreviousDateInFormat_Error(){
		String strDate = "Date Error";
		try {
			DateFunction.getPreviousDateInFormat(strDate, format, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfWeek(){
		try {
			DateFunction.getStartDateOfWeek(format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfWeek_null(){
		try {
			DateFunction.getStartDateOfWeek(null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfWeek_length(){
		String format = "";
		try {
			DateFunction.getStartDateOfWeek(format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfWeek_StartDateNull(){
		String expected = "0000-00-00";
		try {
			String actual = DateFunction.getStartDateOfWeek(null, format, format);
			Assert.assertEquals(expected, actual);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfWeek_inFormatNull(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getStartDateOfWeek(strDate, null, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfWeek_outFormatNull(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getStartDateOfWeek(strDate, format, null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfWeek_2(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getStartDateOfWeek(strDate, format, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfWeek_Error(){
		String strDate = "date Error";
		try {
			DateFunction.getStartDateOfWeek(strDate, format, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfWeek_NoOfWeeks(){
		int noOfWeeks = 2;
		try {
			DateFunction.getStartDateOfWeek(noOfWeeks, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfWeek_NoOfWeekNull(){
		int noOfWeeks = 2;
		try {
			DateFunction.getStartDateOfWeek(noOfWeeks, null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfWeek_NoOfWeeks_2(){
		int noOfWeeks = 2;
		DateFunction.getStartDateOfWeek(noOfWeeks);
	}
	
	@Test
	public void getEndDateOfWeek(){
		try {
			DateFunction.getEndDateOfWeek(format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfWeek_null(){
		try {
			DateFunction.getEndDateOfWeek(null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfWeek_length(){
		String format = "";
		try {
			DateFunction.getEndDateOfWeek(format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfWeek_StartDateNull(){
		String expected = "0000-00-00";
		try {
			String actual = DateFunction.getEndDateOfWeek(null, format, format);
			Assert.assertEquals(expected, actual);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfWeek_inFormatNull(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getEndDateOfWeek(strDate, null, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfWeek_outFormatNull(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getEndDateOfWeek(strDate, format, null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfWeek_2(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getEndDateOfWeek(strDate, format, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfWeek_Error(){
		String strDate = "date Error";
		try {
			DateFunction.getEndDateOfWeek(strDate, format, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfWeek_NoOfWeeks(){
		int noOfWeeks = 2;
		try {
			DateFunction.getEndDateOfWeek(noOfWeeks, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfWeek_NoOfWeekNull(){
		int noOfWeeks = 2;
		try {
			DateFunction.getEndDateOfWeek(noOfWeeks, null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfWeek_NoOfWeeks_2(){
		try {
			DateFunction.getEndDateOfWeek(format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfMonth(){
		try {
			DateFunction.getStartDateOfMonth(format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfMonth_null(){
		try {
			DateFunction.getStartDateOfMonth(null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfMonth(){
		try {
			DateFunction.getEndDateOfMonth(format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfMonth_null(){
		try {
			DateFunction.getEndDateOfMonth(null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfMonth_length(){
		String format = "";
		try {
			DateFunction.getEndDateOfMonth(format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfMonth_StartDateNull(){
		String expected = "0000-00-00";
		try {
			String actual = DateFunction.getEndDateOfMonth(null, format, format);
			Assert.assertEquals(expected, actual);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfMonth_inFormatNull(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getEndDateOfMonth(strDate, null, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfMonth_outFormatNull(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getEndDateOfMonth(strDate, format, null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfMonth_2(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getEndDateOfMonth(strDate, format, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfMonth_Error(){
		String strDate = "date Error";
		try {
			DateFunction.getEndDateOfMonth(strDate, format, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfMonth_NoOfMonthss(){
		int noOfWeeks = 2;
		try {
			DateFunction.getEndDateOfMonth(noOfWeeks, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getEndDateOfMonth_NoOfMonthsNull(){
		int noOfWeeks = 2;
		try {
			DateFunction.getEndDateOfMonth(noOfWeeks, null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getDateFromString(){
		String date = "2020-02-02";
		Date expected = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			expected = new Date(format.parse(date).getTime());
			Date actual = DateFunction.getDateFromString(date);
			Assert.assertEquals(expected.toString(), actual.toString());
		} catch (ParseException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getDateFromString_Error(){
		String date = "date error";
		DateFunction.getDateFromString(date);
	}
	
	@Test
	public void getStringFromDate() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String expected = dateFormat.format(date);
		String actual = DateFunction.getStringFromDate(date);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getPreviousDate() {
	    DateFunction.getPreviousDate();
	}

	@Test
	public void getPreviousDate_nfd() {
		int noOfDays = 2; 
		DateFunction.getPreviousDate(noOfDays);
	}
	
	@Test
	public void getPreviousDateInMillis() {
		DateFunction.getPreviousDateInMillis();
	}
	
	@Test
	public void getDateInSecs_Null(){
		try {
			DateFunction.getDateInSecs(null, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
		
	}
	
	@Test
	public void getDateInSecs_NullFormat(){
		String dateStr = "2020-02-02";
		try {
			DateFunction.getDateInSecs(dateStr, null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
		
	}
	
	@Test
	public void getDateInSecs_EmptyFormat(){
		String dateStr = "2020-02-02";
		String format = "";
		try {
			DateFunction.getDateInSecs(dateStr, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
		
	}
	
	@Test
	public void getDateInSecs_Error(){
		String dateStr = "Date Error";
		try {
			DateFunction.getDateInSecs(dateStr, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
		
	}
	
	@Test
	public void getDateInSecs(){
		String dateStr = "2020-02-02";
		try {
			DateFunction.getDateInSecs(dateStr, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
		
	}
	
	@Test
	public void getCurrentDateInSecs(){
		Calendar cal = Calendar.getInstance();
	    Long expected =  cal.getTimeInMillis() / 1000;
	    Long actual = DateFunction.getCurrentDateInSecs();
	    Assert.assertEquals(expected, actual);
		
	}
	
	@Test
	public void getPreviousDateInSecs(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
	    Long expected =  cal.getTimeInMillis() / 1000;
	    Long actual = DateFunction.getPreviousDateInSecs();
		Assert.assertEquals(expected, actual);
		
	}
	
	@Test
	public void getCurrentDateInMillis(){
		DateFunction.getCurrentDateInMillis();
		
	}
	
	@Test
	public void getStartDateOfMonth_NoOf() {
		int noOfMonths = 2;
		DateFunction.getStartDateOfMonth(noOfMonths);
		}
	
	@Test
	public void getStartDateOfMonth_Format() {
		int noOfMonths = 2;
		try {
			DateFunction.getStartDateOfMonth(noOfMonths,format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
		}
	
	@Test
	public void getStartDateOfMonth_NullFormat() {
		int noOfMonths = 2;
		try {
			DateFunction.getStartDateOfMonth(noOfMonths,null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
		}
	
	@Test
	public void getStartDateOfMonth_EmptyFormat() {
		int noOfMonths = 2;
		String format = "";
		try {
			DateFunction.getStartDateOfMonth(noOfMonths,format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
		}
	
	@Test
	public void getStartDateOfMonth_StartDateNull(){
		String expected = "0000-00-00";
		try {
			String actual = DateFunction.getStartDateOfMonth(null, format, format);
			Assert.assertEquals(expected, actual);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfMonth_inFormatNull(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getStartDateOfMonth(strDate, null, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfMonth_outFormatNull(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getStartDateOfMonth(strDate, format, null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfMonth_2(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getStartDateOfMonth(strDate, format, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getStartDateOfMonth_Error(){
		String strDate = "date Error";
		try {
			DateFunction.getStartDateOfMonth(strDate, format, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getPreviousDateInFormat_Format() {
		Date date = new Date();
		try {
			DateFunction.getPreviousDateInFormat(date,format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
		}
	
	@Test
	public void getPreviousDateInFormat_EmptyFormat() {
		try {
			DateFunction.getPreviousDateInFormat(null,format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
		}
	
	
	@Test
	public void getDateInMillis_StartDateNull(){
		try {
			DateFunction.getDateInMillis(null, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getDateInMillis_formatNull(){
		String strDate = "2020-02-02";
		try {
			DateFunction.getDateInMillis(strDate, null);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getDateInMillis_formatEmpty(){
		String strDate = "2020-02-02";
		String format = "";
		try {
			DateFunction.getDateInMillis(strDate, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getDateInMillis_2(){
		DateFunction dateFunction = new DateFunction();
		String strDate = "2020-02-02";
		try {
			DateFunction.getDateInMillis(strDate, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void getDateInMillis_Error(){
		String strDate = "date Error";
		try {
			DateFunction.getDateInMillis(strDate, format);
		} catch (FunctionException e) {
			LOGGER.error(e);
		}
	}
}
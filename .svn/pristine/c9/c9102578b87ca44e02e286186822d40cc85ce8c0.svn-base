package com.comviva.api.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class WriteEDRTest {

	private static final Logger LOGGER = Logger.getLogger(WriteEDRTest.class);
	
	private  static   WriteEDR writeEDR;
	
	private String edrGenerationFolderPath = "/APIOffers";
    private String fileName = "test";
    private int totalRecordsPerFile = 600;
    private  long sizeForFileRotation = 1l;

	@Before
	public void setUp() {
		writeEDR = WriteEDR.getInstance(fileName, edrGenerationFolderPath, totalRecordsPerFile, sizeForFileRotation, null);
		writeEDR = null;
		writeEDR = WriteEDR.getInstance(fileName, edrGenerationFolderPath, totalRecordsPerFile, sizeForFileRotation);
	}
	

	@Test
	public void testGenerateEDR() {
		List<String> edrList = new ArrayList<>();
		edrList.add("For Testing");
		edrList.add("For Testing");
		edrList.add("For Testing");
		writeEDR.generateEDR(edrList);
	}

	private Method getWriteHeaderMethod() throws NoSuchMethodException {
		Method method = WriteEDR.class.getDeclaredMethod("writeHeader");
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testWriteHeader() {
		try {
			getWriteHeaderMethod().invoke(writeEDR, null);
		} catch (IllegalAccessException e) {
			LOGGER.error("Error in test :: ", e);
		} catch (IllegalArgumentException e) {
			LOGGER.error("Error in test :: ", e);
		} catch (InvocationTargetException e) {
			LOGGER.error("Error in test :: ", e);
		} catch (NoSuchMethodException e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	private Method makeFileExtensionMethod() throws NoSuchMethodException {
		Method method = WriteEDR.class.getDeclaredMethod("makeFileExtension");
		method.setAccessible(true);
		return method;
	}
	
	@Test
	public void testMakeFileExtension() {
		try {
			makeFileExtensionMethod().invoke(writeEDR, null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

}

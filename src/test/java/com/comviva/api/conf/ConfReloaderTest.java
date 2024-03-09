package com.comviva.api.conf;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ConfReloaderTest {

	private ConfReloader confReloader = new ConfReloader();
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void getAbsoluteConfFilePathsForTracking() {
		byte key = 0;
		String absoluteConfFilePath = "Test";
		confReloader.registerAbsoluteConfFilePathForTracking(key, absoluteConfFilePath);
		Hashtable<Byte, String> actual = confReloader.getAbsoluteConfFilePathsForTracking();
		assertEquals(true, !actual.isEmpty());
	}
	
	@Test
	public void setConfigurationLoader() {
		confReloader.setConfigurationLoader(new ConfLoader("/APIOffers/config/ApiOffers.properties"));
	}
	
	@Test
	public void reloadConfigurationFromConfFile_Empty() {
		confReloader.reloadConfigurationFromConfFile(new ArrayList<>());
	}
	
	@Test
	public void reloadConfigurationFromConfFile() {
		List<Byte> fileId = new ArrayList<>();
		fileId.add((byte) 1);
		confReloader.reloadConfigurationFromConfFile(fileId);
	}

}

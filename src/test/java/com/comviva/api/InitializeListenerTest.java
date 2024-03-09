package com.comviva.api;

import org.junit.Before;
import org.junit.Test;

 

public class InitializeListenerTest {
	
	private InitializeListener initializeListener;
	
	@Before
	public void setUp() throws Exception {
		initializeListener = new InitializeListener();
	}

	@Test
	public void testcontextInitialized() {
		initializeListener.contextInitialized(null);
	}

	@Test
	public void testcontextDestroyed() {
		initializeListener.contextDestroyed(null);
	}
	

}

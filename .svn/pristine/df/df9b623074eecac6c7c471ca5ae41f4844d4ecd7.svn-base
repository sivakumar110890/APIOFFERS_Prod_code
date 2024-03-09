package com.comviva.api.j4u.dao;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.voltdb.client.Client;

import com.comviva.voltdb.factory.DAOFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DAOFactory.class })
@SuppressStaticInitializationFor("com.comviva.voltdb.factory.DAOFactory")
public class PEDUpdateDAOTest {
	
	private static final Logger LOGGER = Logger.getLogger(PEDUpdateDAOTest.class);
	private PEDUpdateDAO pedUpdateDAO;
	
	@Mock
	private UpdaterDAO updaterDAO;
	@Mock
	private Client voltDbClient;
	
	public static void setupBeforeClass() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);

	}

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);
		pedUpdateDAO = new PEDUpdateDAO();
	}



	@Test
	public void testInsertIntoPlayHistory() {
		String msisdn = "243819708618"; 
		String prizeId = "328738";
		String status = "success";
		try {
			pedUpdateDAO.insertIntoPlayHistory(msisdn, prizeId, status);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testInsertIntoPlayHistoryEcap() {
		String msisdn = "243819708618"; 
		String prizeId = "328738";
		String status = "success";
		String txnId = "484939";
		try {
			pedUpdateDAO.insertIntoPlayHistoryEcap(msisdn, prizeId, status, txnId);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testUpsertPrizeRewardCount() { 
		String prizeId = "328738";
		try {
			pedUpdateDAO.upsertPrizeRewardCount(0, prizeId);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testIUpsertPedPlayedCountByMsisdn() {
		String msisdn = "243819708618"; 
		try {
			pedUpdateDAO.upsertPedPlayedCountByMsisdn(msisdn, 0);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

}

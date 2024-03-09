package com.comviva.api.j4u.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.voltdb.client.Client;

import com.comviva.api.j4u.model.ApiLog;
import com.comviva.api.j4u.model.RAGAndSAGUserInfo;
import com.comviva.voltdb.factory.DAOFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DAOFactory.class })
@SuppressStaticInitializationFor("com.comviva.voltdb.factory.DAOFactory")
public class UpdaterDAOTest {

	private static final Logger LOGGER = Logger.getLogger(UpdaterDAOTest.class);

	@Mock
	private Client voltDbClient;

	UpdaterDAO updaterDAO;
	
	private Map<String, String> sagUserRecord;
	private Map<String, String> ragUserRecord;

	@BeforeClass
	public static void setupBeforeClass() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);

	}

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);
		updaterDAO = new UpdaterDAO();
		
		ragUserRecord = new HashMap<>();
		ragUserRecord.put("MSISDN", null);
		ragUserRecord.put("WEEK_START_DATE", null);
		ragUserRecord.put("WEEK_END_DATE", null);
		ragUserRecord.put("NEXT_AVAILABLE_OFFER_DATE", null);
		ragUserRecord.put("REWARD_CODE", null);
		ragUserRecord.put("TARGET_TYPE", null);
		ragUserRecord.put("PAYMENT_METHOD", null);
		ragUserRecord.put("RECHARGE_TARGET", null);
		
		sagUserRecord = new HashMap<>();
		sagUserRecord.put("MSISDN", null);
		sagUserRecord.put("WEEK_START_DATE", null);
		sagUserRecord.put("WEEK_END_DATE", null);
		sagUserRecord.put("NEXT_AVAILABLE_OFFER_DATE", null);
		sagUserRecord.put("REWARD_CODE", null);
		sagUserRecord.put("TARGET_TYPE", null);
		sagUserRecord.put("PAYMENT_METHOD", null);
		sagUserRecord.put("SPEND_TARGET", null);
	}

	@Test
	public void upsertMLOfferMsg() {
		String msisdn = "12345";
		String subMenuType = "Test";
		String prodIds = "Test";
		String messageBody = "Test";
		String prodEvs = "Test";
		try {
			updaterDAO.upsertMLOfferMsg(msisdn, subMenuType, prodIds, messageBody, prodEvs);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void updateReducedCCR() {
		String subscriberId = "243123456789";
		String offerRefFlag = "Test";
		try {
			updaterDAO.updateReducedCCR(offerRefFlag, subscriberId);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void insertApiLog() {
		ApiLog apiLog = new ApiLog();
		apiLog.setApiType("Test");
		apiLog.setCellId("tset");
		apiLog.setChannel("test");
		apiLog.setComments("Test Log");
		apiLog.setCustomerBalance("test");
		apiLog.setDateTime(new Date());
		apiLog.setMlFlag("T");
		apiLog.setMsisdn("12345");
		apiLog.setPoolId("Tets");
		apiLog.setProdIds("Test");
		apiLog.setProdType("Test");
		apiLog.setRandomFlag("Test");
		apiLog.setRefNum("test");
		apiLog.setSelectedProdId("test");
		apiLog.setStatus("test");
		apiLog.setThirdPartyRef("test");
		apiLog.setTransactionId("test");
		updaterDAO.insertApiLog(apiLog);
	}

	@Test
	public void upsertNonSocialMLOfferMsg() {
		String msisdn = "243810525327";
		String subMenuType = "data";
		String prodSubType = "J4U data";
		String prodIds = "4893991";
		String messageBody = "messageBody";
		String prodEvs = "prodEvs";
		try {
			updaterDAO.upsertNonSocialMLOfferMsg(msisdn, subMenuType, prodSubType, prodIds, messageBody, prodEvs);
			;
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void upsertNonSocialMLOfferMsg1() {
		String msisdn = "243810525327";
		String subMenuType = "data";
		String prodSubType = null;
		String prodIds = "4893991";
		String messageBody = "messageBody";
		String prodEvs = "prodEvs";
		try {
			updaterDAO.upsertNonSocialMLOfferMsg(msisdn, subMenuType, prodSubType, prodIds, messageBody, prodEvs);
			;
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void insertRagUserRecordDB() {
		
		try {
			updaterDAO.insertRagUserRecordDB(ragUserRecord);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}

	}

	@Test
	public void insertSagUserRecordDB() {
		
		try {
			updaterDAO.insertSagUserRecordDB(sagUserRecord);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void insertRagUserCatFile() {
		String msisdn = "243810525327";
		try {
			updaterDAO.insertRagUserCatFile(ragUserRecord, msisdn);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void insertSagUserCatFile() {
		String msisdn = "243810525327";
		try {
			updaterDAO.insertSagUserCatFile(sagUserRecord, msisdn);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void updateRAGOptInfo() {
		String msisdn = "243810525327";
		RAGAndSAGUserInfo userInfo = new RAGAndSAGUserInfo();
		userInfo.setMsisdn(msisdn);
		userInfo.setRagNeverOptInFlag(true);
		try {
			updaterDAO.updateRAGOptInfo(userInfo);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void updateRAGOptInfo1() {
		String msisdn = "243810525327";
		RAGAndSAGUserInfo userInfo = new RAGAndSAGUserInfo();
		userInfo.setMsisdn(msisdn);
		userInfo.setRagNeverOptInFlag(false);
		try {
			updaterDAO.updateRAGOptInfo(userInfo);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void insertAlreadyOptInUserRAG() {
		String msisdn = "243810525327";
		RAGAndSAGUserInfo userInfo = new RAGAndSAGUserInfo();
		userInfo.setMsisdn(msisdn);
		userInfo.setRagNeverOptInFlag(true);
		try {
			updaterDAO.insertAlreadyOptInUserRAG(userInfo);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void upsertAlreadyOptInUserSAG() {
		String msisdn = "243810525327";
		RAGAndSAGUserInfo userInfo = new RAGAndSAGUserInfo();
		userInfo.setMsisdn(msisdn);
		userInfo.setRagNeverOptInFlag(true);
		try {
			updaterDAO.upsertAlreadyOptInUserSAG(userInfo);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void updateSAGOptInfo() {
		String msisdn = "243810525327";
		RAGAndSAGUserInfo userInfo = new RAGAndSAGUserInfo();
		userInfo.setMsisdn(msisdn);
		userInfo.setRagNeverOptInFlag(true);
		try {
			updaterDAO.updateSAGOptInfo(userInfo);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void updateSAGOptInfo1() {
		String msisdn = "243810525327";
		RAGAndSAGUserInfo userInfo = new RAGAndSAGUserInfo();
		userInfo.setMsisdn(msisdn);
		userInfo.setRagNeverOptInFlag(false);
		try {
			updaterDAO.updateSAGOptInfo(userInfo);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

}

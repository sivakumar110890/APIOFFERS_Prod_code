package com.comviva.api.j4u.service;

import static com.comviva.api.j4u.utils.J4UOfferConstants.CATEGORY;
import static com.comviva.api.j4u.utils.J4UOfferConstants.LANGUAGE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.PRIZE_WON;
import static com.comviva.api.j4u.utils.J4UOfferConstants.PRODUCT_VALUE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.RECHARGE_TARGET;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_NUM;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REMAINING_EFFORT;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TRANSACTION_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.WEEK_END_DATE;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.voltdb.client.Client;

import com.comviva.api.j4u.dao.LookUpDAO;
import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.RAGAndSAGUserInfo;
import com.comviva.api.j4u.model.UserInfo;
import com.comviva.voltdb.factory.DAOFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DAOFactory.class })
@SuppressStaticInitializationFor("com.comviva.voltdb.factory.DAOFactory")
public class RAGOfferServiceTest {

	private static final Logger LOGGER = Logger.getLogger(RAGOfferServiceTest.class);

	@Mock
	private LookUpDAO lookUpDAO;
	@Mock
	private UpdaterDAO updaterDAO;
	@Mock
	private Client voltDbClient;

	@Mock
	private UserInfo userInfo;
	@Mock
	private RAGAndSAGUserInfo ragandsagUserInfo;

	@InjectMocks
	RAGOfferService ragOfferService;

	private JSONObject requestJson;

	@BeforeClass
	public static void setupBeforeClass() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);

	}

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);
		requestJson = new JSONObject();
		requestJson.put(MSISDN, "43816975892");
		requestJson.put(LANGUAGE, "EN");
		String category = "Data";
		requestJson.put(CATEGORY, category);
		requestJson.put(TRANSACTION_ID, "4644677");
		requestJson.put(REF_NUM, " ");
		requestJson.put(TRANSACTION_ID, "84993");
		requestJson.put(MSISDN, 73838);
		requestJson.put(PRIZE_WON, "yes");
		
		userInfo = new UserInfo();
		userInfo.setLocationRandomFlag(true);
		userInfo.setRandomFlag(true);
		userInfo.setJFUEligible(true);
		userInfo.setMlFlag(true);
		userInfo.setLangCode("2");
		userInfo.setRagUser(false);
		userInfo.setRagEligibleFlag(true);
		userInfo.setRagOptInFlag(true);
		userInfo.setRagGoalReachedFlag(true);
		
		ragandsagUserInfo = new RAGAndSAGUserInfo();
		ragandsagUserInfo.setRagEligibleFlag(false);
		ragandsagUserInfo.setRagOptInFlag(false);
		ragandsagUserInfo.setRagGoalReachedFlag(false);
		ragandsagUserInfo.setTxId("93jdnasjk8e");
		HashMap<String, String> ragInfo = new HashMap<>();
		ragInfo.put(PRODUCT_VALUE, "5000");
		ragInfo.put("PRODUCT_VALIDITY", "20231212");
		ragInfo.put(RECHARGE_TARGET, "RECHARGE_TARGET");
		ragInfo.put(WEEK_END_DATE, "20231212");
		ragInfo.put(REMAINING_EFFORT, "REMAINING_EFFORT");
		ragandsagUserInfo.setRagInfo(ragInfo);
	}

	@Test
	public void testGetRagOptIn() {
		try {
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getRagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			ragOfferService.getRagOptIn(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetRagOptIn1() {
		try {
			ragandsagUserInfo.setRagEligibleFlag(true);
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getRagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			ragOfferService.getRagOptIn(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetRagOptIn2() {
		try {
			ragandsagUserInfo.setRagEligibleFlag(true);
			ragandsagUserInfo.setRagOptInFlag(true);
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getRagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			ragOfferService.getRagOptIn(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetRagOptIn3() {
		try {
			ragandsagUserInfo.setRagEligibleFlag(true);
			ragandsagUserInfo.setRagOptInFlag(true);
			ragandsagUserInfo.setRagGoalReachedFlag(true);
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getRagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			ragOfferService.getRagOptIn(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetRagOptIn4() {
		try {
			userInfo.setJFUEligible(false);
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getRagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			ragOfferService.getRagOptIn(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testInsertRagUserRecord() {
		UserInfo userInfo = new UserInfo();
		userInfo.setMsisdn("738392");
		userInfo.setLangCode("2");
		userInfo.setJFUEligible(true);
		userInfo.setMlFlag(true);
		userInfo.setRagUser(false);
		try {
			ragOfferService.insertRagUserRecord(userInfo);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testGetRagUserRecord() {
		RAGAndSAGUserInfo ragAndSAGUserInfo = new RAGAndSAGUserInfo();
		ragAndSAGUserInfo.setMsisdn("746534279073");
		try {
			ragOfferService.getRagUserRecord(ragAndSAGUserInfo, null);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method processRAGOptInResponseJsonMethod() throws NoSuchMethodException {
		Method method = RAGOfferService.class.getDeclaredMethod("processRAGOptInResponseJson", JSONObject.class,
				int.class, String.class, JSONObject.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testProcessRAGOptInResponseJsone() {
		JSONObject offer = new JSONObject();
		try {
	//		Mockito.when(userInfo.getTxId()).thenReturn("93jdnasjk8e");
			JSONObject actualJson = (JSONObject) processRAGOptInResponseJsonMethod().invoke(ragOfferService,
					requestJson, 4500, "success", offer);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testGetRagOptOutWhenFLAG_Y() {
		try {
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getRAGOptinStatus(anyString())).thenReturn("Y");
			Mockito.when(lookUpDAO.getRagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			ragOfferService.getRagOptOut(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetRagOptOutWhenFLAG_N() {
		try {
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getRAGOptinStatus(anyString())).thenReturn("N");
			Mockito.when(lookUpDAO.getRagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			ragOfferService.getRagOptOut(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetRagOptOut() {
		try {
			userInfo.setJFUEligible(false);
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getRAGOptinStatus(anyString())).thenReturn("N");
			Mockito.when(lookUpDAO.getRagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			ragOfferService.getRagOptOut(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testRagGetOfferInfoWhenFLAG_Y() {
		try {
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getRAGOptinStatus(anyString())).thenReturn("Y");
			Mockito.when(lookUpDAO.getRagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			ragOfferService.RagGetOfferInfo(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testRagGetOfferInfoWhenFLAG_N() {
		try {
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getRAGOptinStatus(anyString())).thenReturn("N");
			ragOfferService.RagGetOfferInfo(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testRagGetOfferInfo() {
		try {
			userInfo.setJFUEligible(false);
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getRAGOptinStatus(anyString())).thenReturn("N");
			JSONObject actualJson = (JSONObject) ragOfferService.RagGetOfferInfo(requestJson);
			String expected = "{\"MSISDN\":73838,\"StatusCode\":20,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"Invalid Request parameters\"}";
			assertTrue(expected.equals(actualJson.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method setRAGGetOfferResponseJSONMethod() throws NoSuchMethodException {
		Method method = RAGOfferService.class.getDeclaredMethod("setRAGGetOfferResponseJSON", JSONObject.class,
				JSONObject.class, String.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testSetRAGGetOfferResponseJSON() {
		JSONObject offer = new JSONObject();
		try {
			JSONObject actualJson = (JSONObject) setRAGGetOfferResponseJSONMethod().invoke(ragOfferService, offer,
					requestJson, "758943");
			String expected = "{\"REWARDS\":{},\"MSISDN\":73838,\"StatusCode\":0,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"Success\"}";
			assertTrue(expected.equals(actualJson.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testSetSAGGetOfferResponseJSONIfOfferDoesExistForUser() {
		JSONObject offer = null;
		try {
			JSONObject actualJson = (JSONObject) setRAGGetOfferResponseJSONMethod().invoke(ragOfferService, offer,
					requestJson, "758943");
			String expected = "{\"MSISDN\":73838,\"StatusCode\":30,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"No offers are available\"}";
			assertTrue(expected.equals(actualJson.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}


	private Method processRagOptOutMethod() throws NoSuchMethodException {
		Method method = RAGOfferService.class.getDeclaredMethod("processRagOptOut", JSONObject.class, int.class,
				String.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testProcessRagOptOut() {
		try {
			JSONObject actualJson = (JSONObject) processRagOptOutMethod().invoke(ragOfferService, requestJson, 4500, "success");
			String expected = "{\"MSISDN\":73838,\"StatusCode\":4500,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"success\"}";
			assertTrue(expected.equals(actualJson.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

}

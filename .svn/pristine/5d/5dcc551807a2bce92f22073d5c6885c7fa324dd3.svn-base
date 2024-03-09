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
public class SAGOfferServiceTest {

	private static final Logger LOGGER = Logger.getLogger(SAGOfferServiceTest.class);

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
	SAGOfferService sagOfferService;

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
		userInfo.setSagUser(false);
		userInfo.setSagEligibleFlag(true);
		userInfo.setSagOptInFlag(true);
		userInfo.setSagGoalReachedFlag(true);
		
		ragandsagUserInfo = new RAGAndSAGUserInfo();
		ragandsagUserInfo.setSagEligibleFlag(false);
		ragandsagUserInfo.setSagOptInFlag(false);
		ragandsagUserInfo.setSagGoalReachedFlag(false);
		ragandsagUserInfo.setTxId("93jdnasjk8e");
		HashMap<String, String> sagInfo = new HashMap<>();
		sagInfo.put(PRODUCT_VALUE, "5000");
		sagInfo.put("PRODUCT_VALIDITY", "20231212");
		sagInfo.put(RECHARGE_TARGET, "RECHARGE_TARGET");
		sagInfo.put(WEEK_END_DATE, "20231212");
		sagInfo.put(REMAINING_EFFORT, "REMAINING_EFFORT");
		ragandsagUserInfo.setSagInfo(sagInfo);
	}

	@Test
	public void testGetSagOptIn() {
		try {
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getSagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			sagOfferService.getSagOptIn(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	
	@Test
	public void testGetSagOptIn1() {
		try {
			ragandsagUserInfo.setSagEligibleFlag(true);
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getSagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			sagOfferService.getSagOptIn(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetSagOptIn2() {
		try {
			ragandsagUserInfo.setSagEligibleFlag(true);
			ragandsagUserInfo.setSagOptInFlag(true);
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getSagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			sagOfferService.getSagOptIn(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetSagOptIn3() {
		try {
			ragandsagUserInfo.setSagEligibleFlag(true);
			ragandsagUserInfo.setSagOptInFlag(true);
			ragandsagUserInfo.setSagGoalReachedFlag(true);
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getSagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			sagOfferService.getSagOptIn(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetSagOptIn4() {
		try {
			userInfo.setJFUEligible(false);
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getSagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			sagOfferService.getSagOptIn(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	

	@Test
	public void testGetSagOptOut() {
		try {
			userInfo.setJFUEligible(false);
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getSAGOptinStatus(anyString())).thenReturn("N");
			Mockito.when(lookUpDAO.getSagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			sagOfferService.getSagOptOut(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetSagOptOutWhenFLAG_Y() {
		try {
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getSAGOptinStatus(anyString())).thenReturn("FLAG_Y");
			Mockito.when(lookUpDAO.getSagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			sagOfferService.getSagOptOut(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetSagOptOutWhenFLAG_N() {
		try {
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getSAGOptinStatus(anyString())).thenReturn("N");
			sagOfferService.getSagOptOut(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	
	@Test
	public void testSagGetOfferInfo() {
		try {
			userInfo.setJFUEligible(false);
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getSAGOptinStatus(anyString())).thenReturn("N");
			Mockito.when(lookUpDAO.getSagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			sagOfferService.SagGetOfferInfo(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testSagGetOfferInfoWhenFLAG_Y() {
		try {
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getSAGOptinStatus(anyString())).thenReturn("FLAG_Y");
			Mockito.when(lookUpDAO.getSagUserInfo(any(JSONObject.class))).thenReturn(ragandsagUserInfo);
			sagOfferService.SagGetOfferInfo(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testSagGetOfferInfoWhenFLAG_N() {
		try {
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getSAGOptinStatus(anyString())).thenReturn("N");
			sagOfferService.SagGetOfferInfo(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}



	@Test
	public void testInsertSagUserRecord() {
		UserInfo userInfo = new UserInfo();
		userInfo.setMsisdn("738392");
		userInfo.setLangCode("2");
		userInfo.setJFUEligible(true);
		userInfo.setMlFlag(true);
		userInfo.setRagUser(false);
		try {
			sagOfferService.insertSagUserRecord(userInfo);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testGetSagUserRecord() {
		RAGAndSAGUserInfo ragAndSAGUserInfo = new RAGAndSAGUserInfo();
		ragAndSAGUserInfo.setMsisdn("746534279073");
		try {
			sagOfferService.getSagUserRecord(ragAndSAGUserInfo, null);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method processSAGOptInResponseJsonMethod() throws NoSuchMethodException {
		Method method = SAGOfferService.class.getDeclaredMethod("processSAGOptInResponseJson", RAGAndSAGUserInfo.class,
				int.class, String.class, JSONObject.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testExecutePlayRoutine() {
		RAGAndSAGUserInfo ragAndSAGUserInfo = new RAGAndSAGUserInfo();
		ragAndSAGUserInfo.setMsisdn("746534279073");
		ragAndSAGUserInfo.setTxId("12");
		JSONObject offer = new JSONObject();
		try {
			JSONObject actualJson = (JSONObject) processSAGOptInResponseJsonMethod().invoke(sagOfferService,
					ragAndSAGUserInfo, 4500, "success", offer);
			String expected = "{\"MSISDN\":\"746534279073\",\"StatusCode\":4500,\"REWARD\":{},\"TransactionID\":\"12\",\"StatusMessage\":\"success\"}";
			assertTrue(expected.equals(actualJson.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}



	private Method setSAGGetOfferResponseJSONMethod() throws NoSuchMethodException {
		Method method = SAGOfferService.class.getDeclaredMethod("setSAGGetOfferResponseJSON", JSONObject.class,
				JSONObject.class, String.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testSetSAGGetOfferResponseJSON() {
		JSONObject offer = new JSONObject();
		try {
			JSONObject actualJson = (JSONObject) setSAGGetOfferResponseJSONMethod().invoke(sagOfferService, offer,
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
			JSONObject actualJson = (JSONObject) setSAGGetOfferResponseJSONMethod().invoke(sagOfferService, offer,
					requestJson, "758943");
			String expected = "{\"MSISDN\":73838,\"StatusCode\":30,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"No offers are available\"}";
			assertTrue(expected.equals(actualJson.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method sendOptInSMSNotificationMethod() throws NoSuchMethodException {
		Method method = SAGOfferService.class.getDeclaredMethod("sendOptInSMSNotification", RAGAndSAGUserInfo.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testSendOptInSMSNotification() {
		RAGAndSAGUserInfo ragAndSAGUserInfo = new RAGAndSAGUserInfo();
		ragAndSAGUserInfo.setMsisdn("746534279073");
		ragAndSAGUserInfo.setTxId("12");
		ragAndSAGUserInfo.setLangCode("2");
		HashMap<String, String> map = new HashMap<>();
		map.put("PRODUCT_VALUE", "200");
		map.put("SPEND_TARGET", "4");
		map.put("WEEK_END_DATE", "20221208");
		map.put("PRODUCT_VALIDITY", "20231208");
		map.put("REWARD_CODE", "100");
		map.put("REWARD_INFO", "2345");
		map.put("NEXT_AVAILABLE_OFFER_DATE", "20221226");
		map.put("REMAINING_EFFORT", "200");
		map.put("LAST_SPEND_TIME", "20221226");
		ragAndSAGUserInfo.setSagInfo(map);
		try {
			sendOptInSMSNotificationMethod().invoke(sagOfferService, ragAndSAGUserInfo);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method getSAGOfferInfoMethod() throws NoSuchMethodException {
		Method method = SAGOfferService.class.getDeclaredMethod("getSAGOfferInfo", UserInfo.class, String.class,
				JSONObject.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testGetSAGOfferInfo() {
		UserInfo userInfo = new UserInfo();
		userInfo.setMsisdn("746534279073");
		userInfo.setTxId("12");
		userInfo.setLangCode("2");
		HashMap<String, String> map = new HashMap<>();
		map.put("PRODUCT_VALUE", "200");
		map.put("SPEND_TARGET", "4");
		map.put("WEEK_END_DATE", "20221208");
		map.put("PRODUCT_VALIDITY", "20231208");
		map.put("REWARD_CODE", "100");
		map.put("REWARD_INFO", "2345");
		map.put("NEXT_AVAILABLE_OFFER_DATE", "20221226");
		map.put("REMAINING_EFFORT", "200");
		map.put("LAST_SPEND_TIME", "20221226");
		userInfo.setRagInfo(map);
		try {
			getSAGOfferInfoMethod().invoke(sagOfferService, userInfo, "746534279073", requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

}

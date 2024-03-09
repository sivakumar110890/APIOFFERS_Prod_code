package com.comviva.api.j4u.service;

import static com.comviva.api.j4u.utils.J4UOfferConstants.CATEGORY;
import static com.comviva.api.j4u.utils.J4UOfferConstants.KEY_ISENHANCED;
import static com.comviva.api.j4u.utils.J4UOfferConstants.LANGUAGE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.PRIZE_WON;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_NUM;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TRANSACTION_ID;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Random;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
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

import com.comviva.api.j4u.dao.LookUpDAO;
import com.comviva.api.j4u.dao.PEDLookUPDAO;
import com.comviva.api.j4u.dao.PEDUpdateDAO;
import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.UserInfo;
import com.comviva.voltdb.factory.DAOFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DAOFactory.class })
@SuppressStaticInitializationFor("com.comviva.voltdb.factory.DAOFactory")
public class PEDProcessServiceTestForAllMethod {

	private static final Logger LOGGER = Logger.getLogger(PEDProcessServiceTestForAllMethod.class);

	@Mock
	private LookUpDAO lookUpDAO;
	@Mock
	private UpdaterDAO updaterDAO;
	@Mock
	private Client voltDbClient;
	@Mock
	private PEDLookUPDAO pedLookUpDao;
	@Mock
	private PEDUpdateDAO pedUpdateDao;
	@Mock
	private Random random;
	@Mock
	private UserInfo userInfo;

	PEDProcessService pedProcessService;

	private JSONObject requestJson;

	@BeforeClass
	public static void setupBeforeClass() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);

	}

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);
		pedProcessService = new PEDProcessService();
		requestJson = new JSONObject();
		requestJson.put(MSISDN, "843816975892");
		requestJson.put(LANGUAGE, "EN");
		String category = "Data";
		requestJson.put(CATEGORY, category);
		requestJson.put(TRANSACTION_ID, "4644677");
		requestJson.put(REF_NUM, " ");
		requestJson.put(TRANSACTION_ID, "84993");
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
	}

	@Test
	public void testGetAvailablePlays() {
		try {
			// Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			pedProcessService.getAvailablePlays(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testRedeemPlay() {
		try {
			pedProcessService.redeemPlay(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testProcessPlay() {
		String msisdn = "738291112";
		String txnId = "674838";
		int langCode = 2;
		try {
			pedProcessService.processPlay(msisdn, txnId, langCode);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method executePlayRoutineMethod() throws NoSuchMethodException {
		Method method = PEDProcessService.class.getDeclaredMethod("executePlayRoutine", int.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testExecutePlayRoutine() {
		try {
			requestJson.put(KEY_ISENHANCED, true);
			executePlayRoutineMethod().invoke(pedProcessService, 2);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testGetPlayHistory() {
		try {
			pedProcessService.getPlayHistory(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testGetPrizeHistory1() {
		try {
			pedProcessService.getPrizeHistory1("934876253");
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testIncreasePrizeRecordCount() {
		try {
			pedProcessService.increasePrizeRecordCount("8493002");
			;
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method increasePlayedCountOfMsisdnMethod() throws NoSuchMethodException {
		Method method = PEDProcessService.class.getDeclaredMethod("increasePlayedCountOfMsisdn", String.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testIncreasePlayedCountOfMsisdn() {
		try {
			requestJson.put(KEY_ISENHANCED, true);
			increasePlayedCountOfMsisdnMethod().invoke(pedProcessService, "3445503");
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testRecordRewardedPlayEcap() {
		try {
			pedProcessService.recordRewardedPlayEcap(null, null, null, null);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testRecordRewardedPlay() {
		try {
			pedProcessService.recordRewardedPlay(null, null, null, null);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testCheckPedProcessPlayFlag() {
		try {
			pedProcessService.checkPedProcessPlayFlag();
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method setResponseJSONForGetPlayCountMethod() throws NoSuchMethodException {
		Method method = PEDProcessService.class.getDeclaredMethod("setResponseJSONForGetPlayCount", int.class,
				JSONObject.class, String.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testSetResponseJSONForGetPlayCount() {
		try {
			setResponseJSONForGetPlayCountMethod().invoke(pedProcessService, 2, requestJson, "784368932");
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method setResponseJSONForPlayHistoryMethod() throws NoSuchMethodException {
		Method method = PEDProcessService.class.getDeclaredMethod("setResponseJSONForPlayHistory", JSONArray.class,
				JSONObject.class, String.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testSetResponseJSONForPlayHistory() {
		try {
			JSONObject actual = (JSONObject) setResponseJSONForPlayHistoryMethod().invoke(pedProcessService,
					new JSONArray(), requestJson, "784368932");
			String expected = "{\"MSISDN\":843816975892,\"PLAYHISTORY\":[],\"StatusCode\":0,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"Success\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testSetResponseJSONForPlayHistoryFor_null() {
		try {
			JSONObject actual = (JSONObject) setResponseJSONForPlayHistoryMethod().invoke(pedProcessService, null,
					requestJson, "784368932");
			String expected = "{\"MSISDN\":843816975892,\"StatusCode\":30,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"No offers are available\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method setResponseJSONForPlayRedeemptionMethod() throws NoSuchMethodException {
		Method method = PEDProcessService.class.getDeclaredMethod("setResponseJSONForPlayRedeemption", String.class,
				JSONObject.class, String.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testSetResponseJSONForPlayRedeemptionForNoPlay() {
		try {
			setResponseJSONForPlayRedeemptionMethod().invoke(pedProcessService, "NO_PLAY", requestJson, "784368932");
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testSetResponseJSONForPlayRedeemption() {
		try {
			setResponseJSONForPlayRedeemptionMethod().invoke(pedProcessService, "PLAY", requestJson, "784368932");
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method publishToTopicMethod() throws NoSuchMethodException {
		Method method = PEDProcessService.class.getDeclaredMethod("publishToTopic", String.class, String.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testPublishToTopic() {
		try {
			publishToTopicMethod().invoke(pedProcessService, "MPESAQBCONSUMER", requestJson.toString());
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

}









 
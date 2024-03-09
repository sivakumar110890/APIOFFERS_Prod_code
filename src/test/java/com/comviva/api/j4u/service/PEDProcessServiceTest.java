package com.comviva.api.j4u.service;

import static com.comviva.api.j4u.utils.J4UOfferConstants.CATEGORY;
import static com.comviva.api.j4u.utils.J4UOfferConstants.LANGUAGE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.PRIZE_WON;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_NUM;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TRANSACTION_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.json.JSONArray;
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
import com.comviva.api.j4u.dao.PEDLookUPDAO;
import com.comviva.api.j4u.dao.PEDUpdateDAO;
import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.PEDRandomPrizeInfo;
import com.comviva.api.j4u.model.PrizeLibrary;
import com.comviva.api.j4u.model.UserInfo;
import com.comviva.voltdb.factory.DAOFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DAOFactory.class })
@SuppressStaticInitializationFor("com.comviva.voltdb.factory.DAOFactory")
public class PEDProcessServiceTest {

	private static final Logger LOGGER = Logger.getLogger(PEDProcessServiceTest.class);

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

	@InjectMocks
	private PEDProcessService pedProcessService;

	private JSONObject requestJson;

	@BeforeClass
	public static void setupBeforeClass() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);

	}

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);
		requestJson = new JSONObject();
		requestJson.put(MSISDN, "843816975820");
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

	public void getAvailablePlays() {
		try {
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			JSONObject actual = pedProcessService.getAvailablePlays(requestJson);
			String expected = "{\"MSISDN\":843816975820,\"StatusCode\":0,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"Success\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	public void getAvailablePlaysWhenMsisdnLengthNotValid() {
		try {
			requestJson.put(MSISDN, "84816975820");
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			JSONObject actual = pedProcessService.getAvailablePlays(requestJson);
			String expected = "{\"MSISDN\":84816975820,\"StatusCode\":20,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"Invalid Request parameters\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	public void getAvailablePlaysWhenMlFlagIsFalse() {
		try {
			userInfo.setMlFlag(false);
			requestJson.put(MSISDN, "843816975820");
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			JSONObject actual = pedProcessService.getAvailablePlays(requestJson);
			String expected = "{\"MSISDN\":843816975820,\"StatusCode\":20,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"Invalid Request parameters\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	public void getPlayHistoryWhenMlFlagIsFalse() {
		try {
			userInfo.setMlFlag(false);
			requestJson.put(MSISDN, "843816975820");
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			JSONObject actual = pedProcessService.getPlayHistory(requestJson);
			String expected = "{\"MSISDN\":843816975820,\"PLAYHISTORY\":\"\",\"StatusCode\":20,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"Invalid Request parameters\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	public void getPlayHistory() {
		try {
			userInfo.setMlFlag(true);
			requestJson.put(MSISDN, "843816975820");
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			JSONObject actual = pedProcessService.getPlayHistory(requestJson);
			String expected = "{\"MSISDN\":843816975820,\"PLAYHISTORY\":\"\",\"StatusCode\":30,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"No offers are available\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	public void redeemPlay() {
		try {
			userInfo.setMlFlag(true);
			requestJson.put(MSISDN, "843816975820");
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			JSONObject actual = pedProcessService.redeemPlay(requestJson);
			String expected = "{\"MSISDN\":843816975820,\"StatusCode\":1,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"No plays Available\",\"PRIZE_WON\":\"NO_PRIZE\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	public void redeemPlayWhenJFUEligibleIsFalse() {
		try {
			userInfo.setJFUEligible(false);
			requestJson.put(MSISDN, "843816975820");
			Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
			JSONObject actual = pedProcessService.redeemPlay(requestJson);
			String expected = "{\"MSISDN\":843816975820,\"StatusCode\":20,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"Invalid Request parameters\",\"PRIZE_WON\":\"\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	public void processPlay() {
		List<PEDRandomPrizeInfo> pedRandomPrizeInfoList = new ArrayList<>();
		PEDRandomPrizeInfo pedPriceInfo = new PEDRandomPrizeInfo();
		pedPriceInfo.setPrizeID("4567");
		pedPriceInfo.setMaxRange(100);
		pedPriceInfo.setMinRange(4);
		pedPriceInfo.setWeightage(1000);
		PEDRandomPrizeInfo pedPriceInfo1 = new PEDRandomPrizeInfo();
		pedPriceInfo1.setPrizeID("456788");
		pedPriceInfo1.setMaxRange(50);
		pedPriceInfo1.setMinRange(0);
		pedPriceInfo1.setWeightage(100);
		pedRandomPrizeInfoList.add(pedPriceInfo);
		pedRandomPrizeInfoList.add(pedPriceInfo1);

		String msisdn = "738291112";
		String txnId = "674838";
		int langCode = 2;
		try {
			Mockito.when(pedLookUpDao.getPedPlaysExpirayDays()).thenReturn(5).thenReturn(-1);
			Mockito.when(pedLookUpDao.getAvailablePlaysHistory(anyString())).thenReturn(5);
			Mockito.when(pedLookUpDao.getAvailablePlaysDOD1(anyString())).thenReturn(5);
			Mockito.when(pedLookUpDao.getRandomPrize()).thenReturn(pedRandomPrizeInfoList);
			pedProcessService.processPlay(msisdn, txnId, langCode);
			String actual = pedProcessService.processPlay(msisdn, txnId, langCode);
			String expexted = "NO_PRIZE";
			assertTrue(actual.equals(expexted));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	public void increasePrizeRecordCount() {
		try {
			Mockito.when(pedLookUpDao.retrieveTotalPrizeCount(anyString())).thenReturn(5);
			pedProcessService.increasePrizeRecordCount("77383");
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	public void checkPedProcessPlayFlag() {
		try {

			Mockito.when(pedLookUpDao.getPedProcessFlag(anyString())).thenReturn(true).thenReturn(false);
			boolean actual = pedProcessService.checkPedProcessPlayFlag();
			boolean actual1 = pedProcessService.checkPedProcessPlayFlag();
			assertTrue(actual);
			assertFalse(actual1);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	public void getTotalPrizeCount() {
		String prizeId = "67327";
		try {

			Mockito.when(pedLookUpDao.retrieveTotalPrizeCount(anyString())).thenReturn(6).thenReturn(15);
			int actual = pedProcessService.getTotalPrizeCount(prizeId);
			int actual1 = pedProcessService.getTotalPrizeCount(prizeId);
			assertEquals(6, actual);
			assertEquals(15, actual1);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	public void recordRewardedPlay() {
		PrizeLibrary prizeLibrary = null;
		String status = "Success";
		String msisdn = "738291112";
		String txnId = "674838";
		try {
			pedProcessService.recordRewardedPlay(msisdn, prizeLibrary, status, txnId);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	public void getPrizeDetails() {
		String prizeId = "67327";
		int langCode = 2;
		PrizeLibrary prizeLibrary = new PrizeLibrary();
		prizeLibrary.setLanguageCode(2);
		prizeLibrary.setMaxWins(5);
		prizeLibrary.setPrizeDescription("Reward");
		prizeLibrary.setPrizeId("74783");
		prizeLibrary.setPrizeType("Data");
		prizeLibrary.setProbability(1);
		prizeLibrary.setRedemptionCode("dbfF74RF7F4RFB");
		try {

			Mockito.when(pedLookUpDao.retrievePrizeDetailsById(anyString(), anyInt())).thenReturn(prizeLibrary);
			PrizeLibrary actual = pedProcessService.getPrizeDetails(prizeId, langCode);
			assertTrue(actual.equals(prizeLibrary));

		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method publishToTopicMethod() throws NoSuchMethodException {
		Method method = PEDProcessService.class.getDeclaredMethod("publishToTopic", String.class, String.class);
		method.setAccessible(true);
		return method;
	}

	public void publishToTopic() {
		try {
			publishToTopicMethod().invoke(pedProcessService, "MPESAQBCONSUMER", requestJson.toString());
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

	public void setResponseJSONForPlayHistory() {
		try {
			JSONObject actual = (JSONObject) setResponseJSONForPlayHistoryMethod().invoke(pedProcessService,
					new JSONArray(), requestJson, "784368932");
			String expected = "{\"MSISDN\":843816975820,\"PLAYHISTORY\":[],\"StatusCode\":0,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"Success\"}";
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

	public void setResponseJSONForPlayRedeemption() {
		try {
			JSONObject actual =(JSONObject) setResponseJSONForPlayRedeemptionMethod().invoke(pedProcessService, "PLAY", requestJson, "784368932");
			String expected = "{\"MSISDN\":843816975820,\"StatusCode\":0,\"RefNum\":\" \",\"TransactionID\":\"84993\",\"StatusMessage\":\"Success\",\"PRIZE_WON\":\"PLAY\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void test() {
		getAvailablePlays();
		getAvailablePlaysWhenMsisdnLengthNotValid();
		getAvailablePlaysWhenMlFlagIsFalse();
		getPlayHistoryWhenMlFlagIsFalse();
		getPlayHistory();
		redeemPlay();
		redeemPlayWhenJFUEligibleIsFalse();
		processPlay();
		increasePrizeRecordCount();
		checkPedProcessPlayFlag();
		recordRewardedPlay();
		getTotalPrizeCount();
		getPrizeDetails();
		publishToTopic();
		setResponseJSONForPlayHistory();
		setResponseJSONForPlayRedeemption();
	}
}

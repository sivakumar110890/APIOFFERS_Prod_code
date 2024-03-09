package com.comviva.api.j4u.service;

import static com.comviva.api.j4u.utils.J4UOfferConstants.CATEGORY;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CELL_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CHANNEL;
import static com.comviva.api.j4u.utils.J4UOfferConstants.IS_PURCHASED;
import static com.comviva.api.j4u.utils.J4UOfferConstants.KEY_ISENHANCED;
import static com.comviva.api.j4u.utils.J4UOfferConstants.KEY_ISMORNING;
import static com.comviva.api.j4u.utils.J4UOfferConstants.KEY_ISSOCIAL;
import static com.comviva.api.j4u.utils.J4UOfferConstants.LANGUAGE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.POOL_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_NUM;
import static com.comviva.api.j4u.utils.J4UOfferConstants.SUB_CATEGORY;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TRANSACTION_ID;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
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

import com.comviva.api.exception.ConfigException;
import com.comviva.api.j4u.dao.LookUpDAO;
import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.HTTPResponse;
import com.comviva.api.j4u.model.UserInfo;
import com.comviva.api.j4u.utils.HTTPSclient;
import com.comviva.voltdb.factory.DAOFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DAOFactory.class })
@SuppressStaticInitializationFor("com.comviva.voltdb.factory.DAOFactory")
public class J4UOfferServiceTest {

	private static final Logger LOGGER = Logger.getLogger(J4UOfferServiceTest.class);
	@Mock
	private LookUpDAO lookUpDAO;

	@Mock
	private UpdaterDAO updaterDAO;

	@Mock
	private HTTPSclient httpsClient;
	@Mock
	private UserInfo userInfo;;
	@Mock
	private Random random;

	@InjectMocks
	J4UOfferService offerService;

	private JSONObject requestJson;

	@BeforeClass
	public static void setupBeforeClass() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);

	}

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);
		requestJson = new JSONObject();
		requestJson.put(MSISDN, "243811404441");
		requestJson.put(LANGUAGE, "en");
		requestJson.put(CATEGORY, "Data");
		requestJson.put(KEY_ISSOCIAL, false);
		requestJson.put(CHANNEL, "Web");
		requestJson.put(POOL_ID, "");
		requestJson.put(CELL_ID, "");
		requestJson.put(KEY_ISENHANCED, false);
		requestJson.put(KEY_ISMORNING , false);
		requestJson.put(SUB_CATEGORY , "Data");
		requestJson.put(REF_NUM, "Target_001");
		requestJson.put(TRANSACTION_ID, "c27955aa-cd72-481f-b021-6fe9d9ed852c");
		
		userInfo = new UserInfo();
		userInfo.setLocationRandomFlag(true);
		userInfo.setRandomFlag(true);
		userInfo.setJFUEligible(true);
		userInfo.setMlFlag(true);
		userInfo.setPrefPayMethod("M");
//		Mockito.when(lookUpDAO.getUserInfo(anyString())).thenReturn(userInfo);
	}

	@Test
	public void getSynchOfferLocationTest() {
		requestJson.put(KEY_ISMORNING , true);
		Map<String, String> productPriceMap = new HashMap<String, String>();
		productPriceMap.put("12345", "123");
		productPriceMap.put("12354", "123");
		productPriceMap.put("123534", "123");
		List<String> prodIdsList = new ArrayList<>();
		prodIdsList.add("12345");
		prodIdsList.add("12354");
		prodIdsList.add("123534");
		UserInfo userInfo = new UserInfo();
		HTTPResponse httpResponce = new HTTPResponse();
		httpResponce.setStatus(200);
		httpResponce.setOutput("<?xml version = \"1.0\" encoding = \"UTF-8\"?> " + "<DistanceQueryReqRsp> "
				+ "<ApartyCellInfo>12</ApartyCellInfo> " + "</DistanceQueryReqRsp>");
		userInfo.setJFUEligible(true);
		userInfo.setMlFlag(true);
		userInfo.setRandomFlag(true);
		userInfo.setLocationRandomFlag(true);
		userInfo.setMorningofferEligilibiltyFlag("Y");
		try {
			Mockito.when(lookUpDAO.getUserInfo(Mockito.anyString())).thenReturn(userInfo);
			Mockito.when(httpsClient.sendRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
					.thenReturn(httpResponce);
			offerService.getSynchOffer(requestJson);
		} catch (UnsupportedEncodingException | ConfigException | ParserConfigurationException e) {
			LOGGER.error("Error in test :: ", e);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void getSynchOfferLocationTest1() {
		requestJson.put(KEY_ISMORNING , true);
		Map<String, String> productPriceMap = new HashMap<String, String>();
		productPriceMap.put("12345", "123");
		productPriceMap.put("12354", "123");
		productPriceMap.put("123534", "123");
		List<String> prodIdsList = new ArrayList<>();
		prodIdsList.add("12345");
		prodIdsList.add("12354");
		prodIdsList.add("123534");
		UserInfo userInfo = null;
		HTTPResponse httpResponce = new HTTPResponse();
		httpResponce.setStatus(200);
		httpResponce.setOutput("<?xml version = \"1.0\" encoding = \"UTF-8\"?> " + "<DistanceQueryReqRsp> "
				+ "<ApartyCellInfo>12</ApartyCellInfo> " + "</DistanceQueryReqRsp>");
		try {
			Mockito.when(lookUpDAO.getUserInfo(Mockito.anyString())).thenReturn(userInfo);
			Mockito.when(httpsClient.sendRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
			.thenReturn(httpResponce);
			offerService.getSynchOffer(requestJson);
		} catch (UnsupportedEncodingException | ConfigException | ParserConfigurationException e) {
			LOGGER.error("Error in test :: ", e);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void getSynchOfferLocationTest2() {
		requestJson.put(KEY_ISMORNING , false);
		Map<String, String> productPriceMap = new HashMap<String, String>();
		productPriceMap.put("12345", "123");
		productPriceMap.put("12354", "123");
		productPriceMap.put("123534", "123");
		List<String> prodIdsList = new ArrayList<>();
		prodIdsList.add("12345");
		prodIdsList.add("12354");
		prodIdsList.add("123534");
		UserInfo userInfo = null;
		HTTPResponse httpResponce = new HTTPResponse();
		httpResponce.setStatus(200);
		httpResponce.setOutput("<?xml version = \"1.0\" encoding = \"UTF-8\"?> " + "<DistanceQueryReqRsp> "
				+ "<ApartyCellInfo>12</ApartyCellInfo> " + "</DistanceQueryReqRsp>");
		try {
			Mockito.when(lookUpDAO.getUserInfo(Mockito.anyString())).thenReturn(userInfo);
			Mockito.when(httpsClient.sendRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
			.thenReturn(httpResponce);
			offerService.getSynchOffer(requestJson);
		} catch (UnsupportedEncodingException | ConfigException | ParserConfigurationException e) {
			LOGGER.error("Error in test :: ", e);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetSynchOfferGivesError() {
		requestJson.put(KEY_ISMORNING , true);
		Map<String, String> productPriceMap = new HashMap<String, String>();
		productPriceMap.put("12345", "123");
		productPriceMap.put("12354", "123");
		productPriceMap.put("123534", "123");
		List<String> prodIdsList = new ArrayList<>();
		prodIdsList.add("12345");
		prodIdsList.add("12354");
		prodIdsList.add("123534");
		UserInfo userInfo = new UserInfo();
		HTTPResponse httpResponce = new HTTPResponse();
		httpResponce.setStatus(200);
		httpResponce.setOutput("<?xml version = \"1.0\" encoding = \"UTF-8\"?> " + "<DistanceQueryReqRsp> "
				+ "<ApartyCellInfo>12</ApartyCellInfo> " + "</DistanceQueryReqRsp>");
		userInfo.setJFUEligible(true);
		userInfo.setMlFlag(true);
		userInfo.setRandomFlag(true);
		userInfo.setLocationRandomFlag(true);
		try {
			Mockito.when(lookUpDAO.getUserInfo(Mockito.anyString())).thenReturn(userInfo);
			Mockito.when(httpsClient.sendRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
			.thenReturn(httpResponce);
			offerService.getSynchOffer(requestJson);
		} catch (UnsupportedEncodingException | ConfigException | ParserConfigurationException e) {
			LOGGER.error("Error in test :: ", e);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getASynchOffers_UserNull() {
		requestJson.put(REF_NUM, "123");
		requestJson.put(TRANSACTION_ID, "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		try {
			Mockito.when(lookUpDAO.getUserInfo(Mockito.anyString())).thenReturn(null);
			offerService.getAsynchOffer(requestJson);
		} catch (UnsupportedEncodingException | ConfigException | ParserConfigurationException e) {
			LOGGER.error("Error in test :: ", e);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getASynchOffers_NotJ4UEligible() {
		UserInfo userInfo = new UserInfo();
		userInfo.setJFUEligible(false);
		requestJson.put(REF_NUM, "123");
		requestJson.put(TRANSACTION_ID, "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		try {
			Mockito.when(lookUpDAO.getUserInfo(Mockito.anyString())).thenReturn(userInfo);
			offerService.getAsynchOffer(requestJson);
		} catch (UnsupportedEncodingException | ConfigException | ParserConfigurationException e) {
			LOGGER.error("Error in test :: ", e);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getASynchOffers_J4UEligible() {
		UserInfo userInfo = new UserInfo();
		userInfo.setJFUEligible(true);
		userInfo.setRandomFlag(true);
		requestJson.put(REF_NUM, "123");
		requestJson.put(TRANSACTION_ID, "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		try {
			Mockito.when(lookUpDAO.getUserInfo(Mockito.anyString())).thenReturn(userInfo);
			offerService.getAsynchOffer(requestJson);
		} catch (UnsupportedEncodingException | ConfigException | ParserConfigurationException e) {
			LOGGER.error("Error in test :: ", e);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getASynchOffers_J4UEligible_NotRandom() {
		UserInfo userInfo = new UserInfo();
		userInfo.setJFUEligible(true);
		userInfo.setRandomFlag(false);
		userInfo.setOfferRefreshFlag("N");
		userInfo.setPrefPayMethod("M");
		requestJson.put(REF_NUM, "123");
		requestJson.put(TRANSACTION_ID, "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		try {
			Mockito.when(lookUpDAO.getUserInfo(Mockito.anyString())).thenReturn(userInfo);
			offerService.getAsynchOffer(requestJson);
		} catch (UnsupportedEncodingException | ConfigException | ParserConfigurationException e) {
			LOGGER.error("Error in test :: ", e);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getASynchOffers_withMLOffers() {
		UserInfo userInfo = new UserInfo();
		userInfo.setJFUEligible(true);
		userInfo.setRandomFlag(false);
		userInfo.setOfferRefreshFlag("N");
		userInfo.setPrefPayMethod("D");
		requestJson.put(REF_NUM, "123");
		requestJson.put(TRANSACTION_ID, "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		JSONObject offer1 = new JSONObject();
		JSONObject offer2 = new JSONObject();
		JSONArray offers = new JSONArray();
		offers.put(offer1);
		offers.put(offer2);
		try {
			Mockito.when(lookUpDAO.getOfferForTgtMLUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
					.thenReturn(offers);
			Mockito.when(lookUpDAO.getUserInfo(Mockito.anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getCSType(Mockito.anyString())).thenReturn("OP");
			offerService.getAsynchOffer(requestJson);
		} catch (UnsupportedEncodingException | ConfigException | ParserConfigurationException e) {
			LOGGER.error("Error in test :: ", e);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getASynchOffers_withMLOffers1() {
		UserInfo userInfo = new UserInfo();
		userInfo.setJFUEligible(true);
		userInfo.setRandomFlag(false);
		userInfo.setOfferRefreshFlag("N");
		userInfo.setPrefPayMethod("D");
		requestJson.put(REF_NUM, "123");
		requestJson.put(TRANSACTION_ID, "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		JSONObject offer1 = new JSONObject();
		JSONObject offer2 = new JSONObject();
		JSONArray offers = new JSONArray();
		offers.put(offer1);
		offers.put(offer2);
		try {
			Mockito.when(lookUpDAO.getOfferForTgtMLUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
					.thenReturn(offers);
			Mockito.when(lookUpDAO.getUserInfo(Mockito.anyString())).thenReturn(userInfo);
			Mockito.when(lookUpDAO.getCSType(Mockito.anyString())).thenReturn("CP");
			offerService.getAsynchOffer(requestJson);
		} catch (UnsupportedEncodingException | ConfigException | ParserConfigurationException e) {
			LOGGER.error("Error in test :: ", e);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getASynchOffers_MLOffers_CellIdPoolId() {
		UserInfo userInfo = new UserInfo();
		userInfo.setJFUEligible(true);
		userInfo.setRandomFlag(false);
		userInfo.setOfferRefreshFlag("Data");
		requestJson.put(REF_NUM, "123");
		requestJson.put(TRANSACTION_ID, "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		requestJson.put(CELL_ID, "1234");
		requestJson.put(POOL_ID, "1234");
		JSONObject offer1 = new JSONObject();
		JSONObject offer2 = new JSONObject();
		JSONArray offers = new JSONArray();
		offers.put(offer1);
		offers.put(offer2);
		try {
			Mockito.when(lookUpDAO.getOfferForTgtMLUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
					.thenReturn(offers);
			Mockito.when(lookUpDAO.getUserInfo(Mockito.anyString())).thenReturn(userInfo);
			offerService.getAsynchOffer(requestJson);
		} catch (UnsupportedEncodingException | ConfigException | ParserConfigurationException e) {
			LOGGER.error("Error in test :: ", e);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void activateOffer() {
		JSONObject requestJson = new JSONObject();
		requestJson.put(MSISDN, "43816975892");
		requestJson.put(REF_NUM, "12");
		requestJson.put(TRANSACTION_ID, "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		offerService.activateOffer(requestJson);
	}

	@Test
	public void activateOffer_IsPurchased() {
		JSONObject requestJson = new JSONObject();
		requestJson.put(MSISDN, "43816975892");
		requestJson.put(REF_NUM, "12");
		requestJson.put(IS_PURCHASED, "true");
		requestJson.put(OFFER_ID, "1234");
		requestJson.put(CHANNEL, "WEB");
		requestJson.put(TRANSACTION_ID, "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		try {
			Mockito.when(lookUpDAO.getCSType(Mockito.anyString())).thenReturn("CP");
			Mockito.when(lookUpDAO.getProductTypeById(Mockito.anyString())).thenReturn("J4U Voice");
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
		offerService.activateOffer(requestJson);
	}
	@Test
	public void activateOfferFor_J4U_MORNING_OFFER() {
		JSONObject requestJson = new JSONObject();
		requestJson.put(MSISDN, "43816975892");
		requestJson.put(REF_NUM, "12");
		requestJson.put(IS_PURCHASED, "true");
		requestJson.put(OFFER_ID, "1234");
		requestJson.put(CHANNEL, "WEB");
		requestJson.put(TRANSACTION_ID, "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		try {
			Mockito.when(lookUpDAO.getProductTypeById(Mockito.anyString())).thenReturn("J4U Morning Offer");
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
		offerService.activateOffer(requestJson);
	}

	@Test
	public void activateOffer_IsPurchased_USSD() {
		JSONObject requestJson = new JSONObject();
		requestJson.put(MSISDN, "43816975892");
		requestJson.put(REF_NUM, "12");
		requestJson.put(IS_PURCHASED, "true");
		requestJson.put(OFFER_ID, "1234");
		requestJson.put(CHANNEL, "USSD");
		requestJson.put(TRANSACTION_ID, "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		try {
			Mockito.when(lookUpDAO.getProductTypeById(Mockito.anyString())).thenReturn("J4U Social Media");
			Mockito.when(lookUpDAO.getCSType(Mockito.anyString())).thenReturn("OP");
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
		offerService.activateOffer(requestJson);
	}

	@Test
	public void provisionRewardForUser_Voice() {
		JSONObject requestJson = new JSONObject();
		requestJson.put(MSISDN, "43816975892");
		requestJson.put(REF_NUM, "12");
		requestJson.put(IS_PURCHASED, "true");
		requestJson.put(OFFER_ID, "1234");
		requestJson.put(CHANNEL, "APP");
		requestJson.put(LANGUAGE, "en");
		requestJson.put(TRANSACTION_ID, "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		try {
			Mockito.when(lookUpDAO.getProductTypeById(Mockito.anyString())).thenReturn("J4U Voice");
			offerService.provisionRewardForUser(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}

	}

	@Test
	public void provisionRewardForUser_Data() {
		JSONObject requestJson = new JSONObject();
		requestJson.put(MSISDN, "43816975892");
		requestJson.put(REF_NUM, "12");
		requestJson.put(IS_PURCHASED, "true");
		requestJson.put(OFFER_ID, "1234");
		requestJson.put(CHANNEL, "USSD");
		requestJson.put(TRANSACTION_ID, "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		try {
			Mockito.when(lookUpDAO.getProductTypeById(Mockito.anyString())).thenReturn("J4U Data");
			offerService.provisionRewardForUser(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}

	}

	@Test
	public void provisionRewardForUse_Integrated() {
		JSONObject requestJson = new JSONObject();
		requestJson.put(MSISDN, "43816975892");
		requestJson.put(REF_NUM, "12");
		requestJson.put(IS_PURCHASED, "true");
		requestJson.put(OFFER_ID, "1234");
		requestJson.put(CHANNEL, "USSD");
		requestJson.put(TRANSACTION_ID, "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		try {
			Mockito.when(lookUpDAO.getProductTypeById(Mockito.anyString())).thenReturn("J4U Integrated & SMS");
			offerService.provisionRewardForUser(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}

	}

	@Test
	public void provisionRewardForUse_Hourly() {
		JSONObject requestJson = new JSONObject();
		requestJson.put(MSISDN, "43816975892");
		requestJson.put(REF_NUM, "12");
		requestJson.put(IS_PURCHASED, "true");
		requestJson.put(OFFER_ID, "1234");
		requestJson.put(CHANNEL, "USSD");
		requestJson.put(TRANSACTION_ID, "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		try {
			Mockito.when(lookUpDAO.getProductTypeById(Mockito.anyString())).thenReturn("J4U Hourly Data");
			offerService.provisionRewardForUser(requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}

	}

	@Test
	public void getOfferForTargetMLUser() {
		JSONArray expected = new JSONArray();
		JSONArray actual = null;
		JSONObject offerRequest = new JSONObject();
		offerRequest.put(MSISDN, "123456");
		try {
			Mockito.when(lookUpDAO.getOfferForTgtMLUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
					.thenReturn(expected);
			actual = offerService.getOfferForTargetMLUser(offerRequest, "J4U Data");
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
		Assert.assertEquals(expected, actual);

	}

	@Test
	public void checkOfferRefreshFlag_true() {
		String offerRefreshFlag = "Voice";
		String category = "Data";
		boolean expected = true;
		boolean actual = offerService.checkOfferRefreshFlag(offerRefreshFlag, category);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void checkOfferRefreshFlag_false() {
		String offerRefreshFlag = "DATA";
		String category = "Data";
		boolean expected = false;
		boolean actual = offerService.checkOfferRefreshFlag(offerRefreshFlag, category);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void checkOfferRefreshFlag_N() {
		String offerRefreshFlag = "N";
		String category = "Data";
		boolean expected = true;
		boolean actual = offerService.checkOfferRefreshFlag(offerRefreshFlag, category);
		Assert.assertEquals(expected, actual);
	}

	private Method getRequestCellIDMethod() throws NoSuchMethodException {
		Method method = J4UOfferService.class.getDeclaredMethod("requestCellID", JSONObject.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testRequestCellID() {
		requestJson.put(CELL_ID, "1234");
		try {
			getRequestCellIDMethod().invoke(offerService, requestJson);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method getMorningMLUserMethod() throws NoSuchMethodException {
		Method method = J4UOfferService.class.getDeclaredMethod("getMorningMLUser", JSONObject.class, String.class,
				int.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testGetMorningMLUser() {
		try {
			getMorningMLUserMethod().invoke(offerService, requestJson, "morningOffer", 2);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method getCellIdMethod() throws NoSuchMethodException {
		Method method = J4UOfferService.class.getDeclaredMethod("getCellId", String.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testGetCellId() {
		try {
			getCellIdMethod().invoke(offerService, "morningOffer");
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method getOffersSyncMethod() throws NoSuchMethodException {
		Method method = J4UOfferService.class.getDeclaredMethod("getOffersSync", UserInfo.class, String.class,
				JSONArray.class, JSONObject.class, String.class, String.class, int.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testGetOffersSyncForRandomMLUser() {
		UserInfo userInfo = new UserInfo();
		userInfo.setLocationRandomFlag(true);
		userInfo.setRandomFlag(true);
		requestJson.put(KEY_ISENHANCED, true);
		try {
			getOffersSyncMethod().invoke(offerService, userInfo, "123456789", new JSONArray(), requestJson,
					"morningOffer", "567", 2);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetOffersSyncForRandomMLUser1() {
		UserInfo userInfo = new UserInfo();
		userInfo.setLocationRandomFlag(false);
		userInfo.setRandomFlag(false);
		requestJson.put(KEY_ISENHANCED, true);
		try {
			getOffersSyncMethod().invoke(offerService, userInfo, "123456789", new JSONArray(), requestJson,
					"morningOffer", null, 2);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testGetOffersSyncForTargetMLUser() {
		try {
			getOffersSyncMethod().invoke(offerService, userInfo, "123456789", new JSONArray(), requestJson,
					"morningOffer", null, 2);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testGetOffersSyncForTargetMLUser1() {
		try {
			requestJson.put(KEY_ISSOCIAL, true);
			getOffersSyncMethod().invoke(offerService, userInfo, "123456789", new JSONArray(), requestJson,
					"morningOffer", null, 2);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method setResponseJSONMethod() throws NoSuchMethodException {
		Method method = J4UOfferService.class.getDeclaredMethod("setResponseJSON", JSONArray.class, JSONObject.class,
				String.class, String.class, String.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testSetResponseJSON() {
		try {
			requestJson.put(KEY_ISENHANCED, true);
			JSONObject actual =(JSONObject) setResponseJSONMethod().invoke(offerService, new JSONArray(), requestJson, "65767", "567", "123456789");
			String expected = "{\"POOL_ID\":\"65767\",\"MSISDN\":243811404441,\"Language\":\"en\",\"CELL_ID\":\"567\",\"Offers\":[],\"StatusCode\":0,\"RefNum\":\"Target_001\",\"TransactionID\":\"c27955aa-cd72-481f-b021-6fe9d9ed852c\",\"StatusMessage\":\"Success\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testSetResponseJSON1() {
		try {
			requestJson.put(KEY_ISENHANCED, false);
			requestJson.put(KEY_ISMORNING, true);
			JSONObject actual =(JSONObject) setResponseJSONMethod().invoke(offerService, new JSONArray(), requestJson, "65767", "567", "123456789");
			String expected = "{\"POOL_ID\":\"65767\",\"MSISDN\":243811404441,\"Language\":\"en\",\"CELL_ID\":\"567\",\"Offers\":[],\"StatusCode\":0,\"RefNum\":\"Target_001\",\"TransactionID\":\"c27955aa-cd72-481f-b021-6fe9d9ed852c\",\"StatusMessage\":\"Success\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testSetResponseJSON2() {
		try {
			requestJson.put(KEY_ISENHANCED, false);
			requestJson.put(KEY_ISMORNING, false);
			JSONObject actual =(JSONObject) setResponseJSONMethod().invoke(offerService, new JSONArray(), requestJson, "65767", "567", "123456789");
			String expected = "{\"POOL_ID\":\"65767\",\"MSISDN\":243811404441,\"Language\":\"en\",\"CELL_ID\":\"567\",\"Offers\":[],\"StatusCode\":30,\"RefNum\":\"Target_001\",\"TransactionID\":\"c27955aa-cd72-481f-b021-6fe9d9ed852c\",\"StatusMessage\":\"No offers are available\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testSetResponseJSON3() {
		try {
			requestJson.put(KEY_ISENHANCED, false);
			requestJson.put(KEY_ISMORNING, false);
			JSONObject actual =(JSONObject) setResponseJSONMethod().invoke(offerService, new JSONArray(), requestJson, null, "567", "123456789");
			String expected = "{\"MSISDN\":243811404441,\"Language\":\"en\",\"CELL_ID\":\"567\",\"Offers\":[],\"StatusCode\":0,\"RefNum\":\"Target_001\",\"TransactionID\":\"c27955aa-cd72-481f-b021-6fe9d9ed852c\",\"StatusMessage\":\"Success\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testSetResponseJSON4() {
		try {
			requestJson.put(KEY_ISENHANCED, false);
			requestJson.put(KEY_ISMORNING, true);
			JSONObject actual =(JSONObject) setResponseJSONMethod().invoke(offerService, null, requestJson, null, "567", "123456789");
			String expected = "{\"MSISDN\":243811404441,\"Language\":\"en\",\"CELL_ID\":\"567\",\"StatusCode\":30,\"RefNum\":\"Target_001\",\"TransactionID\":\"c27955aa-cd72-481f-b021-6fe9d9ed852c\",\"StatusMessage\":\"No offers are available\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testSetResponseJSON5() {
		try {
			requestJson.put(KEY_ISENHANCED, false);
			requestJson.put(KEY_ISMORNING, false);
			JSONObject actual =(JSONObject) setResponseJSONMethod().invoke(offerService, null, requestJson, null, "567", "123456789");
			String expected = "{\"MSISDN\":243811404441,\"Language\":\"en\",\"CELL_ID\":\"567\",\"StatusCode\":30,\"RefNum\":\"Target_001\",\"TransactionID\":\"c27955aa-cd72-481f-b021-6fe9d9ed852c\",\"StatusMessage\":\"No offers are available\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testSetResponseJSON6() {
		try {
			requestJson.put(KEY_ISENHANCED, false);
			requestJson.put(KEY_ISMORNING, false);
			JSONObject actual =(JSONObject) setResponseJSONMethod().invoke(offerService, new JSONArray(), requestJson, "65767", "567", "123456789");
			String expected = "{\"POOL_ID\":\"65767\",\"MSISDN\":243811404441,\"Language\":\"en\",\"CELL_ID\":\"567\",\"Offers\":[],\"StatusCode\":30,\"RefNum\":\"Target_001\",\"TransactionID\":\"c27955aa-cd72-481f-b021-6fe9d9ed852c\",\"StatusMessage\":\"No offers are available\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testCachedOffer() {
		J4UOfferService offerService = new J4UOfferService();
		JSONObject requestJson = new JSONObject();
		requestJson.put("MSISDN", "243814091623");
		requestJson.put("Language", "EN");
		requestJson.put("Category", "Hourly");
		requestJson.put("RefNum", "jgnkgkgkkkgwgwo");
		requestJson.put("Channel", "WEB");
		requestJson.put("TRANSACTION_ID", "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		requestJson.put("TransactionID", "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		try {
			JSONArray array = offerService.getOfferForTargetMLUser(requestJson, "Hourly");
			Assert.assertEquals("Offer1",
					"{\"OfferID\":61081,\"Amount\":160000,\"OfferDesc\":\"300 SMS at $0.15,Vdc-Vdc,24H\"}",
					array.get(0).toString());
			Assert.assertEquals("Offer2",
					"{\"OfferID\":61080,\"Amount\":150000,\"OfferDesc\":\"500 SMS at $0.15,Vdc-Vdc,24H\"}",
					array.get(1).toString());
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getLocationOffer() throws Exception {
		JSONObject requestJson = new JSONObject();
		requestJson.put("MSISDN", "243816975892");
		requestJson.put("Language", "EN");
		requestJson.put("Category", "Hourly");
		requestJson.put("RefNum", "jgnkgkgkkkgwgwo");
		requestJson.put("Channel", "WEB");
		requestJson.put("TRANSACTION_ID", "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		requestJson.put("TransactionID", "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		offerService.getAsynchOffer(requestJson);
	}

}

package com.comviva.api.j4u.service;

import static com.comviva.api.j4u.utils.J4UOfferConstants.ACCOUNT_BALANCE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.A_VALUE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CATEGORY;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CELL_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CHANNEL;
import static com.comviva.api.j4u.utils.J4UOfferConstants.KEY_ISENHANCED;
import static com.comviva.api.j4u.utils.J4UOfferConstants.LANGUAGE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.ML_FLAG;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OCS_COMMENTS;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OCS_STATUS;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_REFRESH_FLAG;
import static com.comviva.api.j4u.utils.J4UOfferConstants.POOL_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.PRODUCT_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_NUM;
import static com.comviva.api.j4u.utils.J4UOfferConstants.SOURCE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_CODE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_MESSAGE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.SUB_CATEGORY;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TRANSACTION_ID;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.OfferParams;
import com.comviva.api.j4u.model.RankingFormulae;
import com.comviva.api.j4u.model.TemplateDTO;
import com.comviva.api.j4u.utils.ProductInfoCache;
import com.comviva.voltdb.factory.DAOFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DAOFactory.class })
@SuppressStaticInitializationFor("com.comviva.voltdb.factory.DAOFactory")
public class QueryBalanceServiceTest {

	private static final Logger LOGGER = Logger.getLogger(QueryBalanceServiceTest.class);

	@Mock
	private LookUpDAO lookUpDAO;
	@Mock
	private UpdaterDAO updaterDAO;
	@Mock
	private Client voltDbClient;
	@Mock
	private RankingFormulae rf;
//	@Mock
//	private  Map<String, TemplateDTO> mlTemplateCache;

	@InjectMocks
	QueryBalanceService queryBalanceService;

	private JSONObject requestJson;
	private List<String> prodIdsList;
	private List<String> prodIdsList1;
	private TemplateDTO templateDTO;
	private List<OfferParams> offerParamsList;

	/**
	 * Mocks static class before execution of this Test class
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setupBeforeClass() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);

	}

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);
		requestJson = new JSONObject();
		requestJson.put(OCS_STATUS, "OCS_SUCCESS");
		requestJson.put(PRODUCT_ID, "OCS_SUCCESS");
		requestJson.put(OCS_COMMENTS, "OCS_SUCCESS");
		requestJson.put(ACCOUNT_BALANCE, 11820000);
		requestJson.put(MSISDN, "243811404441");
		requestJson.put(CATEGORY, "Data");
		requestJson.put(SUB_CATEGORY, "Data");
		requestJson.put(LANGUAGE, "EN");
		requestJson.put(OFFER_REFRESH_FLAG, "VDISH");
		requestJson.put(CELL_ID, "53021");
		requestJson.put(POOL_ID, "PL_KIN_008");
		requestJson.put(ML_FLAG, "1234");
		requestJson.put(REF_NUM, "Target_001");
		requestJson.put(STATUS_CODE, STATUS_CODE);
		requestJson.put(STATUS_MESSAGE, STATUS_MESSAGE);
		requestJson.put("TRANSACTION_ID", "Transaction");
		requestJson.put(TRANSACTION_ID, "Transaction");
		requestJson.put(KEY_ISENHANCED, true);
		requestJson.put(SOURCE, "MPESA_QUARY_BAL");
		requestJson.put(A_VALUE, "0.0");
		requestJson.put(CHANNEL, "data");

		prodIdsList = new ArrayList<>();
		prodIdsList.add("640010");
		prodIdsList.add("612124");
		prodIdsList.add("612129");

		templateDTO = new TemplateDTO();
		templateDTO.setOfferOrderCSV("ProdId_1,ProdId_2,ProdId_3");
		templateDTO.setTemplate("1)@ProdId_1@\r\n" + "2)@ProdId_2@\r\n" + "3)@ProdId_3@");
		templateDTO.setLangCd(2);
		templateDTO.setTempalteId("ML_SUB_MENU");

		rf = new RankingFormulae();
		rf.setAirtimeBalance(10000000);
		rf.setAaEligible(true);
		rf.setAaBalance(0);
		rf.setCellId("53021");
		offerParamsList = new ArrayList<>();
		prodIdsList1 = new ArrayList<>();
		OfferParams offerParams;
		for (int i = 1; i <= 10; i++) {
			offerParams = new OfferParams();
			offerParams.setOfferPrice(i + 1000);
			offerParams.setExpectedValue(Float.parseFloat("0.00" + i));
			offerParams.setOfferId("10" + i);

			prodIdsList1.add("10" + i);
			offerParamsList.add(offerParams);
		}
		rf.setOfferParams(offerParamsList);

	}

	@Test
	public void processActivateOffer() {
		queryBalanceService.processActivateOffer(requestJson);
	}

	@Test
	public void processActivateOffer_FailuerStatus() {
		JSONObject requestJsons = new JSONObject();
		requestJsons.put(OCS_STATUS, "OCS_FAILURE");
		requestJsons.put(PRODUCT_ID, "OCS_SUCCESS");
		requestJsons.put(OCS_COMMENTS, "OCS_FAILURE");
		requestJsons.put(ACCOUNT_BALANCE, 11820000);
		requestJsons.put(MSISDN, "243811404441");
		requestJsons.put(CATEGORY, "Data");
		requestJsons.put(LANGUAGE, "EN");
		requestJsons.put(OFFER_REFRESH_FLAG, "VDISH");
		requestJson.put(CELL_ID, "53021");
		requestJson.put(POOL_ID, "PL_KIN_008");
		requestJsons.put(ML_FLAG, "1234");
		requestJsons.put(REF_NUM, "Target_001");
		requestJsons.put(STATUS_CODE, STATUS_CODE);
		requestJsons.put(STATUS_MESSAGE, STATUS_MESSAGE);
		requestJsons.put("TRANSACTION_ID", "Transaction");
		queryBalanceService.processActivateOffer(requestJsons);

	}

	@Test
	public void testProcessQueryBalanceRequest() throws Exception {
		try {
			Mockito.when(lookUpDAO.getRFParams(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
					Mockito.anyInt(), Mockito.any(RankingFormulae.class))).thenReturn(rf);
			queryBalanceService.processQueryBalanceRequest(requestJson);

		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testProcessQueryBalanceRequest1() throws Exception {
		try {
			requestJson.put(CATEGORY, "Social");
			Mockito.when(lookUpDAO.getSocialRFParams(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
					Mockito.any(RankingFormulae.class))).thenReturn(rf);
			requestJson.put(CATEGORY, "Social");
//			Mockito.when(templateCache.getMLMenu(anyString())).thenReturn(templateDTO);
			queryBalanceService.processQueryBalanceRequest(requestJson);

		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testProcessQueryBalanceRequest2() throws Exception {
		try {
			rf.setAirtimeBalance(0);
			requestJson.put(CATEGORY, "Social");
			Mockito.when(lookUpDAO.getSocialRFParams(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
					Mockito.any(RankingFormulae.class))).thenReturn(rf);
			queryBalanceService.processQueryBalanceRequest(requestJson);

		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testProcessQueryBalanceRequest3() throws Exception {
		try {
			rf.setAaEligible(false);
			rf.setAirtimeBalance(0);
			requestJson.put(CATEGORY, "Social");
			Mockito.when(lookUpDAO.getSocialRFParams(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
					Mockito.any(RankingFormulae.class))).thenReturn(rf);
			queryBalanceService.processQueryBalanceRequest(requestJson);

		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testProcessQueryBalanceRequest4() throws Exception {
		try {
			rf.setAaEligible(false);
			requestJson.put(CATEGORY, "Social");
			Mockito.when(lookUpDAO.getSocialRFParams(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
					Mockito.any(RankingFormulae.class))).thenReturn(rf);
			queryBalanceService.processQueryBalanceRequest(requestJson);

		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method getOfferForMlTgtUserNonSocialLocationOffersMethod() throws NoSuchMethodException {
		Method method = QueryBalanceService.class.getDeclaredMethod("getOfferForMlTgtUserNonSocialLocationOffers",
				JSONArray.class, List.class, List.class, JSONObject.class, String.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testGetOfferForMlTgtUserNonSocialLocationOffers() {
		try {
			List<String> list = new ArrayList<>();
			boolean actual = (boolean) getOfferForMlTgtUserNonSocialLocationOffersMethod().invoke(queryBalanceService,
					new JSONArray(), prodIdsList, list, requestJson, "J4U");
			assertTrue(actual);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testGetOfferForMlTgtUserNonSocialLocationOffers1() {
		try {
			List<String> list = new ArrayList<>();
			Mockito.when(lookUpDAO.getRFParams(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
					Mockito.anyInt(), Mockito.any(RankingFormulae.class))).thenReturn(rf);
			boolean actual = (boolean) getOfferForMlTgtUserNonSocialLocationOffersMethod().invoke(queryBalanceService,
					new JSONArray(), prodIdsList1, list, requestJson, "J4U");
			assertFalse(actual);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method formOfferJSONArrayMethod() throws NoSuchMethodException {
		Method method = QueryBalanceService.class.getDeclaredMethod("formOfferJSONArray", List.class, int.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testFormOfferJSONArray() {
		try {

			formOfferJSONArrayMethod().invoke(queryBalanceService, prodIdsList, 2);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method formOfferJSONArrayForSocialMethod() throws NoSuchMethodException {
		Method method = QueryBalanceService.class.getDeclaredMethod("formOfferJSONArrayForSocial", List.class,
				int.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testFormOfferJSONArrayForSocial() {
		try {
			formOfferJSONArrayForSocialMethod().invoke(queryBalanceService, prodIdsList, 2);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method generateSubMenuMethod() throws NoSuchMethodException {
		Method method = QueryBalanceService.class.getDeclaredMethod("generateSubMenu", int.class, List.class,
				ProductInfoCache.class, TemplateDTO.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testGenerateSubMenu() {
		ProductInfoCache productInfoCache = ProductInfoCache.instance();
		try {
			generateSubMenuMethod().invoke(queryBalanceService, 2, prodIdsList, productInfoCache, templateDTO);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method generateSubMenuSocialMethod() throws NoSuchMethodException {
		Method method = QueryBalanceService.class.getDeclaredMethod("generateSubMenuSocial", int.class, List.class,
				ProductInfoCache.class, TemplateDTO.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testGenerateSubMenuSocial() {
		ProductInfoCache productInfoCache = ProductInfoCache.instance();
		try {
			generateSubMenuSocialMethod().invoke(queryBalanceService, 2, prodIdsList, productInfoCache, templateDTO);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method getSubMenuTemplateForLocationMethod() throws NoSuchMethodException {
		Method method = QueryBalanceService.class.getDeclaredMethod("getSubMenuTemplateForLocation", int.class,
				JSONObject.class, int.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testGetSubMenuTemplateForLocationForOneProdect() {
		Map<String, TemplateDTO> map = new HashMap<>();
		map.put("ML_HOURLY_DATA_1OFFER_MENU_2", templateDTO);
		map.put("ML_HOURLY_DATA_1OFFER_MENU_2", templateDTO);
		map.put("ML_SUB_MENU_2", templateDTO);

		try {
//			Mockito.when(templateCache.getMLMenu(anyString())).thenReturn(templateDTO);
//			Mockito.when(mlTemplateCache.get(anyString())).thenReturn(templateDTO);
//			Mockito.when(lookUpDAO.getTemplatesMap()).thenReturn(map);
//			Mockito.when(lookUpDAO.getMLTemplatesMap()).thenReturn(map);
			getSubMenuTemplateForLocationMethod().invoke(queryBalanceService, 2, requestJson, 1);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testGetSubMenuTemplateForLocationForTwoProdect() {
		try {
			getSubMenuTemplateForLocationMethod().invoke(queryBalanceService, 2, requestJson, 2);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void testGetSubMenuTemplateForLocationForManyProdect() {
		try {
			getSubMenuTemplateForLocationMethod().invoke(queryBalanceService, 2, requestJson, 3);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method updateReducedCCRMethod() throws NoSuchMethodException {
		Method method = QueryBalanceService.class.getDeclaredMethod("updateReducedCCR", String.class, String.class,
				String.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testUpdateReducedCCR() {
		try {
			updateReducedCCRMethod().invoke(queryBalanceService, "243811404441", "D", "VDISH");
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	private Method processResponseJsonMethod() throws NoSuchMethodException {
		Method method = QueryBalanceService.class.getDeclaredMethod("processResponseJson", JSONObject.class, int.class,
				String.class, JSONArray.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testProcessResponseJson() {
		JSONArray offers = new JSONArray();
		try {
			JSONObject actual = (JSONObject) processResponseJsonMethod().invoke(queryBalanceService, requestJson, 1,
					"success", offers);
			String expected = "{\"POOL_ID\":\"PL_KIN_008\",\"MSISDN\":243811404441,\"Language\":\"EN\",\"Channel\":\"data\",\"CELL_ID\":\"53021\",\"Offers\":[],\"StatusCode\":1,\"RefNum\":\"Target_001\",\"TransactionID\":\"Transaction\",\"StatusMessage\":\"success\"}";
			assertTrue(expected.equals(actual.toString()));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
			e.printStackTrace();
		}
	}

}

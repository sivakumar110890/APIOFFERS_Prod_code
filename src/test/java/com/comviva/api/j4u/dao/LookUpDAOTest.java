package com.comviva.api.j4u.dao;

import static com.comviva.api.j4u.utils.J4UOfferConstants.LANG_CODE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_ID;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
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

import com.comviva.api.j4u.model.OfferParams;
import com.comviva.api.j4u.model.RankingFormulae;
import com.comviva.api.j4u.model.TemplateDTO;
import com.comviva.api.j4u.model.UserInfo;
import com.comviva.voltdb.factory.DAOFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DAOFactory.class })
@SuppressStaticInitializationFor("com.comviva.voltdb.factory.DAOFactory")
public class LookUpDAOTest {
	
	private static final Logger LOGGER = Logger.getLogger(LookUpDAOTest.class);

	@Mock
	private UpdaterDAO updaterDAO;
	@Mock
	private Client voltDbClient;

//	@InjectMocks
	private LookUpDAO lookUpDAO;

	@BeforeClass
	public static void setupBeforeClass() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);

	}

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);
		lookUpDAO = new LookUpDAO();
	}

	@Test
	public void getMLTemplatesMap_Test() {
		try {
			Map<String, TemplateDTO> templatesMap = lookUpDAO.getMLTemplatesMap();
			Assert.assertEquals(true, !templatesMap.isEmpty());
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getOfferForTgtMLUser() {
		try {
			JSONArray offerAry = lookUpDAO.getOfferForTgtMLUser("243810776942", "J4U Voice", "null");
			Assert.assertNotNull(offerAry);
			JSONObject offer = offerAry.getJSONObject(0);
			Assert.assertEquals(true, offer.has(OFFER_ID));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getOfferForTgtMLUser_1() {
		try {
			JSONArray offerAry = lookUpDAO.getOfferForTgtMLUser("243811662073", "J4U Hourly Data", "null");
			Assert.assertNotNull(offerAry);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getOfferLocationValues() {
		String msisdn = "243819708618";
		String prodType = "J4U Voice";
		List<OfferParams> offerParamsList = new ArrayList<>();
		OfferParams offer = new OfferParams();
		offer.setOfferId("611019");
		offerParamsList.add(offer);
		offer.setOfferId("611021");
		offerParamsList.add(offer);
		List<String> prodIdsList = new ArrayList<>();
		prodIdsList.add("611022");
		prodIdsList.add("611023");
		int langCode = 1;
		String poolID = "PL_KIN_002";
		try {
			List<OfferParams> offerParams = lookUpDAO.getOfferLocationValues(msisdn, prodType, offerParamsList,
					prodIdsList, langCode, poolID);
			Assert.assertEquals(false, !offerParams.isEmpty());
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getOffersByBalanceAndRank() {
		String msisdn = "243819708618";
		String prodType = "J4U Integrated & SMS";
		double accBal = 350000;
		List<String> evsList = new ArrayList<>();
		try {
			List<String> productIdList = lookUpDAO.getOffersByBalanceAndRank(msisdn, prodType, "null", accBal, evsList);
			Assert.assertEquals(true, !productIdList.isEmpty());
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getUserInfo() {
		String msisdn = "810405314";
		String expected = "810405314";
		try {
			UserInfo userInfo = lookUpDAO.getUserInfo(msisdn);
			Assert.assertEquals(expected, userInfo.getMsisdn());
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getUserInfo_countryCode() {
		String msisdn = "243810405314";
		String expected = "810405314";
		try {
			UserInfo userInfo = lookUpDAO.getUserInfo(msisdn);
			Assert.assertEquals(expected, userInfo.getMsisdn());
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getRandomFlag() {
		String msisdn = "811429502";
		boolean expected = true;
		try {
			boolean actual = lookUpDAO.getRandomFlag(msisdn);
			Assert.assertEquals(expected, actual);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getRandomFlag_countryCode() {
		String msisdn = "243811429502";
		boolean expected = true;
		try {
			boolean actual = lookUpDAO.getRandomFlag(msisdn);
			Assert.assertEquals(expected, actual);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	
	private Method validateMSISDNMethod() throws NoSuchMethodException {
		Method method = LookUpDAO.class.getDeclaredMethod("validateMSISDN", String.class);
		method.setAccessible(true);
		return method;
	}

	@Test
	public void testValidateMSISDN() {
		try {
			String msisdn = "124381052537";
			String actual =(String) validateMSISDNMethod().invoke(lookUpDAO, msisdn);
			String expected = "243124381052537";
			assertTrue(actual.equals(expected));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}
	@Test
	public void testValidateMSISDN_PREFIX_ZERO() {
		try {
			String msisdn = "024381052537";
			String actual =(String) validateMSISDNMethod().invoke(lookUpDAO, msisdn);
			String expected = "24324381052537";
			assertTrue(actual.equals(expected));
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getOffersByExpectedValue() {
		String msisdn = "243810525327";
		String prodType = "J4U Integrated & SMS";
		List<String> evsList = new ArrayList<>();
		List<String> prodIdsList = new ArrayList<>();
		prodIdsList.add("611060");
		prodIdsList.add("611083");
		List<String> offers;
		boolean expected = true;
		try {
			offers = lookUpDAO.getOffersByExpectedValue(msisdn, prodType, "null", prodIdsList, evsList);
			Assert.assertEquals(expected, !offers.isEmpty());
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getProductPriceMap() {
		Map<String, String> productPriceMap;
		boolean expected = true;
		try {
			productPriceMap = lookUpDAO.getProductPriceMap();
			Assert.assertEquals(expected, !productPriceMap.isEmpty());
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getTemplatesMap() {
		Map<String, TemplateDTO> templatesMap = new HashMap<>();
		boolean expected = true;
		try {
			templatesMap = lookUpDAO.getTemplatesMap();
			Assert.assertEquals(expected, !templatesMap.isEmpty());
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getProductTypeById() {
		String productId = "610979";
		String expected = "J4U Integrated & SMS";
		try {
			String actualProductType = lookUpDAO.getProductTypeById(productId);
			Assert.assertEquals(expected, actualProductType);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getProductTypeById_Null() {
		String productId = "";
		try {
			String actualProductType = lookUpDAO.getProductTypeById(productId);
			Assert.assertEquals(null, actualProductType);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getRFParams() {
		RankingFormulae rankingFormulae = new RankingFormulae();
		boolean expected = true;
		String msisdn = "243819729087";
		String prodType = "J4U Integrated & SMS";
		int langCode = 3;
		try {
			rankingFormulae = lookUpDAO.getRFParams(msisdn, prodType, prodType, langCode, rankingFormulae);
			Assert.assertEquals(expected, !rankingFormulae.getOfferParams().isEmpty());
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getProductDescRandomLocationOffers() {
		String productId = "610974";
		int languageCode = 3;
		try {
			lookUpDAO.getProductDescRandomLocationOffers(productId, languageCode);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getSocialOffersByBalanceAndRank() {
		String msisdn = "243810525327";
		String prodType = "J4U Integrated & SMS";
		List<String> evsList = new ArrayList<>();
		List<String> prodIdsList = new ArrayList<>();
		prodIdsList.add("611060");
		prodIdsList.add("611083");
		List<String> offers;
		boolean expected = true;
		try {
			offers = lookUpDAO.getSocialOffersByBalanceAndRank(msisdn, prodType, 0, evsList);
			Assert.assertEquals(expected, !offers.isEmpty());
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getSocialOffersByExpectedValue() {
		String msisdn = "243810525327";
		String prodType = "J4U Integrated & SMS";
		List<String> evsList = new ArrayList<>();
		List<String> prodIdsList = new ArrayList<>();
		prodIdsList.add("611060");
		prodIdsList.add("611083");
		List<String> offers;
		boolean expected = true;
		try {
			offers = lookUpDAO.getSocialOffersByExpectedValue(msisdn, prodType, prodIdsList, evsList);
			Assert.assertEquals(expected, !offers.isEmpty());
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getOfferValues() {
		String msisdn = "243810525327";
		String prodType = "J4U Integrated & SMS";
		List<String> prodIdsList = new ArrayList<>();
		prodIdsList.add("611060");
		prodIdsList.add("611083");
		try {
			lookUpDAO.getOfferValues(msisdn, prodType, null, prodIdsList, 0);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getSocialOfferValues() {
		String msisdn = "243810525327";
		String prodType = "J4U Integrated & SMS";
		List<String> prodIdsList = new ArrayList<>();
		prodIdsList.add("611060");
		prodIdsList.add("611083");
		try {
			lookUpDAO.getSocialOfferValues(msisdn, prodType, null, prodIdsList, 0);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getMorningOfferWhiteList() {
		String msisdn = "243810525327";
		try {
			lookUpDAO.getMorningOfferWhiteList(msisdn);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getRagUserInfo() {
		String msisdn = "243810525327";
		JSONObject requestJSON = new JSONObject();
		requestJSON.put(MSISDN, msisdn);
		requestJSON.put(LANG_CODE, 2);
		try {
			lookUpDAO.getRagUserInfo(requestJSON);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getSagUserInfo() {
		String msisdn = "243810525327";
		JSONObject requestJSON = new JSONObject();
		requestJSON.put(MSISDN, msisdn);
		requestJSON.put(LANG_CODE, 2);
		try {
			lookUpDAO.getSagUserInfo(requestJSON);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getSAGOptinStatus() {
		String msisdn = "243810525327";
		try {
			lookUpDAO.getSAGOptinStatus(msisdn);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

	@Test
	public void getRAGOptinStatus() {
		String msisdn = "243810525327";
		try {
			lookUpDAO.getRAGOptinStatus(msisdn);
		} catch (Exception e) {
			LOGGER.error("Error in test :: ", e);
		}
	}

}

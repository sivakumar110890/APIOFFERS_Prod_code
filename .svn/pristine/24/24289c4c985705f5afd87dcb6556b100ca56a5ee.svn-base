package com.comviva.api.offers;

import static com.comviva.api.j4u.utils.J4UOfferConstants.CATEGORY;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CHANNEL;
import static com.comviva.api.j4u.utils.J4UOfferConstants.KEY_ISMORNING;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFERS;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_DESC;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_NUM;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REQUEST_BUFFER;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_CODE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_MESSAGE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.SUB_CATEGORY;
import static com.comviva.api.j4u.utils.J4UOfferConstants.THIRD_PARTY_REF;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TRANSACTION_ID;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.xml.parsers.ParserConfigurationException;

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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.comviva.api.exception.ConfigException;
import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.ApiLog;
import com.comviva.api.j4u.service.J4UOfferService;
import com.comviva.voltdb.factory.DAOFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DAOFactory.class })
@SuppressStaticInitializationFor("com.comviva.voltdb.factory.DAOFactory")
public class GetOffersTest {
	public final static Logger LOGGER = Logger.getLogger(GetOffersTest.class);
	private String refNum = "refNum_1234";

	@Mock
	private UpdaterDAO updaterDAO = null;

	@Mock
	private J4UOfferService j4uOfferService;

	@InjectMocks
	private GetOffers getOffers;

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);
	}

	@BeforeClass
	public static void setupBeforeClass() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);

	}

	@Test
	public void doGet() {
		JSONObject actOfferResObj = new JSONObject();
		Long msisdn = 43816975892L;
		actOfferResObj.put(REF_NUM, refNum);
		actOfferResObj.put(MSISDN, msisdn);
		actOfferResObj.put(CATEGORY, "Integrated");
		actOfferResObj.put(STATUS_CODE, 0);
		actOfferResObj.put(TRANSACTION_ID, "1234");
		actOfferResObj.put(CHANNEL, "Web");
		actOfferResObj.put(THIRD_PARTY_REF, "ThirdPartyRef");
		actOfferResObj.put(SUB_CATEGORY, "ThirdPartyRef");
		JSONObject syncOfferObj = new JSONObject(actOfferResObj.toString());
		JSONObject offer1 = new JSONObject();
		offer1.put(OFFER_ID, "123");
		offer1.put(OFFER_DESC, "OfferDesc1");
		JSONObject offer2 = new JSONObject();
		offer2.put(OFFER_ID, "1234");
		offer2.put(OFFER_DESC, "OfferDesc2");
		JSONArray offers = new JSONArray();
		offers.put(0, offer1);
		offers.put(1, offer2);
		syncOfferObj.put(OFFERS, offers);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		try {
			Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
			Mockito.when(j4uOfferService.getSynchOffer(Mockito.any(JSONObject.class))).thenReturn(syncOfferObj);
			getOffers.doGet(request, response);
		} catch (ServletException | IOException | ConfigException | ParserConfigurationException e) {
			LOGGER.error(e);
		}
	}
	@Test
	public void doGetWhenMorningOffer() {
		JSONObject actOfferResObj = new JSONObject();
		Long msisdn = 43816975892L;
		actOfferResObj.put(REF_NUM, refNum);
		actOfferResObj.put(MSISDN, msisdn);
		actOfferResObj.put(CATEGORY, "MorningOffer");
		actOfferResObj.put(STATUS_CODE, 0);
		actOfferResObj.put(TRANSACTION_ID, "1234");
		actOfferResObj.put(CHANNEL, "Web");
		actOfferResObj.put(THIRD_PARTY_REF, "ThirdPartyRef");
		actOfferResObj.put(SUB_CATEGORY, "ThirdPartyRef");
		JSONObject syncOfferObj = new JSONObject(actOfferResObj.toString());
		JSONObject offer1 = new JSONObject();
		offer1.put(OFFER_ID, "123");
		offer1.put(OFFER_DESC, "OfferDesc");
		JSONObject offer2 = new JSONObject();
		offer2.put(OFFER_ID, "123");
		offer2.put(OFFER_DESC, "OfferDesc");
		JSONArray offers = new JSONArray();
		offers.put(0, offer1);
		offers.put(1, offer2);
		syncOfferObj.put(OFFERS, offers);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		try {
			Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
			Mockito.when(j4uOfferService.getSynchOffer(Mockito.any(JSONObject.class))).thenReturn(syncOfferObj);
			getOffers.doGet(request, response);
		} catch (ServletException | IOException | ConfigException | ParserConfigurationException e) {
			LOGGER.error(e);
		}
	}
	@Test
	public void doGetWhenMorningOfferButStatusCodeNotZero() {
		JSONObject actOfferResObj = new JSONObject();
		Long msisdn = 43816975892L;
		actOfferResObj.put(REF_NUM, refNum);
		actOfferResObj.put(MSISDN, msisdn);
		actOfferResObj.put(CATEGORY, "MorningOffer");
		actOfferResObj.put(STATUS_CODE, 30);
		actOfferResObj.put(TRANSACTION_ID, "1234");
		actOfferResObj.put(CHANNEL, "Web");
		actOfferResObj.put(THIRD_PARTY_REF, "ThirdPartyRef");
		actOfferResObj.put(SUB_CATEGORY, "ThirdPartyRef");
		JSONObject syncOfferObj = new JSONObject(actOfferResObj.toString());
		JSONObject offer1 = new JSONObject();
		offer1.put(OFFER_ID, "123");
		offer1.put(OFFER_DESC, "OfferDesc");
		JSONObject offer2 = new JSONObject();
		offer2.put(OFFER_ID, "123");
		offer2.put(OFFER_DESC, "OfferDesc");
		JSONArray offers = new JSONArray();
		offers.put(0, offer1);
		offers.put(1, offer2);
		syncOfferObj.put(OFFERS, offers);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		try {
			Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
			Mockito.when(j4uOfferService.getSynchOffer(Mockito.any(JSONObject.class))).thenReturn(syncOfferObj);
			getOffers.doGet(request, response);
		} catch (ServletException | IOException | ConfigException | ParserConfigurationException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void doGet_Null() {
		JSONObject actOfferResObj = new JSONObject();
		Long msisdn = 43816975892L;
		actOfferResObj.put(REF_NUM, refNum);
		actOfferResObj.put(MSISDN, msisdn);
		actOfferResObj.put(CATEGORY, "Integrated");
		actOfferResObj.put(STATUS_CODE, 0);
		actOfferResObj.put(TRANSACTION_ID, "1234");
		actOfferResObj.put(CHANNEL, "Web");
		actOfferResObj.put(THIRD_PARTY_REF, "ThirdPartyRef");
		actOfferResObj.put(SUB_CATEGORY, "ThirdPartyRef");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		try {
			Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
			Mockito.when(j4uOfferService.getSynchOffer(Mockito.any(JSONObject.class))).thenReturn(null);
			getOffers.doGet(request, response);
		} catch (ServletException | IOException | ConfigException | ParserConfigurationException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void doGet_NoOffers() {
		JSONObject actOfferResObj = new JSONObject();
		Long msisdn = 43816975892L;
		actOfferResObj.put(REF_NUM, refNum);
		actOfferResObj.put(MSISDN, msisdn);
		actOfferResObj.put(CATEGORY, "Integrated");
		actOfferResObj.put(STATUS_CODE, 30);
		actOfferResObj.put(TRANSACTION_ID, "1234");
		actOfferResObj.put(CHANNEL, "Web");
		actOfferResObj.put(THIRD_PARTY_REF, "ThirdPartyRef");
		actOfferResObj.put(SUB_CATEGORY, "ThirdPartyRef");
		JSONObject syncOfferObj = new JSONObject(actOfferResObj.toString());
		JSONObject offer1 = new JSONObject();
		offer1.put(OFFER_ID, "123");
		offer1.put(OFFER_DESC, "OfferDesc");
		JSONObject offer2 = new JSONObject();
		offer2.put(OFFER_ID, "123");
		offer2.put(OFFER_DESC, "OfferDesc");
		JSONArray offers = new JSONArray();
		offers.put(0, offer1);
		offers.put(1, offer2);
		syncOfferObj.put(OFFERS, offers);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		try {
			Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
			Mockito.when(j4uOfferService.getSynchOffer(Mockito.any(JSONObject.class))).thenReturn(syncOfferObj);
			getOffers.doGet(request, response);
		} catch (ServletException | IOException | ConfigException | ParserConfigurationException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void doGet_GenralFailuer() {
		JSONObject actOfferResObj = new JSONObject();
		Long msisdn = 43816975892L;
		actOfferResObj.put(REF_NUM, refNum);
		actOfferResObj.put(MSISDN, msisdn);
		actOfferResObj.put(CATEGORY, "Integrated");
		actOfferResObj.put(STATUS_CODE, 90);
		actOfferResObj.put(TRANSACTION_ID, "1234");
		actOfferResObj.put(SUB_CATEGORY, "ThirdPartyRef");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		try {
			Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
			Mockito.when(j4uOfferService.getSynchOffer(Mockito.any(JSONObject.class))).thenReturn(actOfferResObj);
			Mockito.doNothing().when(j4uOfferService).getAsynchOffer(Mockito.any(JSONObject.class));
			getOffers.doGet(request, response);
		} catch (ServletException | IOException | ConfigException | ParserConfigurationException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void doGet_Error() {
		JSONObject actOfferResObj = new JSONObject();
		Long msisdn = 43816975892L;
		actOfferResObj.put(REF_NUM, refNum);
		actOfferResObj.put(STATUS_MESSAGE, "a");
		actOfferResObj.put(STATUS_CODE, 12);
		actOfferResObj.put(MSISDN, msisdn);
		actOfferResObj.put(SUB_CATEGORY, "ThirdPartyRef");
		Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		try {
			getOffers.doGet(request, response);
		} catch (ServletException | IOException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void doGet_Exception() {
		JSONObject actOfferResObj = new JSONObject();
		Long msisdn = 43816975892L;
		actOfferResObj.put(CATEGORY, "Integrated");
		actOfferResObj.put(MSISDN, msisdn);
		Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		try {
			getOffers.doGet(request, response);
		} catch (ServletException | IOException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void receiveAsyncOffers() {
		JSONObject actOfferResObj = new JSONObject();		
		JSONObject offer1 = new JSONObject();
		offer1.put(OFFER_ID, "123");
		offer1.put(OFFER_DESC, "OfferDesc1");
		JSONObject offer2 = new JSONObject();
		offer2.put(OFFER_ID, "1234");
		offer2.put(OFFER_DESC, "OfferDesc2");
		JSONArray offersArray = new JSONArray();
		offersArray.put(0, offer1);
		offersArray.put(1, offer2);	
		actOfferResObj.put(OFFERS, offersArray);		
		Long msisdn = 43816975892L;
		actOfferResObj.put(REF_NUM, refNum);
		actOfferResObj.put(MSISDN, msisdn);
		actOfferResObj.put(CATEGORY, "Integrated");
		actOfferResObj.put(STATUS_CODE, 90);
		actOfferResObj.put(TRANSACTION_ID, "1234");
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
		getOffers.receiveAsyncOffers(actOfferResObj);
	}
	
	@Test
	public void testDoGetWhenStatusCodeZero() {
		JSONObject actOfferResObj = new JSONObject();
		Long msisdn = 438169758921L;
		actOfferResObj.put(REF_NUM, refNum);
		actOfferResObj.put(MSISDN, msisdn);
		actOfferResObj.put(CATEGORY, "Integrated");
		actOfferResObj.put(STATUS_CODE, 0);
		actOfferResObj.put(TRANSACTION_ID, "1234");
		actOfferResObj.put(CHANNEL, "Web");
		actOfferResObj.put(THIRD_PARTY_REF, "ThirdPartyRef");
		actOfferResObj.put(KEY_ISMORNING, false);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());

		String str = "{\"POOL_ID\":\"PL_KIN_008\",\"MSISDN\":243811404441,\"Language\":\"en\",\"CELL_ID\":\"53021\",\"StatusCode\":30,\"RefNum\":\"Target_001\",\"TransactionID\":\"c27955aa-cd72-481f-b021-6fe9d9ed852c\",\"StatusMessage\":\"Nooffersareavailable\"}";
		JSONObject synchOffersObj = new JSONObject(str);

		try {
			Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
			when(j4uOfferService.getSynchOffer(Mockito.any(JSONObject.class))).thenReturn(synchOffersObj);
			getOffers.doGet(request, response);
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}
}

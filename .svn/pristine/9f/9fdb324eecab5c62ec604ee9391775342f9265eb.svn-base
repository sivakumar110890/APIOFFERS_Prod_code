package com.comviva.api.offers;

import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_CODE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_MESSAGE;

import java.io.IOException;

import javax.servlet.ServletException;

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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.voltdb.client.Client;

import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.ApiLog;
import com.comviva.voltdb.factory.DAOFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DAOFactory.class })
@SuppressStaticInitializationFor("com.comviva.voltdb.factory.DAOFactory")
public class ActivateOfferTest {

	public final static Logger LOGGER = Logger.getLogger(ActivateOfferTest.class);

	private String refNum = "refNum1";

	@Mock
	private UpdaterDAO updaterDAO;

	@Mock
	private Client voltClient;

	@InjectMocks
	public ActivateOffer activateOffer;

	@BeforeClass
	public static void setupBeforeClass() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);

	}

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);
	}

	@Test
	public void actOfferResponseAsync() {
		JSONObject actOfferResObj = new JSONObject();
		actOfferResObj.put("RefNum", refNum);
		actOfferResObj.put(STATUS_MESSAGE, "a");
		actOfferResObj.put(STATUS_CODE, 12);
		actOfferResObj.put("MSISDN", "43816975892");
		actOfferResObj.put("RESULT_CODE", "405000000");
		actOfferResObj.put("COMMENTS", "405000000 Operation successfully.");
		actOfferResObj.put("Language", "EN");
		actOfferResObj.put("ACCOUNT_BALANCE", 500000);
		actOfferResObj.put("Category", "Integrated");
		actOfferResObj.put("OFFER_REFRESH_FLAG", "VDI");
		actOfferResObj.put("STATUS", "OCS_SUCCESS");
		actOfferResObj.put("AIRTIME_ADVANCE_BALANCE", 165000);
		actOfferResObj.put("LANG_CODE", 2);
		actOfferResObj.put("A_VALUE", "0.3");
		actOfferResObj.put("CELL_ID", "630010101756998");
		actOfferResObj.put("POOL_ID", "Ban20");
		actOfferResObj.put("TransactionID", "a4f4fea8-e783-4efd-a1d5-9e5c3b8ec51b");
		Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
		activateOffer.actOfferResponseAsync(actOfferResObj);
	}

	@Test
	public void doGet() {
		JSONObject actOfferResObj = new JSONObject();
		Long msisdn = 43816975892L;
		actOfferResObj.put("RefNum", refNum);
		actOfferResObj.put("MSISDN", msisdn);
		actOfferResObj.put("Language", "EN");
		actOfferResObj.put("LANG_CODE", 2);
		actOfferResObj.put("A_VALUE", "0.3");
		actOfferResObj.put("CELL_ID", "630010101756998");
		actOfferResObj.put("POOL_ID", "Ban20");
		actOfferResObj.put("OfferID", "6300");
		actOfferResObj.put("Channel", "Web");
		actOfferResObj.put("ThirdPartyRef", "ThirdPartyRef");
		actOfferResObj.put("RefTransactionID", "12345");
		Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute("REQUEST_BUFFER", actOfferResObj.toString());
		try {
			activateOffer.goGet(request, response);
		} catch (ServletException | IOException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void doGet_Error() {
		JSONObject actOfferResObj = new JSONObject();
		Long msisdn = 43816975892L;
		actOfferResObj.put("RefNum", refNum);
		actOfferResObj.put(STATUS_MESSAGE, "a");
		actOfferResObj.put(STATUS_CODE, 12);
		actOfferResObj.put("MSISDN", msisdn);
		actOfferResObj.put("Channel", "Web");
		actOfferResObj.put("ThirdPartyRef", "ThirdPartyRef");
		Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute("REQUEST_BUFFER", actOfferResObj.toString());
		try {
			activateOffer.goGet(request, response);
		} catch (ServletException | IOException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void doGet_Exception() {
		JSONObject actOfferResObj = new JSONObject();
		actOfferResObj.put(STATUS_MESSAGE, "a");
		actOfferResObj.put(STATUS_CODE, 12);
		actOfferResObj.put("OfferID", "6300");
		actOfferResObj.put("Channel", "Web");
		actOfferResObj.put("ThirdPartyRef", "ThirdPartyRef");
		actOfferResObj.put("RefTransactionID", "12345");
		Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute("REQUEST_BUFFER", actOfferResObj.toString());
		try {
			activateOffer.goGet(request, response);
		} catch (ServletException | IOException e) {
			LOGGER.error(e);
		}
	}

//	@Test
//	public void test(){
//		doGet();
//		actOfferResponseAsync();
//		doGet_Error();
//		doGet_Exception();
//	}
}

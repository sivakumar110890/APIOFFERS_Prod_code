package com.comviva.api.offers;

import static com.comviva.api.j4u.utils.J4UOfferConstants.CATEGORY;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CHANNEL;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_NUM;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REQUEST_BUFFER;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_CODE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.THIRD_PARTY_REF;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TRANSACTION_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.xml.parsers.ParserConfigurationException;

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

import com.comviva.api.exception.ConfigException;
import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.ApiLog;
import com.comviva.api.j4u.service.SAGOfferService;
import com.comviva.voltdb.factory.DAOFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DAOFactory.class })
@SuppressStaticInitializationFor("com.comviva.voltdb.factory.DAOFactory")
public class SAGOptInTest {

	public final static Logger LOGGER = Logger.getLogger(SAGOptInTest.class);
	private String refNum = "refNum_1234";

	@Mock
	private UpdaterDAO updaterDAO = null;
	@Mock
	private SAGOfferService sagOfferService;


	@InjectMocks
	private SAGOptIn sagOptIn;

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
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		
		String str = "{\"POOL_ID\":\"PL_KIN_008\",\"MSISDN\":243811404441,\"Language\":\"en\",\"CELL_ID\":\"53021\",\"StatusCode\":30,\"RefNum\":\"Target_001\",\"TransactionID\":\"c27955aa-cd72-481f-b021-6fe9d9ed852c\",\"StatusMessage\":\"Nooffersareavailable\"}";
		JSONObject synchOffersObj = new JSONObject(str);
		try {
			Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
			when(sagOfferService.getSagOptIn(any(JSONObject.class))).thenReturn(synchOffersObj);
			sagOptIn.doGet(request, response);
		} catch (ServletException | IOException | ConfigException | ParserConfigurationException e) {
			LOGGER.error(e);
		}
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
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());

		String str = "{\"POOL_ID\":\"PL_KIN_008\",\"MSISDN\":243811404441,\"Language\":\"en\",\"CELL_ID\":\"53021\",\"StatusCode\":0,\"RefNum\":\"Target_001\",\"TransactionID\":\"c27955aa-cd72-481f-b021-6fe9d9ed852c\",\"StatusMessage\":\"Success\"}";
		JSONObject synchOffersObj = new JSONObject(str);

		try {
			Mockito.doNothing().when(updaterDAO).insertApiLog(Mockito.any(ApiLog.class));
			when(sagOfferService.getSagOptIn(any(JSONObject.class))).thenReturn(synchOffersObj);
			sagOptIn.doGet(request, response);
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

}

package com.comviva.api.offers;

import static com.comviva.api.j4u.utils.J4UOfferConstants.CATEGORY;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CHANNEL;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_NUM;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REQUEST_BUFFER;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_CODE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_MESSAGE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.THIRD_PARTY_REF;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TRANSACTION_ID;

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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.service.J4UOfferService;
import com.comviva.voltdb.factory.DAOFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DAOFactory.class})
@SuppressStaticInitializationFor("com.comviva.voltdb.factory.DAOFactory")
public class StatusUpdateTest {

	public final static Logger LOGGER = Logger.getLogger(StatusUpdateTest.class);
	private String refNum = "refNum_1234";
	
	@Mock
	private UpdaterDAO updaterDAO = null;
	
	@Mock
    private J4UOfferService j4uOfferService;
	
	@InjectMocks
	private StatusUpdate statusUpdate;

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);
	}
	
	@BeforeClass
	public static void setupBeforeClass() throws Exception{
		PowerMockito.mockStatic(DAOFactory.class);

	}

	@Test
	public void test(){
		doPost();
		doPost_NoStatusMessage();
	}

	private void doPost_NoStatusMessage() {
		JSONObject actOfferResObj = new JSONObject();
		Long msisdn = 43816975892L;
		actOfferResObj.put(REF_NUM, refNum);
		actOfferResObj.put(MSISDN, msisdn);
		actOfferResObj.put(STATUS_CODE, 0);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		try {
			statusUpdate.doPost(request, response);
		} catch (ServletException | IOException  e) {
			LOGGER.error(e);
		}
	}
	
	private void doPost() {
		JSONObject actOfferResObj = new JSONObject();
		Long msisdn = 43816975892L;
		actOfferResObj.put(REF_NUM, refNum);
		actOfferResObj.put(MSISDN, msisdn);
		actOfferResObj.put(CATEGORY, "Integrated");
		actOfferResObj.put(STATUS_CODE, 0);
		actOfferResObj.put(TRANSACTION_ID, "1234");
		actOfferResObj.put(CHANNEL, "Web");
		actOfferResObj.put(THIRD_PARTY_REF, "ThirdPartyRef");
		actOfferResObj.put(STATUS_MESSAGE, "success Message");
		actOfferResObj.put(OFFER_ID, "67890");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		try {
			statusUpdate.doPost(request, response);
		} catch (ServletException | IOException  e) {
			LOGGER.error(e);
		}
	}
}

package com.comviva.api.offers;

import static com.comviva.api.j4u.utils.J4UOfferConstants.REQUEST_BUFFER;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.comviva.voltdb.factory.DAOFactory;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ DAOFactory.class })
@SuppressStaticInitializationFor("com.comviva.voltdb.factory.DAOFactory")
public class ReloadCacheTest {
	
	public final static Logger LOGGER = Logger.getLogger(ReloadCacheTest.class);
	public static final String CACHE = "cache";
    public static final String OP = "op";
	private String refNum = "refNum_1234";
	

	@InjectMocks
	ReloadCache reloadCache;
	
	private JSONObject actOfferResObj;
	
	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);
	}

	@BeforeClass
	public static void setupBeforeClass() throws Exception {
		PowerMockito.mockStatic(DAOFactory.class);

	}

	@Test
	public void testDoGet() {
		JSONObject actOfferResObj = new JSONObject();
		actOfferResObj.put(OP, "status");
		actOfferResObj.put(CACHE, "productpricecache");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		try {
			reloadCache.doPost(request, response);
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}
	@Test
	public void testDoGet1() {
		JSONObject actOfferResObj = new JSONObject();
		actOfferResObj.put(OP, "reload");
		actOfferResObj.put(CACHE, "productinfocache");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		try {
			reloadCache.doPost(request, response);
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}
	@Test
	public void testDoGet2() {
		JSONObject actOfferResObj = new JSONObject();
		actOfferResObj.put(OP, "reload");
		actOfferResObj.put(CACHE, "productpricecache");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		try {
			reloadCache.doPost(request, response);
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}
	@Test
	public void testDoGet3() {
		JSONObject actOfferResObj = new JSONObject();
		actOfferResObj.put(OP, "reload");
		actOfferResObj.put(CACHE, "cellpoolidcache");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute(REQUEST_BUFFER, actOfferResObj.toString());
		try {
			reloadCache.doPost(request, response);
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

}

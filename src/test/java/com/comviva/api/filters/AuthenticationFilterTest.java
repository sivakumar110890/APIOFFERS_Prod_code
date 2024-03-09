package com.comviva.api.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.comviva.api.conf.ConfValues;

public class AuthenticationFilterTest {
	
	public final static Logger LOGGER = Logger.getLogger(AuthenticationFilterTest.class);

	private AuthenticationFilter authenticationFilter;
	@Before
	public void setUp() throws Exception {
		authenticationFilter = new AuthenticationFilter();
	}

	@Test
	public void objectToJSONObject_Empty() {
		Object object = "";
		AuthenticationFilter.objectToJSONObject(object);
	}
	
	@Test
	public void objectToJSONObject_EmptyJSON() {
		Object object = "{}";
		AuthenticationFilter.objectToJSONObject(object);
	}
	
	@Test
	public void objectToJSONObject_JSONObject() {
		Object object = "{\"Test\" : \"Test\"}";
		AuthenticationFilter.objectToJSONObject(object);
	}
	
	@Test
	public void objectToJSONObject_JSONArray() {
		Object object = "{\"Test\" : \"{\"Test\" : \"Test\"}\"}";
		AuthenticationFilter.objectToJSONObject(object);
	}
	
	@Test
	public void destroy() {
		authenticationFilter.destroy();
	}
	
	@Test
	public void doFilter_EmptyUser() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ConfValues.setUrl_Authentication(true);
		ConfValues.setPwd("Test");
		ConfValues.setUserid("Test");
		request.setAttribute("REQUEST_BUFFER", "{\"username\":\"\",\"password\":\"\"}");
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
			}
		};
		try {
			authenticationFilter.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void doFilter_NotAutheticated() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ConfValues.setUrl_Authentication(true);
		ConfValues.setPwd("Test");
		ConfValues.setUserid("Test");
		request.setAttribute("REQUEST_BUFFER", "{\"username\":\"Test1\",\"password\":\"Test1\"}");
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
			}
		};
		try {
			authenticationFilter.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void doFilter_Null() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
			}
		};
		try {
			authenticationFilter.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void doFilter_NullBuffer() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
			}
		};
		try {
			authenticationFilter.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			LOGGER.error(e);
		}
	}
	
	@Test
	public void doFilter_Autheticated() {
		ConfValues.setUrl_Authentication(true);
		ConfValues.setPwd("Test");
		ConfValues.setUserid("Test");
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute("REQUEST_BUFFER", "{\"username\":\"Test\",\"password\":\"Test\"}");
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
			}
		};
		try {
			authenticationFilter.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			LOGGER.error(e);
		}
	}

}


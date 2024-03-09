package com.comviva.api.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class ValidationFilterTest {

	public final static Logger LOGGER = Logger.getLogger(ValidationFilterTest.class);
	private ValidationFilter validationFilter;

	@Before
	public void setUp() throws Exception {
		validationFilter = new ValidationFilter();
	}

	@Test
	public void doFilter_ValidContent() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute("REQUEST_BUFFER",
				"{\"username\":\"user\",\"password\":\"user\",\"MSISDN\":\"1234567\",\"RefNum\":\"RefNum\"}");
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response)
					throws IOException, ServletException {
			}
		};
		try {
			validationFilter.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void doFilter_EmptyContent() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute("REQUEST_BUFFER",
				"{\"username\":\"\",\"password\":\"\",\"MSISDN\":\"0\",\"RefNum\":\"\"}");
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response)
					throws IOException, ServletException {
			}
		};
		try {
			validationFilter.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void doFilter_EmptyAtribute() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setAttribute("REQUEST_BUFFER", "");
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response)
					throws IOException, ServletException {
			}
		};
		try {
			validationFilter.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			LOGGER.error(e);
		}
	}

	@Test(expected = NullPointerException.class)
	public void testIsIncluded() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute("REQUEST_BUFFER",
				"{\"username\":\"user\",\"password\":\"user\",\"MSISDN\":\"1234567\",\"RefNum\":\"RefNum\"}");
		HttpServletRequest req = (HttpServletRequest) request;
		validationFilter.isIncluded(req);
	}

}

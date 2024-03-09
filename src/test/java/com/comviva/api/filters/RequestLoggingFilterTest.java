package com.comviva.api.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class RequestLoggingFilterTest {

	public final static Logger LOGGER = Logger.getLogger(RequestLoggingFilterTest.class);
	private RequestLoggingFilter requestLoggingFilter;

	@Before
	public void setUp() throws Exception {
		requestLoggingFilter = new RequestLoggingFilter();
	}

	@Test
	public void doFilter_ContentTypeNull() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setContentType(null);
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response)
					throws IOException, ServletException {
			}
		};
		try {
			requestLoggingFilter.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void doFilter_JSONEmptyContent() {

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setContentType("application/json");
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response)
					throws IOException, ServletException {
			}
		};
		try {
			requestLoggingFilter.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void doFilter_JSONContent() {
		String content = "{}";

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setContentType("application/json");
		request.setContent(content.getBytes());
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response)
					throws IOException, ServletException {
			}
		};
		try {
			requestLoggingFilter.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void doFilter_InvalidXMLContent() {
		String content = "{}";

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setContentType("application/xml");
		request.setContent(content.getBytes());
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response)
					throws IOException, ServletException {
			}
		};
		try {
			requestLoggingFilter.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void doFilter_ValidXMLContent() {
		String content = "<test>Test</test>";
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setContentType("application/xml");
		request.setContent(content.getBytes());
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response)
					throws IOException, ServletException {
			}
		};
		try {
			requestLoggingFilter.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void doFilter_EmptyXMLContent() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setContentType("application/xml");
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response)
					throws IOException, ServletException {
			}
		};
		try {
			requestLoggingFilter.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void doFilter_TextContent() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setContentType("text");
		Map<String, String> params = new HashMap<String, String>();
		params.put("Test", "test");
		params.put("password", "test");
		request.setParameters(params);
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response)
					throws IOException, ServletException {
			}
		};
		try {
			requestLoggingFilter.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			LOGGER.error(e);
		}
	}
	

}

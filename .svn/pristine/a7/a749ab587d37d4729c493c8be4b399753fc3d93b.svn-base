/*
 * COPYRIGHT: Mahindra Comviva Technologies Pvt. Ltd.
 *
 * This software is the sole property of Comviva and is protected
 * by copyright law and international treaty provisions. Unauthorized
 * reproduction or redistribution of this program, or any portion of
 * it may result in severe civil and criminal penalties and will be
 * prosecuted to the maximum extent possible under the law.
 * Comviva reserves all rights not expressly granted. You may not
 * reverse engineer, decompile, or disassemble the software, except
 * and only to the extent that such activity is expressly permitted
 * by applicable law notwithstanding this limitation.
 *
 * THIS SOFTWARE IS PROVIDED TO YOU "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE. YOU ASSUME THE ENTIRE RISK AS TO THE ACCURACY
 * AND THE USE OF THIS SOFTWARE. Comviva SHALL NOT BE LIABLE FOR
 * ANY DAMAGES WHATSOEVER ARISING OUT OF THE USE OF OR INABILITY TO
 * USE THIS SOFTWARE, EVEN IF Comviva HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.comviva.api.filters;

import com.comviva.api.utils.InterfaceAdaptorConstants;
import com.comviva.api.wrapper.ResponseWrapper;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;

/**
 * @author nitin.gupta
 */
@WebFilter(filterName = "/RequestLoggingFilter")
public class RequestLoggingFilter implements Filter {
    public static final Logger LOGGER = Logger.getLogger(RequestLoggingFilter.class);
    public static final Logger cdrLOGGER = Logger.getLogger("cdr");
    private static  String responseType;
    private static String type;
    private ServletContext context;

    @Override
    public void destroy() {
        this.context.log("Request Logging filter destroyed");

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        LOGGER.info("Inside RequestLoggingFilter Filter ");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        ResponseWrapper responseWrapper = new ResponseWrapper(res);

        String contentType = req.getContentType();

        LOGGER.info("ContentType :: " + contentType);

        if (null == contentType) {
            contentType = "application/text";
        }

        LOGGER.info("Final Content Type is " + contentType);
        /* Block to read JSON input */
        if (contentType.equals("application/json")) {
            readJSON(req);
            res.setContentType("application/json");
            setResponseType(InterfaceAdaptorConstants.JSON);
        } /* to read XML input */ else if (contentType.equals("application/xml") || contentType.equals("text/xml")) {
            try {
                readXML(req);
            } catch (ParserConfigurationException e) {
                LOGGER.error("Exception " +e);
            }
            res.setContentType("application/xml");
            setResponseType(InterfaceAdaptorConstants.XML);
        } /* to read key/value input */ else {
            setResponseType(InterfaceAdaptorConstants.XML);
            res.setContentType("application/xml");
            Enumeration<String> params = req.getParameterNames();
            while (params.hasMoreElements()) {
                String name = params.nextElement();
                String value = request.getParameter(name);
                if (!name.equals("password")) {
                    LOGGER.debug(req.getRemoteAddr() + "::Request Params::{" + name + "=" + value + "}");
                }
            }
        }
        LOGGER.info("Response Type :: " + getResponseType());
        LOGGER.info("Post  :: " + req.getAttribute("REQUEST_BUFFER"));

        chain.doFilter(req, responseWrapper);
        responseWrapper.flushBuffer();

    }

    private void readXML(HttpServletRequest req) throws ParserConfigurationException {
        StringBuilder requestBuffer = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                requestBuffer.append(line);
        } catch (Exception e) {
            LOGGER.error("Error while reading input buffer", e);

        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); 
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new InputSource(new StringReader(requestBuffer.toString())));

            if (null != document) {
                req.setAttribute("REQUEST_BUFFER", document);
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOGGER.error("Error parsing XML request string", e);
        }
    }

    private void readJSON(HttpServletRequest req) {
        String strPostData = "";
        try {
            ServletInputStream inputStream = req.getInputStream();
            strPostData = new String(IOUtils.toByteArray(inputStream));
            JSONObject jsonObject = new JSONObject(strPostData);
            LOGGER.info("Postdata JSON object - " + jsonObject);
            req.setAttribute("REQUEST_BUFFER", jsonObject);
        } catch (Exception e) {
            LOGGER.error("Error while reading input buffer", e);
        }
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        this.context = fConfig.getServletContext();
        this.context.log("Request Logging filter initialized");
    }

    public static String getResponseType() {
        return responseType;
    }

    public static void setResponseType(String responseType) {
        RequestLoggingFilter.responseType = responseType;
    }

}

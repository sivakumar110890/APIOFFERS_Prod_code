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
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * @author nitin.gupta
 */

/**
 * This filter is used to validate the mandatory input parameter for all the
 * request
 */

@WebFilter(filterName = "/ValidationFilter", initParams = { @WebInitParam(name = "includeValidation", value = "") })
public class ValidationFilter implements Filter {
    public static final Logger LOGGER = Logger.getLogger(ValidationFilter.class);
    private static Set<String> included;
    private ServletContext context;

    @Override
    public void destroy() {
        //not required to implement
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info("Inside Validation Filter ");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        StringBuilder sb = new StringBuilder();

        if (validateInputParam(req)) {
            LOGGER.info("Validation is true going to call the API...");
            chain.doFilter(req, res);
        } else {
            PrintWriter out = res.getWriter();
            sb.append("{");
            sb.append("\"StatusCode\": 1,");
            sb.append("\"StatusMessage\": \"FAIL\"");
            sb.append("}");
            out.println(sb.toString());
        }

    }

    private boolean validateInputParam(HttpServletRequest req) {
        boolean isValidationTrue = true;
        try {
            JSONObject postDataObj = AuthenticationFilter.objectToJSONObject(req.getAttribute("REQUEST_BUFFER"));
            Long msisdn = postDataObj.getLong(InterfaceAdaptorConstants.MSISDN);
            String username = postDataObj.getString(InterfaceAdaptorConstants.Username);
            String password = postDataObj.getString("password");
            String refNum = postDataObj.getString(InterfaceAdaptorConstants.refNum);

            if (null == msisdn || msisdn == 0) {
                isValidationTrue = false;
            }
            if (null == username || username.isEmpty()) {
                isValidationTrue = false;
            }
            if (null == password || password.isEmpty()) {
                isValidationTrue = false;
            }
            if (null == refNum || refNum.isEmpty()) {
                isValidationTrue = false;
            }
        } catch (Exception e) {
            LOGGER.error("Error while validating input parameters", e);

        }
        LOGGER.info("Validation result - " + isValidationTrue);
        return isValidationTrue;
    }

    boolean isIncluded(HttpServletRequest request) {
        String path = request.getServletPath();
        LOGGER.info("path " + path);

        String requestAPI = null;
        if (path.length() > 1) {
            requestAPI = path.substring(path.indexOf('/') + 1, path.lastIndexOf('/')).toLowerCase();
        }
        LOGGER.debug("requestAPI:: " + requestAPI);

        return included.contains(requestAPI);
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        this.context = fConfig.getServletContext();
        this.context.log("Validation Filter initialized");
        LOGGER.debug("included validation for :: " + included);

    }

}

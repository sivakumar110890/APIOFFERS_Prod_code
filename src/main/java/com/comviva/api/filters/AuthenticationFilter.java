/**
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


import com.comviva.api.conf.ConfValues;
import com.comviva.api.utils.InterfaceAdaptorConstants;


import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author nitin.gupta
 *
 * AuthenticationFilter class implements Filter interface
 */
@WebFilter(filterName = "/AuthenticationFilter")
public class AuthenticationFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class);
    private ServletContext context;

    /**
     * @param object
     *
     * function convertes any Object to JSONObject
     *
     * @return JSONObject
    */
    public static JSONObject objectToJSONObject(Object object) {
        Object json = null;
        JSONObject jsonObject = new JSONObject();
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            LOGGER.error("Error while converting object to JSONObject", e);
        }
        if (json instanceof JSONObject) {
            jsonObject = (JSONObject) json;
        } else {
            LOGGER.warn(" Object is not converted to JSON");
        }
        return jsonObject;
    }

    @Override
    public void destroy() {
        //Not implemented
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        LOGGER.info("Inside Authentication Filter , URLS Authentication is :: " + ConfValues.isUrl_Authentication());

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        StringBuilder sb = new StringBuilder();
        LOGGER.info("Configured password - " + ConfValues.getPwd());
        LOGGER.info("Configured username - " + ConfValues.getUserid());

        try {
            String username = "";
            String password = "";
            if (req.getAttribute("REQUEST_BUFFER") != null) {
                if (ConfValues.isUrl_Authentication()) {
                    JSONObject postDataObj = objectToJSONObject(req.getAttribute("REQUEST_BUFFER"));
                    username = postDataObj.getString(InterfaceAdaptorConstants.Username);
                    password = postDataObj.getString("password");

                    if (null == username || null == password || username.isEmpty() || password.isEmpty()) {
                        PrintWriter out = res.getWriter();
                        LOGGER.info("Username or password is empty");
                        sb.append("{");
                        sb.append("\"StatusCode\": 1,");
                        sb.append("\"StatusMessage\": \"FAIL\"");
                        sb.append("}");
                        out.println(sb.toString());
                        return;
                    }

                    else if (!password.equals(ConfValues.getPwd()) || !username.equals(ConfValues.getUserid())) {
                        PrintWriter out = res.getWriter();
                        LOGGER.info("Username or password is invalid");
                        sb.append("{");
                        sb.append("\"StatusCode\": 1,");
                        sb.append("\"StatusMessage\": \"FAIL\"");
                        sb.append("}");
                        out.println(sb.toString());
                        return;
                    } else {
                        LOGGER.info("::Authentication success::");
                        chain.doFilter(req, res);
                    }
                } else {
                    chain.doFilter(req, res);
                }
            } else {
                PrintWriter out = res.getWriter();
                LOGGER.info("Request is invalid");
                sb.append("{");
                sb.append("\"StatusCode\": 1,");
                sb.append("\"StatusMessage\": \"FAIL\"");
                sb.append("}");
                out.println(sb.toString());
                return;
            }
        } catch (JSONException e) {
            LOGGER.error("Error while accessing values from JSONObject", e);
        }
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        this.context = fConfig.getServletContext();
        this.context.log("AuthenticationFilter initialized");
    }

}

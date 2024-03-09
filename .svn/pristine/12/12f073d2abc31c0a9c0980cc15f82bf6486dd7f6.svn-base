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
package com.comviva.api.utils;

import com.comviva.api.filters.RequestLoggingFilter;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.PrintWriter;

/**
 * @author nitin.gupta
 */
public class ApiUtilsImpl implements ApiUtils {

    public final static Logger LOGGER = Logger.getLogger(ApiUtilsImpl.class);
    
    private RequestLoggingFilter requestloggingfilter;

    @Override
    public void returnResponse(PrintWriter out, int errorCode, String errorMsg) {

        if (requestloggingfilter.getResponseType().equalsIgnoreCase(InterfaceAdaptorConstants.TEXT)) {
            out.println(errorMsg);
        } else if (RequestLoggingFilter.getResponseType().equalsIgnoreCase(InterfaceAdaptorConstants.XML) || RequestLoggingFilter.getResponseType().equalsIgnoreCase(InterfaceAdaptorConstants.JSON)) {
            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

            sb.append(XMLTagConstants.RESPONSE_START_TAG);
            sb.append(XMLTagConstants.ERROR_MSG_START_TAG + errorMsg + XMLTagConstants.ERROR_MSG_END_TAG + XMLTagConstants.NEWLINE);
            sb.append(XMLTagConstants.ERROR_CODE_START_TAG + errorCode + XMLTagConstants.ERROR_CODE_END_TAG + XMLTagConstants.NEWLINE);
            sb.append(XMLTagConstants.RESPONSE_END_TAG);

            if (RequestLoggingFilter.getResponseType().equalsIgnoreCase(InterfaceAdaptorConstants.JSON)) {
                try {
                    JSONObject json = XML.toJSONObject(sb.toString());
                    out.println(json.toString());

                    LOGGER.info("Response in json ::" + json.toString());
                } catch (JSONException je) {
                    LOGGER.info("Wrong Operation Type::" + je);
                }

            } else {
                out.println(sb.toString());
            }
        }

    }

}

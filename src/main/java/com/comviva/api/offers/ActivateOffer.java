package com.comviva.api.offers;

import com.comviva.api.async.AppAsyncListener;
import com.comviva.api.filters.AuthenticationFilter;
import com.comviva.api.j4u.config.PropertiesLoader;
import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.ApiLog;
import com.comviva.api.j4u.service.J4UOfferService;
import com.comviva.api.j4u.utils.Utils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.comviva.api.j4u.utils.J4UOfferConstants.ACTIVATE_OFFER;
import static com.comviva.api.j4u.utils.J4UOfferConstants.API_ACT_OFFER_FINAL_RESP_SENT;
import static com.comviva.api.j4u.utils.J4UOfferConstants.API_ACT_OFFER_REQ_RECEIVED;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CELL_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CHANNEL;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MPESA_ASYNC_TIMEOUT;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.POOL_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_NUM;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_TRANSACTION_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REQUEST_BUFFER;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_MESSAGE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.THIRD_PARTY_REF;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TRANSACTION_ID;
import static com.comviva.api.utils.InterfaceAdaptorConstants.refNum;

/**
 * @author Yeswanth.reddy
 */

@WebServlet(urlPatterns = { "/J4U/activateOffer" })
public class ActivateOffer extends HttpServlet {
    public final static Logger LOGGER = Logger.getLogger(ActivateOffer.class);
    private static final long serialVersionUID = 1L;
    private UpdaterDAO updaterDAO = null;

    public ActivateOffer() {
        try {
            updaterDAO = new UpdaterDAO();
        } catch (Exception e) {
      
            LOGGER.debug("Initialization error:: " + e.getMessage());
        }
    }

    protected void goGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
        response.setContentType("application/json");
        request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);

        J4UOfferService service = new J4UOfferService();

        try {
            JSONObject postDataObj = AuthenticationFilter.objectToJSONObject(request.getAttribute(REQUEST_BUFFER));

            // need to get from system
            postDataObj.put(TRANSACTION_ID, postDataObj.getString(REF_TRANSACTION_ID));
            LOGGER.info("ActivteOffer request :: " + postDataObj);
            insertApiLog(postDataObj, ACTIVATE_OFFER, API_ACT_OFFER_REQ_RECEIVED, postDataObj.get(refNum).toString(), postDataObj.getString(OFFER_ID));

            postDataObj.put(MSISDN, postDataObj.getLong(MSISDN) + "");

            AsyncContext asyncCtx = request.startAsync();
            asyncCtx.setTimeout(PropertiesLoader.getIntValue(MPESA_ASYNC_TIMEOUT));
            asyncCtx.addListener(new AppAsyncListener());
            AsyncMap asyncMap = AsyncMap.instance();
            asyncMap.putActOffer(postDataObj.getString(refNum), asyncCtx);
            service.activateOffer(postDataObj);
        } catch (Exception e) {
            LOGGER.error("Exception while activating offer request : ", e);
        }
    }

    public void actOfferResponseAsync(JSONObject actOfferResObj) {
        LOGGER.info("ActivateOfferResponse  :: " + actOfferResObj);
        try {
            AsyncMap asyncMap = AsyncMap.instance();
            AsyncContext asyncCtx = asyncMap.getActOffer(actOfferResObj.getString(refNum));
            insertApiLog(actOfferResObj, ACTIVATE_OFFER, API_ACT_OFFER_FINAL_RESP_SENT, actOfferResObj.getString(STATUS_MESSAGE), null);
            PrintWriter out = asyncCtx.getResponse().getWriter();
            asyncMap.removeActOffer(actOfferResObj.getString(refNum));
            out.println(actOfferResObj.toString());
            asyncCtx.complete();
        } catch (Exception e) {
            LOGGER.error("Exception while activating offer response - ", e);
        }
    }

    private void insertApiLog(JSONObject jsonObject, String apiType, String status, String comments, String offerId) {
        String thirdPartyRef = "";
        String refNum = "";
        String channel = "";
        if (jsonObject.has(CHANNEL)) {
            channel = jsonObject.get(CHANNEL).toString();
        }
        if (jsonObject.has(THIRD_PARTY_REF)) {
            thirdPartyRef = jsonObject.get(THIRD_PARTY_REF).toString();
        }
        if (jsonObject.has(REF_NUM)) {
            refNum = jsonObject.get(REF_NUM).toString();
        }
        try {
            ApiLog apiLog = new ApiLog();
            apiLog.setMsisdn(jsonObject.get(MSISDN).toString());
            apiLog.setApiType(apiType);
            apiLog.setComments(comments);
            apiLog.setDateTime(Utils.getCurrentTimeStamp());
            apiLog.setProdIds(offerId);
            apiLog.setSelectedProdId(offerId);
            apiLog.setRefNum(refNum);
            apiLog.setStatus(status);
            apiLog.setProdType("");
            apiLog.setThirdPartyRef(thirdPartyRef);
            apiLog.setTransactionId(jsonObject.getString(TRANSACTION_ID));
            apiLog.setChannel(channel);
            apiLog.setCellId(jsonObject.has(CELL_ID) ? jsonObject.getString(CELL_ID) : "");
            apiLog.setPoolId(jsonObject.has(POOL_ID) ? jsonObject.getString(POOL_ID) : "");
            updaterDAO.insertApiLog(apiLog);

        } catch (Exception e) {
            LOGGER.error("Exception while inserting api log :: " + e.getMessage());
        }
    }

}

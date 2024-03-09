package com.comviva.api.offers;

import static com.comviva.api.j4u.utils.J4UOfferConstants.CHANNEL;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_NUM;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REQUEST_BUFFER;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_CODE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TRANSACTION_ID;
import static com.comviva.api.utils.InterfaceAdaptorConstants.refNum;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.comviva.api.filters.AuthenticationFilter;
import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.ApiLog;
import com.comviva.api.j4u.service.SAGOfferService;
import com.comviva.api.j4u.utils.J4UOfferConstants;
import com.comviva.api.j4u.utils.J4UOfferStatusCode;
import com.comviva.api.j4u.utils.Utils;

@WebServlet(urlPatterns = { "/J4U/sagOptout" }, asyncSupported = true)
public class SAGOptOut extends HttpServlet {
	public static final Logger LOGGER = Logger.getLogger(PEDPlayCount.class);
	private static final long serialVersionUID = 1L;
	private PrintWriter out;
	private UpdaterDAO updaterDAO = null;
	private SAGOfferService sagOfferService;

	public SAGOptOut() {
		try {
			updaterDAO = new UpdaterDAO();
			sagOfferService = new SAGOfferService();
		} catch (Exception e) {
			LOGGER.debug("Initialization error:: ", e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			doPost(request, response);
		} catch (Exception e) {
			LOGGER.debug("SAG OPT OUT  error:: ", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
		try {
			JSONObject postDataObj = AuthenticationFilter.objectToJSONObject(request.getAttribute(REQUEST_BUFFER));
			String txId = new Utils().getTransactionID();
			postDataObj.put(TRANSACTION_ID, txId);
			postDataObj.put(MSISDN, postDataObj.get(MSISDN).toString());
			LOGGER.info("SAGOptOUT Request received => " + postDataObj);

			insertApiLog(postDataObj, J4UOfferConstants.SAG_OPT_OUT, J4UOfferConstants.API_OFFER_REQ_RECEIVED,
					postDataObj.get(refNum).toString());
			JSONObject synchOffersObj = null;
			synchOffersObj = sagOfferService.getSagOptOut(postDataObj);
			LOGGER.info("synchOffers Response => " + synchOffersObj);
			if (synchOffersObj != null) {
				Integer statusCode = synchOffersObj.getInt(STATUS_CODE);
				if (statusCode == J4UOfferStatusCode.SUCCESS.getStatusCode()) {
					out = response.getWriter();
					out.println(synchOffersObj.toString());

				} else {
					// other
					String comments = statusCode + " :: " + J4UOfferStatusCode.getStatusMap(statusCode);
					// insertApiLog(postDataObj, J4UOfferConstants.SAG_OPT_OUT,
					// J4UOfferConstants.API_SAG_OPT_OUT_REQ_FAILED, comments);
					LOGGER.error("Error response  :: " + postDataObj + " ==> " + comments);
					out = response.getWriter();
					out.println(synchOffersObj.toString());
				}
			}

		} catch (

		Exception e) {
			LOGGER.error("Exception while getting offer details", e);
		}
	}

	private void insertApiLog(JSONObject jsonObject, String apiType, String status, String comments) throws Exception {
		String thirdPartyRef = "";
		String refNum = "";
		String channel = "";
		if (jsonObject.has(CHANNEL)) {
			channel = jsonObject.get(CHANNEL).toString();
		}
		if (jsonObject.has(REF_NUM)) {
			refNum = jsonObject.get(REF_NUM).toString();
		}
		ApiLog apiLog = new ApiLog();
		apiLog.setMsisdn(jsonObject.get(MSISDN).toString());
		apiLog.setApiType(apiType);
		apiLog.setComments(comments);
		apiLog.setDateTime(Utils.getCurrentTimeStamp());
		apiLog.setRefNum(refNum);
		apiLog.setStatus(status);
		apiLog.setThirdPartyRef(thirdPartyRef);
		apiLog.setTransactionId(jsonObject.getString(TRANSACTION_ID));
		apiLog.setChannel(channel);
		LOGGER.debug("APilog....." + apiLog);
		updaterDAO.insertApiLog(apiLog);
	}

}

package com.comviva.api.offers;

import com.comviva.api.filters.AuthenticationFilter;
import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.ApiLog;
import com.comviva.api.j4u.utils.Utils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.comviva.api.j4u.utils.J4UOfferConstants.CELL_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CHANNEL;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.POOL_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_NUM;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_TRANSACTION_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REQUEST_BUFFER;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_CODE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_MESSAGE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.THIRD_PARTY_REF;

@WebServlet(urlPatterns = { "/J4U/statusUpdate" })
public class StatusUpdate extends HttpServlet {
	public final static Logger LOGGER = Logger.getLogger(ActivateOffer.class);
	private UpdaterDAO updaterDAO = null;

	public StatusUpdate() {
		try {
			updaterDAO = new UpdaterDAO();
		} catch (Exception e) {

			LOGGER.debug("Initialization error:: " + e.getMessage());
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONObject requestJson = AuthenticationFilter.objectToJSONObject(request.getAttribute(REQUEST_BUFFER));
		JSONObject responseJson = new JSONObject();
		response.setContentType("application/json");
		try {
			LOGGER.debug("request received for status update: " + requestJson);
			String comments = requestJson.getInt(STATUS_CODE) + ": " + requestJson.getString(STATUS_MESSAGE);
			String offerId = requestJson.getString(OFFER_ID);
			this.insertApiLog(requestJson, "STATUS_UPDATE", "OFFER_FAILURE", comments, offerId);
			responseJson.put(MSISDN, requestJson.getLong(MSISDN));
			responseJson.put(STATUS_CODE, 0);
			out.println(responseJson.toString());
		} catch (Exception e) {
			responseJson.put(MSISDN, requestJson.getLong(MSISDN));
			responseJson.put(STATUS_CODE, 90);
			out.println(responseJson.toString());
			LOGGER.error("Status update error:: ", e);
		}

	}

	private void insertApiLog(JSONObject jsonObject, String apiType, String status, String comments, String offerId)
			throws Exception {
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
		apiLog.setTransactionId(jsonObject.has(REF_TRANSACTION_ID) ? jsonObject.getString(REF_TRANSACTION_ID) : "");
		apiLog.setChannel(channel);
		apiLog.setCellId(jsonObject.has(CELL_ID) ? jsonObject.getString(CELL_ID) : "");
		apiLog.setPoolId(jsonObject.has(POOL_ID) ? jsonObject.getString(POOL_ID) : "");
		updaterDAO.insertApiLog(apiLog);

	}
}

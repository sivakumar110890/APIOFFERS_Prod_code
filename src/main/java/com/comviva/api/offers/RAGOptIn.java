package com.comviva.api.offers;

import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;

import static com.comviva.api.j4u.utils.J4UOfferConstants.REQUEST_BUFFER;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_CODE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TRANSACTION_ID;
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
import com.comviva.api.j4u.config.PropertiesLoader;
import com.comviva.api.j4u.service.RAGOfferService;
import com.comviva.api.j4u.utils.J4UOfferConstants;
import com.comviva.api.j4u.utils.J4UOfferStatusCode;
import com.comviva.api.j4u.utils.Utils;
import com.comviva.api.utils.InterfaceAdaptorConstants;

@WebServlet(urlPatterns = { "/J4U/ragOptin" })
public class RAGOptIn extends HttpServlet {
	public static final Logger LOGGER = Logger.getLogger(RAGOptIn.class);
	private static final long serialVersionUID = 1L;
	private static final String RAG_OPT_IN_REQ_RECIEVED = "RAG_OPT_IN_REQ_RECIEVED";
	private static final String RAG_OPT_IN = "RAG_OPT_IN";
	private static final String RAG_OPT_IN_RESP_SENT = "RAG_OPT_IN_RESP_SENT";
	private static final String RAG_OPT_IN_REQ_RECEIVED = "RAG_OPT_IN_REQ_RECEIVED";
	private PrintWriter out;
	/* private UpdaterDAO updaterDAO = null; */
	private RAGOfferService ragOfferService;

	public RAGOptIn() {
		try {
			// updaterDAO = new UpdaterDAO();
			ragOfferService = new RAGOfferService();
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
			LOGGER.debug("RAG OPT IN  error:: ", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		try {
			JSONObject postDataObj = AuthenticationFilter.objectToJSONObject(request.getAttribute(REQUEST_BUFFER));
			String txId = new Utils().getTransactionID();
			postDataObj.put(TRANSACTION_ID, txId);
			String msisdn = postDataObj.get(MSISDN).toString();
			if (msisdn != null && msisdn.length() == PropertiesLoader.getIntValue(J4UOfferConstants.SUBID_LENGTH)) {
				msisdn = (msisdn.replaceFirst(PropertiesLoader.getValue(J4UOfferConstants.SUBID_CNTRYCD), ""));
				postDataObj.put(MSISDN, msisdn);
			}

			LOGGER.info("RAGOptIn Request received => " + postDataObj);
			Utils.insertApiLog(postDataObj, J4UOfferConstants.RAG_OPT_IN, RAG_OPT_IN_REQ_RECEIVED,
					postDataObj.get(InterfaceAdaptorConstants.refNum).toString(), "RAG_OPT_IN");
			JSONObject synchOffersObj = null;
			synchOffersObj = ragOfferService.getRagOptIn(postDataObj);
			LOGGER.info("synchOffers Response => " + synchOffersObj);
			if (synchOffersObj != null) {
				Integer statusCode = synchOffersObj.getInt(STATUS_CODE);

				if (statusCode == J4UOfferStatusCode.SUCCESS.getStatusCode()) {

					Utils.insertApiLog(synchOffersObj, RAG_OPT_IN, RAG_OPT_IN_RESP_SENT,
							J4UOfferStatusCode.SUCCESS.getStatusMessage(), "RAG_OPT_IN");

					out = response.getWriter();
					out.println(synchOffersObj.toString());
				} else {
					// other
					String comments = statusCode + " :: " + J4UOfferStatusCode.getStatusMap(statusCode);

					Utils.insertApiLog(postDataObj, RAG_OPT_IN, RAG_OPT_IN_REQ_RECIEVED, comments, "RAG_OPT_IN");

					LOGGER.info("RAG OPY IN response  :: " + postDataObj + " ==> " + comments);
					out = response.getWriter();
					out.println(synchOffersObj.toString());
				}
			}

		} catch (

		Exception e) {
			LOGGER.error("Exception while getting offer details", e);
		}
	}

	/*
	 * private void insertApiLog(JSONObject jsonObject, String apiType, String
	 * status, String comments) throws Exception { String thirdPartyRef = "";
	 * String refNum = ""; String channel = ""; if (jsonObject.has(CHANNEL)) {
	 * channel = jsonObject.get(CHANNEL).toString(); } if
	 * (jsonObject.has(REF_NUM)) { refNum = jsonObject.get(REF_NUM).toString();
	 * } ApiLog apiLog = new ApiLog();
	 * apiLog.setMsisdn(jsonObject.get(MSISDN).toString());
	 * apiLog.setApiType(apiType); apiLog.setComments(comments);
	 * apiLog.setDateTime(Utils.getCurrentTimeStamp());
	 * apiLog.setRefNum(refNum); apiLog.setStatus(status);
	 * apiLog.setThirdPartyRef(thirdPartyRef);
	 * apiLog.setTransactionId(jsonObject.getString(TRANSACTION_ID));
	 * apiLog.setChannel(channel); LOGGER.debug("APilog....." + apiLog);
	 * updaterDAO.insertApiLog(apiLog); }
	 */
}

package com.comviva.api.offers;

import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
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
import com.comviva.api.j4u.service.PEDProcessService;
import com.comviva.api.j4u.utils.J4UOfferStatusCode;
import com.comviva.api.j4u.utils.Utils;

@WebServlet(urlPatterns = { "/J4U/getPlayHistory" })
public class PEDPlayHistory extends HttpServlet {
	public static final Logger LOGGER = Logger.getLogger(PEDPlayHistory.class);
	private static final long serialVersionUID = 1L;
	private static final String PED_PLAY_HISTORY_REQ_FAILED = "PED_PLAY_HISTORY_REQ_FAILED";
	private static final String PED_PLAY_HISTORY = "PED_PLAY_HISTORY";
	private static final String PED_PLAY_HISTORY_RESP_SENT = "PED_PLAY_HISTORY_RESP_SENT";
	private static final String PED_PLAYHISTORY_REQ_RECIEVED = "PED_PLAYHISTORY_REQ_RECIEVED";
	private PrintWriter out;
	private PEDProcessService pedprocessService;

	public PEDPlayHistory() {
		try {
			pedprocessService = new PEDProcessService();
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
			LOGGER.debug("GetOffer  error:: ", e);
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
			LOGGER.info("PlayEveryday Request received => " + postDataObj);
			Utils.insertApiLog(postDataObj, PED_PLAY_HISTORY, PED_PLAYHISTORY_REQ_RECIEVED,
					postDataObj.get(refNum).toString(), "PED_PLAY_HISTORY");
			JSONObject synchOffersObj = null;
			synchOffersObj = pedprocessService.getPlayHistory(postDataObj);
			LOGGER.info("synchOffers Response => " + synchOffersObj);

			if (synchOffersObj != null) {
				Integer statusCode = synchOffersObj.getInt(STATUS_CODE);

				if (statusCode == J4UOfferStatusCode.SUCCESS.getStatusCode()) {
					Utils.insertApiLog(synchOffersObj, PED_PLAY_HISTORY, PED_PLAY_HISTORY_RESP_SENT,
							J4UOfferStatusCode.SUCCESS.getStatusMessage(), "PED_PLAY_HISTORY");
					out = response.getWriter();
					out.println(synchOffersObj.toString());
				} else {
					// other
					String comments = statusCode + " :: " + J4UOfferStatusCode.getStatusMap(statusCode);
					Utils.insertApiLog(postDataObj, PED_PLAY_HISTORY, PED_PLAY_HISTORY_REQ_FAILED, comments,
							"PED_PLAY_HISTORY");
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

}

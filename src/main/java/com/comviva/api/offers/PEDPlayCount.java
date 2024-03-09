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
import com.comviva.api.j4u.utils.J4UOfferConstants;
import com.comviva.api.j4u.utils.J4UOfferStatusCode;
import com.comviva.api.j4u.utils.Utils;

@WebServlet(urlPatterns = { "/J4U/getAvailablePlays" })
public class PEDPlayCount extends HttpServlet {
	public static final Logger LOGGER = Logger.getLogger(PEDPlayCount.class);
	private static final long serialVersionUID = 1L;
	private static final String PED_PLAYCOUNT_REQ_RECIVED = "PED_PLAYCOUNT_REQ_RECIVED";
	private static final String PED_GET_PLAYCOUNT = "PED_GET_PLAYCOUNT";
	private static final String PED_PLAYCOUNT_RESP_SENT = "PED_PLAYCOUNT_RESP_SENT";
	private static final String PED_PLAYCOUNT_REQ_FAILED = "PED_PLAYCOUNT_REQ_FAILED";
	private PrintWriter out;
	private PEDProcessService pedprocessService;

	public PEDPlayCount() {
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
		try {
			JSONObject postDataObj = AuthenticationFilter.objectToJSONObject(request.getAttribute(REQUEST_BUFFER));
			String txId = new Utils().getTransactionID();
			postDataObj.put(TRANSACTION_ID, txId);
			postDataObj.put(MSISDN, postDataObj.get(MSISDN).toString());
			LOGGER.info("PlayEveryday Request received => " + postDataObj);
			Utils.insertApiLog(postDataObj, PED_GET_PLAYCOUNT, PED_PLAYCOUNT_REQ_RECIVED,
					postDataObj.get(refNum).toString(), "PLAY_COUNT");
			JSONObject availablePlayObje = null;
			availablePlayObje = pedprocessService.getAvailablePlays(postDataObj);
			LOGGER.info("synchOffers Response => " + availablePlayObje);
			if (availablePlayObje != null) {
				Integer statusCode = availablePlayObje.getInt(STATUS_CODE);

				if (statusCode == J4UOfferStatusCode.SUCCESS.getStatusCode()) {
					Utils.insertApiLog(availablePlayObje, J4UOfferConstants.AVILABLE_PLAY_COUNT, PED_PLAYCOUNT_RESP_SENT,
							J4UOfferStatusCode.SUCCESS.getStatusMessage(), "PLAY_COUNT");
					out = response.getWriter();
					out.println(availablePlayObje.toString());
				} else {
					// other
					String comments = statusCode + " :: " + J4UOfferStatusCode.getStatusMap(statusCode);
					Utils.insertApiLog(postDataObj, PED_GET_PLAYCOUNT, PED_PLAYCOUNT_REQ_FAILED, comments,
							"PLAY_COUNT");
					LOGGER.error("Error response  :: " + postDataObj + " ==> " + comments);
					out = response.getWriter();
					out.println(availablePlayObje.toString());
				}
			}

		} catch (Exception e) {
			LOGGER.error("Exception while getting play count  details", e);
		}
	}

}

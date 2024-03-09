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
import com.comviva.api.j4u.service.RAGOfferService;
import com.comviva.api.j4u.utils.J4UOfferConstants;
import com.comviva.api.j4u.utils.J4UOfferStatusCode;
import com.comviva.api.j4u.utils.Utils;

@WebServlet(urlPatterns = { "/J4U/ragOptout" })
public class RAGOptOut extends HttpServlet {
	public static final Logger LOGGER = Logger.getLogger(PEDPlayCount.class);
	private static final long serialVersionUID = 1L;
	private PrintWriter out;

	private RAGOfferService ragOfferService;

	public RAGOptOut() {
		try {
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
			LOGGER.info("RAGOptOut Request received => " + postDataObj);
			Utils.insertApiLog(postDataObj, J4UOfferConstants.RAG_OPT_OUT, J4UOfferConstants.RAG_OPT_OUT_REQ_RECEIVED,
					postDataObj.get(refNum).toString(), "RAG_OPT_OUT");
			JSONObject synchOffersObj = null;
			synchOffersObj = ragOfferService.getRagOptOut(postDataObj);
			LOGGER.info("synchOffers Response => " + synchOffersObj);
			if (synchOffersObj != null) {

				Integer statusCode = synchOffersObj.getInt(STATUS_CODE);
				if (statusCode == J4UOfferStatusCode.SUCCESS.getStatusCode()) {
					Utils.insertApiLog(postDataObj, J4UOfferConstants.RAG_OPT_OUT,
							J4UOfferConstants.RAG_OPT_OUT_RESP_SENT, postDataObj.get(refNum).toString(), "RAG_OPT_OUT");
					out = response.getWriter();
					out.println(synchOffersObj.toString());

				} else {
					// other
					String comments = statusCode + " :: " + J4UOfferStatusCode.getStatusMap(statusCode);
					LOGGER.error("Error response  :: " + postDataObj + " ==> " + comments);
					Utils.insertApiLog(postDataObj, J4UOfferConstants.RAG_OPT_OUT,
							J4UOfferConstants.RAG_OPT_OUT_REQ_FAILED, comments, "RAG_OPT_OUT");
					out = response.getWriter();
					out.println(synchOffersObj.toString());
				}
			}

		} catch (Exception e) {
			LOGGER.error("Exception while getting offer details", e);
		}
	}

}

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
import com.comviva.api.j4u.service.SAGOfferService;
import com.comviva.api.j4u.utils.J4UOfferConstants;
import com.comviva.api.j4u.utils.J4UOfferStatusCode;
import com.comviva.api.j4u.utils.Utils;

@WebServlet(urlPatterns = { "/J4U/getSAGOfferInfo" })
public class SAGGetOffer extends HttpServlet {
	public static final Logger LOGGER = Logger.getLogger(PEDPlayCount.class);
	private static final long serialVersionUID = 1L;
	private PrintWriter out;
	private SAGOfferService sagOfferService;

	public SAGGetOffer() {
		try {
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
			LOGGER.info("SAGOptIn Request received => " + postDataObj);
			Utils.insertApiLog(postDataObj, J4UOfferConstants.SAG_OPT_IN, J4UOfferConstants.SAG_OFFER_REQ_RECEIVED,
					postDataObj.get(refNum).toString(), "SAG_GET_OFFER");
			JSONObject synchOffersObj = null;
			synchOffersObj = sagOfferService.SagGetOfferInfo(postDataObj);
			LOGGER.info("synchOffers Response => " + synchOffersObj);
			if (synchOffersObj != null) {
				Integer statusCode = synchOffersObj.getInt(STATUS_CODE);
				if (statusCode == J4UOfferStatusCode.SUCCESS.getStatusCode()) {
					Utils.insertApiLog(synchOffersObj, J4UOfferConstants.SAG_GET_OFFER,
							J4UOfferConstants.SAG_GET_OFFER_REQ_SENT, J4UOfferStatusCode.SUCCESS.getStatusMessage(),
							"SAG_GET_OFFER");
					out = response.getWriter();
					out.println(synchOffersObj.toString());
				} else {
					// other
					String comments = statusCode + " :: " + J4UOfferStatusCode.getStatusMap(statusCode);
					Utils.insertApiLog(postDataObj, J4UOfferConstants.SAG_GET_OFFER,
							J4UOfferConstants.SAG_GET_OFFER_REQ_FAILED, comments, "SAG_GET_OFFER");
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

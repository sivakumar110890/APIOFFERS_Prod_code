package com.comviva.api.offers;

import static com.comviva.api.j4u.utils.J4UOfferConstants.API_FINAL_OFFERS_SENT;
import static com.comviva.api.j4u.utils.J4UOfferConstants.API_FINAL_OFFERS_SENT_BLACKLIST;
import static com.comviva.api.j4u.utils.J4UOfferConstants.API_OFFER_REQ_FAILED;
import static com.comviva.api.j4u.utils.J4UOfferConstants.API_OFFER_REQ_RECEIVED;
import static com.comviva.api.j4u.utils.J4UOfferConstants.BLACKLISTED_COMMENTS;
import static com.comviva.api.j4u.utils.J4UOfferConstants.BLACKLIST_ENABLED;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CATEGORY;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CELL_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CHANNEL;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CHANNEL_EVC;
import static com.comviva.api.j4u.utils.J4UOfferConstants.GET_OFFER;
import static com.comviva.api.j4u.utils.J4UOfferConstants.LANGUAGE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MPESA_ASYNC_TIMEOUT;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFERS;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_DESC;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.POOL_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_NUM;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REQUEST_BUFFER;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_CODE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_MESSAGE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.THIRD_PARTY_REF;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TRANSACTION_ID;
import static com.comviva.api.utils.InterfaceAdaptorConstants.refNum;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.BiPredicate;
import java.util.function.Function;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.comviva.api.async.AppAsyncListener;
import com.comviva.api.filters.AuthenticationFilter;
import com.comviva.api.j4u.config.PropertiesLoader;
import com.comviva.api.j4u.dao.LookUpDAO;
import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.ApiLog;
import com.comviva.api.j4u.service.J4UOfferService;
import com.comviva.api.j4u.utils.J4UOfferConstants;
import com.comviva.api.j4u.utils.J4UOfferStatusCode;
import com.comviva.api.j4u.utils.Utils;

/**
 * @author rajveer.bhadauria
 */

@WebServlet(urlPatterns = { "/J4U/getOffer" }, asyncSupported = true)
public class GetOffers extends HttpServlet {
	public static final Logger LOGGER = Logger.getLogger(GetOffers.class);
	private static final long serialVersionUID = 1L;
	private PrintWriter out;
	private String category;
	private UpdaterDAO updaterDAO = null;
	private LookUpDAO lookUpDAO;
	private static int isBlacklistEnabled;
	private J4UOfferService j4uOfferService = new J4UOfferService();

	public GetOffers() {
		try {
			updaterDAO = new UpdaterDAO();
		    lookUpDAO = new LookUpDAO();
		    isBlacklistEnabled = PropertiesLoader.getIntValue(BLACKLIST_ENABLED);
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

	private static Function<String, Boolean> checkSocialCategory = (
			category) -> (null != category && category.equalsIgnoreCase(J4UOfferConstants.CATEGORY_SOCIAL)) ? true
					: false;
	private static Function<String, Boolean> checkMorningCategory = (
			category) -> (null != category && category.equalsIgnoreCase(J4UOfferConstants.CATEGORY_MORNING)) ? true
					: false;
	private static BiPredicate<String, String> checkSubCategory = (category,
			subCategory) -> (null != category && (category.equalsIgnoreCase(J4UOfferConstants.CATEGORY_VOICE)
					|| category.equalsIgnoreCase(J4UOfferConstants.CATEGORY_INTEGRATED)) && null != subCategory) ? true
							: false;

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
			LOGGER.info("GetOffers Request received => " + postDataObj);
			category = postDataObj.getString(CATEGORY);
			insertApiLog(postDataObj, GET_OFFER, API_OFFER_REQ_RECEIVED, postDataObj.get(refNum).toString(), category);
			if (1 == isBlacklistEnabled) {
		        boolean isBlacklist = lookUpDAO.isMsisdnBlacklist(postDataObj.get(MSISDN).toString());
		        if (isBlacklist) {
		          int statusCode = J4UOfferStatusCode.GENERAL_FAILURE.getStatusCode();
		          String statusMessage = J4UOfferStatusCode.GENERAL_FAILURE.getStatusMessage();
		          LOGGER.info("transactionId::" + txId + "| msisdn is blacklisted :- " + postDataObj.get(MSISDN));
		          insertApiLog(postDataObj, GET_OFFER, API_FINAL_OFFERS_SENT_BLACKLIST, BLACKLISTED_COMMENTS, category);
		          JSONObject synchOffersObj = j4uOfferService.processResponseJson(postDataObj, statusCode, statusMessage, null, "", "");
		          
		          out = response.getWriter();
		          out.println(synchOffersObj.toString());
		        } else {
		          processGetOffers(postDataObj, category, request, response);
		        } 
		      } else {
		        processGetOffers(postDataObj, category, request, response);
		      } 

		} catch (Exception e) {
			LOGGER.error("Exception while getting offer details", e);
		}
	}
	
	private void processGetOffers(JSONObject postDataObj, String category, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String subCategory = null;
		if (postDataObj.has(J4UOfferConstants.SUB_CATEGORY)) {
			subCategory = postDataObj.getString(J4UOfferConstants.SUB_CATEGORY);
		}
		postDataObj.put(J4UOfferConstants.KEY_ISENHANCED, checkSubCategory.test(category, subCategory));
		postDataObj.put(J4UOfferConstants.KEY_ISSOCIAL, checkSocialCategory.apply(category));
		postDataObj.put(J4UOfferConstants.KEY_ISMORNING, checkMorningCategory.apply(category));
		
		LOGGER.info("Category:: " + category);
		JSONObject synchOffersObj = null;
		// Doing a Synchronous Offer Call
		synchOffersObj = j4uOfferService.getSynchOffer(postDataObj);
		LOGGER.info("synchOffers Response => " + synchOffersObj);

		if (synchOffersObj != null) {
			Integer statusCode = synchOffersObj.getInt(STATUS_CODE);
			if (postDataObj.getBoolean(J4UOfferConstants.KEY_ISMORNING)) {
				if (statusCode == J4UOfferStatusCode.SUCCESS.getStatusCode()) {
					insertApiLog(synchOffersObj, GET_OFFER, API_FINAL_OFFERS_SENT,
							J4UOfferStatusCode.SUCCESS.getStatusMessage(), category);
					out = response.getWriter();
					out.println(synchOffersObj.toString());
				} else {
					String comments = statusCode + " :: " + J4UOfferStatusCode.getStatusMap(statusCode);
					insertApiLog(postDataObj, GET_OFFER, API_OFFER_REQ_FAILED, comments, category);
					LOGGER.info("getOffer: snchoffer calling :: NO_OFFERS PRESENT   :: " + postDataObj + " ==> "
							+ comments);
					out = response.getWriter();
					out.println(synchOffersObj.toString());
				}
			} else {
				if (statusCode == J4UOfferStatusCode.SUCCESS.getStatusCode()) {
					insertApiLog(synchOffersObj, GET_OFFER, API_FINAL_OFFERS_SENT,
							J4UOfferStatusCode.SUCCESS.getStatusMessage(), category);
					out = response.getWriter();
					out.println(synchOffersObj.toString());
				} else if (statusCode == J4UOfferStatusCode.NO_OFFERS.getStatusCode()) {
					AsyncContext asyncCtx = request.startAsync();
					asyncCtx.setTimeout(PropertiesLoader.getIntValue(MPESA_ASYNC_TIMEOUT));
					asyncCtx.addListener(new AppAsyncListener());
					AsyncMap asyncMap = AsyncMap.instance();
					asyncMap.put(synchOffersObj.getString(refNum), asyncCtx);
					LOGGER.debug("getOffer: aysnchoffer calling :: NO_OFFERS code :: " + statusCode);
					j4uOfferService.getAsynchOffer(postDataObj);
				} else {
					// other
					String comments = statusCode + " :: " + J4UOfferStatusCode.getStatusMap(statusCode);
					insertApiLog(postDataObj, GET_OFFER, API_OFFER_REQ_FAILED, comments, category);
					LOGGER.error("Error response  :: " + postDataObj + " ==> " + comments);
					out = response.getWriter();
					out.println(synchOffersObj.toString());
				}
			}
		}
	}

	public void receiveAsyncOffers(JSONObject asyncOffersObj) {
		try {
			LOGGER.info("AsynchOffers Response => " + asyncOffersObj);
			AsyncMap asyncMap = AsyncMap.instance();
			AsyncContext asyncCtx = asyncMap.get(asyncOffersObj.getString(refNum));
			asyncMap.remove(asyncOffersObj.getString(refNum));
			Integer statusCode = asyncOffersObj.getInt(STATUS_CODE);
			String comments = statusCode + " :: " + J4UOfferStatusCode.getStatusMap(statusCode);
			// Log insertion in Oracle table
			insertApiLog(asyncOffersObj, GET_OFFER, API_FINAL_OFFERS_SENT, comments, category);
			//ResponseJson formation 
			JSONObject responseJsonObject = processResponseJson(asyncOffersObj);
			out = asyncCtx.getResponse().getWriter();
			out.println(responseJsonObject.toString());
			asyncCtx.complete();
		} catch (Exception e) {
			LOGGER.error("Exception while receiving Async offers :: ", e);
		}
	}
	
	private JSONObject processResponseJson(JSONObject requestJson) throws JSONException {
		
		JSONObject responseJson = new JSONObject();
		responseJson.put(REF_NUM, requestJson.getString(REF_NUM));
		responseJson.put(TRANSACTION_ID, requestJson.getString(TRANSACTION_ID));
		responseJson.put(STATUS_CODE, requestJson.getInt(STATUS_CODE));
		responseJson.put(STATUS_MESSAGE, requestJson.getString(STATUS_MESSAGE));
		responseJson.put(MSISDN, Long.parseLong(requestJson.getString(MSISDN)));
		responseJson.put(LANGUAGE, requestJson.getString(LANGUAGE));
		responseJson.put(OFFERS, requestJson.getJSONArray(OFFERS));
		responseJson.put(CELL_ID, requestJson.has(CELL_ID) ? requestJson.getString(CELL_ID) : "");
		responseJson.put(POOL_ID, requestJson.has(POOL_ID) ? requestJson.getString(POOL_ID) : "");
		
		if(!requestJson.getString(CHANNEL).equalsIgnoreCase(CHANNEL_EVC)) {
			responseJson.put(CHANNEL, requestJson.getString(CHANNEL));
		}		
		return responseJson;		
	}

	private void insertApiLog(JSONObject jsonObject, String apiType, String status, String comments, String category)
			throws Exception {
		StringBuilder offerIds = new StringBuilder();
		StringBuilder comment = new StringBuilder();
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
		if (jsonObject.has(OFFERS)) {
			JSONArray offerArray = jsonObject.getJSONArray(OFFERS);
			offerIds.append("[");
			comment.append("[");
			for (int i = 0; i < offerArray.length(); i++) {
				JSONObject objectInArray = offerArray.getJSONObject(i);
				if (i < offerArray.length() - 1) {
					offerIds.append(objectInArray.getString(OFFER_ID) + ", ");
					comment.append(objectInArray.getString(OFFER_DESC) + ", ");
				} else {
					offerIds.append(objectInArray.getString(OFFER_ID));
					comment.append(objectInArray.getString(OFFER_DESC));
				}

			}
			offerIds.append("]");
			comment.append("]");
			comments = comment.toString();
			LOGGER.debug("offer Desc comments" + comments + " \n size : " + comments.length());
		}

		ApiLog apiLog = new ApiLog();
		apiLog.setMsisdn(jsonObject.get(MSISDN).toString());
		apiLog.setApiType(apiType);
		apiLog.setComments(comments);
		apiLog.setDateTime(Utils.getCurrentTimeStamp());
		apiLog.setProdIds(offerIds.toString());
		apiLog.setRefNum(refNum);
		apiLog.setStatus(status);
		apiLog.setProdType(category);
		apiLog.setThirdPartyRef(thirdPartyRef);
		apiLog.setTransactionId(jsonObject.getString(TRANSACTION_ID));
		apiLog.setChannel(channel);
		apiLog.setCellId(jsonObject.has(CELL_ID) ? jsonObject.getString(CELL_ID) : "");
		apiLog.setPoolId(jsonObject.has(POOL_ID) ? jsonObject.getString(POOL_ID) : "");
		LOGGER.debug("APilog....." + apiLog);
		updaterDAO.insertApiLog(apiLog);
	}

}

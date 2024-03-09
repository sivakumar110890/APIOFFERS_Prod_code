package com.comviva.api.j4u.service;

import com.comviva.api.exception.ConfigException;
import com.comviva.api.j4u.config.PropertiesLoader;
import com.comviva.api.j4u.dao.LookUpDAO;
import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.ApiLog;
import com.comviva.api.j4u.model.HTTPResponse;
import com.comviva.api.j4u.model.UserInfo;
import com.comviva.api.j4u.service.publisher.J4UOfferEventPublisher;
import com.comviva.api.j4u.utils.CellIDPoolIDCache;
import com.comviva.api.j4u.utils.J4UOfferStatusCode;
import com.comviva.api.j4u.utils.J4UOfferConstants;
import com.comviva.api.j4u.utils.ProductInfoCache;
import com.comviva.api.j4u.utils.ProductPriceCache;
import com.comviva.api.j4u.utils.HTTPSclient;
import com.comviva.api.j4u.utils.Utils;
import com.comviva.api.offers.ActivateOffer;
import com.comviva.api.offers.GetOffers;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;

import static com.comviva.api.j4u.utils.J4UOfferConstants.*;

/**
 * This class provide service for j4u offer
 *
 * @author Saloni.Gupta
 */
public class J4UOfferService {
	private static final Logger LOG = Logger.getLogger(J4UOfferService.class);

	private LookUpDAO lookUpDAO;
	private UpdaterDAO updaterDAO;
	private UserInfo userInfo;
	private HTTPSclient httpsClient;
	private String csType;
	private SecureRandom random;

	public J4UOfferService() {
		try {
			userInfo = new UserInfo();
			lookUpDAO = new LookUpDAO();
			updaterDAO = new UpdaterDAO();
			httpsClient = HTTPSclient.getInstance();
			random = new SecureRandom();
		} catch (Exception e) {

			LOG.error("Exception :: " + e.getMessage(), e);
		}
	}

	private static String getTagValue(String tag, Element element) {
		NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = nodeList.item(0);
		if (node == null) {
			return null;
		}
		return node.getNodeValue();
	}

	/**
	 * This method returns the json object with offer details for queried json
	 * request
	 *
	 * @param requestJson
	 *            synched refreshed json request
	 * @return json object with offer details
	 */

	private String prepareRequestXML(JSONObject requestJson) throws UnsupportedEncodingException {
		String dateTime = Utils.getDateAsString(new Date(), "yyyymmddHHMMssSSS");
		LOG.debug("dateTime" + dateTime);
		StringBuilder request = new StringBuilder();
		request.append("<DistanceQueryReq>");
		request.append("<SessionId>");
		request.append(requestJson.get(MSISDN).toString() + dateTime).append("</SessionId>");
		request.append("<SessionTime>").append(dateTime).append("</SessionTime>");

		request.append("<MSISDNA>");
		request.append(requestJson.get(MSISDN).toString()).append("</MSISDNA>");
		request.append("<MSISDNB>");
		request.append(requestJson.get(MSISDN).toString()).append("</MSISDNB>");
		request.append("</DistanceQueryReq>");
		String data = request.toString();
		LOG.debug("Message request  for DistanceQueryReq::" + data);
		return data;

	}

	private static Function<JSONObject, Integer> getLangCode = (
			requestJSON) -> requestJSON.has(LANGUAGE) && requestJSON.getString(LANGUAGE).equalsIgnoreCase("en") ? 2 : 1;
	private static Function<UserInfo, Boolean> checkMorningEligibilityNewCusotmer = (userInfo) -> {
		return null == userInfo || userInfo.isJFUEligible();
	};

	public JSONObject getSynchOffer(JSONObject requestJson)
			throws UnsupportedEncodingException, ConfigException, ParserConfigurationException {
		JSONArray offers = null;
		JSONObject synchResJson;
		int statusCode = -1;
		String statusMessage = "";

		requestJson.put(J4UOfferConstants.CELL_ID, "");
		requestJson.put(J4UOfferConstants.POOL_ID, "");

		LOG.info("getSynchOffer :: requestJson = " + requestJson);
		String msisdn = requestJson.get(MSISDN).toString();
		int langCode = getLangCode.apply(requestJson);
		if (!requestJson.getBoolean(KEY_ISSOCIAL) && !requestJson.getBoolean(KEY_ISMORNING)) {
			requestJson = requestCellID(requestJson);
		}
		String cellID = requestJson.getString(CELL_ID);
		String poolId = requestJson.getString(POOL_ID);

		try {
			userInfo = lookUpDAO.getUserInfo(msisdn);

			String offerCategory = requestJson.getString(CATEGORY);
			if (null != userInfo && userInfo.isJFUEligible() && userInfo.isMlFlag()) {
				insertApiLog(requestJson, userInfo.getRandomFlag() ? "Y" : "N", userInfo.isMlFlag() ? "Y" : "N",
						GET_OFFER, API_SYNCH_REQ_RECEIVED, null);

				if (requestJson.getBoolean(KEY_ISMORNING)) {
					if (Utils.checkMorningEligiblity(offerCategory, userInfo, requestJson)) {
						offers = getMorningMLUser(requestJson, offerCategory, langCode);
						LOG.info("offers after get syncMethod called ::" + offers);

						if (offers.length() == 0) {
							offers = null;
						}
					} else {
						offers = null;
						LOG.info("User not eligible for the morning offer please try another day.. ");
					}
					synchResJson = setResponseJSON(offers, requestJson, poolId, cellID, msisdn);

				} else {
					offers = getOffersSync(userInfo, msisdn, offers, requestJson, offerCategory, poolId, langCode);
					LOG.info("OFFERS after getOFFER sync called : " + offers);
					synchResJson = setResponseJSON(offers, requestJson, poolId, cellID, msisdn);

				}
			}

			else if (requestJson.getBoolean(KEY_ISMORNING) && checkMorningEligibilityNewCusotmer.apply(userInfo)) {
				if (Utils.checkMorningEligiblity(offerCategory, userInfo, requestJson)) {
					offers = getMorningMLUser(requestJson, offerCategory, langCode);
					if (offers.length() == 0) {
						offers = null;
					}
					LOG.info("offers after get syncMethod called ::" + offers);
				} else {
					offers = null;
					LOG.info("User not eligible for the morning offer please try another day.. ");
				}

				synchResJson = setResponseJSON(offers, requestJson, poolId, cellID, msisdn);
			} else {
				statusCode = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusCode();
				statusMessage = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusMessage();

				String comments = null;
				if (null != userInfo) {
					comments = "J4UEligible/ML Flag may not be valid.";
					insertApiLog(requestJson, "N", null, GET_OFFER, API_SYNCH_REQ_RECEIVED, comments);
				} else {
					comments = "User not found";
					insertApiLog(requestJson, null, null, GET_OFFER, API_SYNCH_REQ_RECEIVED, comments);
				}
				synchResJson = processResponseJson(requestJson, statusCode, statusMessage, offers, cellID, poolId);
				LOG.error(msisdn + " " + comments);
			}

		}

		catch (Exception e) {
			// db exception
			statusCode = J4UOfferStatusCode.GENERAL_FAILURE.getStatusCode();
			statusMessage = J4UOfferStatusCode.GENERAL_FAILURE.getStatusMessage() + " : " + e;
			synchResJson = processResponseJson(requestJson, statusCode, statusMessage, offers, cellID, poolId);
			LOG.error("Error in sync operation : " + e);
		}

		return synchResJson;

	}

	private JSONObject requestCellID(JSONObject requestJson)
			throws UnsupportedEncodingException, ConfigException, ParserConfigurationException {
		HTTPResponse httpResponse = new HTTPResponse(-1, J4UOfferConstants.STATUS_FAILURE);

		String requestXMLString = prepareRequestXML(requestJson);
		httpResponse = httpsClient.sendRequest(requestXMLString,
				PropertiesLoader.getValue(J4UOfferConstants.URL_GET_CELL_ID), "POST");
		String cellID = "";
		if (httpResponse.getStatus() == 200) {
			cellID = getCellId(httpResponse.getOutput());
		}
		LOG.debug("CELL_ID:: " + cellID);
		CellIDPoolIDCache cellIDPoolIDCache = CellIDPoolIDCache.instance();
		String poolId = cellIDPoolIDCache.getPoolIDForCellID(cellID);
		LOG.debug("poolId:: " + poolId);
		if (poolId != null) {
			requestJson.put(J4UOfferConstants.CELL_ID, cellID);
			requestJson.put(J4UOfferConstants.POOL_ID, poolId);
		}

		return requestJson;
	}

	private JSONObject setResponseJSON(JSONArray offers, JSONObject requestJson, String poolId, String cellid,
			String msisdn) {

		JSONObject synchResJson;
		int statusCode = -1;
		String statusMessage = "";

		if (offers != null) {

			if (requestJson.getBoolean(KEY_ISENHANCED)) {
				// checking the Morning offers availability

				statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
				statusMessage = J4UOfferStatusCode.SUCCESS.getStatusMessage();
				synchResJson = processResponseJson(requestJson, statusCode, statusMessage, offers, cellid, poolId);
				LOG.debug("SubCategory Cached  Offer(s) found for user :" + msisdn);
			} else if (requestJson.getBoolean(KEY_ISMORNING)) {
				// checking the Morning offers availability

				statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
				statusMessage = J4UOfferStatusCode.SUCCESS.getStatusMessage();
				synchResJson = processResponseJson(requestJson, statusCode, statusMessage, offers, cellid, poolId);
				LOG.debug("Morning Offer(s) found for user :" + msisdn);
			} else {

				if (userInfo.getLocationRandomFlag() || userInfo.getRandomFlag()) {

					// checking the offers availability

					statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
					statusMessage = J4UOfferStatusCode.SUCCESS.getStatusMessage();
					synchResJson = processResponseJson(requestJson, statusCode, statusMessage, offers, cellid, poolId);
					LOG.debug("Offer(s) found for user :" + msisdn);
				} else {

					if (poolId == null || poolId.isEmpty() || userInfo.isMlFlag()) {
						statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
						statusMessage = J4UOfferStatusCode.SUCCESS.getStatusMessage();
						synchResJson = processResponseJson(requestJson, statusCode, statusMessage, offers, cellid,
								poolId);
						LOG.debug("Offer(s) found for user :" + msisdn);

					} else {

						// if offer does exist for user
						// send error status, so Asynch method will be called
						statusCode = J4UOfferStatusCode.NO_OFFERS.getStatusCode();
						statusMessage = J4UOfferStatusCode.NO_OFFERS.getStatusMessage();
						synchResJson = processResponseJson(requestJson, statusCode, statusMessage, offers, cellid,
								poolId);
						LOG.info(" Location offers present for msisdn: " + msisdn);
					}
				}

			}
		} else {
			if (requestJson.getBoolean(KEY_ISMORNING)) {
				// if offer does exist for user
				// send error status, so Asynch method will be called
				statusCode = J4UOfferStatusCode.NO_OFFERS.getStatusCode();
				statusMessage = J4UOfferStatusCode.NO_OFFERS.getStatusMessage();
				synchResJson = processResponseJson(requestJson, statusCode, statusMessage, offers, cellid, poolId);
				LOG.info("No Morning offers present for msisdn: " + msisdn);
			}

			else {

				// if offer does exist for user
				// send error status, so Asynch method will be called
				statusCode = J4UOfferStatusCode.NO_OFFERS.getStatusCode();
				statusMessage = J4UOfferStatusCode.NO_OFFERS.getStatusMessage();
				LOG.info("no offers statusCode ::" + statusCode);
				LOG.info("no offers statusMessage ::" + statusMessage);
				synchResJson = processResponseJson(requestJson, statusCode, statusMessage, offers, cellid, poolId);
				LOG.info("Either No cached offer found for user/ Location offers present for msisdn: " + msisdn);
			}
		}
		return synchResJson;

	}

	private JSONArray getOffersSync(UserInfo userInfo, String msisdn, JSONArray offers, JSONObject requestJson,
			String offerCategory, String poolId, int langCode) throws Exception {
		LOG.info("getoffer Method called ");

		if (userInfo.getRandomFlag() || userInfo.getLocationRandomFlag()) {
			// offer for random user ..Either
			// LOCATION_RANDOM_FLAG/RANDOM_FLAG = true

			LOG.debug("getSynchOffer :: calling Random user block for msisdn = " + msisdn);
			offers = getOfferForRandomMLUser(requestJson, offerCategory, poolId, langCode);
			if (offers.length() == 0) {
				offers = null;
			}
			insertApiLog(requestJson, "Y", userInfo.isMlFlag() ? "Y" : "N", GET_OFFER, API_SYNCH_REQ_RECEIVED, null);

		} else {
			// offer for refreshed
			LOG.info("OFFER CATEGORY ::");

			if (((requestJson.has(SUB_CATEGORY) && requestJson.getBoolean(KEY_ISENHANCED))
					|| this.checkOfferRefreshFlag(userInfo.getOfferRefreshFlag(), offerCategory)
					|| (poolId == null || poolId.isEmpty()))) {
				LOG.debug("getSynchOffer :: calling target user block for msisdn => " + msisdn + ", OfferRefreshFlag = "
						+ userInfo.getOfferRefreshFlag());
				offers = getOfferForTargetMLUser(requestJson, offerCategory);
				if (offers.length() == 0) {
					offers = null;
				}
			}

		}

		return offers;

	}

	private String getCellId(String httpResponse) throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		String cellid = "";
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(httpResponse)));
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("DistanceQueryReqRsp");
			Node node = nodeList.item(0);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				cellid = getTagValue("ApartyCellInfo", element);

			}

		} catch (SAXException | IOException | ParserConfigurationException e) {
			LOG.error("Exception in processing request::", e);
		}
		return cellid;
	}

	/**
	 * This method is used for asynch request when offer is not refreshed
	 *
	 * @param requestJson
	 *            Async refreshed json request
	 */
	public void getAsynchOffer(JSONObject requestJson) {
		JSONArray offers = null;
		JSONObject asynchResJson = null;		
		GetOffers getOffersObj = new GetOffers();
		LOG.info("getAsynchOffer method :: requestJson = " + requestJson);

		String msisdn = requestJson.get(MSISDN).toString();
		try {
			userInfo = lookUpDAO.getUserInfo(msisdn);
			if (null != userInfo && userInfo.isJFUEligible()) {
				insertApiLog(requestJson, userInfo.getRandomFlag() ? "Y" : "N", userInfo.isMlFlag() ? "Y" : "N",
						GET_OFFER, API_ASYNCH_REQ_RECEIVED, null);
				if (userInfo.getRandomFlag()) {
					// no offer for random user
					LOG.info("No offer for random user in getAsynchOffer method = " + msisdn);					
					requestJson.put(STATUS_CODE, J4UOfferStatusCode.NO_OFFERS.getStatusCode());
					requestJson.put(STATUS_MESSAGE, J4UOfferStatusCode.NO_OFFERS.getStatusMessage());
					requestJson.put(OFFERS, offers);
					getOffersObj.receiveAsyncOffers(requestJson);
					
				} else {
					if (requestJson.has(CELL_ID)) {
						String cellid = requestJson.getString(CELL_ID);
						LOG.debug("CELL_ID: " + cellid);
						requestJson.put(J4UOfferConstants.CELL_ID, cellid);
					} else {
						requestJson.put(J4UOfferConstants.CELL_ID, "");
					}
					if (requestJson.has(POOL_ID)) {
						String poolId = requestJson.getString(POOL_ID);
						LOG.debug("POOL_ID: " + poolId);
						requestJson.put(J4UOfferConstants.POOL_ID, requestJson.getString(POOL_ID));
					} else {
						requestJson.put(J4UOfferConstants.POOL_ID, "");
					}
					// make ocs call prepare json request and send to ocs
					LOG.debug("Calling generateUserBalanceReq to get balance for ML target user = " + msisdn);
					generateUserBalanceReq(requestJson, userInfo);
				}

			} else {
				requestJson.put(STATUS_CODE, J4UOfferStatusCode.PARTIAL_CONTENT.getStatusCode());
				requestJson.put(STATUS_MESSAGE, J4UOfferStatusCode.PARTIAL_CONTENT.getStatusMessage());
							
				String comments = null;
				if (null != userInfo) {
					comments = "J4UEligible/ML Flag may not be valid.";
					insertApiLog(requestJson, userInfo.getRandomFlag() ? "Y" : "N", userInfo.isMlFlag() ? "Y" : "N",
							GET_OFFER, API_ASYNCH_REQ_RECEIVED, comments);
				} else {
					comments = "User not found";
					insertApiLog(requestJson, null, null, GET_OFFER, API_ASYNCH_REQ_RECEIVED, comments);
				}
				LOG.error(msisdn + " " + comments);
				requestJson.put(OFFERS, offers);
				getOffersObj.receiveAsyncOffers(asynchResJson);

			}
		} catch (Exception e) {
			LOG.error("Error in async operation : " + e.getMessage(), e);

		}
	}

	/**
	 * Activate the offer for user by calling ocs
	 *
	 * @param requestJson
	 *            json request for activate the offer
	 */
	public void activateOffer(JSONObject requestJson) {
		JSONObject responseJson = new JSONObject();
		LOG.info("request Json " + requestJson.toString());
		try {
			String txnId = requestJson.getString(TRANSACTION_ID);
			LOG.info("MSISDN :: " + requestJson.getString(MSISDN) + "IS PUSRCHAED ::" + requestJson.has(IS_PURCHASED));
			if (requestJson.has(IS_PURCHASED) && null != requestJson.getString(MSISDN)) {

				provisionRewardForUser(requestJson);
			} else {
				responseJson.put(REF_NUM, requestJson.getInt(REF_NUM));
				responseJson.put(TRANSACTION_ID, txnId);
				responseJson.put(STATUS_CODE, J4UOfferStatusCode.PARTIAL_CONTENT.getStatusCode());
				responseJson.put(STATUS_MESSAGE, J4UOfferStatusCode.PARTIAL_CONTENT.getStatusMessage());
				// call async method
				ActivateOffer activate = new ActivateOffer();
				activate.actOfferResponseAsync(responseJson);
				LOG.error("activateOffer :: Partial  request received " + requestJson);
			}
		} catch (Exception e) {
			LOG.error("Error while activating offer : " + e.getMessage(), e);
		}
	}

	public void provisionRewardForUser(JSONObject requestJson) throws Exception {
		JSONObject responseJson = new JSONObject();
		JSONObject message = createMessageForRewardsPlugin(requestJson);
		LOG.info("Published message on rewards topic for Ml user :: " + message);
		LOG.info("Publish to Reward topic is called :: ");
		if (message.has(J4UOfferConstants.SEL_PROD_TYPE)
				&& message.getString(J4UOfferConstants.SEL_PROD_TYPE).equalsIgnoreCase(MORNING_FLAG)) {
			if (Utils.getValidMorningOffer()) {
				LOG.info(
						"Customer asking for Morning offer Activation Kindly publish to for request to reward plugin:::");
				publishToRewardsTopic(message.toString(), requestJson.getString(MSISDN));
			} else {
				LOG.info("Not falling in morning window");
				responseJson.put(MSISDN, requestJson.getString(MSISDN));
				responseJson.put(REF_NUM, requestJson.getString(REF_NUM));
				responseJson.put(TRANSACTION_ID, requestJson.getString(TRANSACTION_ID));
				responseJson.put(STATUS_CODE, J4UOfferStatusCode.NO_OFFERS.getStatusCode());
				responseJson.put(STATUS_MESSAGE, J4UOfferStatusCode.NO_OFFERS.getStatusMessage());
				// call async method
				ActivateOffer activate = new ActivateOffer();
				activate.actOfferResponseAsync(responseJson);
				LOG.info("activateOffer :: Customer did not requested in morning offer Window ::" + requestJson);
			}
		} else {
			LOG.info("Publish for Categories VDISH ::");
			publishToRewardsTopic(message.toString(), requestJson.getString(MSISDN));
		}
	}

	public JSONObject processResponseJson(JSONObject requestJson, int statusCode, String statusMessage,
			JSONArray offers, String cellid, String poolId) {
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put(REF_NUM, requestJson.getString(REF_NUM));
			responseJson.put(TRANSACTION_ID, requestJson.getString(TRANSACTION_ID));
			responseJson.put(STATUS_CODE, statusCode);
			responseJson.put(STATUS_MESSAGE, statusMessage);
			responseJson.put(MSISDN, requestJson.getLong(MSISDN));
			responseJson.put(LANGUAGE, requestJson.getString(LANGUAGE));
			responseJson.put(OFFERS, offers);
			responseJson.put(J4UOfferConstants.CELL_ID, cellid);
			responseJson.put(J4UOfferConstants.POOL_ID, poolId);
		} catch (Exception e) {
			LOG.error("exception in json parsing:: ", e);
		}
		LOG.info("Response JSON from J4U service :: " + responseJson);
		return responseJson;
	}

	private JSONArray getOfferForRandomMLUser(JSONObject offerRequest, String offerCategory, String poolId,
			int langCode) throws Exception {

		ProductInfoCache productInfoCache = ProductInfoCache.instance();
		boolean bIsSocial = offerRequest.getBoolean(KEY_ISSOCIAL);
		LOG.debug("Product Type:: " + Utils.getProductCategory().get(offerCategory));

		Set<String> randomIdsSet = new HashSet<>();
		JSONArray offerArray = new JSONArray();
		if (bIsSocial) {
			getSocialRandomIds(offerCategory, langCode, productInfoCache, randomIdsSet);
		} else {
			String prodSubType = null;
			if (offerRequest.has(SUB_CATEGORY) && offerRequest.getBoolean(KEY_ISENHANCED)) {

				if (offerRequest.getString(SUB_CATEGORY).isEmpty()) {
					prodSubType = null;
					LOG.info("SUB Category ::" + prodSubType);

				} else {
					prodSubType = offerRequest.getString(SUB_CATEGORY);
					LOG.info("SUB Category ::" + prodSubType);
				}
				LOG.info("SUB Category ::" + prodSubType);
			}
			getRandomIdsSet(offerCategory, prodSubType, poolId, langCode, productInfoCache, randomIdsSet);
			LOG.info("getRandomIdsSet  picked ::");
		}

		if (randomIdsSet.size() == MAX_PRODIDS_CNT
				|| randomIdsSet.size() < MAX_PRODIDS_CNT && HOURLY.equalsIgnoreCase(offerCategory)
				|| randomIdsSet.size() < MAX_PRODIDS_CNT && offerRequest.has(SUB_CATEGORY)) {
			LOG.info("Inside random size block ");
			ProductPriceCache priceCache = ProductPriceCache.instance();

			for (String productId : randomIdsSet) {
				JSONObject offerJson = new JSONObject();
				int languageCode = offerRequest.getString("Language").equalsIgnoreCase("fr") ? 1 : 2;
				String key = productId + "_" + languageCode;
				String productDescription = "";
				if (bIsSocial) {
					productDescription = productInfoCache.getSocial(key).getProductDesc();
				} else {
					productDescription = productInfoCache.get(key).getProductDesc();
				}

				offerJson.put(OFFER_ID, (productId));
				offerJson.put(OFFER_DESC, productDescription);
				offerJson.put(AMOUNT, Integer.parseInt(priceCache.get(productId)));
				offerArray.put(offerJson);

			}

		}

		return offerArray;
	}

	private JSONArray getMorningMLUser(JSONObject offerRequest, String offerCategory, int langCode) throws Exception {
		LOG.info("getMorningMLUser called ::");
		ProductInfoCache productInfoCache = ProductInfoCache.instance();
		LOG.debug("Product Type:: " + Utils.getProductCategory().get(offerCategory));
		Set<String> morningIdsSet = new HashSet<>();
		JSONArray offerArray = new JSONArray();
		String msisdn = offerRequest.getString(MSISDN).toString();
		getMorningIds(offerCategory, langCode, morningIdsSet, msisdn);
		int languageCode = offerRequest.getString("Language").equalsIgnoreCase("fr") ? 1 : 2;
		if (morningIdsSet != null) {
			for (String productId : morningIdsSet) {
				JSONObject offerJson = new JSONObject();
				String key = productId + "_" + languageCode;
				String productDescription = "";
				productDescription = productInfoCache.getMorning(key).getProductDesc();
				offerJson.put(OFFER_ID, (productId));
				offerJson.put(OFFER_DESC, productDescription);
				offerJson.put(LANG_CODE, languageCode);
				offerArray.put(offerJson);
				LOG.info("Availablree offers ::" + offerArray);
			}

		}
		LOG.info("getMorningMLUser END ::");
		return offerArray;
	}

	private Set<String> getRandomIdsSet(String offerCategory, String prodSubType, String poolId, int langCode,
			ProductInfoCache productInfoCache, Set<String> randomIdsSet)
			throws NoConnectionsException, IOException, ProcCallException {

		int prodIdsListLen = 0;
		// String key = null;
		List<String> prodIdsList = new ArrayList<>();
		if (userInfo.getLocationRandomFlag()) {
			prodIdsList = productInfoCache.getLocationProdIds(Utils.getProductCategory().get(offerCategory),
					prodSubType, poolId, langCode);
			LOG.info("Location prodIdsList :: " + prodIdsList);

		}

		if (prodIdsList == null || prodIdsList.isEmpty()
				|| prodIdsList.size() < 3 && !HOURLY.equalsIgnoreCase(offerCategory)
				|| prodIdsList.size() < 3 && prodSubType != null) {

			if (userInfo.getRandomFlag()) {
				if (null != prodSubType) {
					LOG.info("Into Sub Category flow");
					prodIdsList = productInfoCache.getProductIdsListBySubCategory(
							Utils.getProductCategory().get(offerCategory) + "_" + prodSubType);
				} else {
					LOG.info("normal flow");
					prodIdsList = productInfoCache.getProductIdsList(Utils.getProductCategory().get(offerCategory));
				}
				// prodIdsList =
				// lookUpDAO.getProdIds(Utils.getProductCategory().get(offerCategory),
				// langCode);

				LOG.debug("prodIdsList_random:: " + prodIdsList);

			}

		}

		if (prodIdsList != null) {
			prodIdsListLen = prodIdsList.size();
			// LOG.debug("prodIdsListLen: " + prodIdsListLen);
		}

		if (prodIdsListLen >= MAX_PRODIDS_CNT && prodIdsList != null) {
			while (true) {
				randomIdsSet.add(prodIdsList.get(random.nextInt(prodIdsListLen)));
				if (randomIdsSet.size() == MAX_PRODIDS_CNT) {
					break;
				}
			}
		} else {
			randomIdsSet.addAll(prodIdsList);
		}

		LOG.debug("Product list :: " + randomIdsSet);
		return randomIdsSet;
	}

	private Set<String> getSocialRandomIds(String offerCategory, int langCode, ProductInfoCache productInfoCache,
			Set<String> randomIdsSet) throws NoConnectionsException, IOException, ProcCallException {

		int prodIdsListLen = 0;
		List<String> prodIdsList = new ArrayList<>();
		if (userInfo.getRandomFlag()) {
			prodIdsList = productInfoCache.getSocialProductIdsList();
			LOG.debug("prodIdsList_random:: " + prodIdsList);
		}
		if (prodIdsList != null) {
			prodIdsListLen = prodIdsList.size();
			LOG.debug("prodIdsListLen: " + prodIdsListLen);
		}
		if (prodIdsListLen >= MAX_PRODIDS_CNT && prodIdsList != null) {
			while (true) {
				randomIdsSet.add(prodIdsList.get(random.nextInt(prodIdsListLen)));
				if (randomIdsSet.size() == MAX_PRODIDS_CNT) {
					break;
				}
			}
		} else {
			randomIdsSet.addAll(prodIdsList);
		}

		LOG.debug("Product list :: " + randomIdsSet);
		return randomIdsSet;
	}

	private Set<String> getMorningIds(String offerCategory, int langCode, Set<String> randomIdsSet, String msisdn)
			throws Exception {

		int prodIdsListLen = 0;
		List<String> prodIdsList = new ArrayList<>();
		prodIdsList = lookUpDAO.getMorningOfferWhiteList(msisdn);
		LOG.debug("prodIdsList_random:: " + prodIdsList);
		if (prodIdsList != null) {
			prodIdsListLen = prodIdsList.size();
			LOG.debug("prodIdsListLen: " + prodIdsListLen);
			randomIdsSet.addAll(prodIdsList);
		}

		LOG.debug("Product list :: " + randomIdsSet);
		return randomIdsSet;
	}

	private void generateUserBalanceReq(JSONObject requestJson, UserInfo userInfo) throws Exception {
		requestJson.put(SOURCE, MPESA_QUARY_BAL);
		requestJson.put(OFFER_REFRESH_FLAG, userInfo.getOfferRefreshFlag());
		requestJson.put(A_VALUE, userInfo.getaValue());
		requestJson.put(PREF_PAY_METHOD, userInfo.getPrefPayMethod());
		// add required field for rf calculation
		LOG.info("Prefered  Pay method :: " + userInfo.getPrefPayMethod());
		J4UOfferEventPublisher mpesaEventPublisher = new J4UOfferEventPublisher();
		if (userInfo.getPrefPayMethod().equalsIgnoreCase(PREF_PAY_MET_MPESA)) {
			LOG.info("GetOffer topic  fetch : " + PropertiesLoader.getValue(J4UOfferConstants.MPESA_QUERY_BAL_TOPIC) + "message " + requestJson.toString());
			mpesaEventPublisher.addEvent(PropertiesLoader.getValue(J4UOfferConstants.MPESA_QUERY_BAL_TOPIC),
					requestJson.toString());

		} else {
			if (Integer.parseInt(PropertiesLoader.getValue(CS_FLAG)) == 1) {
				csType = lookUpDAO.getCSType(userInfo.getMsisdn());
				LOG.info("Fetching CS type ----" + csType);
				LOG.info("CS flag " + Integer.parseInt(PropertiesLoader.getValue(CS_FLAG)));
				LOG.info("CS flag check done : ");
				csType = lookUpDAO.getCSType(userInfo.getMsisdn());
				LOG.info("Fetching CS type ----" + csType);
				if (csType.equalsIgnoreCase(J4UOfferConstants.CS_TYPE_OP)) {
					LOG.debug("csType op check done ");
					mpesaEventPublisher.addEvent(PropertiesLoader.getValue(CCS_QUERY_BAL_TOPIC),
							requestJson.toString());
					LOG.debug("Published to OCS  for user bal check: topic name for openet => "
							+ PropertiesLoader.getValue(CCS_QUERY_BAL_TOPIC));
				} else {
					LOG.info("GetOffer topic  fetch : " + PropertiesLoader.getValue(OCS_QUERY_BAL_TOPIC) + ", requestJson:: " + requestJson.toString());
					mpesaEventPublisher.addEvent(PropertiesLoader.getValue(OCS_QUERY_BAL_TOPIC),
							requestJson.toString());
				}
			} else {

				LOG.info("GetOffer topic  fetch : " + OCS_QUERY_BAL_TOPIC + "message " + requestJson.toString());
				mpesaEventPublisher.addEvent(PropertiesLoader.getValue(OCS_QUERY_BAL_TOPIC), requestJson.toString());

			}
		}
	}

	public JSONArray getOfferForTargetMLUser(JSONObject offerRequest, String offerCategory) throws Exception {
		LOG.debug("offer for refreshed getOfferForTargetMLUser : " + offerRequest.get(MSISDN).toString()
				+ ", OfferCategory :: " + offerCategory);
		String prodSubType = null;
		if (offerRequest.has(SUB_CATEGORY) && offerRequest.getBoolean(KEY_ISENHANCED))
			prodSubType = offerRequest.getString(SUB_CATEGORY);
		return lookUpDAO.getOfferForTgtMLUser(offerRequest.get(MSISDN).toString(),
				Utils.getProductCategory().get(offerCategory), prodSubType);
	}

	/**
	 * checks the offer refresh flag for today if already refresh send true else
	 * false
	 */
	public boolean checkOfferRefreshFlag(String offerRefreshFlag, String category) {
		String offerCategoryFlag = Utils.getMlRefreshFlagMap().get(category);
		LOG.info("offerCategoryFlag :: " + offerCategoryFlag);
		LOG.info("offerRefreshFlag :: " + offerRefreshFlag.indexOf(offerCategoryFlag));
		return (offerRefreshFlag.equalsIgnoreCase("N") || offerRefreshFlag.indexOf(offerCategoryFlag) == -1);
	}

	private JSONObject createMessageForRewardsPlugin(JSONObject reqestJson) throws Exception {

		String prodType = null;
		JSONObject message = new JSONObject();
		boolean randomFlag = lookUpDAO.getRandomFlag("" + reqestJson.getLong(MSISDN));
		LOG.debug("random flag :: " + randomFlag);
		String productId = reqestJson.getString(OFFER_ID);
		prodType = lookUpDAO.getProductTypeById(productId);
		boolean isEnhancedFlowCategory = false;
		if (prodType.equals(J4U_VOICE)) {
			prodType = "V";
			isEnhancedFlowCategory = true;
		} else if (prodType.equals(J4U_DATA)) {
			prodType = "D";
		} else if (prodType.equals(J4U_INTEGRATED)) {
			prodType = "I";
			isEnhancedFlowCategory = true;
		} else if (prodType.equals(J4U_HOURLY_DATA)) {
			prodType = "H";
		} else if (prodType.equals(J4U_SOCIAL_MEDIA)) {
			prodType = "S";
		} else if (prodType.equals(J4U_MORNING_OFFER)) {
			prodType = "M";
		}
		if (!randomFlag || prodType.equals(MORNING_FLAG)) {
			message.put(SEL_PROD_TYPE, prodType);
		}
		ProductInfoCache prodInforCache = ProductInfoCache.instance();
		if (isEnhancedFlowCategory && Utils.getChannel(reqestJson.getString(CHANNEL))) {
			LOG.info("PRODUCT_ID :: " + productId);
			int languageCode = reqestJson.getString("Language").equalsIgnoreCase("fr") ? 1 : 2;
			String key = productId + "_" + languageCode;
			LOG.info("SEL_PROD_SUB_TYPE :: " + Utils.getSubCategoryType(prodInforCache.get(key).getProductSubType()));
			message.put(SEL_PROD_SUB_TYPE, Utils.getSubCategoryType(prodInforCache.get(key).getProductSubType()));
		}

		// Map<String, String> productPriceMap =
		// lookUpDAO.getProductPriceMap(new String[] { productId });
		ProductPriceCache priceCache = ProductPriceCache.instance();
		LOG.debug("product Price :" + priceCache.get(productId));
		message.put(MSISDN, "" + reqestJson.getLong(MSISDN));
		message.put("TRANSACTION_ID", reqestJson.getString(TRANSACTION_ID));
		message.put(ML_FLAG, FLAG_Y);
		message.put(PRODUCT_ID, productId);
		message.put(PRODUCT_PRICE, priceCache.get(productId));
		message.put(IS_PURCHASED, reqestJson.getBoolean(IS_PURCHASED));
		if ("USSD".equalsIgnoreCase(reqestJson.getString(CHANNEL)))
			message.put(SOURCE, MPESA);
		else
			message.put(SOURCE, reqestJson.getString(CHANNEL));
		message.put(CHANNEL, reqestJson.getString(CHANNEL));
		message.put(REF_NUM, reqestJson.getString(REF_NUM));
		message.put(G2_REF_NUM, reqestJson.has(THIRD_PARTY_REF) ? reqestJson.getString(THIRD_PARTY_REF) : null);
		return message;
	}

	/**
	 * Publishes a message to REWARDSCONSUMER topic
	 *
	 * @param message
	 * @throws Exception
	 */
	private void publishToRewardsTopic(final String message, String msisdn) throws Exception {
		LOG.debug("published to reward topic");
		String topicName = PropertiesLoader.getValue(REWARDSPUB_TOPIC_NAME);
		String topicNameOP = PropertiesLoader.getValue(OP_REWARDSPUB_TOPIC_NAME);
		J4UOfferEventPublisher eventPublisher = new J4UOfferEventPublisher();
		int csFlag = Integer.parseInt(PropertiesLoader.getValue(CS_FLAG));
		if (csFlag == 1) {
			LOG.info("CS flag check done : " + Integer.parseInt(PropertiesLoader.getValue(CS_FLAG)));
			csType = lookUpDAO.getCSType(msisdn);
			LOG.info("CS_TYPE : " + csType);
			if (csType.equalsIgnoreCase(J4UOfferConstants.CS_TYPE_OP)) {
				LOG.info("CS_TYPE OP check done : " + csType);
				LOG.info("REWARD topic  fetch  for Openet : " + topicNameOP + "message" + message);
				eventPublisher.addEvent(topicNameOP, message);
			} else {
				LOG.info("REWARD topic  fetch : " + topicName + "message" + message);
				eventPublisher.addEvent(topicName, message);
			}
		} else {
			LOG.info("REWARD topic  fetch : " + topicName + "message" + message);
			eventPublisher.addEvent(topicName, message);
		}

	}

	private void insertApiLog(JSONObject jsonObject, String randomFlag, String mlFlag, String apiType, String status,
			String comments) {
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
			apiLog.setRefNum(refNum);
			apiLog.setStatus(status);
			apiLog.setMlFlag(mlFlag);
			apiLog.setRandomFlag(randomFlag);
			apiLog.setProdType(jsonObject.getString(CATEGORY));
			apiLog.setThirdPartyRef(thirdPartyRef);
			apiLog.setTransactionId(jsonObject.getString(TRANSACTION_ID));
			apiLog.setChannel(channel);
			apiLog.setCellId(jsonObject.has(CELL_ID) ? jsonObject.getString(CELL_ID) : "");
			apiLog.setPoolId(jsonObject.has(POOL_ID) ? jsonObject.getString(POOL_ID) : "");
			LOG.debug("Inserting record to api log=>...." + apiLog);
			updaterDAO.insertApiLog(apiLog);
		} catch (Exception e) {

			LOG.error("Exception while inserting api log :: " + e.getMessage());
		}

	}

}

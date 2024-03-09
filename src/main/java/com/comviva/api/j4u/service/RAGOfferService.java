package com.comviva.api.j4u.service;

import static com.comviva.api.j4u.utils.J4UOfferConstants.LANG_CODE;

import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.NEXT_AVAILABLE_OFFER_DATE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OTHER;
import static com.comviva.api.j4u.utils.J4UOfferConstants.PAYMENT_METHOD;
import static com.comviva.api.j4u.utils.J4UOfferConstants.PRODUCT_VALUE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.RECHARGE_TARGET;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_NUM;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REMAINING_EFFORT;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REWARDS;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REWARD_CODE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_CODE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_MESSAGE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.SUBID_CNTRYCD;
import static com.comviva.api.j4u.utils.J4UOfferConstants.SUBID_LENGTH;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TARGET_TYPE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TRANSACTION_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.USSD_RAG_ATL_REWARD_MAP;
import static com.comviva.api.j4u.utils.J4UOfferConstants.VALIDITY;
import static com.comviva.api.j4u.utils.J4UOfferConstants.VALUE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.VALUE_P;
import static com.comviva.api.j4u.utils.J4UOfferConstants.WEEK_END_DATE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.WEEK_START_DATE;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.comviva.api.exception.ConfigException;
import com.comviva.api.j4u.config.PropertiesLoader;
import com.comviva.api.j4u.dao.LookUpDAO;
import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.RAGAndSAGUserInfo;
import com.comviva.api.j4u.model.UserInfo;
import com.comviva.api.j4u.service.publisher.J4UOfferEventPublisher;
import com.comviva.api.j4u.utils.J4UOfferConstants;
import com.comviva.api.j4u.utils.J4UOfferStatusCode;
import com.comviva.api.j4u.utils.RAGpublicsCache;

public class RAGOfferService {

	private static final Logger LOG = Logger.getLogger(RAGOfferService.class);

	private LookUpDAO lookUpDAO;
	private UpdaterDAO updaterDAO;
	private UserInfo userInfo;
	private RAGAndSAGUserInfo ragandsagUserInfo;
	private static Map<String, String> ragAtlRewardCodeTargetMap;
	private SecureRandom random;
	private static final String ERROR_IN_SYNC_OPERATION= "Error in sync operation : ";

	public RAGOfferService() {
		try {
			userInfo = new UserInfo();
			ragandsagUserInfo = new RAGAndSAGUserInfo();
			lookUpDAO = new LookUpDAO();
			updaterDAO = new UpdaterDAO();
			random = new SecureRandom();
		} catch (Exception e) {

			LOG.error("Exception :: " + e.getMessage(), e);
		}
	}

	static {
		ragAtlRewardCodeTargetMap = getRagAtlRewardMap();
	}

	private static Map<String, String> getRagAtlRewardMap() {
		List<String> rewardList;
		try {
			ragAtlRewardCodeTargetMap = new HashMap<String, String>();
			rewardList = Arrays.asList(PropertiesLoader.getValue(USSD_RAG_ATL_REWARD_MAP).split(","));
			for (String key : rewardList) {
				String rewardMapping[] = key.split(":");
				ragAtlRewardCodeTargetMap.put(rewardMapping[0], rewardMapping[1]);
			}
		} catch (Exception e) {
			LOG.error("Error occured at ==> ", e);
		}
		return ragAtlRewardCodeTargetMap;
	}

	public JSONObject getRagOptIn(JSONObject requestJson)
			throws UnsupportedEncodingException, ConfigException, ParserConfigurationException {
		JSONObject synchResJson = null;
		JSONObject offer = null;
		int statusCode = -1;
		String statusMessage = "";
		String comments = null;
		LOG.info("getragOptIn :: requestJson = " + requestJson);
		String msisdn = requestJson.get(MSISDN).toString();
		try {
			userInfo = lookUpDAO.getUserInfo(msisdn);
			requestJson.put(J4UOfferConstants.LANG_CODE, userInfo.getLangCode());
			if (null != userInfo && userInfo.isJFUEligible() && userInfo.isMlFlag()) {
				if (!userInfo.isRagUser()) {
					insertRagUserRecord(userInfo);
				}
				ragandsagUserInfo = lookUpDAO.getRagUserInfo(requestJson);
				LOG.debug("ragandsagUserInfo.isRagEligibleFlag() => " + ragandsagUserInfo.isRagEligibleFlag());
				LOG.debug("ragandsagUserInfo.isRagOptInFlag() => " + ragandsagUserInfo.isRagOptInFlag());
				LOG.debug("ragandsagUserInfo.isRagGoalReachedFlag() => " + ragandsagUserInfo.isRagGoalReachedFlag());
				if (!ragandsagUserInfo.isRagEligibleFlag() && !ragandsagUserInfo.isRagOptInFlag()
						&& !ragandsagUserInfo.isRagGoalReachedFlag()) {
					LOG.info("Customer is not Eligilbe for RAG Service :");
					statusCode = J4UOfferStatusCode.RAG_NOT_ELIGIBLE.getStatusCode();
					statusMessage = J4UOfferStatusCode.RAG_NOT_ELIGIBLE.getStatusMessage();
				} else if (ragandsagUserInfo.isRagEligibleFlag() && !ragandsagUserInfo.isRagOptInFlag()
						&& !ragandsagUserInfo.isRagGoalReachedFlag()) {
					ragandsagUserInfo.setRagOptInFlag(true);
					updaterDAO.updateRAGOptInfo(ragandsagUserInfo);
					// sendOptInSMSNotification(ragandsagUserInfo);
					statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
					statusMessage = J4UOfferStatusCode.SUCCESS.getStatusMessage();
					offer = getRAGOfferInfo(msisdn, requestJson);
					sendOptInSMSNotification(ragandsagUserInfo);
				} else if (ragandsagUserInfo.isRagEligibleFlag() && ragandsagUserInfo.isRagOptInFlag()
						&& !ragandsagUserInfo.isRagGoalReachedFlag()) {
					statusCode = J4UOfferStatusCode.RAG_ALREADY_OPTED_IN.getStatusCode();
					statusMessage = J4UOfferStatusCode.RAG_ALREADY_OPTED_IN.getStatusMessage();
					offer = getRAGOfferInfo(msisdn, requestJson);
				} else if (userInfo.isRagEligibleFlag() && userInfo.isRagOptInFlag()
						&& userInfo.isRagGoalReachedFlag()) {
					statusCode = J4UOfferStatusCode.RAG_GOAL_REACHED_FLAG.getStatusCode();
					statusMessage = J4UOfferStatusCode.RAG_GOAL_REACHED_FLAG.getStatusMessage();

				}
				synchResJson = processRAGOptInResponseJson(requestJson, statusCode, statusMessage, offer);
			} else {
				statusCode = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusCode();
				statusMessage = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusMessage();
				requestJson.put(J4UOfferConstants.REWARDS, "");
				if (null != userInfo) {
					comments = "J4UEligible/ML Flag may not be valid.";
				} else {
					comments = "User not found";

				}
				synchResJson = processRAGOptInResponseJson(requestJson, statusCode, statusMessage, offer);
				LOG.error(msisdn + " " + comments);
			}

		} catch (

		Exception e) {
			// db exception
			statusCode = J4UOfferStatusCode.GENERAL_FAILURE.getStatusCode();
			statusMessage = J4UOfferStatusCode.GENERAL_FAILURE.getStatusMessage() + " : " + e;
			// synchResJson = processResponseJson(requestJson, statusCode,
			// statusMessage,
			// offers, cellID, poolId);
			LOG.error(ERROR_IN_SYNC_OPERATION , e);
		}

		return synchResJson;

	}

	public Map<String, String> calculateWeekStartAndEndDate() throws ParseException {
		LOG.debug("calculateWeekStartAndEndDate START =>");
		Map<String, String> weekDateMap = new HashMap<>();
		// Get calendar set to current date and time
		Calendar calendar = Calendar.getInstance();

		// Set the calendar to WEDNESDAY of the current week
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);

		// Print dates of the current week starting on WEDNESDAY
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getpublic());
		Date currentdate = new Date();
		Date weekStartingDate = dateFormat.parse(dateFormat.format(calendar.getTime()));
		String weeksStartDate = "", weeksEndDate = "", nextAvailableweek = "";
		weeksStartDate = dateFormat.format(weekStartingDate);

		LOG.debug("current week Start Date = " + weeksStartDate);

		if (currentdate.before(weekStartingDate)) {
			nextAvailableweek = weeksStartDate;
			calendar.add(Calendar.DATE, -7);
			weeksStartDate = dateFormat.format(calendar.getTime());
			calendar.add(Calendar.DATE, 6);
			weeksEndDate = dateFormat.format(calendar.getTime());
		} else {
			calendar.add(Calendar.DATE, 6);
			weeksEndDate = dateFormat.format(calendar.getTime());
			calendar.add(Calendar.DATE, 7);
			nextAvailableweek = dateFormat.format(calendar.getTime());
		}

		LOG.debug("Week Start Date = " + weeksStartDate);
		LOG.debug("Week End Date = " + weeksEndDate);
		LOG.debug("nextAvailableweek Date = " + nextAvailableweek);
		weekDateMap.put("weeksStartDate", weeksStartDate);
		weekDateMap.put("weeksEndDate", weeksEndDate);
		weekDateMap.put("nextAvailableweek", nextAvailableweek);
		LOG.debug("calculateWeekStartAndEndDate END =>");

		return weekDateMap;

	}

	public Map<String, String> getRagUserRecord(RAGAndSAGUserInfo ragMainMenuInfo,
			Map<String, String> ragAtlRewardCodeTargetMap) throws Exception {
		HashMap<String, String> ragUserRecord = null;
		String msisdn = ragMainMenuInfo.getMsisdn();
		if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}
		RAGpublicsCache ragpublicsCache = RAGpublicsCache.instance();
		ragUserRecord = new HashMap<String, String>();
		Object[] rewardKeys = ragAtlRewardCodeTargetMap.keySet().toArray();
		Object rewardKey = null;
		try {
			rewardKey = rewardKeys[random.nextInt(rewardKeys.length)];
		} catch (Exception e) {
			rewardKey = rewardKeys[0];
		}
		LOG.info("KEY :: " + rewardKey);
		ragUserRecord.put(MSISDN, msisdn);
		ragUserRecord.put(WEEK_START_DATE, ragpublicsCache.getWeekStartDate());
		ragUserRecord.put(WEEK_END_DATE, ragpublicsCache.getWeekEndDate());
		ragUserRecord.put(NEXT_AVAILABLE_OFFER_DATE, ragpublicsCache.getNextOfferAvailableDate());
		ragUserRecord.put(REWARD_CODE, rewardKey.toString());
		ragUserRecord.put(TARGET_TYPE, OTHER);
		ragUserRecord.put(PAYMENT_METHOD, VALUE_P);
		ragUserRecord.put(RECHARGE_TARGET, ragAtlRewardCodeTargetMap.get(rewardKey));
		LOG.info("RAG user Record :: " + ragUserRecord);
		return ragUserRecord;
	}

	private JSONObject processRAGOptInResponseJson(JSONObject requestJson, int statusCode, String statusMessage,
			JSONObject offer) {
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put(REF_NUM, requestJson.get(J4UOfferConstants.REF_NUM));
			responseJson.put(TRANSACTION_ID, userInfo.getTxId());
			responseJson.put(MSISDN, requestJson.getString(MSISDN));
			responseJson.put(STATUS_CODE, statusCode);
			responseJson.put(STATUS_MESSAGE, statusMessage);
			responseJson.put(J4UOfferConstants.REWARD, offer);

		} catch (Exception e) {

			LOG.error("exception in json parsing:: ", e);
		}
		LOG.info("Response JSON from J4U service :: " + responseJson);
		return responseJson;
	}

	public JSONObject getRagOptOut(JSONObject requestJson)
			throws UnsupportedEncodingException, ConfigException, ParserConfigurationException {
		JSONObject synchResJson = null;
		int statusCode = -1;
		String statusMessage = "";
		RAGAndSAGUserInfo ragAndSAGUserInfo = new RAGAndSAGUserInfo();
		LOG.info("getragOptOut :: requestJson = " + requestJson);
		String msisdn = requestJson.get(MSISDN).toString();
		try {
			userInfo = lookUpDAO.getUserInfo(msisdn);
			LOG.info("USERINF :: " + (null != userInfo && userInfo.isJFUEligible() && userInfo.isMlFlag()));
			int langCode = (null != userInfo) ? userInfo.getLangCode() : 0;
			LOG.info("langauge Code " + langCode);
			requestJson.put(LANG_CODE, langCode);
			if (null != userInfo && userInfo.isJFUEligible() && userInfo.isMlFlag()) {
				String ragOptFalg = lookUpDAO.getRAGOptinStatus(msisdn);
				LOG.info("ragOptInfo" + ragOptFalg);
				if (ragOptFalg.equalsIgnoreCase(J4UOfferConstants.FLAG_Y)) {
					ragAndSAGUserInfo = lookUpDAO.getRagUserInfo(requestJson);
					LOG.info("rag user info :: " + userInfo.isRagNeverOptInFlag());
					ragAndSAGUserInfo.setRagOptInFlag(false);
					LOG.info("ragoptFlag " + ragAndSAGUserInfo.isRagOptInFlag());
					updaterDAO.updateRAGOptInfo(ragAndSAGUserInfo);
					LOG.info("update done sucessfully");
					statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
					statusMessage = J4UOfferStatusCode.SUCCESS.getStatusMessage();
					sendOptInSMSNotification(ragAndSAGUserInfo);
					LOG.info("sent sms notification");
					synchResJson = processRagOptOut(requestJson, statusCode, statusMessage);

				} else {
					statusCode = J4UOfferStatusCode.RAG_NOT_ALREADY_OPTED_IN.getStatusCode();
					statusMessage = J4UOfferStatusCode.RAG_NOT_ALREADY_OPTED_IN.getStatusMessage();
					synchResJson = processRagOptOut(requestJson, statusCode, statusMessage);

				}
			} else {
				statusCode = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusCode();
				statusMessage = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusMessage();

				String comments = null;
				if (null != userInfo) {
					comments = "J4UEligible/ML Flag may not be valid.";

				} else {
					comments = "User not found";
					LOG.error(msisdn + " " + comments);
				}

			}
		} catch (Exception e) {
			// db exception
			statusCode = J4UOfferStatusCode.GENERAL_FAILURE.getStatusCode();
			statusMessage = J4UOfferStatusCode.GENERAL_FAILURE.getStatusMessage() + " : " + e;
			// synchResJson = processResponseJson(requestJson, statusCode,
			// statusMessage,
			// offers, cellID, poolId);
			LOG.error(ERROR_IN_SYNC_OPERATION , e);
		}

		return synchResJson;

	}

	private JSONObject processRagOptOut(JSONObject requestJson, int statusCode, String statusMessage) {
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put(REF_NUM, requestJson.getString(REF_NUM));
			responseJson.put(TRANSACTION_ID, requestJson.getString(TRANSACTION_ID));
			responseJson.put(STATUS_CODE, statusCode);
			responseJson.put(STATUS_MESSAGE, statusMessage);
			responseJson.put(MSISDN, requestJson.getLong(MSISDN));

		} catch (Exception e) {

			LOG.error("exception in json parsing:: ", e);
		}
		LOG.info("Response JSON from J4U service :: " + responseJson);
		return responseJson;
	}

	public JSONObject RagGetOfferInfo(JSONObject requestJson)
			throws UnsupportedEncodingException, ConfigException, ParserConfigurationException {
		JSONObject offers = null;
		JSONObject synchResJson;
		int statusCode = -1;
		String statusMessage = "";
		LOG.info("getSynchOffer :: requestJson = " + requestJson);
		String msisdn = requestJson.get(MSISDN).toString();

		try {
			userInfo = lookUpDAO.getUserInfo(msisdn);
			int langCode = (null != userInfo) ? userInfo.getLangCode() : 0;
			requestJson.put(LANG_CODE, langCode);
			if (null != userInfo && userInfo.isJFUEligible() && userInfo.isMlFlag()) {
				String ragOptFalg = lookUpDAO.getRAGOptinStatus(msisdn);
				if (ragOptFalg.equalsIgnoreCase(J4UOfferConstants.FLAG_Y)) {
					statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
					statusMessage = J4UOfferStatusCode.SUCCESS.getStatusMessage();
					offers = getRAGOfferInfo(msisdn, requestJson);
					LOG.info("RAG offer Info details :" + offers);
					synchResJson = setRAGGetOfferResponseJSON(offers, requestJson, msisdn);
				} else {
					statusCode = J4UOfferStatusCode.RAG_NOT_ALREADY_OPTED_IN.getStatusCode();
					statusMessage = J4UOfferStatusCode.RAG_NOT_ALREADY_OPTED_IN.getStatusMessage();
					String comments = "RAG OptIn is not done by user";
					synchResJson = processRagGetOfferResponseJson(requestJson, statusCode, statusMessage, offers);
					LOG.error(msisdn + " " + comments);
				}

			} else {
				statusCode = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusCode();
				statusMessage = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusMessage();

				String comments = null;
				if (null != userInfo) {
					comments = "J4UEligible/ML Flag may not be valid.";
				} else {
					comments = "User not found";
				}
				synchResJson = processRagGetOfferResponseJson(requestJson, statusCode, statusMessage, offers);
				LOG.error(msisdn + " " + comments);
			}

		}

		catch (Exception e) {
			// db exception
			statusCode = J4UOfferStatusCode.GENERAL_FAILURE.getStatusCode();
			statusMessage = J4UOfferStatusCode.GENERAL_FAILURE.getStatusMessage() + " : " + e;
			synchResJson = processRagGetOfferResponseJson(requestJson, statusCode, statusMessage, offers);
			LOG.error(ERROR_IN_SYNC_OPERATION , e);
		}

		return synchResJson;

	}

	private JSONObject setRAGGetOfferResponseJSON(JSONObject offers, JSONObject requestJson, String msisdn) {
		JSONObject synchResJson;
		int statusCode = -1;
		String statusMessage = "";

		if (offers != null) {

			statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
			statusMessage = J4UOfferStatusCode.SUCCESS.getStatusMessage();
			synchResJson = processRagGetOfferResponseJson(requestJson, statusCode, statusMessage, offers);
			LOG.debug("RAG Offer(s) found for user :" + msisdn);

		} else {

			// if offer does exist for user
			// send error status, so Asynch method will be called
			statusCode = J4UOfferStatusCode.NO_OFFERS.getStatusCode();
			statusMessage = J4UOfferStatusCode.NO_OFFERS.getStatusMessage();
			LOG.info("no offers statusCode ::" + statusCode);
			LOG.info("no offers statusMessage ::" + statusMessage);
			synchResJson = processRagGetOfferResponseJson(requestJson, statusCode, statusMessage, offers);
			LOG.info("No RAG offer Info are present  for msisdn: " + msisdn);
		}

		return synchResJson;

	}

	private JSONObject getRAGOfferInfo(String msisdn, JSONObject requestJson) throws Exception {
		LOG.info("getRAGOfferInfo called ::");
		JSONObject offerJson = new JSONObject();
		RAGAndSAGUserInfo ragAndSAGUserInfo = new RAGAndSAGUserInfo();
		ragAndSAGUserInfo = lookUpDAO.getRagUserInfo(requestJson);
		if (ragandsagUserInfo != null) {

			offerJson.put(VALUE, ragAndSAGUserInfo.getRagInfo().get(PRODUCT_VALUE));
			offerJson.put(VALIDITY, ragAndSAGUserInfo.getRagInfo().get("PRODUCT_VALIDITY").toString());
			offerJson.put("RechargeTarget", ragAndSAGUserInfo.getRagInfo().get(RECHARGE_TARGET).toString());
			offerJson.put(WEEK_END_DATE, ragAndSAGUserInfo.getRagInfo().get(WEEK_END_DATE));
			offerJson.put("RemainingEffort", ragAndSAGUserInfo.getRagInfo().get(REMAINING_EFFORT).toString());

		}
		LOG.info("OFFER INFO :: " + offerJson);
		LOG.info("getRAGOfferInfo END ::");
		return offerJson;

	}

	private JSONObject processRagGetOfferResponseJson(JSONObject requestJson, int statusCode, String statusMessage,
			JSONObject offers) {
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put(REF_NUM, requestJson.getString(REF_NUM));
			responseJson.put(TRANSACTION_ID, requestJson.getString(TRANSACTION_ID));
			responseJson.put(STATUS_CODE, statusCode);
			responseJson.put(STATUS_MESSAGE, statusMessage);
			responseJson.put(MSISDN, requestJson.getLong(MSISDN));
			responseJson.put(REWARDS, offers);
		} catch (Exception e) {

			LOG.error("exception in json parsing:: ", e);
		}
		LOG.info("Response JSON from J4U service :: " + responseJson);
		return responseJson;

	}

	private void sendOptInSMSNotification(RAGAndSAGUserInfo userInfoOptIn) throws Exception {
		JSONObject jsonObjectOptIn = new JSONObject();
		jsonObjectOptIn.put(TRANSACTION_ID, userInfoOptIn.getTxId());
		jsonObjectOptIn.put(MSISDN, userInfoOptIn.getMsisdn());
		jsonObjectOptIn.put(LANG_CODE, String.valueOf(userInfoOptIn.getLangCode()));
		jsonObjectOptIn.put(J4UOfferConstants.MSG_CODE,
				PropertiesLoader.getValue(J4UOfferConstants.SMS_OPT_IN_TEMPLATE));

		jsonObjectOptIn.put("PRODUCT_VALUE", userInfoOptIn.getRagInfo().get("PRODUCT_VALUE"));
		jsonObjectOptIn.put("RECHARGE_TARGET", userInfoOptIn.getRagInfo().get("RECHARGE_TARGET"));
		jsonObjectOptIn.put("WEEK_END_DATE", userInfoOptIn.getRagInfo().get("WEEK_END_DATE"));

		jsonObjectOptIn.put("PRODUCT_VALIDITY", userInfoOptIn.getRagInfo().get("PRODUCT_VALIDITY"));
		jsonObjectOptIn.put("REWARD_CODE", userInfoOptIn.getRagInfo().get("REWARD_CODE"));
		jsonObjectOptIn.put("REWARD_INFO", userInfoOptIn.getRagInfo().get("REWARD_INFO"));

		jsonObjectOptIn.put("NEXT_AVAILABLE_OFFER_DATE", userInfoOptIn.getRagInfo().get("NEXT_AVAILABLE_OFFER_DATE"));
		jsonObjectOptIn.put("REMAINING_EFFORT", userInfoOptIn.getRagInfo().get("REMAINING_EFFORT"));
		jsonObjectOptIn.put("LAST_RECHARGE_TIME", userInfoOptIn.getRagInfo().get("LAST_RECHARGE_TIME"));
		LOG.info(jsonObjectOptIn.get(TRANSACTION_ID));

		publishToTopic(PropertiesLoader.getValue(J4UOfferConstants.SMS_TOPIC_NAME), jsonObjectOptIn.toString());
	}

	private void publishToTopic(String topicName, String message) throws Exception {
		J4UOfferEventPublisher eventPublisher = new J4UOfferEventPublisher();
		eventPublisher.addEvent(topicName, message);
	}

	public void insertRagUserRecord(UserInfo ragMainMenuInfo) {
		LOG.debug("insertRagUserRecord - START");
		Map<String, String> ragUserRecord = null;
		try {
			String msisdn = ragMainMenuInfo.getMsisdn();
			ragUserRecord = getRagUserRecord(ragMainMenuInfo, ragAtlRewardCodeTargetMap);
			LOG.debug("calling calculateWeekStartAndEndDate =>");
			Map<String, String> weekDateMap = this.calculateWeekStartAndEndDate();
			ragUserRecord.put(WEEK_START_DATE, weekDateMap.get("weeksStartDate"));
			ragUserRecord.put(WEEK_END_DATE, weekDateMap.get("weeksEndDate"));
			ragUserRecord.put(NEXT_AVAILABLE_OFFER_DATE, weekDateMap.get("nextAvailableweek"));

			updaterDAO.insertRagUserRecordDB(ragUserRecord);
			updaterDAO.insertRagUserCatFile(ragUserRecord, msisdn);
		} catch (Exception e) {
			LOG.error("Exception while insertRagUserRecord - ", e);
		}

	}

	public Map<String, String> getRagUserRecord(UserInfo ragMainMenuInfo, Map<String, String> ragAtlRewardCodeTargetMap)
			throws Exception {
		HashMap<String, String> ragUserRecord = null;
		String msisdn = ragMainMenuInfo.getMsisdn();
		if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}
		RAGpublicsCache ragpublicsCache = RAGpublicsCache.instance();
		ragUserRecord = new HashMap<String, String>();
		Object[] rewardKeys = ragAtlRewardCodeTargetMap.keySet().toArray();
		Object rewardKey = null;
		try {
			rewardKey = rewardKeys[random.nextInt(rewardKeys.length)];
		} catch (Exception e) {
			rewardKey = rewardKeys[0];
		}
		ragUserRecord.put(MSISDN, msisdn);
		ragUserRecord.put(WEEK_START_DATE, ragpublicsCache.getWeekStartDate());
		ragUserRecord.put(WEEK_END_DATE, ragpublicsCache.getWeekEndDate());
		ragUserRecord.put(NEXT_AVAILABLE_OFFER_DATE, ragpublicsCache.getNextOfferAvailableDate());
		ragUserRecord.put(REWARD_CODE, rewardKey.toString());
		ragUserRecord.put(TARGET_TYPE, OTHER);
		ragUserRecord.put(PAYMENT_METHOD, VALUE_P);
		ragUserRecord.put(RECHARGE_TARGET, ragAtlRewardCodeTargetMap.get(rewardKey));

		return ragUserRecord;
	}

}

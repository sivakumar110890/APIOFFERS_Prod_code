package com.comviva.api.j4u.service;

import static com.comviva.api.j4u.utils.J4UOfferConstants.LANG_CODE;

import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.NEXT_AVAILABLE_OFFER_DATE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OTHER;
import static com.comviva.api.j4u.utils.J4UOfferConstants.PAYMENT_METHOD;
import static com.comviva.api.j4u.utils.J4UOfferConstants.PRODUCT_VALUE;
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
import static com.comviva.api.j4u.utils.J4UOfferConstants.USSD_SAG_ATL_REWARD_MAP;
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
import com.comviva.api.j4u.utils.SAGpublicsCache;

public class SAGOfferService {

	private static final Logger LOG = Logger.getLogger(RAGOfferService.class);

	private LookUpDAO lookUpDAO;
	private UpdaterDAO updaterDAO;
	private UserInfo userInfo;
	private RAGAndSAGUserInfo ragandsagUserInfo;
	private static Map<String, String> sagAtlRewardCodeTargetMap;
	private SecureRandom random;

	public SAGOfferService() {
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
		sagAtlRewardCodeTargetMap = getSagAtlRewardMap();
	}

	private static Map<String, String> getSagAtlRewardMap() {
		List<String> rewardList;
		try {
			sagAtlRewardCodeTargetMap = new HashMap<String, String>();
			rewardList = Arrays.asList(PropertiesLoader.getValue(USSD_SAG_ATL_REWARD_MAP).split(","));
			for (String key : rewardList) {
				String rewardMapping[] = key.split(":");
				sagAtlRewardCodeTargetMap.put(rewardMapping[0], rewardMapping[1]);
			}
		} catch (Exception e) {
			LOG.error("Error occured at ==> ", e);
		}
		return sagAtlRewardCodeTargetMap;
	}

	public JSONObject getSagOptIn(JSONObject requestJson)
			throws UnsupportedEncodingException, ConfigException, ParserConfigurationException {
		JSONObject synchResJson = null;
		JSONObject offer = null;
		int statusCode = -1;
		String statusMessage = "";
		String comments = null;
		LOG.info("getSagOptIn :: requestJson = " + requestJson);
		String msisdn = requestJson.get(MSISDN).toString();
		try {
			userInfo = lookUpDAO.getUserInfo(msisdn);
			requestJson.put(J4UOfferConstants.LANG_CODE, userInfo.getLangCode());
			if (null != userInfo && userInfo.isJFUEligible() && userInfo.isMlFlag()) {
				if (!userInfo.isRagUser()) {
					insertSagUserRecord(userInfo);
				}
				ragandsagUserInfo = lookUpDAO.getSagUserInfo(requestJson);
				LOG.debug("SagandsagUserInfo.isSagEligibleFlag() => " + ragandsagUserInfo.isSagEligibleFlag());
				LOG.debug("SagandsagUserInfo.isSagOptInFlag() => " + ragandsagUserInfo.isSagOptInFlag());
				LOG.debug("SagandsagUserInfo.isSagGoalReachedFlag() => " + ragandsagUserInfo.isSagGoalReachedFlag());
				if (!ragandsagUserInfo.isSagEligibleFlag() && !ragandsagUserInfo.isSagOptInFlag()
						&& !ragandsagUserInfo.isSagGoalReachedFlag()) {
					LOG.info("Customer is not Eligilbe for RAG Service :");
					statusCode = J4UOfferStatusCode.SAG_NOT_ELIGIBLE.getStatusCode();
					statusMessage = J4UOfferStatusCode.SAG_NOT_ELIGIBLE.getStatusMessage();
				} else if (ragandsagUserInfo.isSagEligibleFlag() && !ragandsagUserInfo.isSagOptInFlag()
						&& !ragandsagUserInfo.isSagGoalReachedFlag()) {
					ragandsagUserInfo.setSagOptInFlag(true);
					updaterDAO.updateSAGOptInfo(ragandsagUserInfo);
					// sendOptInSMSNotification(ragandsagUserInfo);
					statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
					statusMessage = J4UOfferStatusCode.SUCCESS.getStatusMessage();
					offer = getSAGOfferInfo(userInfo, msisdn, requestJson);
					sendOptInSMSNotification(ragandsagUserInfo);
				} else if (ragandsagUserInfo.isSagEligibleFlag() && ragandsagUserInfo.isSagOptInFlag()
						&& !ragandsagUserInfo.isSagGoalReachedFlag()) {
					statusCode = J4UOfferStatusCode.SAG_ALREADY_OPTED_IN.getStatusCode();
					statusMessage = J4UOfferStatusCode.SAG_ALREADY_OPTED_IN.getStatusMessage();
					offer = getSAGOfferInfo(userInfo, msisdn, requestJson);
				} else if (userInfo.isSagEligibleFlag() && userInfo.isSagOptInFlag()
						&& userInfo.isSagGoalReachedFlag()) {
					statusCode = J4UOfferStatusCode.SAG_GOAL_REACHED_FLAG.getStatusCode();
					statusMessage = J4UOfferStatusCode.SAG_GOAL_REACHED_FLAG.getStatusMessage();

				}
				synchResJson = processSAGOptInResponseJson(ragandsagUserInfo, statusCode, statusMessage, offer);
			} else {
				statusCode = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusCode();
				statusMessage = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusMessage();
				requestJson.put(J4UOfferConstants.REWARDS, "");
				if (null != userInfo) {
					comments = "J4UEligible/ML Flag may not be valid.";
				} else {
					comments = "User not found";

				}
				synchResJson = processSAGOptInResponseJson(ragandsagUserInfo, statusCode, statusMessage, offer);
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
			LOG.error("Error in sync operation : " + e);
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

	public Map<String, String> getSagUserRecord(RAGAndSAGUserInfo sagMainMenuInfo,
			Map<String, String> raSAtlRewardCodeTargetMap) throws Exception {
		HashMap<String, String> sagUserRecord = null;
		String msisdn = sagMainMenuInfo.getMsisdn();
		if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}
		SAGpublicsCache sagpublicsCache = SAGpublicsCache.instance();
		sagUserRecord = new HashMap<String, String>();
		Object[] rewardKeys = sagAtlRewardCodeTargetMap.keySet().toArray();
		Object rewardKey = null;
		try {
			rewardKey = rewardKeys[random.nextInt(rewardKeys.length)];
		} catch (Exception e) {
			rewardKey = rewardKeys[0];
		}
		LOG.info("KEY :: " + rewardKey);
		sagUserRecord.put(MSISDN, msisdn);
		sagUserRecord.put(WEEK_START_DATE, sagpublicsCache.getWeekStartDate());
		sagUserRecord.put(WEEK_END_DATE, sagpublicsCache.getWeekEndDate());
		sagUserRecord.put(NEXT_AVAILABLE_OFFER_DATE, sagpublicsCache.getNextOfferAvailableDate());
		sagUserRecord.put(REWARD_CODE, rewardKey.toString());
		sagUserRecord.put(TARGET_TYPE, OTHER);
		sagUserRecord.put(PAYMENT_METHOD, VALUE_P);
		sagUserRecord.put("SPEND_TARGET", sagAtlRewardCodeTargetMap.get(rewardKey));
		LOG.info("SAG user Record :: " + sagUserRecord);
		return sagUserRecord;
	}

	private JSONObject processSAGOptInResponseJson(RAGAndSAGUserInfo userInfo, int statusCode, String statusMessage,
			JSONObject offer) {
		JSONObject responseJson = new JSONObject();
		try {

			responseJson.put(TRANSACTION_ID, userInfo.getTxId());
			responseJson.put(STATUS_CODE, statusCode);
			responseJson.put(STATUS_MESSAGE, statusMessage);
			responseJson.put(MSISDN, userInfo.getMsisdn());
			responseJson.put(J4UOfferConstants.REWARD, offer);

		} catch (Exception e) {

			LOG.error("exception in json parsing:: ", e);
		}
		LOG.info("Response JSON from J4U service :: " + responseJson);
		return responseJson;
	}

	public JSONObject getSagOptOut(JSONObject requestJson)
			throws UnsupportedEncodingException, ConfigException, ParserConfigurationException {
		JSONObject synchResJson = null;
		int statusCode = -1;
		String statusMessage = "";
		LOG.info("getSagOptOut :: requestJson = " + requestJson);
		String msisdn = requestJson.get(MSISDN).toString();
		try {
			userInfo = lookUpDAO.getUserInfo(msisdn);
			int langCode = (null != userInfo) ? userInfo.getLangCode() : 0;
			requestJson.put(LANG_CODE, langCode);
			ragandsagUserInfo = lookUpDAO.getSagUserInfo(requestJson);

			if (null != userInfo && userInfo.isJFUEligible() && userInfo.isMlFlag()) {
				String sagOptFalg = lookUpDAO.getSAGOptinStatus(msisdn);
				if (sagOptFalg.equalsIgnoreCase("FLAG_Y")) {
					ragandsagUserInfo.setSagOptInFlag(false);
					updaterDAO.updateRAGOptInfo(ragandsagUserInfo);
					statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
					statusMessage = J4UOfferStatusCode.SUCCESS.getStatusMessage();
					sendOptInSMSNotification(ragandsagUserInfo);
					synchResJson = processSagOptOut(requestJson, statusCode, statusMessage);

				} else {
					statusCode = J4UOfferStatusCode.SAG_NOT_ALREADY_OPTED_IN.getStatusCode();
					statusMessage = J4UOfferStatusCode.SAG_NOT_ALREADY_OPTED_IN.getStatusMessage();
					synchResJson = processSagOptOut(requestJson, statusCode, statusMessage);

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
			synchResJson = processSagOptOut(requestJson, statusCode, statusMessage);
			LOG.error("Error in sync operation : " + e);
		}

		return synchResJson;

	}

	private JSONObject processSagOptOut(JSONObject requestJson, int statusCode, String statusMessage) {
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

	public JSONObject SagGetOfferInfo(JSONObject requestJson)
			throws UnsupportedEncodingException, ConfigException, ParserConfigurationException {
		JSONObject offers = null;
		JSONObject synchResJson;
		int statusCode = -1;
		String statusMessage = "";
		LOG.info("getSynchOffer :: requestJson = " + requestJson);
		String msisdn = requestJson.get(MSISDN).toString();

		try {
			userInfo = lookUpDAO.getUserInfo(msisdn);
			if (null != userInfo && userInfo.isJFUEligible() && userInfo.isMlFlag()) {
				String sagOptFalg = lookUpDAO.getSAGOptinStatus(msisdn);
				if (sagOptFalg.equalsIgnoreCase("FLAG_Y")) {
					statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
					statusMessage = J4UOfferStatusCode.SUCCESS.getStatusMessage();
					offers = getSAGOfferInfo(userInfo, msisdn, requestJson);
					LOG.info("RAG offer Info details :" + offers);
					synchResJson = setSAGGetOfferResponseJSON(offers, requestJson, msisdn);
				} else {
					statusCode = J4UOfferStatusCode.SAG_NOT_ALREADY_OPTED_IN.getStatusCode();
					statusMessage = J4UOfferStatusCode.SAG_NOT_ALREADY_OPTED_IN.getStatusMessage();
					String comments = "SAG OptIn is not done by user";
					synchResJson = processSagGetOfferResponseJson(requestJson, statusCode, statusMessage, offers);
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
				synchResJson = processSagGetOfferResponseJson(requestJson, statusCode, statusMessage, offers);
				LOG.error(msisdn + " " + comments);
			}

		}

		catch (Exception e) {
			// db exception
			statusCode = J4UOfferStatusCode.GENERAL_FAILURE.getStatusCode();
			statusMessage = J4UOfferStatusCode.GENERAL_FAILURE.getStatusMessage() + " : " + e;
			synchResJson = processSagGetOfferResponseJson(requestJson, statusCode, statusMessage, offers);
			LOG.error("Error in sync operation : " + e);
		}

		return synchResJson;

	}

	private JSONObject setSAGGetOfferResponseJSON(JSONObject offers, JSONObject requestJson, String msisdn) {
		JSONObject synchResJson;
		int statusCode = -1;
		String statusMessage = "";

		if (offers != null) {

			statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
			statusMessage = J4UOfferStatusCode.SUCCESS.getStatusMessage();
			synchResJson = processSagGetOfferResponseJson(requestJson, statusCode, statusMessage, offers);
			LOG.debug("SAG Offer(s) found for user :" + msisdn);

		} else {

			// if offer does exist for user
			// send error status, so Asynch method will be called
			statusCode = J4UOfferStatusCode.NO_OFFERS.getStatusCode();
			statusMessage = J4UOfferStatusCode.NO_OFFERS.getStatusMessage();
			LOG.info("no offers statusCode ::" + statusCode);
			LOG.info("no offers statusMessage ::" + statusMessage);
			synchResJson = processSagGetOfferResponseJson(requestJson, statusCode, statusMessage, offers);
			LOG.info("No SAG offer Info are present  for msisdn: " + msisdn);
		}

		return synchResJson;

	}

	private JSONObject getSAGOfferInfo(UserInfo userInfo, String msisdn, JSONObject requestJson) throws Exception {
		LOG.info("getSAGOfferInfo called ::");
		JSONObject offerJson = new JSONObject();
		ragandsagUserInfo = lookUpDAO.getSagUserInfo(requestJson);
		if (ragandsagUserInfo != null) {

			offerJson.put(VALUE, userInfo.getRagInfo().get(PRODUCT_VALUE));
			offerJson.put(VALIDITY, userInfo.getRagInfo().get(VALIDITY));
			offerJson.put("SPEND_TARGET", userInfo.getRagInfo().get("SPEND_TARGET"));
			offerJson.put(WEEK_END_DATE, userInfo.getRagInfo().get(WEEK_END_DATE));
			offerJson.put(REMAINING_EFFORT, userInfo.getRagInfo().get(REMAINING_EFFORT));

		}
		LOG.info("getRAGOfferInfo END ::");
		return offerJson;

	}

	private JSONObject processSagGetOfferResponseJson(JSONObject requestJson, int statusCode, String statusMessage,
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

		jsonObjectOptIn.put("PRODUCT_VALUE", userInfoOptIn.getSagInfo().get("PRODUCT_VALUE"));
		jsonObjectOptIn.put("SPEND_TARGET", userInfoOptIn.getSagInfo().get("SPEND_TARGET"));
		jsonObjectOptIn.put("WEEK_END_DATE", userInfoOptIn.getSagInfo().get("WEEK_END_DATE"));

		jsonObjectOptIn.put("PRODUCT_VALIDITY", userInfoOptIn.getSagInfo().get("PRODUCT_VALIDITY"));
		jsonObjectOptIn.put("REWARD_CODE", userInfoOptIn.getSagInfo().get("REWARD_CODE"));
		jsonObjectOptIn.put("REWARD_INFO", userInfoOptIn.getSagInfo().get("REWARD_INFO"));

		jsonObjectOptIn.put("NEXT_AVAILABLE_OFFER_DATE", userInfoOptIn.getSagInfo().get("NEXT_AVAILABLE_OFFER_DATE"));
		jsonObjectOptIn.put("REMAINING_EFFORT", userInfoOptIn.getSagInfo().get("REMAINING_EFFORT"));
		jsonObjectOptIn.put("LAST_SPEND_TIME", userInfoOptIn.getSagInfo().get("LAST_SPEND_TIME"));
		LOG.info(jsonObjectOptIn.get(TRANSACTION_ID));

		publishToTopic(PropertiesLoader.getValue(J4UOfferConstants.SMS_TOPIC_NAME), jsonObjectOptIn.toString());
	}

	private void publishToTopic(String topicName, String message) throws Exception {
		J4UOfferEventPublisher eventPublisher = new J4UOfferEventPublisher();
		eventPublisher.addEvent(topicName, message);
	}

	public void insertSagUserRecord(UserInfo sagMainMenuInfo) {
		LOG.debug("insertSagUserRecord - START");
		Map<String, String> sagUserRecord = null;
		try {
			String msisdn = sagMainMenuInfo.getMsisdn();
			sagUserRecord = getSagUserRecord(sagMainMenuInfo, sagAtlRewardCodeTargetMap);
			LOG.debug("calling calculateWeekStartAndEndDate =>");
			Map<String, String> weekDateMap = this.calculateWeekStartAndEndDate();
			sagUserRecord.put(WEEK_START_DATE, weekDateMap.get("weeksStartDate"));
			sagUserRecord.put(WEEK_END_DATE, weekDateMap.get("weeksEndDate"));
			sagUserRecord.put(NEXT_AVAILABLE_OFFER_DATE, weekDateMap.get("nextAvailableweek"));

			updaterDAO.insertSagUserRecordDB(sagUserRecord);
			updaterDAO.insertSagUserCatFile(sagUserRecord, msisdn);
		} catch (Exception e) {
			LOG.error("Exception while insertRagUserRecord - ", e);
		}

	}

	public Map<String, String> getSagUserRecord(UserInfo sagMainMenuInfo, Map<String, String> sagAtlRewardCodeTargetMap)
			throws Exception {
		HashMap<String, String> sagUserRecord = null;
		String msisdn = sagMainMenuInfo.getMsisdn();
		if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}
		SAGpublicsCache sagpublicsCache = SAGpublicsCache.instance();
		sagUserRecord = new HashMap<String, String>();
		Object[] rewardKeys = sagAtlRewardCodeTargetMap.keySet().toArray();
		Object rewardKey = null;
		try {
			rewardKey = rewardKeys[random.nextInt(rewardKeys.length)];
		} catch (Exception e) {
			rewardKey = rewardKeys[0];
		}
		sagUserRecord.put(MSISDN, msisdn);
		sagUserRecord.put(WEEK_START_DATE, sagpublicsCache.getWeekStartDate());
		sagUserRecord.put(WEEK_END_DATE, sagpublicsCache.getWeekEndDate());
		sagUserRecord.put(NEXT_AVAILABLE_OFFER_DATE, sagpublicsCache.getNextOfferAvailableDate());
		sagUserRecord.put(REWARD_CODE, rewardKey.toString());
		sagUserRecord.put(TARGET_TYPE, OTHER);
		sagUserRecord.put(PAYMENT_METHOD, VALUE_P);
		sagUserRecord.put("SPEND_TARGET", sagAtlRewardCodeTargetMap.get(rewardKey));

		return sagUserRecord;
	}

}

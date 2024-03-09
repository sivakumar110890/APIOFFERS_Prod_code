package com.comviva.api.j4u.service;

import com.comviva.api.j4u.config.PropertiesLoader;

import com.comviva.api.j4u.dao.LookUpDAO;
import com.comviva.api.j4u.dao.PEDLookUPDAO;
import com.comviva.api.j4u.dao.PEDUpdateDAO;
import com.comviva.api.j4u.model.PrizeLibrary;
import com.comviva.api.j4u.model.UserInfo;
import com.comviva.api.j4u.service.publisher.J4UOfferEventPublisher;
import com.comviva.api.j4u.utils.J4UOfferConstants;
import com.comviva.api.j4u.utils.J4UOfferStatusCode;
import com.comviva.api.j4u.utils.PEDCONSTANT;
import com.comviva.api.j4u.utils.PEDRandomPrizesCache;
import org.json.JSONArray;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.voltdb.client.ProcCallException;
import java.io.IOException;
import java.security.SecureRandom;
import static com.comviva.api.j4u.utils.J4UOfferConstants.FLAG_Y;
import static com.comviva.api.j4u.utils.J4UOfferConstants.LANG_CODE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.ML_FLAG;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OP_REWARDSPUB_TOPIC_NAME;
import static com.comviva.api.j4u.utils.J4UOfferConstants.PRODUCT_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.PRODUCT_PRICE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.PROG_SHORT_CD;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_NUM;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REWARDSPUB_TOPIC_NAME;
import static com.comviva.api.j4u.utils.J4UOfferConstants.SEL_PROD_TYPE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_CODE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.STATUS_MESSAGE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.SUBID_LENGTH;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TRANSACTION_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CS_FLAG;
import static com.comviva.api.j4u.utils.PEDCONSTANT.AVAILABLEPLAYS;

/**
 * Implementation of PED module database service. Business logic to handle the
 * all PED process.
 * 
 * @author saloni.gupta
 *
 */
public class PEDProcessService implements IPEDProcessService {
	private static final Logger LOGGER = Logger.getLogger(PEDProcessService.class);
	private PEDLookUPDAO pedLookUpDao;
	private PEDUpdateDAO pedUpdateDao;
	private LookUpDAO lookUpDAO;
	private SecureRandom random;
	private UserInfo userInfo;

	public PEDProcessService() throws Exception {
		pedLookUpDao = new PEDLookUPDAO();
		pedUpdateDao = new PEDUpdateDAO();
		lookUpDAO = new LookUpDAO();
		userInfo = new UserInfo();
		random = new SecureRandom();
	}

	@Override
	public int getAvailablePlayCount(String msisdn) throws Exception {
		LOGGER.debug("getAvailablePlayCount=> ");
		// get the play expiary count
		// call the global variable PLAY_EXPIRY_DAYS’
		int playCount = 0;
		if (getPlayExpiryDays() > 0) {
			playCount = pedLookUpDao.getAvailablePlaysHistory(msisdn) + pedLookUpDao.getAvailablePlaysDOD1(msisdn);
			LOGGER.info("play count from History + D0D1 ::" + playCount);
		} else {
			playCount = pedLookUpDao.getAvailablePlaysDOD1(msisdn);
			LOGGER.info("play count from D0D1 ::" + playCount);
		}
		int playedCount = pedLookUpDao.getMsisdnPlayedCount(msisdn);
		LOGGER.info("Avaiable plays" + (playCount - playedCount));
		return playCount - playedCount;
	}

	@Override
	public JSONObject getAvailablePlays(JSONObject requestJson) throws Exception {
		int AvailablePlay = 0;
		int statusCode = -1;
		String statusMessage = "";
		JSONObject availablePlayCountResJson;
		String msisdn = requestJson.getString(MSISDN);

		LOGGER.debug("getAvailablePlays=> " + msisdn);
		try {
			userInfo = lookUpDAO.getUserInfo(msisdn);

			if (null != userInfo && userInfo.isJFUEligible() && userInfo.isMlFlag()) {

				if (msisdn.length() != PropertiesLoader.getIntValue(SUBID_LENGTH)) {
					statusCode = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusCode();
					statusMessage = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusMessage();
					availablePlayCountResJson = processResponseJson(requestJson, statusCode, statusMessage);
				} else {
					AvailablePlay = getAvailablePlayCount(msisdn);
					if (AvailablePlay < 0) {
						AvailablePlay = 0;
					}
					LOGGER.info("Available palys ::" + AvailablePlay);
					availablePlayCountResJson = setResponseJSONForGetPlayCount(AvailablePlay, requestJson, msisdn);
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
				availablePlayCountResJson = processResponseJson(requestJson, statusCode, statusMessage);
				LOGGER.error(msisdn + " " + comments);
			}
		} catch (Exception e) {
			// db exception
			statusCode = J4UOfferStatusCode.GENERAL_FAILURE.getStatusCode();
			statusMessage = J4UOfferStatusCode.GENERAL_FAILURE.getStatusMessage() + " : " + e;
			availablePlayCountResJson = processResponseJson(requestJson, statusCode, statusMessage);
			LOGGER.error("Error in sync operation : " + e);
		}
		return availablePlayCountResJson;
	}

	@Override
	public int getPlayExpiryDays() throws Exception {
		LOGGER.debug("getPlayExpiryDays => ");
		return pedLookUpDao.getPedPlaysExpirayDays();
	}

	@Override
	public JSONArray getPrizeHistory1(String msisdn) throws Exception {
		LOGGER.debug("getPrizeHistory=> " + msisdn);
		return pedLookUpDao.getPrizeHistoryAPI(msisdn);
	}

	public JSONObject getPlayHistory(JSONObject requestJson) throws Exception {

		int statusCode = -1;
		String statusMessage = "";
		JSONObject playHistoryResponse;
		JSONArray prizeHistoryArray = null;
		String msisdn = requestJson.getString(J4UOfferConstants.MSISDN);

		try {
			userInfo = lookUpDAO.getUserInfo(msisdn);

			if (null != userInfo && userInfo.isJFUEligible() && userInfo.isMlFlag()) {
				prizeHistoryArray = getPrizeHistory1(msisdn);
				playHistoryResponse = setResponseJSONForPlayHistory(prizeHistoryArray, requestJson, msisdn);
			} else {

				statusCode = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusCode();
				statusMessage = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusMessage();
				requestJson.put(J4UOfferConstants.PLAYHISTORY, "");
				String comments = null;
				if (null != userInfo) {
					comments = "J4UEligible/ML Flag may not be valid.";

				} else {
					comments = "User not found";
				}
				playHistoryResponse = playHistoryProcessResponseJson(requestJson, statusCode, statusMessage);
				LOGGER.error(msisdn + " " + comments);
			}
		} catch (Exception e) {
			// db exception
			statusCode = J4UOfferStatusCode.GENERAL_FAILURE.getStatusCode();
			statusMessage = J4UOfferStatusCode.GENERAL_FAILURE.getStatusMessage() + " : " + e;
			playHistoryResponse = playHistoryProcessResponseJson(requestJson, statusCode, statusMessage);
			LOGGER.error("Error in sync operation : " + e);
		}
		return playHistoryResponse;

	}

	public JSONObject redeemPlay(JSONObject requestJson) throws Exception {
		int statusCode = -1;
		String statusMessage = "";
		JSONObject redeemPlayResponse;
		String prizeID;
		String msisdn = requestJson.getString(J4UOfferConstants.MSISDN);

		String txnId = requestJson.getString(J4UOfferConstants.TRANSACTION_ID);
		try {

			userInfo = lookUpDAO.getUserInfo(msisdn);
			if (null != userInfo && userInfo.isJFUEligible()) {
				int langcode = userInfo.getLangCode();
				LOGGER.info("language code " + langcode);
				prizeID = processPlay(msisdn, txnId, langcode);
				LOGGER.info("PRIZE Id :: " + prizeID);
				redeemPlayResponse = setResponseJSONForPlayRedeemption(prizeID, requestJson, msisdn);
			} else {

				statusCode = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusCode();
				statusMessage = J4UOfferStatusCode.PARTIAL_CONTENT.getStatusMessage();

				String comments = null;
				if (null != userInfo) {
					comments = "J4UEligible/ML Flag may not be valid.";
				} else {
					comments = "User not found";
				}
				requestJson.put(J4UOfferConstants.PRIZE_WON, "");
				redeemPlayResponse = processResponseJsonReedimtion(requestJson, statusCode, statusMessage);
				LOGGER.error(msisdn + " " + comments);
			}

		} catch (

		Exception e) {
			// db exception
			statusCode = J4UOfferStatusCode.GENERAL_FAILURE.getStatusCode();
			statusMessage = J4UOfferStatusCode.GENERAL_FAILURE.getStatusMessage() + " : " + e;
			redeemPlayResponse = processResponseJsonReedimtion(requestJson, statusCode, statusMessage);
			LOGGER.error("Error in sync operation : " + e);
		}
		return redeemPlayResponse;
	}

	@Override
	public String processPlay(String msisdn, String txnId, int langCode) {
		LOGGER.debug("START processPlay=> msisdn => " + msisdn + " txnId=> " + txnId);
		String prizeId = PEDCONSTANT.NO_PRIZE;
		String prizeDescription = "";
		String status = "";
		PrizeLibrary prizeLibrary = null;
		try {
			// Execute Play Routine & determine Prize / no prize
			LOGGER.info("request getAvailablePlays =>");
			int availablePlays = getAvailablePlayCount(msisdn);

			LOGGER.info("verify  availablePlays =>" + availablePlays);
			if (availablePlays > 0) {
				prizeLibrary = executePlayRoutine(langCode);
				if (!PEDCONSTANT.NO_PRIZE.equalsIgnoreCase(prizeLibrary.getPrizeId())) {
					prizeDescription = prizeLibrary.getPrizeDescription();
					prizeId = prizeLibrary.getPrizeId();
					status = "Redeemed";
					// increase prize record count
					increasePrizeRecordCount(prizeLibrary.getPrizeId());
					// record rewarded Play into play history
					recordRewardedPlay(msisdn, prizeLibrary, status, txnId);
					// sent prize
					// provision the reward if prize
					LOGGER.debug("prizeLibrary.getPrizeType()=>" + prizeLibrary.getPrizeType());
					if (PEDCONSTANT.PRIZE_TYPE_GSM.equalsIgnoreCase(prizeLibrary.getPrizeType())) {
						JSONObject message = new JSONObject();
						message.put(MSISDN, msisdn);
						message.put(TRANSACTION_ID, txnId);
						message.put(PRODUCT_ID, prizeLibrary.getPrizeId());
						message.put(PROG_SHORT_CD, "1");
						message.put(LANG_CODE, langCode);
						message.put(ML_FLAG, FLAG_Y);
						message.put(SEL_PROD_TYPE, prizeLibrary.getPrizeType());
						message.put(PRODUCT_PRICE, 0L);
						message.put(PEDCONSTANT.SOURCE, PEDCONSTANT.PED);

						if (PropertiesLoader.getIntValue(CS_FLAG) == 1) {
							String csType = lookUpDAO.getCSType(msisdn);
							if (csType.equalsIgnoreCase(J4UOfferConstants.CS_TYPE_OP)) {
								publishToTopic(PropertiesLoader.getValue(OP_REWARDSPUB_TOPIC_NAME), message.toString());
								LOGGER.info("Published message on Openet OP_rewards topic for Ml user :: " + message);
							} else {
								publishToTopic(PropertiesLoader.getValue(REWARDSPUB_TOPIC_NAME), message.toString());
								LOGGER.info("Published message on rewards topic for Ml user :: " + message);
							}
						} else {
							publishToTopic(PropertiesLoader.getValue(REWARDSPUB_TOPIC_NAME), message.toString());
							LOGGER.info("Published message on rewards topic for Ml user :: " + message);
						}
					}
				}
				// increase played count of msisdn
				increasePlayedCountOfMsisdn(msisdn);
			} else {
				prizeId = PEDCONSTANT.NO_PLAY;
			}
			if ("".equals(prizeDescription)) {
				recordRewardedPlayEcap(msisdn, prizeId, prizeId, txnId);
			} else {
				recordRewardedPlayEcap(msisdn, prizeId, status, txnId);
				prizeId = prizeDescription;
			}

		} catch (Exception e) {
			LOGGER.error("processPlay service error=>", e);
		}
		LOGGER.info("End processPlay => prizeId =>" + prizeId);
		return prizeId;
	}

	/**
	 * Sample play till it gets prize or No prize
	 * 
	 * @return
	 * @throws Exception
	 */
	private PrizeLibrary executePlayRoutine(int langCode) throws Exception {
		LOGGER.debug("executePlayRoutine => ");
		return getRandomPrizeByRange(langCode);
	}

	/**
	 * @param langCode
	 * @return
	 * @throws Exception
	 */
	public PrizeLibrary getRandomPrizeByRange(int langCode) throws Exception {

		int randomNum = random.nextInt(PEDCONSTANT.RANGE);

		LOGGER.debug("getRandomPrizeByRange => randomNum=>" + randomNum);
		// step2 => 1 offer randomly selected as per probability weightings
		PEDRandomPrizesCache pedRandomPrizesCache = PEDRandomPrizesCache.instance();
		String prizeId = pedRandomPrizesCache.getRandomPrize(randomNum);
		LOGGER.debug("getRandomPrizeByRange => randomNum=>" + randomNum + "prizeId=>" + prizeId);

		PrizeLibrary prizeLibrary = new PrizeLibrary();
		if (null != prizeId && !PEDCONSTANT.NO_PRIZE.equalsIgnoreCase(prizeId)) {
			prizeLibrary = getPrizeDetails(prizeId, langCode);
			int totalPrizeCount = getTotalPrizeCount(prizeId);
			// Check if max number of wins have been reached
			LOGGER.debug("Check if max number of wins have been reached totalPrizeCount=>" + totalPrizeCount
					+ "=> max wins =>" + prizeLibrary.getMaxWins());
			if (totalPrizeCount >= prizeLibrary.getMaxWins()) {
				prizeLibrary.setPrizeId(PEDCONSTANT.NO_PRIZE);
			}
		} else {
			prizeLibrary.setPrizeId(PEDCONSTANT.NO_PRIZE);
		}

		return prizeLibrary;
	}

	/**
	 * increase prize record count in ECMP_T_PED_PRIZE_STATS
	 * 
	 * @param prizeId
	 */
	public void increasePrizeRecordCount(String prizeId) {
		LOGGER.debug("increasePrizeRecordCount => ");
		try {
			int count = pedLookUpDao.retrieveTotalPrizeCount(prizeId);
			pedUpdateDao.upsertPrizeRewardCount(count + 1, prizeId);

		} catch (IOException | ProcCallException e) {
			LOGGER.error("Error increasePrizeRecordCount => ", e);
		} catch (Exception e) {
			LOGGER.error("Exception increasePrizeRecordCount => ", e);
		}

	}

	/**
	 * increase prize record count
	 * 
	 * @throws Exception
	 */
	private void increasePlayedCountOfMsisdn(String msisdn) throws Exception {
		LOGGER.debug("increasePlayedCountOfMsisdn => ");
		int playedCount = pedLookUpDao.getMsisdnPlayedCount(msisdn);
		pedUpdateDao.upsertPedPlayedCountByMsisdn(msisdn, playedCount + 1);

	}

	/**
	 * @param msisdn
	 * @param prizeId
	 * @param status
	 * @param txnId
	 * @throws Exception
	 */
	public void recordRewardedPlayEcap(String msisdn, String prizeId, String status, String txnId) throws Exception {
		LOGGER.debug("recordRewardedPlayEcap => ");
		// insert into ECMP_T_PLAY_HISTORY_ECAP
		pedUpdateDao.insertIntoPlayHistoryEcap(msisdn, prizeId, status, txnId);

	}

	/**
	 * record rewarded Play ECMP_T_PLAY_HISTORY and ECMP_T_PLAY_HISTORY_ECAP.
	 * 
	 * @param msisdn
	 * @param prizeLibrary
	 * @param status
	 * @param txnId
	 * @throws Exception
	 */
	public void recordRewardedPlay(String msisdn, PrizeLibrary prizeLibrary, String status, String txnId)
			throws Exception {
		LOGGER.debug("recordRewardedPlay => ");
		// insert into ECMP_T_PLAY_HISTORY
		pedUpdateDao.insertIntoPlayHistory(msisdn, prizeLibrary.getPrizeDescription(), status);
	}

	/**
	 *
	 */
	@Override
	public boolean checkPedProcessPlayFlag() throws Exception {
		LOGGER.debug("checkPedProcessPlayFlag => ");
		return pedLookUpDao.getPedProcessFlag(PEDCONSTANT.STOP_PLAY);

	}

	/**
	 * @param prizeId
	 * @return
	 * @throws Exception
	 */
	public int getTotalPrizeCount(String prizeId) throws Exception {
		LOGGER.debug("getTotalPrizeCount => ");
		return pedLookUpDao.retrieveTotalPrizeCount(prizeId);
	}

	/**
	 * @param prizeId
	 * @param langCode
	 * @return
	 * @throws Exception
	 */
	public PrizeLibrary getPrizeDetails(String prizeId, int langCode) throws Exception {
		LOGGER.debug("getPrizeDetails => ");
		return pedLookUpDao.retrievePrizeDetailsById(prizeId, langCode);
	}

	private void publishToTopic(String topicName, String message) throws Exception {
		J4UOfferEventPublisher eventPublisher = new J4UOfferEventPublisher();
		eventPublisher.addEvent(topicName, message);
	}

	private JSONObject setResponseJSONForPlayHistory(JSONArray prizeHistoryArray, JSONObject requestJson,
			String msisdn) {

		JSONObject synchResJson;
		int statusCode = -1;
		String statusMessage = "";

		if (null != prizeHistoryArray) {
			// checking the offers availability
			statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
			statusMessage = J4UOfferStatusCode.SUCCESS.getStatusMessage();
			// requestJson.put(J4UOfferConstants.PLAYHISTORY,
			// prizeHistoryArray);
			requestJson.put(J4UOfferConstants.PLAYHISTORY, prizeHistoryArray);
			synchResJson = playHistoryProcessResponseJson(requestJson, statusCode, statusMessage);
			LOGGER.debug("Play History of  user :" + msisdn);
		} else {
			// send error status, so Asynch method will be called
			statusCode = J4UOfferStatusCode.NO_OFFERS.getStatusCode();
			statusMessage = J4UOfferStatusCode.NO_OFFERS.getStatusMessage();
			synchResJson = playHistoryProcessResponseJson(requestJson, statusCode, statusMessage);
			LOGGER.info(" No Play History available for user : " + msisdn);
		}
		return synchResJson;
	}

	private JSONObject setResponseJSONForGetPlayCount(int playCount, JSONObject requestJson, String msisdn) {

		JSONObject synchResJson = new JSONObject();
		int statusCode = -1;
		String statusMessage = "";
		requestJson.put(AVAILABLEPLAYS, playCount);
		try {
			// checking the offers availability
			statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
			statusMessage = J4UOfferStatusCode.SUCCESS.getStatusMessage();
			synchResJson = processResponseJson(requestJson, statusCode, statusMessage);
			LOGGER.debug("Plays Available for  user :" + msisdn);
		} catch (Exception e) {

			LOGGER.error("exception in json parsing:: ", e);
		}
		LOGGER.info("Response JSON from J4U service :: " + synchResJson);
		return synchResJson;
	}

	private JSONObject setResponseJSONForPlayRedeemption(String prizeId, JSONObject requestJson, String msisdn) {

		JSONObject synchResJson;
		int statusCode = -1;
		String statusMessage = "";

		if (prizeId.equalsIgnoreCase("NO_PLAY")) {
			// checking the offers availability
			statusCode = J4UOfferStatusCode.NO_PLAYS_AVAILABLE.getStatusCode();
			statusMessage = J4UOfferStatusCode.NO_PLAYS_AVAILABLE.getStatusMessage();
			requestJson.put(J4UOfferConstants.PRIZE_WON, "NO_PRIZE");
			synchResJson = processResponseJsonReedimtion(requestJson, statusCode, statusMessage);
			LOGGER.info(" No Play(s) available for users : " + msisdn);
		} else {
			// send error status, so Asynch method will be called
			statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
			statusMessage = J4UOfferStatusCode.SUCCESS.getStatusMessage();
			requestJson.put(J4UOfferConstants.PRIZE_WON, prizeId);
			LOGGER.info("PRIZE_WON : " + requestJson.getString(J4UOfferConstants.PRIZE_WON));

			synchResJson = processResponseJsonReedimtion(requestJson, statusCode, statusMessage);
			LOGGER.debug("User played the play :" + msisdn);

		}
		return synchResJson;
	}

	private JSONObject playHistoryProcessResponseJson(JSONObject requestJson, int statusCode, String statusMessage) {
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put(REF_NUM, requestJson.getString(REF_NUM));
			responseJson.put(TRANSACTION_ID, requestJson.getString(TRANSACTION_ID));
			responseJson.put(STATUS_CODE, statusCode);
			responseJson.put(STATUS_MESSAGE, statusMessage);
			responseJson.put(MSISDN, requestJson.getLong(MSISDN));
			responseJson.put(J4UOfferConstants.PLAYHISTORY, requestJson.get(J4UOfferConstants.PLAYHISTORY));

		} catch (Exception e) {

			LOGGER.error("exception in json parsing:: ", e);
		}
		LOGGER.info("Response JSON from J4U service :: " + responseJson);
		return responseJson;
	}

	private JSONObject processResponseJson(JSONObject requestJson, int statusCode, String statusMessage) {
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put(REF_NUM, requestJson.getString(REF_NUM));
			responseJson.put(TRANSACTION_ID, requestJson.getString(TRANSACTION_ID));
			responseJson.put(STATUS_CODE, statusCode);
			responseJson.put(STATUS_MESSAGE, statusMessage);
			responseJson.put(MSISDN, requestJson.getLong(MSISDN));
		} catch (Exception e) {

			LOGGER.error("exception in json parsing:: ", e);
		}
		LOGGER.info("Response JSON from J4U service :: " + responseJson);
		return responseJson;
	}

	private JSONObject processResponseJsonReedimtion(JSONObject requestJson, int statusCode, String statusMessage) {
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put(REF_NUM, requestJson.getString(REF_NUM));
			responseJson.put(TRANSACTION_ID, requestJson.getString(TRANSACTION_ID));
			responseJson.put(STATUS_CODE, statusCode);
			responseJson.put(STATUS_MESSAGE, statusMessage);
			responseJson.put(MSISDN, requestJson.getLong(MSISDN));
			responseJson.put(J4UOfferConstants.PRIZE_WON, requestJson.getString(J4UOfferConstants.PRIZE_WON));

		} catch (Exception e) {

			LOGGER.error("exception in json parsing:: ", e);
		}
		LOGGER.info("Response JSON from J4U service :: " + responseJson);
		return responseJson;
	}

}

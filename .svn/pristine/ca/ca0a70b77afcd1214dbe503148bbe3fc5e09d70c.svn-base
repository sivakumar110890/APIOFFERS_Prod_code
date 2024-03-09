package com.comviva.api.j4u.utils;

import com.comviva.api.exception.ConfigException;

import com.comviva.api.j4u.config.PropertiesLoader;
import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.ApiLog;
import com.comviva.api.j4u.model.UserInfo;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static com.comviva.api.j4u.utils.J4UOfferConstants.CELL_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.CHANNEL;
import static com.comviva.api.j4u.utils.J4UOfferConstants.J4U_OFFER_REFRESH_FLAG_MAP;
import static com.comviva.api.j4u.utils.J4UOfferConstants.J4U_SUB_MENU_PROD_TYPE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFERS;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_DESC;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.POOL_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.REF_NUM;
import static com.comviva.api.j4u.utils.J4UOfferConstants.THIRD_PARTY_REF;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TRANSACTION_ID;

/**
 * This class contains common utility methods for application
 *
 * @author chandra.tekam
 */
public class Utils {
	private static final Logger LOG = Logger.getLogger(Utils.class);
	protected static Map<String, String> mlSubMenuProdTypeMap;
	private static Map<String, String> productCategory;
	private static Map<String, String> mlRefreshFlagMap;
	private static Map<String, String> subCategoryMap;
	private static List<String> channelList;
	private static LocalTime startTime;
	private static LocalTime endTime;
	private static UpdaterDAO updaterDAO;

	public Utils() throws Exception {
		updaterDAO = new UpdaterDAO();
	}

	static {
		try {
			setMlRefreshFlagMap(loadMlRefreshFlagMap());
			setProductCategory(loadProductCategory());
			mlSubMenuProdTypeMap = getSubMenuProdTypeMap();
		} catch (Exception e) {
			LOG.error("Error " + e);
		}
	}

	public static boolean isNullOrEmpty(String str) {
		if (str == null || str.trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	private static Map<String, String> loadMlRefreshFlagMap() throws Exception {
		setMlRefreshFlagMap(new HashMap<>());
		List<String> offerRefreshFlagList = Arrays
				.asList(PropertiesLoader.getValue(J4U_OFFER_REFRESH_FLAG_MAP).split(","));
		for (String key : offerRefreshFlagList) {
			String mapAry[] = key.split(":");
			getMlRefreshFlagMap().put(mapAry[0], mapAry[1]);
		}
		LOG.debug("mlRefreshFlagMap : " + getMlRefreshFlagMap().toString());
		return getMlRefreshFlagMap();
	}

	private static Map<String, String> loadProductCategory() {
		try {
			setProductCategory(new HashMap<>());
			List<String> offerRefreshFlagList = Arrays
					.asList(PropertiesLoader.getValue(J4U_SUB_MENU_PROD_TYPE).split(","));
			for (String key : offerRefreshFlagList) {
				String mapAry[] = key.split(":");
				getProductCategory().put(mapAry[0], mapAry[1]);
			}
		} catch (Exception e) {
			LOG.error(e);
		}
		LOG.debug("productCategory: " + getProductCategory());
		return getProductCategory();
	}

	private static Map<String, String> getSubMenuProdTypeMap() {
		List<String> prodTypeList;
		try {
			mlSubMenuProdTypeMap = new HashMap<>();
			prodTypeList = Arrays.asList(PropertiesLoader.getValue(J4U_SUB_MENU_PROD_TYPE).split(","));
			for (String key : prodTypeList) {
				String prodMapping[] = key.split(":");
				mlSubMenuProdTypeMap.put(prodMapping[0], prodMapping[1]);
			}
		} catch (Exception e) {
			LOG.error(e);
			// e.printStackTrace();
		}

		return mlSubMenuProdTypeMap;
	}

	public static String convertToDBString(String comments) {
		if (null != comments) {
			if (comments.contains("'")) {
				comments = comments.replace("'", "");
			}

			if (comments.length() > 250) {
				comments = comments.substring(0, 249);
			}
		}
		return comments;
	}

	/**
	 * Returns the current {@java.util.Date} in Client's timezone.
	 *
	 * @return
	 * @throws Exception
	 */
	public static Date getCurrentTimeStamp() {
		String timezone = null;
		try {
			timezone = PropertiesLoader.getValue(J4UOfferConstants.AUDIT_TIMEZONE);
		} catch (Exception e) {
			timezone = "GMT+01";
			Logger.getRootLogger().error("Error occurred in reading AUDIT_TIMEZONE property:: " + e.getMessage(), e);
		}
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timezone));
		cal.add(Calendar.MILLISECOND, cal.get(Calendar.ZONE_OFFSET));
		return cal.getTime();
	}

	public static String getDateAsString(final java.util.Date date, final String dateFormat) {
		return new SimpleDateFormat(dateFormat).format(date);

	}

	public synchronized String getTransactionID() {
		return UUID.randomUUID().toString();
	}

	public static Map<String, String> getProductCategory() {
		return productCategory;
	}

	public static void setProductCategory(Map<String, String> productCategory) {
		Utils.productCategory = productCategory;
	}

	public static Map<String, String> getMlRefreshFlagMap() {
		return mlRefreshFlagMap;
	}

	public static void setMlRefreshFlagMap(Map<String, String> mlRefreshFlagMap) {
		Utils.mlRefreshFlagMap = mlRefreshFlagMap;
	}

	private static final BiPredicate<LocalTime, LocalTime> checkWindowTime = (startTime, endTime) -> {
		LocalTime currTime = LocalTime.now();
		LOG.info("MORNING OFFER CURRENT TIME : " + currTime);
		return currTime.compareTo(startTime) >= 0 && currTime.compareTo(endTime) <= 0;
	};

	public static boolean getValidMorningOffer() throws ConfigException {
		if (null == startTime) {
			startTime = LocalTime.parse(PropertiesLoader.getValue(J4UOfferConstants.J4U_MORNING_OFFER_WINDOW_START));
			LOG.info("MORNING OFFER START TIME : " + startTime);
		}
		if (null == endTime) {
			endTime = LocalTime.parse(PropertiesLoader.getValue(J4UOfferConstants.J4U_MORNING_OFFER_WINDOW_END));
			LOG.info("MORNING OFFER END TIME : " + endTime);
		}

		return checkWindowTime.test(startTime, endTime);
	}

	public static boolean checkMorningEligiblity(String offerCategory, UserInfo userInfo, JSONObject requestJson)
			throws ConfigException {
		if (getValidMorningOffer()) {
			LOG.info("Customer falls under the Morning configured Time Zone ");
			if (userInfo == null
					|| userInfo.getMorningofferEligilibiltyFlag().equalsIgnoreCase(J4UOfferConstants.FLAG_Y)) {
				return true;
			}
		}
		return false;

	}

	private static void loadSubCategory() throws Exception {
		subCategoryMap = Arrays.stream(PropertiesLoader.getValue(J4UOfferConstants.SUB_CATEGORY_MAP).split(","))
				.map(s -> s.split(":")).collect(Collectors.toMap(s -> s[0], s -> s[1]));
		LOG.info("SUB CATEGORY LOADED" + subCategoryMap);
	}

	public static String getSubCategoryType(String key) throws Exception {
		if (null == subCategoryMap)
			loadSubCategory();
		if (!subCategoryMap.containsKey(key))
			return key;

		return subCategoryMap.get(key);
	}

	private static void loadChannel() throws Exception {
		channelList = Arrays.stream(PropertiesLoader.getValue(J4UOfferConstants.CHANNEL_FOR_ENHANCED).split(","))
				.collect(Collectors.toList());
		LOG.info(
				"Channel List Load :: 																																																																																																																																																																	"
						+ channelList);
	}

	public static boolean getChannel(String key) throws Exception {
		if (null == channelList)
			loadChannel();
		if (channelList.contains(key))
			return true;
		return false;
	}

	

	public static void insertApiLog(JSONObject jsonObject, String apiType, String status, String comments,
			String category) throws Exception {
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
			LOG.debug("offer Desc comments" + comments + " \n size : " + comments.length());
		}

		ApiLog apiLog = new ApiLog();
		apiLog.setMsisdn(jsonObject.get(MSISDN).toString());
		apiLog.setApiType(apiType);
		apiLog.setComments(comments);
		apiLog.setDateTime(Utils.getCurrentTimeStamp());
		apiLog.setProdIds("");
		apiLog.setRefNum(refNum);
		apiLog.setStatus(status);
		apiLog.setProdType(category);
		apiLog.setThirdPartyRef(thirdPartyRef);
		apiLog.setTransactionId(jsonObject.getString(TRANSACTION_ID));
		apiLog.setChannel(channel);
		apiLog.setSelectedProdId("");
		apiLog.setCustomerBalance("");
		apiLog.setMlFlag("");
		apiLog.setRandomFlag("");
		apiLog.setCellId(jsonObject.has(CELL_ID) ? jsonObject.getString(CELL_ID) : "");
		apiLog.setPoolId(jsonObject.has(POOL_ID) ? jsonObject.getString(POOL_ID) : "");
		LOG.debug("APilog....." + apiLog);
		updaterDAO.insertApiLog(apiLog);
	}
}

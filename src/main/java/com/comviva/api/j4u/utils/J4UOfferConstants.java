package com.comviva.api.j4u.utils;

/**
 * This class contains all constant declaration
 *
 * @author chandra.tekam
 */
public class J4UOfferConstants {

	// configuration and db file location
	public static final String USER_DIR = System.getProperty("user.dir");
	public static final String J4U_CONNECTION_PROPERTIES_FILE = USER_DIR + "/config/ApiOffers.properties";
	public static final String DB_PROPERTIES_FILENAME = USER_DIR + "/config/ApiOffersDB.properties";
	// timezone setting
	public static final String AUDIT_TIMEZONE = "j4u.offer.audit.timzone";
	public static final String J4U_ML_SUB_MENU_TEMPLATE = "j4u.offer.ml.sub.menu.template";
	// constant declaration
	public static final int MAX_PRODIDS_CNT = 3;
	// variable declaration for json String
	public static final String MSISDN = "MSISDN";
	public static final String GET_OFFER = "GET_OFFER";
	public static final String RAG_OPT_IN = "RAG_OPT_IN";
	public static final String ACTIVATE_OFFER = "ACTIVATE_OFFER";
	public static final String GET_PLAY = "GET_PLAY";
	public static final String OFFERS = "Offers";
	public static final String TRANSACTION_ID = "TransactionID";
	public static final String REQUEST_BUFFER = "REQUEST_BUFFER";
	public static final String OFFER_ID = "OfferID";
	public static final String STATUS_MESSAGE = "StatusMessage";
	public static final String CHANNEL = "Channel";
	public static final String USER_MSG_REF = "USER_MSG_REF";
	public static final String SHORT_MSG = "SHORT_MSG";
	public static final String PRODUCT_TYPE = "PRODUCT_TYPE";
	public static final String PRODUCT_SUBTYPE = "PRODUCT_SUBTYPE";
	public static final String THIRD_PARTY_REF = "ThirdPartyRef";
	public static final String REF_NUM = "RefNum";
	public static final String OFFER_DESC = "OfferDesc";
	public static final String STATUS_CODE = "StatusCode";
	public static final String CATEGORY = "Category";
	public static final String IS_PURCHASED = "IsPurchased";
	public static final String REF_TRANSACTION_ID = "RefTransactionID";
	public static final String LANGUAGE = "Language";
	public static final String AMOUNT = "Amount";
	public static final String LANG_CODE = "LANG_CODE";
	public static final String J4U_ELIGIBILITY = "J4U_ELIGIBILITY";
	public static final String RANDOM_FLAG = "RANDOM_FLAG";
	public static final String ML_CUSTOMER_FLAG = "ML_CUSTOMER_FLAG";
	public static final String OFFER_REFRESH_FLAG = "OFFER_REFRESH_FLAG";
	public static final String ACCOUNT_BALANCE = "ACCOUNT_BALANCE";
	public static final String FLAG_Y = "Y";
	public static final String SOURCE = "SOURCE";
	public static final String TEMPLATE_ID = "TEMPLATE_ID";
	public static final String TEMPLATE = "TEMPLATE";
	public static final String OFFER_ORDER_CSV = "OFFER_ORDER_CSV";
	public static final String ML_FLAG = "ML_FLAG";
	public static final String PROG_SHORT_CD = "PROG_SHORT_CD";
	public static final String SEL_PROD_TYPE = "SEL_PROD_TYPE";
	public static final String MPESA = "MPESA";
	public static final String MPESA_QUARY_BAL = "MPESA_QUARY_BAL";
	public static final String G2_REF_NUM = "G2_REF_NUM";
	public static final String PRODUCT_PRICE = "PRODUCT_PRICE";
	public static final String OCS_SUCCESS = "OCS_SUCCESS";
	public static final String OCS_STATUS = "OCS_STATUS";
	public static final String OCS_FAILURE = "OCS_FAILURE";
	public static final String OCS_COMMENTS = "OCS_COMMENTS";
	// Query Balance plugin entries
	public static final String OCS_QUERY_BAL_TOPIC = "ocs.query.bal.topic";
	public static final String MPESA_QUERY_BAL_TOPIC = "mpesa.query.bal.topic";
	// public static final String
	public static final String REWARDSPUB_TOPIC_NAME = "j4u.offer.rewardspublisher.topic.name";
	// ml offer refresh flag setting
	public static final String J4U_OFFER_REFRESH_FLAG_MAP = "j4u.offer.refresh.flag.map";
	// product menu
	public static final String J4U_SUB_MENU_PROD_TYPE = "j4u.offer.sub.menu.prod.type";
	// log details
	public static final String J4U_LOG_PROC_NAME = "j4u.offer.log.proc.name";
	public static final String J4U_TRX_PRODID_MAP_NAME = "j4u.offer.trxprodidmap.proc.name";
	public static final String J4U_TRXPRODIDMAP_DELETE_NAME = "j4u.offer.trxprodidmap.deleteproc.name";
	// Product information
	public static final String PRODUCT_DESC = "PRODUCT_DESC";
	public static final String PRODUCT_ID = "PRODUCT_ID";
	public static final String SUBID_CNTRYCD = "j4u.offer.msisdn.countryCode"; // country code
	public static final String SUBID_LENGTH = "j4u.offer.msisdn.length";
	public static final String J4U_PRODINFO_PROC_NAME = "j4u.offer.prodInfo.proc.name";
	public static final String MPESA_CONSUMER_TOPIC = "mpesa.topic";
	public static final String MPESA_ASYNC_TIMEOUT = "mpesa.async.timeout";
	// volt db constants
	public static final String BACK_PRESSURE_SLP_TIME = "back.pressure.sleeping.time";
	public static final String API_OFFER_REQ_RECEIVED = "API_OFFER_REQ_RECEIVED";

	/*
	 * Api getOffer & Activate offer status codes
	 */
	public static final String API_SYNCH_REQ_RECEIVED = "API_SYNCH_REQ_RECEIVED";
	public static final String API_ASYNCH_REQ_RECEIVED = "API_ASYNCH_REQ_RECEIVED";
	public static final String API_CUST_BAL_RECEIVED = "API_CUST_BAL_RECEIVED";
	public static final String API_FINAL_OFFERS_SENT = "API_FINAL_OFFERS_SENT";
	public static final String API_FINAL_MSG_RAG_OPT_IN_SENT = "API_FINAL_MSG_RAG_OPT_IN_SENT";
	public static final String API_OFFER_REQ_FAILED = "API_OFFER_REQ_FAILED";
	public static final String API_RAG_OPT_IN_REQ_FAILED = "API_RAG_OPT_IN_REQ_FAILED";
	public static final String API_ACT_OFFER_REQ_RECEIVED = "API_ACT_OFFER_REQ_RECEIVED";
	public static final String API_GET_PLAY_REQ_RECEIVED = "API_GET_PLAY_REQ_RECEIVED";
	public static final String API_REDEEM_PLAY_REQ_RECEIVED = "API_REDEEM_PLAY_REQ_RECEIVED";
	public static final String API_ALL_AVAILABLE_PLAYS_SENT = "API_ALL_AVAILABLE_PLAYS_SENT";
	public static final String API_PLAY_REQUEST_FAILED = "API_PLAY_REQUEST_FAILED";
	public static final String API_ACT_OFFER_OCS_RESP_RECEIVED = "API_ACT_OFFER_OCS_RESP_RECEIVED";
	public static final String API_ACT_OFFER_FINAL_RESP_SENT = "API_ACT_OFFER_FINAL_RESP_SENT";

	public static final String STATUS_FAILURE = "LOCATION_FAILURE";
	public static final String DATE_FORMAT = "dd MMM yyyy HH:mm:ss";
	public static final String READTIMEOUT = "location.request.timeoutMillis";

	public static final String CONNECTIONTIMEOUT = "location.connection.timeoutMillis";
	public static final int HTTP_SUCCESS = 200;

	public static final String SSL_CERTIFICATE_CHECK_ENABLED = "SSL_CERTIFICATE_CHECK_ENABLED";
	public static final String URL_GET_CELL_ID = "locationmodule.query.service.get.cellid";
	public static final String ECMP_P_J4U_ML_LOCATION_OFFER = "location.module.get.offer";
	public static final String LOCATION_MODULE_GET_POOL_ID = "location.module.get.poolid";

	public static final String REGION_ID = "REGION_ID";
	public static final String LOCATION_MODULE_GET_OFFER = "j4u.location.module.get.offer";
	public static final String ENBA_P_GET_LOCATION_INFO = "j4u.location.module.get.offer.info";
	public static final String ECMP_T_J4U_API_LOG_INSERT = "j4u.procedure.api.log.insert";
	public static final String USSD_P_ERED_T_REDUCED_CCR_SELECT = "j4u.procedure.get.msisdn.ccr.detail";
	public static final String CELL_ID = "CELL_ID";
	public static final String POOL_ID = "POOL_ID";
	public static final String LOCATION_OFFER = "LOCATIONOFFER";
	public static final String LOCATION_OFFER_COUNT = "LOCATION_OFFER_COUNT";
	public static final String USER_SELECTION = "USER_SELECTION";
	public static final String DEST_ADDRESS = "DEST_ADDRESS";
	public static final String PREF_PAY_METHOD = "PREF_PAY_METHOD";
	public static final String A_VALUE = "A_VALUE";
	public static final String PREF_PAY_MET_MPESA = "M";
	public static final String LOCATION_MODULE_GET_OFFER_COUNT = "location.module.get.offer.count";
	public static final String J4U_VOICE = "J4U Voice";
	public static final String J4U_DATA = "J4U Data";
	public static final String J4U_INTEGRATED = "J4U Integrated & SMS";
	public static final String J4U_HOURLY_DATA = "J4U Hourly Data";
	public static final String J4U_SOCIAL_MEDIA = "J4U Social Media";
	public static final String USER_SEL_1 = "1";
	public static final String USER_SEL_2 = "2";
	public static final String ML_HOURLY_DATA_2OFFER_MENU = "j4u.offer.ml.hourly2offer.menu.template";
	public static final String ML_HOURLY_DATA_1OFFER_MENU = "j4u.offer.ml.hourly1offer.menu.template";
	public static final String HOURLY = "Hourly";
	public static final String ECMP_P_GET_PRODUCT_PRICE = "ecmp.procedure.get.product.price";
	public static final String J4U_PRODINFO_PROD_TYPE_PROC = "j4u.prodInfo.prodType.name";
	public static final String CATEGORY_SOCIAL = "Social";
	public static final String KEY_ISSOCIAL = "isSocial";

	// OPENNET
	public static final String ROUTER_COUNTRY_CODE = "j4u.offer.msisdn.countryCode";
	public static final String ROUTER_MSISDN_RANGE_LENGTH = "j4u.offer.msisdnrange.length";
	public static final String CS_TYPE_HW = "HW";
	public static final String CS_TYPE_IM = "IM";
	public static final String CS_TYPE_OP = "OP";
	public static final String CS_FLAG = "J4u.offer.enable.multi.cs";
	public static final String ROUTER_CS_MSISDNRANGE_QUERY = "J4u.offer.cs.msisdnrange.query";
	public static final String ROUTER_CS_MSISDN_QUERY = "J4u.offer.cs.msisdn.query";
	public static final String OP_REWARDSPUB_TOPIC_NAME = "j4u.offer.op.rewardspublisher.topic.name";
	public static final String CCS_QUERY_BAL_TOPIC = "J4u.offer.op.ccs.query.bal.topic";
	// J4U Social Bundle
	public static final String J4U_SOCIAL_PRODINFO_PROC_NAME = "j4u.ecmp.social.prodinfo.proc";
	public static final String J4U_SOCIAL_PRODIDS_PROC_NAME = "j4u.ecmp.social.prodids.proc";
	public static final String J4U_SOCIAL_OFFERS_PROC = "j4u.offers.ml.social.query.offer.proc";
	public static final String J4U_SOCIAL_ML_PRODINFO_PROCNAME = "j4u.offers.social.prod.info.proc";
	public static final String J4U_SOCIAL_ML_OFFERS_BYRANK_PROC = "j4u.offers.ml.social.offer.byrank.proc";
	public static final String USSD_ML_SOCIAL_OFFER_BYEXP_PROC = "ussd.ml.social.offer.byexp.proc";
	public static final String B_VALUE = "B_VALUE";
	public static final String C_VALUE = "C_VALUE";

	// morning offer
	public static final String J4U_MORNING_OFFER = "J4U Morning Offer";
	public static final String CATEGORY_MORNING = "MorningOffer";
	public static final String KEY_ISMORNING = "isMorning";
	public static final String MORNING_FLAG = "M";
	public static final String MORNING_OFFER_FLAG = "Y";
	public static final String J4U_MORNING_OFFER_WINDOW_START = "j4u.offers.morning.offer.window.start";
	public static final String J4U_MORNING_OFFER_WINDOW_END = "j4u.offers.morning.offer.window.end";
	public static final String J4U_MORNING_OFFER_PROD_INFO = "j4u.offers.morning.offer.prodinfo";
	public static final String J4U_ENBA_MORNING_OFFER = "j4u.offers.get.morning.offer.proc";
	public static final String MORNING_OFFER_ELIGIBILITY = "MORNING_OFFER_ELIGIBILITY";

	// NewAPIIntegration
	public static final String CATEGORY_VOICE = "Voice";
	public static final String CATEGORY_INTEGRATED = "Integrated";
	public static final String SUB_CATEGORY = "Subcategory";
	public static final String KEY_ISENHANCED = "isEnhanced";
	public static final String ENBA_J4U_ML_LOCATION_OFFER_SUB_CAT = "j4u.location.subtype.module.get.offer.info";
	public static final String P_UPSERT_OFFER_TO_CACHE = "j4u.offers.cache.insert.proc";
	public static final String P_GET_OFFER_FROM_CACHE = "j4u.offers.cache.get.proc";
	public static final String USSD_P_ML_AA_OFFER_SUB_TYPE = "j4u.pl.aa.ml.offer.sub.category";
	public static final String SEL_PROD_SUB_TYPE = "SEL_PROD_SUB_TYPE";
	public static final String CHANNEL_FOR_ENHANCED = "j4u.enhanced.flow.channel";
	public static final String SUB_CATEGORY_MAP = "j4u.request.sub.category.map";
	/* public static final String SUB_CATEGORY_TYPE = "j4u.subcategory.list"; */

	/*
	 * public static final String ONNET = "Onnet"; public static final String
	 * ALLNET = "allnet"; public static final String INTERNATIONAL =
	 * "International"; public static final String INTEGRATED_BUNDLES =
	 * "Integrated Bundles "; public static final String ONNET_SMS = "Onnet SMS"
	 * ; public static final String ALLNET_SMS = "Allnet SMS";
	 */

	public static final String USSD_P_ERED_GET_USER_INFO_WITH_PED = "ussd.reduced.ccr.get.info";
	public static final String ERED_P_GET_RAG_FLAG = "j4u.rag.opt.flag.wl";
	public static final String RAG_OPT_FLAG = "RAG_OPT_FLAG";
	public static final String WEEK_START_DATE = "WEEK_START_DATE";
	public static final String WEEK_END_DATE = "WEEK_END_DATE";
	public static final String NEXT_AVAILABLE_OFFER_DATE = "NEXT_AVAILABLE_OFFER_DATE";
	public static final String REWARD_CODE = "REWARD_CODE";
	public static final String TARGET_TYPE = "TARGET_TYPE";
	public static final String OTHER = "OTHER";
	public static final String PAYMENT_METHOD = "PAYMENT_METHOD";
	public static final String VALUE_P = "VALUE_P";
	public static final String RECHARGE_TARGET = "RECHARGE_TARGET";
	public static final String USSD_P_ML_RAG_USER_RECORD_SELECT = "USSD_P_ML_RAG_USER_RECORD_SELECT";
	public static final String USSD_P_ML_SAG_USER_RECORD_SELECT = "USSD_P_ML_SAG_USER_RECORD_SELECT";
	public static final String INSERT_RAG_USER_CAT_PROC_NAME = "insert.raguser.cat.proc.name";
	public static final String INSERT_SAG_USER_CAT_PROC_NAME = "insert.saguser.cat.proc.name";
	public static final String USSD_RAG_ATL_REWARD_MAP = "ussd.rag.atl.reward.map";
	public static final String USSD_PED_ENABLED = "ussd.ped.enabled";
	public static final String USSD_ML_PED_MENU = "ussd.ml.ped.menu.template";
	public static final String USSD_ML_PED_SUB_MENU = "ussd.ml.ped.sub.menu.template";
	public static final String USSD_ML_PED_PLAY_ALERT_MENU = "ussd.ml.ped.play.alert.menu";
	public static final String USSD_ML_PED_NO_PLAY_MENU = "ussd.ml.ped.no.play.menu.template";
	public static final String USSD_ML_PED_OFFER_MENU = "ussd.ml.ped.offer.menu.template";
	public static final String USSD_ML_PED_NO_PRIZE_MENU = "ussd.ml.ped.no.prize.menu.template";
	public static final String USSD_ML_PED_AVAILABLE_PLAYS_MENU = "ussd.ml.ped.available.plays.menu.template";
	public static final String USSD_ML_PED_HISTORY_MENU = "ussd.ml.ped.history.menu.template";
	public static final String USSD_ML_PED_NO_HISTORY_MENU = "ussd.ml.ped.no.history.menu.template";
	public static final String USSD_HOURLY_DATA_ENABLED = "ussd.hourly.enabled";
	public static final String LOCATION_RANDOM_FLAG = "LOCATION_RANDOM_FLAG";

	// Srithar ML_RAG Changes
	public static final String USSD_ML_RAG_FIRST_OPT_IN_MENU = "ussd.ml.rag.first.opt.in.menu.template";
	public static final String USSD_ML_RAG_ALREADY_OPT_IN_MENU = "ussd.ml.rag.already.opt.in.menu.template";
	public static final String USSD_ML_RAG_GOAL_REACHED_MENU = "ussd.ml.rag.goal.reached.template";
	public static final String USSD_ML_RAG_INELIGIBLE_MENU = "ussd.ml.rag.ineligible.template";
	public static final String USSD_ML_RAG_OFFER_INFO = "ussd.ml.rag.offer.info";

	public static final String USSD_ML_RAG_MAIN_MENU_OPTIONS = "ussd.ml.rag.main.menu.options";
	public static final String USSD_ML_RAG_SUB_MENU_OPTIONS = "ussd.ml.rag.sub.menu.options";
	public static final String USSD_ML_RAG_OFFER_INFO_OPTIONS = "ussd.ml.rag.offer.info.options";
	public static final String USSD_PRODUCT_PRICE_LIST_PROC = "ussd.product.price.list.proc";

	public static final String USSD_ML_RAG_MAIN_MENU_RECHARGE_AND_GET_SELECTION = "ussd.ml.rag.main.menu.recharge.and.get.selection";

	public static final String USSD_ML_RAG_SUB_MENU_OFFER_INFO_SELECTION = "ussd.ml.rag.sub.menu.offer.info.selection";
	public static final String USSD_ML_RAG_SUB_MENU_OPT_OUT_SELECTION = "ussd.ml.rag.sub.menu.opt.out.selection";

	public static final String USSD_ML_RAG_OFFER_INFO_BACK_SELECTION = "ussd.ml.rag.offer.info.back.selection";
	public static final String MSG_CODE = "MSG_CODE";
	public static final String RAG_OPT_OUT = "RAG_OPT_OUT";
	public static final String API_FINAL_MSG_RAG_OPT_OUT_SENT = "API_FINAL_MSG_RAG_OPT_OUT_SENT";
	public static final String API_RAG_OPT_OUT_REQ_FAILED = "API_RAG_OPT_OUT_REQ_FAILED";
	public static final String RAG_OPT_GET_OFFER = "RAG_OPT_GET_OFFER";
	public static final String API_RAG_OFFER_REQ_RECEIVED = "API_RAG_OFFER_REQ_RECEIVED";
	public static final String API_FINAL_MSG_RAG_GET_OFFER_SENT = "API_FINAL_MSG_RAG_GET_OFFER_SENT";
	public static final String API_RAG_GET_OFFER_REQ_FAILED = "API_RAG_GET_OFFER_REQ_FAILED";
	public static final String REWARDS = "REWARDS";
	public static final String VALUE = "Value";
	public static final String VALIDITY = "Validity";
	public static final String REMAINING_EFFORT = "REMAINING_EFFORT";
	public static final String PRODUCT_VALUE = "PRODUCT_VALUE";
	public static final String API_SAG_GET_OFFER_REQ_FAILED = "API_SAG_GET_OFFER_REQ_FAILED";
	public static final String SAG_OPT_GET_OFFER = "SAG_OPT_GET_OFFER";
	public static final String API_FINAL_MSG_SAG_GET_OFFER_SENT = "API_FINAL_MSG_SAG_GET_OFFER_SENT";
	public static final String API_SAG_OFFER_REQ_RECEIVED = "API_SAG_OFFER_REQ_RECEIVED";
	public static final String SAG_OPT_IN = "SAG_OPT_IN";
	public static final String API_FINAL_MSG_SAG_OPT_IN_SENT = "API_FINAL_MSG_SAG_OPT_IN_SENT";
	public static final String API_SAG_OPT_IN_REQ_FAILED = "API_SAG_OPT_IN_REQ_FAILED";
	public static final String PLAYHISTORY = "PLAYHISTORY";
	public static final String PED_P_PLAY_HISTORY_SELECT_APIOFFER = "PED_P_PLAY_HISTORY_SELECT_APIOFFER";
	public static final String PLAY_DATE = "PLAY_DATE";
	public static final String PRIZE_DESC = "PRIZE_DESC";
	public static final String PRIZE_WON = "PRIZE_WON";
	public static final String ERED_P_GET_SAG_OPTFLAG = "ERED_P_GET_SAG_OPTFLAG";
	public static final String ERED_P_GET_RAG_OPTFLAG = "ERED_P_GET_RAG_OPTFLAG";
	public static final String SAG_OPT_FLAG = "SAG_OPT_FLAG";
	public static final String SAG_OPT_OUT = "SAG_OPT_OUT";
	public static final String API_FINAL_MSG_SAG_OPT_OUT_SENT = "API_FINAL_MSG_SAG_OPT_OUT_SENT";
	public static final String API_SAG_OPT_OUT_REQ_FAILED = "public static final String";
	public static final String SMS_OPT_IN_TEMPLATE = "sms.optin.template";
	public static final String SMS_TOPIC_NAME = "sms.topic.name";
	public static final String NO_PLAYS = "NO_PLAYS";
	public static final String AVILABLE_PLAY_COUNT = "AVILABLE_PLAY_COUNT";
	public static final String PED_REDEEMPLAY_RESP_SENT = "PED_REDEEMPLAY_RESP_SENT";
	public static final String PED_REDEEMPLAY_REQUEST_RECIEVED = "PED_REDEEMPLAY_REQUEST_RECIEVED";
	public static final String PED_REDEEMPLAY_REQ_FAILED = "PED_REDEEMPLAY_REQ_FAILED";
	public static final String RAG_OFFERINFO_REQ_RECEIVED = "RAG_OFFERINFO_REQ_RECEIVED";
	public static final String RAG_GET_OFFERINFO = "RAG_GET_OFFERINFO";
	public static final String RAG_OFFERINFO_RESP_SENT = "RAG_OFFERINFO_RESP_SENT";
	public static final String RAG_OFFERINFO_REQ_FAILED = "RAG_OFFERINFO_REQ_FAILED";
	public static final String RAG_OPT_OUT_REQ_RECEIVED = "RAG_OPT_OUT_REQ_RECEIVED";
	public static final String RAG_OPT_OUT_RESP_SENT = "RAG_OPT_OUT_RESP_SENT";
	public static final String RAG_OPT_OUT_REQ_FAILED = "RAG_OPT_OUT_REQ_FAILED";
	public static final String SAG_OFFER_REQ_RECEIVED = "SAG_OFFER_REQ_RECEIVED";
	public static final String SAG_GET_OFFER = "SAG_OFFER_REQ_RECEIVED";
	public static final String SAG_GET_OFFER_REQ_FAILED = "SAG_GET_OFFER_REQ_FAILED";
	public static final String SAG_OPT_IN_REQ_FAILED = "SAG_OPT_IN_REQ_FAILED";
	public static final String SAG_GET_OFFER_REQ_SENT = "SAG_GET_OFFER_REQ_SENT";
	public static final String SAG_OPT_IN_RESP_SENT = "SAG_OPT_IN_RESP_SENT";
	public static final String REWARD = "REWARD";
	public static final String PED_ELIGIBILITY = "PED_ELIGIBILITY";
	public static final String USSD_SAG_ATL_REWARD_MAP = "ussd.sag.atl.reward.map";
	public static final String PREF_PAY_METHOD_G = "G";
	public static final String PREF_PAY_METHOD_M = "M";
	public static final String CHANNEL_EVC = "EVC";	
	
	public static final String BLACKLIST_ENABLED = "blacklisting.msisdns.enabled";
	public static final String GET_BLACKLIST_MSISDN = "blacklist.msisdn.procedure";
	public static final String API_FINAL_OFFERS_SENT_BLACKLIST = "API_FINAL_OFFERS_SENT_BLACKLIST";
	public static final String BLACKLISTED_COMMENTS = "msisdn is blacklisted";
	
	private J4UOfferConstants() {
	}

}
package com.comviva.api.j4u.service;

import com.comviva.api.j4u.config.PropertiesLoader;

import com.comviva.api.j4u.dao.LookUpDAO;
import com.comviva.api.j4u.dao.UpdaterDAO;
import com.comviva.api.j4u.model.ApiLog;
import com.comviva.api.j4u.model.InboundUSSDMessage;
import com.comviva.api.j4u.model.OfferParams;
import com.comviva.api.j4u.model.RankingFormulae;
import com.comviva.api.j4u.model.TemplateDTO;
import com.comviva.api.j4u.utils.J4UOfferConstants;
import com.comviva.api.j4u.utils.J4UOfferStatusCode;
import com.comviva.api.j4u.utils.ProductInfoCache;
import com.comviva.api.j4u.utils.TemplateCache;
import com.comviva.api.j4u.utils.Utils;
import com.comviva.api.j4u.utils.ProductPriceCache;
import com.comviva.api.offers.ActivateOffer;
import com.comviva.api.offers.GetOffers;
import com.mchange.v2.cfg.PropertiesConfigSource.Parse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;
import java.util.function.Function;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.comviva.api.j4u.utils.J4UOfferConstants.*;

public class QueryBalanceService {

	private static final Logger LOG = Logger.getLogger(QueryBalanceService.class);
	private static final String AA_ELIGIBLE = "AA_ELIGIBLE";
	private static final String AIRTIME_ADVANCE_BALANCE = "AIRTIME_ADVANCE_BALANCE";
	private static final String OFF_PARAM_GET_OFFER_ID = "offParam getOfferId - ";
	private static final String INBOUND_USSDMSG = "inboundUSSDMsg";
	private static final String LANGUAGE_EN = "en";
	private static final String API_MPESA_CUST_BAL_RECEIVED = "API_MPESA_CUST_BAL_RECEIVED";
	private RankingFormulae rf;
	private LookUpDAO lookUpDAO;
	private UpdaterDAO updaterDAO;

	public QueryBalanceService() {
		try {
			lookUpDAO = new LookUpDAO();
			updaterDAO = new UpdaterDAO();
			rf = new RankingFormulae();
		} catch (Exception e) {
			LOG.error("Database access error", e);
		}
	}

	private final BiPredicate<JSONArray, JSONObject> checkOffersAry = (offersAry,queryBaljson) -> null != offersAry 
			&& (offersAry.length() == MAX_PRODIDS_CNT 
			|| (offersAry.length() < 3 && HOURLY.equals(queryBaljson.getString(CATEGORY)))
			|| (offersAry.length() < 3 && queryBaljson.getBoolean(KEY_ISENHANCED)));

	private static Predicate<String> checkSocialCategory = category -> (null != category
			&& category.equalsIgnoreCase(J4UOfferConstants.CATEGORY_SOCIAL));



	/**
	 * This method consumed response from topic and process for query balance to
	 * ocs and returns three best offer for user based on his balance
	 */
	public void processQueryBalanceRequest(JSONObject queryBaljson) throws Exception {        
		
		if (PREF_PAY_METHOD_M.equalsIgnoreCase(queryBaljson.getString(PREF_PAY_METHOD))) {
			LOG.info("RESPONSE RECIEVED FROM MPESA QUERY BALANCE");
			insertApiLog(queryBaljson, GET_OFFER, API_MPESA_CUST_BAL_RECEIVED, null);
		} else {
			LOG.info("RESPONSE RECIEVED FROM QUERY BALANCE Plugin!");
			insertApiLog(queryBaljson, GET_OFFER, API_CUST_BAL_RECEIVED, null);
		}
		JSONArray offersArray = this.getOfferForMlTgtUser(queryBaljson);
		LOG.debug("getOfferForMlTgtUser :: offersArray ::  " + offersArray);
		boolean testCondition = checkOffersAry.test(offersArray, queryBaljson);		
		if (testCondition) {
			//Adding Success code and msg
			queryBaljson.put(STATUS_CODE, J4UOfferStatusCode.SUCCESS.getStatusCode());
			queryBaljson.put(STATUS_MESSAGE, J4UOfferStatusCode.SUCCESS.getStatusMessage());			

		} else {
			// add error code and desc send			
			queryBaljson.put(STATUS_CODE, J4UOfferStatusCode.NO_OFFERS.getStatusCode());
			queryBaljson.put(STATUS_MESSAGE, J4UOfferStatusCode.NO_OFFERS.getStatusMessage());			

		}		
		queryBaljson.put(OFFERS, offersArray);
		GetOffers getOffersObj = new GetOffers();
		getOffersObj.receiveAsyncOffers(queryBaljson);
	}	

	public JSONArray getOfferForMlTgtUser(JSONObject queryBaljson) throws Exception {
		JSONArray jsonArray;
		String prodType = Utils.getProductCategory().get(queryBaljson.getString(CATEGORY));
		LOG.debug("DB product Type = " + prodType);
		Boolean isSocial = checkSocialCategory.test(queryBaljson.getString(CATEGORY));
		if (Boolean.TRUE.equals(isSocial)) {
			jsonArray = getOfferForMlTgtUserSocial(queryBaljson, prodType);
		} else {
			jsonArray = getOfferForMlTgtUserNonSocial(queryBaljson, prodType);
		}
		return jsonArray;
	}

	private final ToIntFunction<String> getLanguage = lang -> lang.equalsIgnoreCase(LANGUAGE_EN) ? 2 : 1;
	private final Function<List<String>, StringBuilder> formProdIDSb = prodIdsList -> {
		StringBuilder prodIdSb = new StringBuilder();
		ProductPriceCache priceCache = ProductPriceCache.instance();
		for (String prodId : prodIdsList) {
			prodIdSb = prodIdSb.append(prodId + "~" + priceCache.get(prodId) + ",");
		}
		return prodIdSb;
	};

	private final Function<List<String>, StringBuilder> formProdEvSb = rfsList -> {
		StringBuilder prodEvSb = new StringBuilder();
		for (String prodEv : rfsList) {
			prodEvSb = prodEvSb.append(prodEv + ",");
			LOG.debug("prodEvSb 2 - " + prodEvSb);
		}
		return prodEvSb;
	};

	private JSONArray getOfferForMlTgtUserSocial(JSONObject queryBaljson, String prodType) throws Exception {
		String msisdn = queryBaljson.getString(MSISDN);
		LOG.debug(" getOfferForMlTgtUserSocial :: msisdn :" + msisdn + " CATEGORY" + queryBaljson.getString(CATEGORY));

		double actBal = queryBaljson.getDouble(ACCOUNT_BALANCE);
		LOG.debug(" Airtime Bal = " + actBal);
		int langCode = getLanguage.applyAsInt(queryBaljson.getString(LANGUAGE));
		int size = MAX_PRODIDS_CNT;
		List<String> evsList = new ArrayList<>();
		List<String> prodIdsList = new ArrayList<>();
		List<String> rfsList = new ArrayList<>();
		JSONArray offerArray = new JSONArray();

		LOG.debug("processRFRequestSocial => ");
		rf = processRFRequestSocial(queryBaljson, prodType);
		List<OfferParams> offerParamList = rf.getOfferParams();
		LOG.info("Size of OFfferParamList ::" + offerParamList.size());
		size = (size > offerParamList.size()) ? offerParamList.size() : size;
		LOG.info("size ::" + size);
		for (int i = 0; i < size; i++) {
			OfferParams offParam = offerParamList.get(i);
			LOG.debug(OFF_PARAM_GET_OFFER_ID + i + "_" + offParam.getOfferId());
			prodIdsList.add(offParam.getOfferId());
			rfsList.add(Float.toString(offParam.getRfValue()));
		}
		if (prodIdsList.isEmpty()) {
			prodIdsList = lookUpDAO.getSocialOffersByBalanceAndRank(msisdn, prodType, actBal, evsList);
			LOG.debug("evsList 1 :: " + evsList);
			prodIdsList = (prodIdsList.size() == MAX_PRODIDS_CNT) ? prodIdsList
					: lookUpDAO.getSocialOffersByExpectedValue(msisdn, prodType, prodIdsList, evsList);
			LOG.debug("call by expected value list=>:: " + evsList);

		}
		LOG.debug("prodIdsList :: " + prodIdsList);
		if (prodIdsList.size() == MAX_PRODIDS_CNT) {
			// provison offer
			TemplateDTO templateDTO = getSubMenuTemplateForLocation(langCode, queryBaljson, prodIdsList.size());

			ProductInfoCache productInfoCache = ProductInfoCache.instance();
			if (null != templateDTO) {
				InboundUSSDMessage inboundUSSDMsg = generateSubMenuSocial(langCode, prodIdsList, productInfoCache,
						templateDTO);
				StringBuilder prodIdSb = formProdIDSb.apply(prodIdsList);
				// creating entry in ENBA_T_J4U_ML_OFFER_MSG table
				LOG.debug("creating entry in ENBA_T_J4U_ML_OFFER_MSG table for the msisdn = " + msisdn);
				if (!rfsList.isEmpty()) {
					StringBuilder prodEvSb = formProdEvSb.apply(rfsList);

					// creating entry in ENBA_T_J4U_ML_OFFER_MSG table
					updaterDAO.upsertMLOfferMsg(msisdn, prodType,
							prodIdSb.deleteCharAt(prodIdSb.length() - 1).toString(), inboundUSSDMsg.getClobString(),
							prodEvSb.deleteCharAt(prodEvSb.length() - 1).toString());
				} else {
					updaterDAO.upsertMLOfferMsg(msisdn, prodType,
							prodIdSb.deleteCharAt(prodIdSb.length() - 1).toString(), inboundUSSDMsg.getClobString(),
							null);
				}
				String categoryType = Utils.getMlRefreshFlagMap().get(queryBaljson.getString(CATEGORY));
				LOG.debug("Prod Type category:: " + categoryType);
				LOG.debug("OFFER_REFRESH_FLAG: " + queryBaljson.getString(OFFER_REFRESH_FLAG));
				this.updateReducedCCR(msisdn, categoryType, queryBaljson.getString(OFFER_REFRESH_FLAG));
				offerArray = formOfferJSONArrayForSocial(prodIdsList, langCode);
			}
		}

		return offerArray;
	}

	private JSONArray formOfferJSONArray(List<String> prodIdsList, int langCode) {
		ProductInfoCache productInfoCache = ProductInfoCache.instance();
		ProductPriceCache priceCache = ProductPriceCache.instance();
		JSONArray offerArray = new JSONArray();
		for (String productId : prodIdsList) {
			JSONObject offerJson = new JSONObject();
			String key = productId + "_" + langCode;
			LOG.info("key" + key);
			String productDescription = productInfoCache.get(key).getProductDesc();
			LOG.info("prod Description " + productDescription);
			offerJson.put(OFFER_ID, (productId));
			offerJson.put(OFFER_DESC, productDescription);
			offerJson.put(AMOUNT, Integer.parseInt(priceCache.get(productId)));
			LOG.debug("Key " + key + ":" + productDescription);
			offerArray.put(offerJson);
		}

		return offerArray;
	}

	private JSONArray formOfferJSONArrayForSocial(List<String> prodIdsList, int langCode) {
		ProductInfoCache productInfoCache = ProductInfoCache.instance();
		ProductPriceCache priceCache = ProductPriceCache.instance();
		JSONArray offerArray = new JSONArray();
		for (String productId : prodIdsList) {
			JSONObject offerJson = new JSONObject();
			String key = productId + "_" + langCode;
			String productDescription = productInfoCache.getSocial(key).getProductDesc();
			offerJson.put(OFFER_ID, (productId));
			offerJson.put(OFFER_DESC, productDescription);
			offerJson.put(AMOUNT, Integer.parseInt(priceCache.get(productId)));
			LOG.debug("Key " + key + ":" + productDescription);
			offerArray.put(offerJson);
		}

		return offerArray;
	}


	private final BiPredicate<List<String>, JSONObject> checkEnoughProdIds = (prodIdsList,queryBaljson) -> 
	!prodIdsList.isEmpty() && (prodIdsList.size() == 3
	|| (prodIdsList.size() < 3 && HOURLY.equals(queryBaljson.getString(CATEGORY)))
	|| (prodIdsList.size() < 3 && queryBaljson.getBoolean(KEY_ISENHANCED)));

	private final BiConsumer<List<String>, List<String>> getProductRFSList = (prodIdsList, rfsList) -> {
		List<OfferParams> offerParamList = rf.getOfferParams();
		int size = 3;
		if (size > offerParamList.size()) {
			size = offerParamList.size();
		}
		for (int i = 0; i < size; i++) {
			OfferParams offParam = offerParamList.get(i);
			LOG.debug(OFF_PARAM_GET_OFFER_ID + i + "_" + offParam.getOfferId());
			prodIdsList.add(offParam.getOfferId());
			rfsList.add(Float.toString(offParam.getRfValue()));
		}
	};

	private JSONArray getOfferForMlTgtUserNonSocial(JSONObject queryBaljson, String prodType) throws Exception {
		String msisdn = queryBaljson.getString(MSISDN);
		LOG.debug(" getOfferForMlTgtUserNonSocial :: msisdn :" + msisdn + ", CATEGORY: " + queryBaljson.getString(CATEGORY));
		double actBal = queryBaljson.getDouble(ACCOUNT_BALANCE);
		LOG.debug(" Airtime Bal = " + actBal);
		int langCode = getLanguage.applyAsInt(queryBaljson.getString(LANGUAGE));
		List<String> evsList = new ArrayList<>();
		List<String> prodIdsList = new ArrayList<>();
		List<String> rfsList = new ArrayList<>();
		/*
		 * Check for the AA eligible flag for the rf calculation or normal flow
		 * and check if the offer refresh is required or not
		 */
		LOG.debug("processRFCalculationForCellId => ");
		rf = processRFCalculationForCellId(queryBaljson, prodType);

		JSONArray offerArray = new JSONArray();
		LOG.debug("Is location Offer present => " + rf.getOfferParams());
		if (null != rf.getOfferParams() && !rf.getOfferParams().isEmpty()) {
			boolean bRet = getOfferForMlTgtUserNonSocialLocationOffers(offerArray, prodIdsList, rfsList, queryBaljson,
					prodType);
			if (bRet) {
				return offerArray;
			}
		} else {
			LOG.debug("No location Offer present calling for normal ml offer=>");
			rf = processRFRequest(queryBaljson, prodType);
			getProductRFSList.accept(prodIdsList, rfsList);
		}
		if (prodIdsList.isEmpty()) {
			String prodSubType = null;
			if (queryBaljson.has(J4UOfferConstants.SUB_CATEGORY) || queryBaljson.getBoolean(KEY_ISENHANCED))
				prodSubType = queryBaljson.getString(J4UOfferConstants.SUB_CATEGORY);
			prodIdsList = lookUpDAO.getOffersByBalanceAndRank(msisdn, prodType, prodSubType, actBal, evsList);
			LOG.debug("evsList 1 :: " + evsList);
			prodIdsList = (prodIdsList.size() == MAX_PRODIDS_CNT) ? prodIdsList
					: lookUpDAO.getOffersByExpectedValue(msisdn, prodType, prodSubType, prodIdsList, evsList);
			LOG.debug("call by expected value list=>:: " + evsList);

		}
		LOG.debug("prodIdsList :: " + prodIdsList);
		boolean tesCondition = checkEnoughProdIds.test(prodIdsList, queryBaljson);
		LOG.debug("tesCondition :: " + tesCondition);
		if (tesCondition) {
			// provison offer
			TemplateDTO templateDTO = getSubMenuTemplateForLocation(langCode, queryBaljson, prodIdsList.size());
			ProductInfoCache productInfoCache = ProductInfoCache.instance();
			if (null != templateDTO) {
				InboundUSSDMessage inboundUSSDMsg = generateSubMenu(langCode, prodIdsList, productInfoCache,
						templateDTO);
				StringBuilder prodIdSb = formProdIDSb.apply(prodIdsList);
				// creating entry in ENBA_T_J4U_ML_OFFER_MSG table
				String prodSubType = null;
				if (queryBaljson.has(J4UOfferConstants.SUB_CATEGORY) || queryBaljson.getBoolean(KEY_ISENHANCED))
					prodSubType = queryBaljson.getString(J4UOfferConstants.SUB_CATEGORY);
				if (!rfsList.isEmpty()) {
					StringBuilder prodEvSb = formProdEvSb.apply(rfsList);
					// creating entry in ENBA_T_J4U_ML_OFFER_MSG table
					updaterDAO.upsertNonSocialMLOfferMsg(msisdn, prodType, prodSubType,
							prodIdSb.deleteCharAt(prodIdSb.length() - 1).toString(), inboundUSSDMsg.getClobString(),
							prodEvSb.deleteCharAt(prodEvSb.length() - 1).toString());
					LOG.info("data updated successfuly in cache table");
				} else {
					updaterDAO.upsertNonSocialMLOfferMsg(msisdn, prodType, prodSubType,
							prodIdSb.deleteCharAt(prodIdSb.length() - 1).toString(), inboundUSSDMsg.getClobString(),
							null);
				}
				if (null == prodSubType) {
					String categoryType = Utils.getMlRefreshFlagMap().get(queryBaljson.getString(CATEGORY));
					LOG.debug("Prod Type category:: " + categoryType);

					LOG.debug("OFFER_REFRESH_FLAG: " + queryBaljson.getString(OFFER_REFRESH_FLAG));
					this.updateReducedCCR(msisdn, categoryType, queryBaljson.getString(OFFER_REFRESH_FLAG));
				}
				LOG.info("Sending prodlist and lang code " + prodIdsList + "" + langCode);
				offerArray = formOfferJSONArray(prodIdsList, langCode);
			}
		}
		return offerArray;

	}

	private boolean getOfferForMlTgtUserNonSocialLocationOffers(JSONArray offerArray, List<String> prodIdsList,
			List<String> rfsList, JSONObject queryBaljson, String prodType) throws Exception {
		List<OfferParams> offerParamList = rf.getOfferParams();
		int size = 3;
		LOG.debug("PoolID ==>" + queryBaljson.getString(POOL_ID));
		if (size > offerParamList.size()) {
			size = offerParamList.size();
		}
		for (int i = 0; i < size; i++) {
			OfferParams offParam = offerParamList.get(i);
			LOG.debug(OFF_PARAM_GET_OFFER_ID + i + "_" + offParam.getOfferId());
			prodIdsList.add(offParam.getOfferId());
			rfsList.add(Float.toString(offParam.getRfValue()));

		}
		StringBuilder prodIdSb = new StringBuilder();
		boolean tesCondition = checkEnoughProdIds.test(prodIdsList, queryBaljson);
		LOG.debug("tesCondition :: " + tesCondition);
		if (tesCondition) {
			for (int i = 0; i < size; i++) {
				OfferParams offParam = offerParamList.get(i);
				LOG.debug(OFF_PARAM_GET_OFFER_ID + i + "_" + offParam.getOfferId());
				prodIdsList.add(offParam.getOfferId());
				rfsList.add(Float.toString(offParam.getRfValue()));
				JSONObject offerJson = new JSONObject();
				offerJson.put(OFFER_ID, offParam.getOfferId());
				offerJson.put(OFFER_DESC, offParam.getProductDescription());
				offerJson.put(AMOUNT, offParam.getOfferPrice());
				prodIdSb.append(offParam.getOfferId()).append("~").append(offParam.getOfferPrice()).append(",");

				offerArray.put(offerJson);

			}

			return true;
		} else {
			prodIdsList.clear();
			LOG.debug("No location Offer present calling for normal ml offer=>");
			rf = processRFRequest(queryBaljson, prodType);
			offerParamList = rf.getOfferParams();

			int sizenormal = 3;
			if (sizenormal > offerParamList.size()) {
				sizenormal = offerParamList.size();
			}
			for (int i = 0; i < sizenormal; i++) {
				OfferParams offParam = offerParamList.get(i);
				LOG.debug(OFF_PARAM_GET_OFFER_ID + i + "_" + offParam.getOfferId());
				prodIdsList.add(offParam.getOfferId());
				rfsList.add(Float.toString(offParam.getRfValue()));

			}
		}

		return false;
	}

	private RankingFormulae processRFCalculationForCellId(JSONObject queryBaljson, String prodType) throws Exception {
		LOG.debug("processRFCalculationForCellId > START");

		LOG.debug("queryBaljson" + queryBaljson);

		rf.setAirtimeBalance(queryBaljson.getLong(J4UOfferConstants.ACCOUNT_BALANCE));
		rf.setAaEligible(queryBaljson.has(AA_ELIGIBLE) && queryBaljson.getInt(AA_ELIGIBLE) == 1);
		rf.setAaBalance(queryBaljson.has(AIRTIME_ADVANCE_BALANCE) ? queryBaljson.getLong(AIRTIME_ADVANCE_BALANCE) : 0);
		rf.setCellId(queryBaljson.getString(CELL_ID));
		if (queryBaljson.has(SOURCE) && queryBaljson.getString(SOURCE).equalsIgnoreCase(MPESA_QUARY_BAL)) {
			rf.setPrefPayMethod(PREF_PAY_METHOD_M);
		}else {
			rf.setPrefPayMethod(PREF_PAY_METHOD_G);
		}		
		LOG.debug("cell_id=> " + queryBaljson.getString(CELL_ID));
		int langCode = queryBaljson.getString(LANGUAGE).equalsIgnoreCase(LANGUAGE_EN) ? 2 : 1;
		if (null != rf.getCellId()) {

			String poolId = queryBaljson.getString(POOL_ID);
			String prodSubType = null;
			if (queryBaljson.has(SUB_CATEGORY) && queryBaljson.getBoolean(KEY_ISENHANCED)) {
				prodSubType = queryBaljson.getString(SUB_CATEGORY);

			}
			LOG.debug("pool_id=> " + poolId);
			if (null != poolId) {
				rf.setPoolId(poolId);
				queryBaljson.put(POOL_ID, rf.getPoolId());

				rf = lookUpDAO.getRFParamsForLocation(queryBaljson.getString(J4UOfferConstants.MSISDN), prodType,
						prodSubType, langCode, rf, poolId);

				calculateRFValue(rf);
			}

		}

		return rf;
	}

	private RankingFormulae processRFRequestSocial(JSONObject queryBaljson, String prodType) throws Exception {
		LOG.debug("processRFRequestSocial - ");
		long startTime = System.currentTimeMillis();
		rf.setAirtimeBalance(queryBaljson.getLong(J4UOfferConstants.ACCOUNT_BALANCE));
		rf.setAaEligible(queryBaljson.has(AA_ELIGIBLE) && queryBaljson.getInt(AA_ELIGIBLE) == 1 ? true : false);
		rf.setAaBalance(queryBaljson.has(AIRTIME_ADVANCE_BALANCE) ? queryBaljson.getLong(AIRTIME_ADVANCE_BALANCE) : 0);
		rf.setCellId(queryBaljson.getString(CELL_ID));
		if (queryBaljson.has(SOURCE) && queryBaljson.getString(SOURCE).equalsIgnoreCase(MPESA_QUARY_BAL)) {
			rf.setPrefPayMethod(PREF_PAY_METHOD_M);
		}else {
			rf.setPrefPayMethod(PREF_PAY_METHOD_G);
		}		
		LOG.debug("cell_id" + queryBaljson.getString(CELL_ID));
		int langCode = queryBaljson.getString(LANGUAGE).equalsIgnoreCase(LANGUAGE_EN) ? 2 : 1;

		rf = lookUpDAO.getSocialRFParams(queryBaljson.getString(MSISDN), prodType, langCode, rf);
		calculateRFValue(rf);
		long endTime = System.currentTimeMillis();
		LOG.info("Procedure call time => " + (endTime - startTime) + " in ms");

		LOG.debug("rf :: " + rf);
		return rf;
	}

	private RankingFormulae processRFRequest(JSONObject queryBaljson, String prodType) throws Exception {
		LOG.debug("processRFRequest - ");
		long startTime = System.currentTimeMillis();
		rf.setAirtimeBalance(queryBaljson.getLong(J4UOfferConstants.ACCOUNT_BALANCE));
		rf.setAaEligible(queryBaljson.has(AA_ELIGIBLE) && queryBaljson.getInt(AA_ELIGIBLE) == 1 ? true : false);
		rf.setAaBalance(queryBaljson.has(AIRTIME_ADVANCE_BALANCE) ? queryBaljson.getLong(AIRTIME_ADVANCE_BALANCE) : 0);
		rf.setCellId(queryBaljson.getString(CELL_ID));		
		if (queryBaljson.has(SOURCE) && queryBaljson.getString(SOURCE).equalsIgnoreCase(MPESA_QUARY_BAL)) {
			rf.setPrefPayMethod(PREF_PAY_METHOD_M);
		}else {
			rf.setPrefPayMethod(PREF_PAY_METHOD_G);
		}		
		LOG.debug("cell_id" + queryBaljson.getString(CELL_ID));
		int langCode = queryBaljson.getString(LANGUAGE).equalsIgnoreCase(LANGUAGE_EN) ? 2 : 1;
		String prodSubType = null;
		if (queryBaljson.has(J4UOfferConstants.SUB_CATEGORY) || queryBaljson.getBoolean(KEY_ISENHANCED))
			prodSubType = queryBaljson.getString(J4UOfferConstants.SUB_CATEGORY);
		rf = lookUpDAO.getRFParams(queryBaljson.getString(J4UOfferConstants.MSISDN), prodType, prodSubType, langCode,
				rf);
		LOG.info("rf value ::" + rf);
		calculateRFValue(rf);
		long endTime = System.currentTimeMillis();
		LOG.info("Procedure call time => " + (endTime - startTime) + " in ms");
		LOG.debug("rf" + rf);
		return rf;
	}

	private RankingFormulae calculateRFValue(RankingFormulae rf) {
		LOG.debug("calculateRFValue - ");
		int offersLength = rf.getOfferParams().size();
		OfferParams offerParams;

		LOG.debug("offersLength - " + offersLength);
		LOG.debug("PREF_PAY_METHOD: "+ rf.getPrefPayMethod());
		if (offersLength > 0) {
			for (int offerCount = 0; offerCount < offersLength; offerCount++) {
				offerParams = rf.getOfferParams().get(offerCount);
				calculateRFValueForOffer(rf, offerCount, offerParams);
				LOG.debug("RfValue - "+ rf.getOfferParams().get(offerCount).getRfValue() 
						+ ", OfferId- "	+ rf.getOfferParams().get(offerCount).getOfferId());
			}
		}
		List<OfferParams> offerParamsList = rf.getOfferParams().stream()
				.sorted((offer1, offer2) -> Float.compare(offer2.getRfValue(), offer1.getRfValue()))
				.collect(Collectors.toList());
		rf.setOfferParams(offerParamsList);
		LOG.debug("Sorted Offer = >" + rf.getOfferParams());

		LOG.debug("calculateRFValue END ");
		return rf;
	}

	private void calculateRFValueForOffer(RankingFormulae rf, int offerCount, OfferParams offerParams) {		
		if (rf.getPrefPayMethod().equalsIgnoreCase(PREF_PAY_MET_MPESA)) {
//			LOG.debug("PREF_PAY_METHOD MPESA");  
			setRfValue(rf, offerParams, offerCount); 
		} else {
//			LOG.debug("PREF_PAY_METHOD : G");
			if (rf.isAaEligible()) {
				setAirtimeAdvanceRfValue(rf, offerParams, offerCount);
			} else {					  
				setRfValue(rf, offerParams, offerCount);                       
			}
		}		
	}

	private void setRfValue(RankingFormulae rf, OfferParams offerParams, int offerCount) {
		if (offerParams.getOfferPrice() <= rf.getAirtimeBalance()) {
			rf.getOfferParams().get(offerCount).setRfValue(BigDecimal.valueOf(offerParams.getExpectedValue())
					.multiply(BigDecimal.valueOf(offerParams.getcValue())).floatValue());
			LOG.debug("Offer price <= AccountBalance -> RF= multiply EV with cValue");
		} else {
			rf.getOfferParams().get(offerCount).setRfValue(offerParams.getExpectedValue());
			LOG.debug("Offer price > AccountBalance -> Rf= EV");
		}            
	}

	private void setAirtimeAdvanceRfValue(RankingFormulae rf, OfferParams offerParams, int offerCount) {
		double netWeight = (double) rf.getAirtimeBalance() + ((double) rf.getAaBalance() * (double) rf.getaValue());
		LOG.debug("calculateRFValue netWeight - " + netWeight + " ,OfferId- " 
				+ rf.getOfferParams().get(offerCount).getOfferId());
		if (offerParams.getOfferPrice() <= netWeight) {
			rf.getOfferParams().get(offerCount).setRfValue(BigDecimal.valueOf(offerParams.getExpectedValue())
					.multiply(BigDecimal.valueOf(offerParams.getbValue())).floatValue());
			LOG.debug("Offer price <= netWeight -> Rf= multiply EV with bValue");
		} else {
			rf.getOfferParams().get(offerCount).setRfValue(offerParams.getExpectedValue());
			LOG.debug("Offer price > netWeight -> Rf= EV");
		}
	}

	private InboundUSSDMessage generateSubMenu(int langCode, List<String> prodIdsList,
			ProductInfoCache productInfoCache, TemplateDTO templateDTO) throws Exception {
		LOG.debug("generateSubMenu =>");
		InboundUSSDMessage inboundUSSDMsg = new InboundUSSDMessage();
		String prodIdsOrder[] = templateDTO.getOfferOrderCSV().split(",");
		String template = templateDTO.getTemplate();
		for (int i = 0; i < prodIdsOrder.length; i++) {
			String offerPattern = "@" + prodIdsOrder[i] + "@";
			String replacement = productInfoCache.get(prodIdsList.get(i) + "_" + langCode).getProductDesc();
			LOG.debug("replacement =>" + replacement);
			template = template.replace(offerPattern, replacement);
		}
		inboundUSSDMsg.setClobString(template);
		LOG.debug(INBOUND_USSDMSG + inboundUSSDMsg);
		return inboundUSSDMsg;

	}

	private InboundUSSDMessage generateSubMenuSocial(int langCode, List<String> prodIdsList,
			ProductInfoCache productInfoCache, TemplateDTO templateDTO) throws Exception {
		LOG.debug("generateSubMenuSocial =>");
		InboundUSSDMessage inboundUSSDMsg = new InboundUSSDMessage();
		String prodIdsOrder[] = templateDTO.getOfferOrderCSV().split(",");
		String template = templateDTO.getTemplate();
		for (int i = 0; i < prodIdsOrder.length; i++) {
			String offerPattern = "@" + prodIdsOrder[i] + "@";
			String replacement = productInfoCache.getSocial(prodIdsList.get(i) + "_" + langCode).getProductDesc();
			LOG.debug("replacement =>" + replacement);
			template = template.replace(offerPattern, replacement);
		}
		inboundUSSDMsg.setClobString(template);
		LOG.debug(INBOUND_USSDMSG + inboundUSSDMsg);
		return inboundUSSDMsg;

	}

	private TemplateDTO getSubMenuTemplateForLocation(int langCode, JSONObject queryBaljson, int productListSize)
			throws Exception {
		TemplateCache templateCache = TemplateCache.instance();
		TemplateDTO templateDTO = null;
		if (Integer.parseInt(USER_SEL_1) == productListSize
				&& ((HOURLY.equals(queryBaljson.getString(CATEGORY))) || queryBaljson.getBoolean(KEY_ISENHANCED))) {
			templateDTO = templateCache
					.getMLMenu(PropertiesLoader.getValue(ML_HOURLY_DATA_1OFFER_MENU) + "_" + langCode);
		} else if (Integer.parseInt(USER_SEL_2) == productListSize
				&& ((HOURLY.equals(queryBaljson.getString(CATEGORY))) || queryBaljson.getBoolean(KEY_ISENHANCED))) {
			templateDTO = templateCache
					.getMLMenu(PropertiesLoader.getValue(ML_HOURLY_DATA_2OFFER_MENU) + "_" + langCode);
		} else {
			templateDTO = templateCache.getMLMenu(PropertiesLoader.getValue(J4U_ML_SUB_MENU_TEMPLATE) + "_" + langCode);

		}
		LOG.debug("templateDTO=>" + templateDTO);
		return templateDTO;
	}

	private void updateReducedCCR(String msisdn, String prodType, String offerRefFlag) throws Exception {
		LOG.debug("updateReducedCCR..... ");
		if (offerRefFlag.indexOf(prodType) >= 0) {
			/*
			 * if we refresh the offer via m-pesa, we need to remove the
			 * data/voice/integrated from OFFER_REFRESH_FLAG column
			 */
			if (offerRefFlag.equalsIgnoreCase(prodType)) {
				offerRefFlag = "N";
			} else {
				offerRefFlag = offerRefFlag.replace(prodType, "");
			}
			LOG.debug("Update ERED_T_REDUCED_CCR [OFFER_REFRESH_FLAG] = " + offerRefFlag + " for MSISDN = " + msisdn);
			updaterDAO.updateReducedCCR(offerRefFlag, msisdn);
		}
	}



	public JSONObject responseForActivateOffer(JSONObject requestJson, int statusCode, String statusMessage)
			throws JSONException {
		JSONObject responseJson = new JSONObject();
		responseJson.put(REF_NUM, requestJson.getString(REF_NUM));
		responseJson.put(TRANSACTION_ID, requestJson.getString("TRANSACTION_ID"));
		responseJson.put(STATUS_CODE, statusCode);
		responseJson.put(STATUS_MESSAGE, statusMessage);
		responseJson.put(MSISDN, Long.parseLong(requestJson.get(MSISDN).toString()));
		return responseJson;
	}

	/**
	 * check the ocs success or failure and creates final response for async api
	 *
	 * @param requestJson
	 */
	public void processActivateOffer(JSONObject requestJson) {
		JSONObject responseJson;
		int statusCode;
		String statusMessage;
		ActivateOffer activateOfferAysnc = new ActivateOffer();
		try {
			insertApiLog(requestJson, ACTIVATE_OFFER, API_ACT_OFFER_OCS_RESP_RECEIVED);
			LOG.debug("OCS_STATUS :: " + requestJson.getString(OCS_STATUS));
			if (requestJson.getString(OCS_STATUS).equalsIgnoreCase(OCS_SUCCESS)) {
				LOG.debug("OCS_SUCCESS :: ");
				statusCode = J4UOfferStatusCode.SUCCESS.getStatusCode();
				statusMessage = "Offer activated successfully for the offerID " + requestJson.getString(PRODUCT_ID);
				responseJson = responseForActivateOffer(requestJson, statusCode, statusMessage);
				// call activate offer asysnc method
				LOG.debug("Offer activated successfully for the offerID " + requestJson.getString(PRODUCT_ID));
				activateOfferAysnc.actOfferResponseAsync(responseJson);
			} else {
				LOG.debug("OCS_FAILURE :: ");
				statusCode = J4UOfferStatusCode.DOWNSTREAM.getStatusCode();
				statusMessage = requestJson.getString(OCS_COMMENTS);
				responseJson = responseForActivateOffer(requestJson, statusCode, statusMessage);
				// call activate offer asysnc method
				LOG.debug("Offer activation failed :: statusMessage " + statusMessage);
				activateOfferAysnc.actOfferResponseAsync(responseJson);
			}
		} catch (Exception e) {
			LOG.error("Error while ocs reward provisioning : " + e.getMessage());

		}
	}

	private void insertApiLog(JSONObject jsonObject, String apiType, String status, String comments) {
		String thirdPartyRef = "";
		String refNum = "";
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
			apiLog.setProdType(jsonObject.has(CATEGORY) ? jsonObject.getString(CATEGORY) : "");
			apiLog.setThirdPartyRef(thirdPartyRef);
			apiLog.setCustomerBalance(
					jsonObject.has(ACCOUNT_BALANCE) ? jsonObject.get(ACCOUNT_BALANCE).toString() : "");
			apiLog.setTransactionId(jsonObject.has("TransactionID") ? jsonObject.getString("TransactionID") : "");
			apiLog.setChannel(jsonObject.has(CHANNEL) ? jsonObject.get(CHANNEL).toString() : "");
			apiLog.setCellId(jsonObject.has(CELL_ID) ? jsonObject.getString(CELL_ID) : "");
			apiLog.setPoolId(jsonObject.has(POOL_ID) ? jsonObject.getString(POOL_ID) : "");
			LOG.debug("Api log insertingg...." + apiLog);
			updaterDAO.insertApiLog(apiLog);

		} catch (Exception e) {

			LOG.error("Exception while inserting api log :: " + e.getMessage());
		}
	}

	private void insertApiLog(JSONObject jsonObject, String apiType, String status) {
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
			apiLog.setMlFlag(jsonObject.getString(ML_FLAG));
			apiLog.setComments(jsonObject.getString(OCS_COMMENTS));
			apiLog.setDateTime(Utils.getCurrentTimeStamp());
			apiLog.setRefNum(refNum);
			apiLog.setStatus(status);
			apiLog.setProdType(jsonObject.has(CATEGORY) ? jsonObject.getString(CATEGORY) : "");
			apiLog.setSelectedProdId(jsonObject.has(PRODUCT_ID) ? jsonObject.getString(PRODUCT_ID) : "");
			apiLog.setThirdPartyRef(thirdPartyRef);
			apiLog.setCustomerBalance(
					jsonObject.has(ACCOUNT_BALANCE) ? jsonObject.get(ACCOUNT_BALANCE).toString() : "");
			apiLog.setTransactionId(jsonObject.has("TRANSACTION_ID") ? jsonObject.getString("TRANSACTION_ID") : "");
			apiLog.setChannel(channel);
			apiLog.setCellId(jsonObject.has(CELL_ID) ? jsonObject.getString(CELL_ID) : "");
			apiLog.setPoolId(jsonObject.has(POOL_ID) ? jsonObject.getString(POOL_ID) : "");
			LOG.debug("Api log insertingg...." + apiLog);
			updaterDAO.insertApiLog(apiLog);
		} catch (Exception e) {

			LOG.error("Exception while inserting api log :: " + e.getMessage());
		}
	}
}

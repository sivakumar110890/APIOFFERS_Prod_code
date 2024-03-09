package com.comviva.api.j4u.dao;

import static com.comviva.api.j4u.utils.J4UOfferConstants.AMOUNT;
import static com.comviva.api.j4u.utils.J4UOfferConstants.A_VALUE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.ECMP_P_GET_PRODUCT_PRICE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.ECMP_P_J4U_ML_LOCATION_OFFER;
import static com.comviva.api.j4u.utils.J4UOfferConstants.ENBA_P_GET_LOCATION_INFO;
import static com.comviva.api.j4u.utils.J4UOfferConstants.GET_BLACKLIST_MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.J4U_ELIGIBILITY;
import static com.comviva.api.j4u.utils.J4UOfferConstants.LANG_CODE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MAX_PRODIDS_CNT;
import static com.comviva.api.j4u.utils.J4UOfferConstants.ML_CUSTOMER_FLAG;
import static com.comviva.api.j4u.utils.J4UOfferConstants.MSISDN;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_DESC;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_ORDER_CSV;
import static com.comviva.api.j4u.utils.J4UOfferConstants.OFFER_REFRESH_FLAG;
import static com.comviva.api.j4u.utils.J4UOfferConstants.POOL_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.PRODUCT_DESC;
import static com.comviva.api.j4u.utils.J4UOfferConstants.PRODUCT_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.PRODUCT_PRICE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.RANDOM_FLAG;
import static com.comviva.api.j4u.utils.J4UOfferConstants.SUBID_CNTRYCD;
import static com.comviva.api.j4u.utils.J4UOfferConstants.SUBID_LENGTH;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TEMPLATE;
import static com.comviva.api.j4u.utils.J4UOfferConstants.TEMPLATE_ID;
import static com.comviva.api.j4u.utils.J4UOfferConstants.USSD_ML_SOCIAL_OFFER_BYEXP_PROC;
import static com.comviva.api.j4u.utils.J4UOfferConstants.USSD_P_ERED_T_REDUCED_CCR_SELECT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;
import org.voltdb.VoltType;
import org.voltdb.client.Client;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.comviva.api.exception.ConfigException;
import com.comviva.api.j4u.config.PropertiesLoader;
import com.comviva.api.j4u.model.OfferParams;
import com.comviva.api.j4u.model.ProductInfo;
import com.comviva.api.j4u.model.RAGAndSAGUserInfo;
import com.comviva.api.j4u.model.RankingFormulae;
import com.comviva.api.j4u.model.TemplateDTO;
import com.comviva.api.j4u.model.UserInfo;
import com.comviva.api.j4u.utils.J4UOfferConstants;
import com.comviva.api.j4u.utils.ProductInfoCache;
import com.comviva.api.j4u.utils.ProductPriceCache;
import com.comviva.api.j4u.utils.Utils;
import com.comviva.voltdb.factory.DAOFactory;

/**
 * @author saloni.gupta
 */
public class LookUpDAO {

	private static final Logger LOG = Logger.getLogger(LookUpDAO.class);
	private static final String LOCATION_RANDOM_FLAG = "LOCATION_RANDOM_FLAG";
	private static final String FIELD_PRODUCT_ID = "PRODUCT_ID";
	private static final String FIELD_EXPECTED_VALUE = "EXPECTED_VALUE";
	private static final String FIELD_PRODUCT_PRICE = "PRODUCT_PRICE";
	public static final String USSD_P_ML_AA_OFFER = "USSD_P_ML_AA_OFFER";
	private static final String STR_VOLT_TABLES = "voltTables - ";
	private Client voltDbClient;
	private static final String CS_TYPE = "CS_TYPE";
	public static final String MSISDN_PREFIX_ZERO = "0";
	private static String countrycode = "243";
	private static int rangelen = 8;

	static {
		try {
			countrycode = PropertiesLoader.getValue(J4UOfferConstants.ROUTER_COUNTRY_CODE);
			rangelen = PropertiesLoader.getIntValue(J4UOfferConstants.ROUTER_MSISDN_RANGE_LENGTH);
		} catch (Exception ex) {
			LOG.error("LookUPDAO Init Exception ", ex);
		}
	}

	public LookUpDAO() throws Exception {
		voltDbClient = DAOFactory.getClient();
	}

	/**
	 * @param subMenuType
	 *            - J4U Voice ,J4U Data and J4U Integrated & SMS
	 * @param msisdn
	 * @throws Exception
	 */
	public JSONArray getOfferForTgtMLUser(String msisdn, String subMenuType, String prodSubType) throws Exception {
		LOG.debug("getOfferForTgtMLUser from db ");
		VoltTable[] voltTables;
		String procName;
		if (prodSubType != null) {
			procName = PropertiesLoader.getValue(J4UOfferConstants.P_GET_OFFER_FROM_CACHE);
			voltTables = voltDbClient.callProcedure(procName, msisdn, subMenuType, prodSubType).getResults();
		} else {
			procName = "USSD_P_ML_OFFER_MSG_SELECT";
			voltTables = voltDbClient.callProcedure(procName, msisdn, subMenuType).getResults();
		}

		JSONArray offerAry = new JSONArray();
		if (voltTables[0].advanceRow()) {
			String productIds = voltTables[0].getString("PRODUCT_IDS");
			LOG.info("products ID " + productIds);
			String[] prodDetailsAry = productIds.split(",");
			String[] prodIdsAry = new String[prodDetailsAry.length];

			for (int i = 0; i < prodDetailsAry.length; i++) {
				if (prodDetailsAry[i].split("~").length == 2) {
					prodIdsAry[i] = prodDetailsAry[i].split("~")[0];
				} else {
					prodIdsAry[i] = prodDetailsAry[i];
				}
			}
			String productDescription = voltTables[0].getString("MENU_CONTENT");
			String[] productDescriptionArry = productDescription.trim().split("\\n");
			LOG.debug("productDescription   " + productDescription);

			LOG.debug("productDescriptionArry Size:: " + productDescriptionArry.length);
			ProductPriceCache priceCache = ProductPriceCache.instance();
			for (int i = 0; i < prodIdsAry.length; i++) {
				JSONObject offerJson = new JSONObject();
				String prodId = prodIdsAry[i];
				String prodDesc = "";
				prodDesc = productDescriptionArry[i];
				LOG.debug("prodDesc: " + prodDesc.substring(2));

				offerJson.put(OFFER_ID, prodId);
				offerJson.put(OFFER_DESC, prodDesc.substring(2));
				offerJson.put(AMOUNT, Integer.parseInt(priceCache.get(prodId)));
				offerAry.put(offerJson);
			}
		}
		LOG.debug("getOfferForTgtMLUser from db offerArray: " + offerAry);
		return offerAry;
	}
	/*
	 * public UserInfo getUserInfo(String msisdn) throws Exception { String sql
	 * = PropertiesLoader.getValue(USSD_P_ERED_T_REDUCED_CCR_SELECT);
	 * 
	 * if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
	 * msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD),
	 * ""); LOG.info("After replacing cntry code :: " + msisdn); }
	 * 
	 * VoltTable[] voltTables = voltDbClient.callProcedure(sql,
	 * msisdn).getResults(); UserInfo userInfo = null; while
	 * (voltTables[0].advanceRow()) { userInfo = new UserInfo();
	 * userInfo.setMsisdn(msisdn);
	 * userInfo.setMlFlag(J4UOfferConstants.FLAG_Y.equalsIgnoreCase(voltTables[0
	 * ].getString(ML_CUSTOMER_FLAG)));
	 * userInfo.setRandomFlag(J4UOfferConstants.FLAG_Y.equalsIgnoreCase(
	 * voltTables[0].getString(RANDOM_FLAG))); userInfo.setJFUEligible(
	 * J4UOfferConstants.FLAG_Y.equalsIgnoreCase(voltTables[0].getString(
	 * J4U_ELIGIBILITY)));
	 * userInfo.setLangCode(voltTables[0].getString(LANG_CODE));
	 * userInfo.setOfferRefreshFlag(voltTables[0].getString(OFFER_REFRESH_FLAG))
	 * ; userInfo.setaValue(voltTables[0].getString(A_VALUE));
	 * userInfo.setLocationRandomFlag(
	 * J4UOfferConstants.FLAG_Y.equalsIgnoreCase(voltTables[0].getString(
	 * LOCATION_RANDOM_FLAG))); userInfo.setMorningofferEligilibiltyFlag(
	 * voltTables[0].getString(J4UOfferConstants.MORNING_OFFER_ELIGIBILITY)); }
	 * 
	 * return userInfo; }
	 */

	public UserInfo getUserInfo(String msisdn) throws Exception {
		String sql = PropertiesLoader.getValue(USSD_P_ERED_T_REDUCED_CCR_SELECT);
		if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}
		LOG.debug("getUserInfo");
		VoltTable[] voltTables = voltDbClient.callProcedure(sql, msisdn).getResults();
		UserInfo userInfo = null;
		while (voltTables[0].advanceRow()) {
			userInfo = new UserInfo();
			userInfo.setMsisdn(msisdn);
			userInfo.setMlFlag(J4UOfferConstants.FLAG_Y.equalsIgnoreCase(voltTables[0].getString(ML_CUSTOMER_FLAG)));
			userInfo.setRandomFlag(J4UOfferConstants.FLAG_Y.equalsIgnoreCase(voltTables[0].getString(RANDOM_FLAG)));
			userInfo.setJFUEligible(
					J4UOfferConstants.FLAG_Y.equalsIgnoreCase(voltTables[0].getString(J4U_ELIGIBILITY)));
			userInfo.setLangCode(voltTables[0].getString(LANG_CODE));
			userInfo.setOfferRefreshFlag(voltTables[0].getString(OFFER_REFRESH_FLAG));
			userInfo.setRagUser(null != voltTables[0].getString("RAG_MSISDN") ? true : false);
			// userInfo.setMpesaUser(J4UOfferConstants.FLAG_Y.equalsIgnoreCase(voltTables[0].getString(MPESA_USER_FLAG)));
			userInfo.setPedEligibility(J4UOfferConstants.FLAG_Y
					.equalsIgnoreCase(voltTables[0].getString(J4UOfferConstants.PED_ELIGIBILITY)));
			// userInfo.setRagUser(null != voltTables[0].getString("RAG_MSISDN")
			// ? true : false);
			userInfo.setSagUser(null != voltTables[0].getString("SAG_MSISDN") ? true : false);
			userInfo.setaValue(voltTables[0].getString(A_VALUE));
			userInfo.setLocationRandomFlag(
					J4UOfferConstants.FLAG_Y.equalsIgnoreCase(voltTables[0].getString(LOCATION_RANDOM_FLAG)));
			userInfo.setMorningofferEligilibiltyFlag(
					voltTables[0].getString(J4UOfferConstants.MORNING_OFFER_ELIGIBILITY));
			userInfo.setPrefPayMethod(voltTables[0].getString(J4UOfferConstants.PREF_PAY_METHOD));
			LOG.debug("getUserInfo method ends");

		}
		return userInfo;
	}

	public boolean getRandomFlag(String msisdn) throws Exception {
		String sql = "USSD_P_GET_RANDOM_FLAG";
		String flagValue = "";
		if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}
		VoltTable[] voltTables = voltDbClient.callProcedure(sql, msisdn).getResults();
		while (voltTables[0].advanceRow()) {
			flagValue = voltTables[0].getString(RANDOM_FLAG);
		}
		return "Y".equalsIgnoreCase(flagValue) ? true : false;
	}

	public Map<String, String> getPoolIdForCellId() throws Exception {
		LOG.info("getPoolIdForCellId - START");
		Map<String, String> cellPoolMap = new HashMap<>();
		VoltTable[] voltTable;
		voltTable = voltDbClient
				.callProcedure(PropertiesLoader.getValue(J4UOfferConstants.LOCATION_MODULE_GET_POOL_ID).trim())
				.getResults();
		while (voltTable[0].advanceRow()) {
			cellPoolMap.put(voltTable[0].getString(J4UOfferConstants.CELL_ID), voltTable[0].getString(POOL_ID));
		}
		LOG.info("getPoolIdForCellId - END - Total Records -" + cellPoolMap.size());
		return cellPoolMap;
	}

	public Map<String, ProductInfo> getProductInfoMap(String procName) throws Exception {
		Map<String, ProductInfo> productInfoMap = new HashMap<>();

		VoltTable[] productInfoTbl = voltDbClient.callProcedure(procName).getResults();
		while (productInfoTbl[0].advanceRow()) {
			ProductInfo productInfo = new ProductInfo();
			String pid = productInfoTbl[0].getString(PRODUCT_ID);
			int langCd = (int) productInfoTbl[0].get(LANG_CODE, VoltType.INTEGER);
			productInfo.setProductID(pid);
			productInfo.setLangCode(langCd);
			productInfo.setProductDesc(productInfoTbl[0].getString(PRODUCT_DESC));
			productInfo.setbValue(productInfoTbl[0].getString(J4UOfferConstants.B_VALUE));
			productInfo.setcValue(productInfoTbl[0].getString(J4UOfferConstants.C_VALUE));
			productInfo.setProductType(productInfoTbl[0].getString(J4UOfferConstants.PRODUCT_TYPE));
			productInfo.setProductSubType(
					Utils.getSubCategoryType(productInfoTbl[0].getString(J4UOfferConstants.PRODUCT_SUBTYPE)));
			productInfoMap.put(pid + "_" + langCd, productInfo);
		}
		return productInfoMap;
	}

	public List<String> getOffersByBalanceAndRank(String msisdn, String prodType, String prodSubType, double accBal,
			List<String> evsList) throws Exception {
		StringBuilder procName;
		VoltTable[] voltTables;
		List<String> prodIdsList = new ArrayList<>();
		if (null != prodSubType) {
			procName = new StringBuilder("USSD_P_ML_OFFER_SELECT_SUB_CAT");
			voltTables = voltDbClient.callProcedure(procName.toString(), msisdn, prodType, prodSubType, accBal)
					.getResults();
		} else {
			procName = new StringBuilder("USSD_P_ML_OFFER_SELECT");
			// String msisndLastDigit = msisdn.substring((msisdn.length() - 1));
			voltTables = voltDbClient.callProcedure(procName.toString(), msisdn, prodType, accBal).getResults();
		}

		LOG.debug("procName=> " + procName);

		while (voltTables[0].advanceRow()) {
			prodIdsList.add(voltTables[0].getString(FIELD_PRODUCT_ID));
			evsList.add(voltTables[0].getString(FIELD_EXPECTED_VALUE));
		}

		return prodIdsList;

	}

	public List<String> getSocialOffersByBalanceAndRank(String msisdn, String prodType, double accBal,
			List<String> evsList) throws Exception {

		String procName = PropertiesLoader.getValue(J4UOfferConstants.J4U_SOCIAL_ML_OFFERS_BYRANK_PROC).trim();
		LOG.debug("procName=> " + procName);
		List<String> prodIdsList = new ArrayList<>();
		VoltTable[] voltTables = voltDbClient.callProcedure(procName, msisdn, accBal).getResults();
		while (voltTables[0].advanceRow()) {
			prodIdsList.add(voltTables[0].getString(FIELD_PRODUCT_ID));
			evsList.add(voltTables[0].getString(FIELD_EXPECTED_VALUE));
		}

		return prodIdsList;

	}

	public List<String> getOffersByExpectedValue(String msisdn, String prodType, String prodSubType,
			List<String> prodIdsList, List<String> evsList) throws Exception {
		StringBuilder procName;
		VoltTable[] voltTables;
		String[] prodIdsArr = prodIdsList.toArray(new String[0]);
		int limit = MAX_PRODIDS_CNT - prodIdsList.size();
		if (null != prodSubType) {
			procName = new StringBuilder("USSD_P_ML_OFFER_SELECT_BY_EXPVAL_SUB_CAT");
			voltTables = voltDbClient
					.callProcedure(procName.toString(), msisdn, prodType, prodSubType, (Object) prodIdsArr, limit)
					.getResults();
			LOG.debug("procName=> " + procName);
		} else {
			LOG.debug("msisdn: " + msisdn + " prodType :" + prodType + " prodIdsList" + prodIdsList);
			procName = new StringBuilder("USSD_P_ML_OFFER_SELECT_BY_EXPVAL");
			voltTables = voltDbClient.callProcedure(procName.toString(), msisdn, prodType, (Object) prodIdsArr, limit)
					.getResults();
		}

		// String msisndLastDigit = msisdn.substring((msisdn.length() - 1));

		while (voltTables[0].advanceRow()) {
			prodIdsList.add(voltTables[0].getString(FIELD_PRODUCT_ID));
			evsList.add(voltTables[0].getString(FIELD_EXPECTED_VALUE));
		}

		return prodIdsList;
	}

	public List<String> getSocialOffersByExpectedValue(String msisdn, String prodType, List<String> prodIdsList,
			List<String> evsList) throws Exception {

		LOG.debug("msisdn: " + msisdn + " prodType :" + prodType + " prodIdsList" + prodIdsList);
		String procName = PropertiesLoader.getValue(USSD_ML_SOCIAL_OFFER_BYEXP_PROC);
		// String msisndLastDigit = msisdn.substring((msisdn.length() - 1));
		LOG.debug("procName=> " + procName);
		String[] prodIdsArr = prodIdsList.toArray(new String[0]);
		int limit = MAX_PRODIDS_CNT - prodIdsList.size();

		VoltTable[] voltTables = voltDbClient.callProcedure(procName, msisdn, (Object) prodIdsArr, limit).getResults();
		while (voltTables[0].advanceRow()) {
			prodIdsList.add(voltTables[0].getString(FIELD_PRODUCT_ID));
			evsList.add(voltTables[0].getString(FIELD_EXPECTED_VALUE));
		}

		return prodIdsList;
	}

	public Map<String, String> getProductPriceMap() throws Exception {
		LOG.debug("getProductPriceMap - START");
		String procName = PropertiesLoader.getValue(ECMP_P_GET_PRODUCT_PRICE);
		Map<String, String> productPriceMap = new HashMap<>();
		VoltTable[] voltTables = voltDbClient.callProcedure(procName).getResults();
		LOG.debug(STR_VOLT_TABLES + voltTables[0].getRowCount());
		while (voltTables[0].advanceRow()) {
			productPriceMap.put(voltTables[0].getString(PRODUCT_ID), voltTables[0].getString(PRODUCT_PRICE));
		}
		LOG.debug("getProductPriceMap - END");
		return productPriceMap;
	}

	public Map<String, TemplateDTO> getTemplatesMap() throws Exception {
		Map<String, TemplateDTO> templatesMap = new HashMap<>();
		String sql = "SELECT * FROM ECMP_T_COMMS_TEMPLATE";
		VoltTable[] templatesTbl = voltDbClient.callProcedure("@AdHoc", sql).getResults();
		while (templatesTbl[0].advanceRow()) {
			String id = templatesTbl[0].getString(TEMPLATE_ID);
			int langCd = (int) templatesTbl[0].get(LANG_CODE, VoltType.INTEGER);
			String template = templatesTbl[0].getString(TEMPLATE);
			String offerOrderCSV = templatesTbl[0].getString(OFFER_ORDER_CSV);
			TemplateDTO templateDTO = new TemplateDTO();
			templateDTO.setLangCd(langCd);
			templateDTO.setOfferOrderCSV(offerOrderCSV);
			templateDTO.setTempalteId(id);
			templateDTO.setTemplate(template);
			templatesMap.put(id, templateDTO);
		}
		return templatesMap;
	}

	public String getProductTypeById(String productId) throws Exception {
		String procName = "USSD_P_GET_PRODUCT_PRICE";
		VoltTable[] voltTables = voltDbClient.callProcedure(procName, productId).getResults();
		if (voltTables[0].advanceRow()) {
			String prodType = voltTables[0].getString("PRODUCT_TYPE");
			return prodType;

		}
		return null;
	}

	public Map<String, TemplateDTO> getMLTemplatesMap() throws Exception {
		Map<String, TemplateDTO> templatesMap = new HashMap<>();
		String sql = "select * from ECMP_T_COMMS_TEMPLATE where TEMPLATE_ID like 'ML%' ";
		VoltTable[] templatesTbl = voltDbClient.callProcedure("@AdHoc", sql).getResults();
		while (templatesTbl[0].advanceRow()) {
			String templateId = templatesTbl[0].getString(TEMPLATE_ID);
			int langCd = (int) templatesTbl[0].get(LANG_CODE, VoltType.INTEGER);
			TemplateDTO templateDTO = new TemplateDTO();
			templateDTO.setTempalteId(templateId);
			templateDTO.setLangCd(langCd);
			templateDTO.setOfferOrderCSV(templatesTbl[0].getString(OFFER_ORDER_CSV));
			templateDTO.setTemplate(templatesTbl[0].getString(TEMPLATE));
			templatesMap.put(templateId + "_" + langCd, templateDTO);
		}

		LOG.debug("ML Templates cache loaded :: templatesMap size = " + templatesMap.size());

		return templatesMap;
	}

	public RankingFormulae getRFParams(String msisdn, String prodType, String prodSubType, int langCode,
			RankingFormulae rankingFormulae) throws Exception {

		VoltTable[] voltTables;
		LOG.debug("getRFParams for - ");
		if (null != prodSubType) {
			LOG.debug("MSISDN - " + msisdn + "----" + prodType + "procedure Name=> "
					+ PropertiesLoader.getValue(J4UOfferConstants.USSD_P_ML_AA_OFFER_SUB_TYPE));
			voltTables = voltDbClient
					.callProcedure(PropertiesLoader.getValue(J4UOfferConstants.USSD_P_ML_AA_OFFER_SUB_TYPE), prodType,
							msisdn, prodSubType)
					.getResults();
		} else {
			LOG.debug("MSISDN - " + msisdn + "----" + prodType + "procedure Name=> " + USSD_P_ML_AA_OFFER);
			voltTables = voltDbClient.callProcedure(USSD_P_ML_AA_OFFER, prodType, msisdn).getResults();
		}

		List<OfferParams> offerParamsList = new ArrayList<>();
		List<String> prodIdsList = new ArrayList<>();
		OfferParams offerParams;
		while (voltTables[0].advanceRow()) {
			offerParams = new OfferParams();
			offerParams.setOfferPrice(Long.parseLong(voltTables[0].getString(FIELD_PRODUCT_PRICE)));
			offerParams.setExpectedValue(Float.parseFloat(voltTables[0].getString(FIELD_EXPECTED_VALUE)));
			offerParams.setOfferId(voltTables[0].getString(FIELD_PRODUCT_ID));
			prodIdsList.add(voltTables[0].getString(FIELD_PRODUCT_ID));

			offerParamsList.add(offerParams);
		}
		rankingFormulae.setOfferParams(getOfferValues(msisdn, prodType, offerParamsList, prodIdsList, langCode));
		LOG.debug("getRFParams - END");
		return rankingFormulae;
	}

	public RankingFormulae getSocialRFParams(String msisdn, String prodType, int langCode,
			RankingFormulae rankingFormulae) throws Exception {
		LOG.debug("getSocialRFParams for - ");
		String procName = PropertiesLoader.getValue(J4UOfferConstants.J4U_SOCIAL_OFFERS_PROC).trim();
		LOG.debug("MSISDN - " + msisdn + "----" + prodType + "procedure Name=> " + procName);
		// String prodType =
		// Utils.productCategory.get(queryBaljson.getString(CATEGORY));
		VoltTable[] voltTables = voltDbClient.callProcedure(procName, msisdn).getResults();
		List<OfferParams> offerParamsList = new ArrayList<>();
		List<String> prodIdsList = new ArrayList<>();
		OfferParams offerParams;
		LOG.debug(STR_VOLT_TABLES + voltTables[0].getRowCount());
		while (voltTables[0].advanceRow()) {
			offerParams = new OfferParams();
			offerParams.setOfferPrice(Long.parseLong(voltTables[0].getString(FIELD_PRODUCT_PRICE)));
			offerParams.setExpectedValue(Float.parseFloat(voltTables[0].getString(FIELD_EXPECTED_VALUE)));
			offerParams.setOfferId(voltTables[0].getString(FIELD_PRODUCT_ID));
			prodIdsList.add(voltTables[0].getString(FIELD_PRODUCT_ID));
			offerParamsList.add(offerParams);
		}
		rankingFormulae.setOfferParams(getSocialOfferValues(msisdn, prodType, offerParamsList, prodIdsList, langCode));
		LOG.debug("getRFParams - END");
		return rankingFormulae;
	}

	public List<OfferParams> getOfferValues(String msisdn, String prodType, List<OfferParams> offerParamsList,
			List<String> prodIdsList, int langCode) throws Exception {

		LOG.debug("getOfferValues - prodType=>" + prodType + " langCode=>" + langCode);
		LOG.debug("prodIdsArr=>" + prodIdsList);
		LOG.debug("ml offerParamsList => " + offerParamsList);
		List<OfferParams> offerParamsList1 = new ArrayList<>();
		ProductInfoCache productInfoCache = ProductInfoCache.instance();
		for (int offerCount = 0; offerCount < offerParamsList.size(); offerCount++) {
			OfferParams offerParams = offerParamsList.get(offerCount);
			ProductInfo productInfo = productInfoCache.getML(offerParams.getOfferId() + "_" + langCode);
			if (null != productInfo) {
				offerParams.setbValue(calculateFloatValue(productInfo.getbValue()));
				offerParams.setcValue(calculateFloatValue(productInfo.getcValue()));
				offerParamsList1.add(offerParams);
			}
		}
		LOG.debug("getOfferValues - END=>  ml offer=>" + offerParamsList1);
		return offerParamsList1;
	}

	public List<OfferParams> getSocialOfferValues(String msisdn, String prodType, List<OfferParams> offerParamsList,
			List<String> prodIdsList, int langCode) throws Exception {

		LOG.debug("getSocialOfferValues - prodType=>" + prodType + " langCode=>" + langCode);
		LOG.debug("prodIdsArr=>" + prodIdsList);
		LOG.debug("ml offerParamsList => " + offerParamsList);
		List<OfferParams> offerParamsList1 = new ArrayList<>();
		ProductInfoCache productInfoCache = ProductInfoCache.instance();
		for (int offerCount = 0; offerCount < offerParamsList.size(); offerCount++) {
			OfferParams offerParams = offerParamsList.get(offerCount);
			ProductInfo productInfo = productInfoCache.getSocial(offerParams.getOfferId() + "_" + langCode);
			if (null != productInfo) {
				offerParams.setbValue(calculateFloatValue(productInfo.getbValue()));
				offerParams.setcValue(calculateFloatValue(productInfo.getcValue()));
				offerParamsList1.add(offerParams);
			}
		}
		LOG.debug("getSocialOfferValues - END=>  ml offer=>" + offerParamsList1.size());
		return offerParamsList1;
	}

	public Map<String, ProductInfo> getLocationProductInfoMap() throws Exception {
		Map<String, ProductInfo> productInfoMap = new HashMap<>();

		VoltTable[] productInfoTbl = voltDbClient
				.callProcedure(PropertiesLoader.getValue(ENBA_P_GET_LOCATION_INFO).trim()).getResults();
		while (productInfoTbl[0].advanceRow()) {
			ProductInfo productInfo = new ProductInfo();
			String pid = productInfoTbl[0].getString(PRODUCT_ID);
			int langCd = (int) productInfoTbl[0].get(LANG_CODE, VoltType.INTEGER);
			productInfo.setProductID(pid);
			productInfo.setLangCode(langCd);
			productInfo.setProductDesc(productInfoTbl[0].getString(PRODUCT_DESC));
			productInfo.setbValue(productInfoTbl[0].getString(J4UOfferConstants.B_VALUE));
			productInfo.setcValue(productInfoTbl[0].getString(J4UOfferConstants.C_VALUE));
			productInfo.setPoolID(productInfoTbl[0].getString(POOL_ID));
			productInfo.setProductType(productInfoTbl[0].getString(J4UOfferConstants.PRODUCT_TYPE));
			productInfo.setProductSubType(
					Utils.getSubCategoryType(productInfoTbl[0].getString(J4UOfferConstants.PRODUCT_SUBTYPE)));
			productInfoMap.put(pid + "_" + langCd, productInfo);

		}

		return productInfoMap;
	}

	public List<OfferParams> getOfferLocationValues(String msisdn, String prodType, List<OfferParams> offerParamsList,
			List<String> prodIdsList, int langCode, String poolId) throws Exception {
		LOG.debug("getOfferLocationValues For Location- ");
		List<OfferParams> offerParamsArrayList = new ArrayList<>();
		ProductInfoCache productInfoCache = ProductInfoCache.instance();
		for (int offerCount = 0; offerCount < offerParamsList.size(); offerCount++) {
			OfferParams offerParams = offerParamsList.get(offerCount);
			ProductInfo productInfo = productInfoCache.getLocation(offerParams.getOfferId() + "_" + langCode);
			if (null != productInfo && poolId.equalsIgnoreCase(productInfo.getPoolID())) {
				offerParams.setbValue(calculateFloatValue(productInfo.getbValue()));
				offerParams.setcValue(calculateFloatValue(productInfo.getcValue()));
				offerParams.setProductDescription(productInfo.getProductDesc());

				offerParamsArrayList.add(offerParams);
			}
		}
		LOG.debug("offerParamsArrayList for location=>" + offerParamsArrayList);
		return offerParamsArrayList;
	}

	private static float calculateFloatValue(String value) {
		if (value == null || value.equals("")) {
			return 1;
		} else {
			return Float.parseFloat(value);
		}
	}

	public RankingFormulae getRFParamsForLocation(String msisdn, String prodType, String prodSubType, int langCode,
			RankingFormulae rankingFormulae, String poolId) throws Exception {
		LOG.debug("getRFParams for - ");
		String procName;
		VoltTable[] voltTables;
		if (null != prodSubType) {
			procName = PropertiesLoader.getValue(J4UOfferConstants.ENBA_J4U_ML_LOCATION_OFFER_SUB_CAT).trim();
			LOG.debug("MSISDN - " + msisdn + "---prodType -" + prodType + ", Procedure Name=> " + procName);
			voltTables = voltDbClient.callProcedure(procName, prodType, msisdn, prodSubType).getResults();
		} else {
			procName = PropertiesLoader.getValue(ECMP_P_J4U_ML_LOCATION_OFFER).trim();
			LOG.debug("MSISDN - " + msisdn + "---prodType -" + prodType + ", Procedure Name=> " + procName);
			voltTables = voltDbClient.callProcedure(procName, prodType, msisdn).getResults();
		}

		List<OfferParams> offerParamsList = new ArrayList<>();
		List<String> prodIdsList = new ArrayList<>();
		OfferParams offerParams;
		while (voltTables[0].advanceRow()) {
			offerParams = new OfferParams();
			offerParams.setOfferPrice(Long.parseLong(voltTables[0].getString(FIELD_PRODUCT_PRICE)));
			offerParams.setExpectedValue(Float.parseFloat(voltTables[0].getString(FIELD_EXPECTED_VALUE)));
			offerParams.setOfferId(voltTables[0].getString(FIELD_PRODUCT_ID));
			prodIdsList.add(voltTables[0].getString(FIELD_PRODUCT_ID));
			offerParamsList.add(offerParams);
		}
		rankingFormulae.setPoolId(poolId);
		LOG.info("offer list location based offers => :: " + offerParamsList);
		rankingFormulae.setOfferParams(
				getOfferLocationValues(msisdn, prodType, offerParamsList, prodIdsList, langCode, poolId));
		LOG.debug("getRFParams - END");
		return rankingFormulae;
	}

	public String getProductDescRandomLocationOffers(String productId, int languageCode)
			throws NoConnectionsException, IOException, ProcCallException {
		String procName = "ECMP_P_GET_PRODUCT_DESC_LOCATION";
		VoltTable[] voltTables = voltDbClient.callProcedure(procName, productId, languageCode).getResults();
		if (voltTables[0].advanceRow()) {
			return voltTables[0].getString("PRODUCT_DESC");
		}
		return null;
	}

	public String getCSType(String msisdn) {
		String csType = J4UOfferConstants.CS_TYPE_HW;
		String finalmsisdn = validateMSISDN(msisdn);
		String msisdnrange = finalmsisdn.substring(0, rangelen);
		try {
			csType = queryCSTypeForRange(msisdnrange);
			if (csType.equalsIgnoreCase(J4UOfferConstants.CS_TYPE_IM)) {
				csType = queryCSTypeForMSISDN(finalmsisdn);
			}
		} catch (Exception e) {
			LOG.error("RouterDao::getCSType Exception ", e);
		}

		return csType;
	}

	private String queryCSTypeForRange(String msisdnrange) throws Exception {
		String csType = J4UOfferConstants.CS_TYPE_HW;
		LOG.debug("queryCSTypeForRange BEGIN - MSISDNRANGE :: " + msisdnrange);

		String procedure = PropertiesLoader.getValue(J4UOfferConstants.ROUTER_CS_MSISDNRANGE_QUERY);

		VoltTable[] voltTable = voltDbClient.callProcedure(procedure, msisdnrange).getResults();
		if (voltTable[0].getRowCount() != 0) {
			VoltTableRow vRow = voltTable[0].fetchRow(0);
			csType = vRow.getString(CS_TYPE);
		}
		LOG.info("queryCSTypeForRange - MSISDNRANGE|CSTYPE - " + msisdnrange + "|" + csType);

		return csType;
	}

	private String queryCSTypeForMSISDN(String msisdn) throws Exception {
		String csType = J4UOfferConstants.CS_TYPE_HW;
		LOG.debug("queryCSTypeForMSISDN BEGIN - MSISDN :: " + msisdn);

		String procedure = PropertiesLoader.getValue(J4UOfferConstants.ROUTER_CS_MSISDN_QUERY);

		VoltTable[] voltTable = voltDbClient.callProcedure(procedure, msisdn).getResults();
		if (voltTable[0].getRowCount() != 0) {
			VoltTableRow vRow = voltTable[0].fetchRow(0);
			csType = vRow.getString(CS_TYPE);
		}
		LOG.info("queryCSTypeForMSISDN - MSISDN|CSTYPE - " + msisdn + "|" + csType);
		return csType;
	}

	private String validateMSISDN(String msisdn) {
		String finalmsisdn = msisdn;
		if (!msisdn.startsWith(countrycode)) {
			StringBuilder msisdnbuilder = new StringBuilder();
			if (msisdn.startsWith(MSISDN_PREFIX_ZERO)) {
				msisdnbuilder.append(countrycode);
				msisdnbuilder.append(msisdn.substring(1));
			} else {
				msisdnbuilder.append(countrycode);
				msisdnbuilder.append(msisdn);
			}
			finalmsisdn = msisdnbuilder.toString();
		}

		return finalmsisdn;
	}

	public Map<String, ProductInfo> getMorningProductInfoMap() throws Exception {
		Map<String, ProductInfo> productInfoMap = new HashMap<>();
		String procName = PropertiesLoader.getValue(J4UOfferConstants.J4U_MORNING_OFFER_PROD_INFO);
		VoltTable[] productInfoTbl = voltDbClient.callProcedure(procName).getResults();
		while (productInfoTbl[0].advanceRow()) {
			ProductInfo productInfo = new ProductInfo();
			String pid = productInfoTbl[0].getString(PRODUCT_ID);
			int langCd = (int) productInfoTbl[0].get(LANG_CODE, VoltType.INTEGER);
			productInfo.setProductID(pid);
			productInfo.setLangCode(langCd);
			productInfo.setProductDesc(productInfoTbl[0].getString(PRODUCT_DESC));
			productInfoMap.put(pid + "_" + langCd, productInfo);
		}
		return productInfoMap;
	}

	public List<String> getMorningOfferWhiteList(String msisdn) throws Exception {
		LOG.info("getMorningOfferWhiteList-- START :: ");
		List<String> morningofferList = new ArrayList<>();
		String procName = PropertiesLoader.getValue(J4UOfferConstants.J4U_ENBA_MORNING_OFFER).trim();
		VoltTable[] voltTable;
		voltTable = voltDbClient.callProcedure(procName, msisdn).getResults();
		while (voltTable[0].advanceRow()) {
			morningofferList.add(voltTable[0].getString(J4UOfferConstants.PRODUCT_ID));
		}
		LOG.info("getMorningOfferWhiteList -- END :: Total Records - " + morningofferList);
		return morningofferList;
	}

	public String getSubCaegory(String Productype, String channel)
			throws ConfigException, NoConnectionsException, IOException, ProcCallException {
		String procName = PropertiesLoader.getValue(J4UOfferConstants.J4U_ENBA_MORNING_OFFER).trim();
		String subCategory = null;
		VoltTable[] voltTable;
		voltTable = voltDbClient.callProcedure(procName, Productype, channel).getResults();
		while (voltTable[0].advanceRow()) {
			subCategory = (voltTable[0].getString(J4UOfferConstants.PRODUCT_ID));
		}
		return subCategory;

	}

	public String getRagFlag(String msisdn)
			throws ConfigException, NoConnectionsException, IOException, ProcCallException {
		String procName = PropertiesLoader.getValue(J4UOfferConstants.ERED_P_GET_RAG_FLAG).trim();
		String ragFlag = null;
		VoltTable[] voltTable = voltDbClient.callProcedure(procName, msisdn).getResults();
		if (voltTable[0].getRowCount() != 0) {
			VoltTableRow vRow = voltTable[0].fetchRow(0);
			ragFlag = vRow.getString(J4UOfferConstants.RAG_OPT_FLAG);
		}
		LOG.info("getRagFlag - MSISDN|ragOptFlag - " + msisdn + "|" + ragFlag);

		return ragFlag;
	}

	public List<String> getRagpublicValues() throws Exception {
		List<String> ragDefValues = new ArrayList<>();
		VoltTable[] voltTables = voltDbClient.callProcedure(J4UOfferConstants.USSD_P_ML_RAG_USER_RECORD_SELECT)
				.getResults();
		if (voltTables[0].getRowCount() > 0) {
			while (voltTables[0].advanceRow()) {
				ragDefValues.add(voltTables[0].getString(J4UOfferConstants.WEEK_START_DATE));
				ragDefValues.add(voltTables[0].getString(J4UOfferConstants.WEEK_END_DATE));
				ragDefValues.add(voltTables[0].getString(J4UOfferConstants.NEXT_AVAILABLE_OFFER_DATE));
			}
		}
		return ragDefValues;
	}

	public List<String> getSagpublicValues() throws Exception {
		List<String> sagDefValues = new ArrayList<>();
		VoltTable[] voltTables = voltDbClient.callProcedure(J4UOfferConstants.USSD_P_ML_SAG_USER_RECORD_SELECT)
				.getResults();
		if (voltTables[0].getRowCount() > 0) {
			while (voltTables[0].advanceRow()) {
				sagDefValues.add(voltTables[0].getString(J4UOfferConstants.WEEK_START_DATE));
				sagDefValues.add(voltTables[0].getString(J4UOfferConstants.WEEK_END_DATE));
				sagDefValues.add(voltTables[0].getString(J4UOfferConstants.NEXT_AVAILABLE_OFFER_DATE));
			}
		}
		return sagDefValues;
	}

	public RAGAndSAGUserInfo getRagUserInfo(JSONObject requestJSON) throws Exception {
		String procedure = "USSD_P_ML_RAG_INFO_SELECT";
		RAGAndSAGUserInfo userRAGInfo = new RAGAndSAGUserInfo();
		String msisdn = requestJSON.getString(J4UOfferConstants.MSISDN);
		HashMap<String, String> ragInfo = null;
		if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}
		VoltTable[] voltTables = voltDbClient.callProcedure(procedure, msisdn, requestJSON.getInt(LANG_CODE))
				.getResults();

		if (voltTables[0].getRowCount() > 0) {
			while (voltTables[0].advanceRow()) {
				userRAGInfo.setRagEligibleFlag(
						("".equals(voltTables[0].getString("MSISDN")) || voltTables[0].getString("MSISDN").isEmpty())
								? false : true);
				userRAGInfo.setRagNeverOptInFlag(
						"N".equalsIgnoreCase(voltTables[0].getString("RAG_OPT_FLAG")) ? true : false);
				userRAGInfo
						.setRagOptInFlag("Y".equalsIgnoreCase(voltTables[0].getString("RAG_OPT_FLAG")) ? true : false);
				userRAGInfo.setRagGoalReachedFlag(
						"Y".equalsIgnoreCase(voltTables[0].getString("RAG_GOAL_REACHED_FLAG")) ? true : false);
				ragInfo = new HashMap<String, String>();
				ragInfo.put("REWARD_CODE", voltTables[0].getString("REWARD_CODE"));
				ragInfo.put("REWARD_INFO", voltTables[0].getString("REWARD_INFO"));
				ragInfo.put("PRODUCT_VALIDITY", voltTables[0].getString("PRODUCT_VALIDITY"));
				ragInfo.put("PRODUCT_VALUE", voltTables[0].getString("PRODUCT_VALUE"));
				ragInfo.put("LAST_RECHARGE_TIME", voltTables[0].getString("LAST_RECHARGE_TIME"));
				ragInfo.put(J4UOfferConstants.WEEK_END_DATE, voltTables[0].getString(J4UOfferConstants.WEEK_END_DATE));
				ragInfo.put(J4UOfferConstants.NEXT_AVAILABLE_OFFER_DATE,
						voltTables[0].getString(J4UOfferConstants.NEXT_AVAILABLE_OFFER_DATE));
				ragInfo.put(J4UOfferConstants.REMAINING_EFFORT,
						String.valueOf((int) voltTables[0].get(J4UOfferConstants.REMAINING_EFFORT, VoltType.INTEGER)));
				ragInfo.put(J4UOfferConstants.RECHARGE_TARGET,
						String.valueOf((int) voltTables[0].get(J4UOfferConstants.RECHARGE_TARGET, VoltType.INTEGER)));
				userRAGInfo.setRagInfo(ragInfo);
			}
		} else {
			userRAGInfo.setRagEligibleFlag(false);
			userRAGInfo.setRagOptInFlag(false);
			userRAGInfo.setRagNeverOptInFlag(true);
			userRAGInfo.setRagGoalReachedFlag(false);
		}

		return userRAGInfo;

	}

	public RAGAndSAGUserInfo getSagUserInfo(JSONObject requestJson) throws Exception {
		String procedure = "USSD_P_ML_SAG_INFO_SELECT";
		RAGAndSAGUserInfo saguserInfo = new RAGAndSAGUserInfo();
		String msisdn = requestJson.getString(J4UOfferConstants.MSISDN);
		HashMap<String, String> sagInfo = null;
		if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}
		VoltTable[] voltTables = voltDbClient.callProcedure(procedure, msisdn, requestJson.getInt(LANG_CODE))
				.getResults();

		if (voltTables[0].getRowCount() > 0) {
			LOG.info("rowcount  ::" + voltTables[0].getRowCount());
			while (voltTables[0].advanceRow()) {
				LOG.info("Inside while loop ");
				saguserInfo.setSagEligibleFlag(
						("".equals(voltTables[0].getString("MSISDN")) || voltTables[0].getString("MSISDN").isEmpty())
								? false : true);
				saguserInfo.setSagNeverOptInFlag(
						"N".equalsIgnoreCase(voltTables[0].getString("SAG_OPT_FLAG")) ? true : false);
				saguserInfo
						.setSagOptInFlag("Y".equalsIgnoreCase(voltTables[0].getString("SAG_OPT_FLAG")) ? true : false);
				saguserInfo.setSagGoalReachedFlag(
						"Y".equalsIgnoreCase(voltTables[0].getString("SAG_GOAL_REACHED_FLAG")) ? true : false);
				sagInfo = new HashMap<String, String>();
				sagInfo.put("REWARD_CODE", voltTables[0].getString("REWARD_CODE"));
				sagInfo.put("REWARD_INFO", voltTables[0].getString("REWARD_INFO"));
				sagInfo.put("PRODUCT_VALIDITY", voltTables[0].getString("PRODUCT_VALIDITY"));
				sagInfo.put("PRODUCT_VALUE", voltTables[0].getString("PRODUCT_VALUE"));
				sagInfo.put("LAST_SPEND_TIME", voltTables[0].getString("LAST_SPEND_TIME"));
				sagInfo.put(J4UOfferConstants.WEEK_END_DATE, voltTables[0].getString(J4UOfferConstants.WEEK_END_DATE));
				sagInfo.put(J4UOfferConstants.NEXT_AVAILABLE_OFFER_DATE,
						voltTables[0].getString(J4UOfferConstants.NEXT_AVAILABLE_OFFER_DATE));
				sagInfo.put("REMAINING_EFFORT",
						String.valueOf((int) voltTables[0].get("REMAINING_EFFORT", VoltType.INTEGER)));
				sagInfo.put("SPEND_TARGET", String.valueOf((int) voltTables[0].get("SPEND_TARGET", VoltType.INTEGER)));
				saguserInfo.setSagInfo(sagInfo);
			}
		} else {
			saguserInfo.setSagEligibleFlag(false);
			saguserInfo.setSagOptInFlag(false);
			saguserInfo.setSagNeverOptInFlag(true);
			saguserInfo.setSagGoalReachedFlag(false);
		}
		LOG.info("radandsagUserInfo Eligibility  isSagEligibleFlag :: " + saguserInfo.isSagEligibleFlag()
				+ " isSagOptInFlag ::" + saguserInfo.isSagOptInFlag());
		return saguserInfo;

	}

	public String getRAGOptinStatus(String msisdn)
			throws ConfigException, NoConnectionsException, IOException, ProcCallException {
		String procName = J4UOfferConstants.ERED_P_GET_RAG_OPTFLAG;
		if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}
		String ragOptFlag = null;
		VoltTable[] voltTable;
		voltTable = voltDbClient.callProcedure(procName, msisdn).getResults();
		while (voltTable[0].advanceRow()) {
			ragOptFlag = (voltTable[0].getString(J4UOfferConstants.RAG_OPT_FLAG));
		}
		return ragOptFlag;
	}

	public String getSAGOptinStatus(String msisdn)
			throws ConfigException, NoConnectionsException, IOException, ProcCallException {
		String procName = J4UOfferConstants.ERED_P_GET_SAG_OPTFLAG;
		String sagOptFlag = null;
		if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
			LOG.info("After replacing cntry code :: " + msisdn);
		}
		VoltTable[] voltTable;
		voltTable = voltDbClient.callProcedure(procName, msisdn).getResults();
		while (voltTable[0].advanceRow()) {
			LOG.info("SAG OPT Status :: " + voltTable[0].getString(J4UOfferConstants.SAG_OPT_FLAG));
			sagOptFlag = (voltTable[0].getString(J4UOfferConstants.SAG_OPT_FLAG));
		}
		return sagOptFlag;
	}

	public boolean isMsisdnBlacklist(String msisdn) throws Exception {
        boolean isBlacklist = false;
        String procedure = PropertiesLoader.getValue(GET_BLACKLIST_MSISDN);
        if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}
        VoltTable[] voltTable = voltDbClient.callProcedure(procedure, msisdn).getResults();
        if (voltTable[0].getRowCount() != 0) {
	        while (voltTable[0].advanceRow()) {
	        	isBlacklist = null != voltTable[0].getString(MSISDN) ? true : false;            
	        }
        }
        return isBlacklist;
    }
	
}
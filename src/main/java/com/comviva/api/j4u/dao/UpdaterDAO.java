/**
 *
 */
package com.comviva.api.j4u.dao;

import com.comviva.api.exception.ConfigException;

import com.comviva.api.j4u.config.PropertiesLoader;
import com.comviva.api.j4u.model.ApiLog;
import com.comviva.api.j4u.model.RAGAndSAGUserInfo;
import com.comviva.api.j4u.model.UserInfo;
import com.comviva.api.j4u.utils.J4UOfferConstants;
import com.comviva.api.j4u.utils.Utils;
import com.comviva.voltdb.factory.DAOCallback;
import com.comviva.voltdb.factory.DAOFactory;
import org.apache.log4j.Logger;
import org.voltdb.client.Client;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static com.comviva.api.j4u.utils.J4UOfferConstants.ECMP_T_J4U_API_LOG_INSERT;
import static com.comviva.api.j4u.utils.J4UOfferConstants.INSERT_RAG_USER_CAT_PROC_NAME;
import static com.comviva.api.j4u.utils.J4UOfferConstants.J4U_LOG_PROC_NAME;
import static com.comviva.api.j4u.utils.J4UOfferConstants.J4U_TRX_PRODID_MAP_NAME;
import static com.comviva.api.j4u.utils.J4UOfferConstants.SUBID_CNTRYCD;
import static com.comviva.api.j4u.utils.J4UOfferConstants.SUBID_LENGTH;

/**
 * DAO class for all CUD operations of USSDPlugin
 */
public class UpdaterDAO {
	private static final Logger LOG = Logger.getLogger(UpdaterDAO.class);
	private Client voltDbClient = null;

	public UpdaterDAO() throws Exception {
		voltDbClient = DAOFactory.getClient();
	}

	private void executeProc(final String sql, final Object... args) throws Exception {
		voltDbClient.callProcedure(new DAOCallback(), sql, args);
	}

	public void insertLog(final Object[] args) throws Exception {
		String sql = PropertiesLoader.getValue(J4U_LOG_PROC_NAME);
		executeProc(sql, args);
	}

	public void insertTrxProdIdMap(UserInfo userInfo, String pid1, String pid2, String pid3) throws Exception {
		String sql = PropertiesLoader.getValue(J4U_TRX_PRODID_MAP_NAME);
		Date currentDate = Utils.getCurrentTimeStamp();
		Object[] args = new Object[] { userInfo.getUserMsgRef(), userInfo.getTxId(), userInfo.getMsisdn(), pid1, pid2,
				pid3, null, null, currentDate, currentDate };
		executeProc(sql, args);
	}

	public void upsertMLOfferMsg(String msisdn, String subMenuType, String prodIds, String messageBody, String prodEvs)
			throws Exception {
		String insertSql = "USSD_P_ML_OFFER_MSG_UPSERT";
		Object[] args = new Object[] { msisdn, subMenuType, prodIds, messageBody, Utils.getCurrentTimeStamp(),
				prodEvs };

		executeProc(insertSql, args);
	}

	public void upsertNonSocialMLOfferMsg(String msisdn, String subMenuType, String prodSubType, String prodIds,
			String messageBody, String prodEvs) throws Exception {
		String insertSql;
		Object[] args;
		/*
		 * LOG.debug("upsertNonSocialMLOfferMsg  parametes :: " + msisdn + "||"
		 * + subMenuType + " ||" + prodIds + "|| " + messageBody + "" +
		 * prodSubType + "|| "+Utils.getCurrentTimeStamp()+"" + prodEvs);
		 */
		if (null != prodSubType) {
			insertSql = PropertiesLoader.getValue(J4UOfferConstants.P_UPSERT_OFFER_TO_CACHE);
			args = new Object[] { msisdn, subMenuType, prodSubType, prodIds, messageBody, Utils.getCurrentTimeStamp(),
					prodEvs };
			LOG.info("creating entry in ENBA_T_J4U_ML_OFFER_CACHE table for Sub Category  for the msisdn = " + msisdn);
		} else {
			insertSql = "USSD_P_ML_OFFER_MSG_UPSERT";
			args = new Object[] { msisdn, subMenuType, prodIds, messageBody, Utils.getCurrentTimeStamp(), prodEvs };
			LOG.debug("creating entry in ENBA_T_J4U_ML_OFFER_MSG table for the msisdn = " + msisdn);
		}
		executeProc(insertSql, args);

	}

	public void updateReducedCCR(String offerRefFlag, String subscriberId) throws Exception {
		String procName = "ECMP_P_UPDATE_REFRESH_FLAG";

		if (subscriberId.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			subscriberId = subscriberId.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}
		voltDbClient.callProcedure(new DAOCallback(), procName, offerRefFlag, subscriberId);
	}

	public void insertApiLog(ApiLog apiLog) {

		try {
			String sql = PropertiesLoader.getValue(ECMP_T_J4U_API_LOG_INSERT);
			LOG.debug("api log procedure name=>" + sql);
			executeProc(sql, apiLog.getTransactionId(), apiLog.getMsisdn(), apiLog.getApiType(), apiLog.getStatus(),
					apiLog.getProdType(), apiLog.getProdIds(), apiLog.getSelectedProdId(), apiLog.getCustomerBalance(),
					apiLog.getMlFlag(), apiLog.getRandomFlag(), apiLog.getDateTime(), apiLog.getComments(),
					apiLog.getRefNum(), apiLog.getThirdPartyRef(), apiLog.getChannel(), apiLog.getCellId(),
					apiLog.getPoolId());
		} catch (Exception e) {
			LOG.error("Error in api log insertion => ", e);
		}
	}

	public void insertRagUserRecordDB(Map<String, String> ragUserRecord) throws Exception {
		LOG.debug("insertRagUserRecordDB started");
		try {
			String procedure = "USSD_P_ML_RAG_USER_RECORD_INSERT";
			Object[] args = new Object[] { ragUserRecord.get("MSISDN"), ragUserRecord.get("MSISDN"),
					ragUserRecord.get("WEEK_START_DATE"), ragUserRecord.get("WEEK_END_DATE"),
					ragUserRecord.get("NEXT_AVAILABLE_OFFER_DATE"), ragUserRecord.get("REWARD_CODE"),
					ragUserRecord.get("TARGET_TYPE"), ragUserRecord.get("PAYMENT_METHOD"),
					ragUserRecord.get("RECHARGE_TARGET"), Utils.getCurrentTimeStamp() };
			executeProc(procedure, args);
		} catch (Exception e) {
			LOG.debug("insertRagUserRecordDB Exception");
		}
		LOG.debug("insertRagUserRecordDB ended");
	}
	
	public void insertSagUserRecordDB(Map<String, String> sagUserRecord) throws Exception {
		LOG.debug("insertSagUserRecordDB started");
		try {
			String procedure = "USSD_P_ML_SAG_USER_RECORD_INSERT";
			Object[] args = new Object[] { sagUserRecord.get("MSISDN"), sagUserRecord.get("MSISDN"),
					sagUserRecord.get("WEEK_START_DATE"), sagUserRecord.get("WEEK_END_DATE"),
					sagUserRecord.get("NEXT_AVAILABLE_OFFER_DATE"), sagUserRecord.get("REWARD_CODE"),
					sagUserRecord.get("TARGET_TYPE"), sagUserRecord.get("PAYMENT_METHOD"),
					sagUserRecord.get("SPEND_TARGET"), Utils.getCurrentTimeStamp() };
			executeProc(procedure, args);
		} catch (Exception e) {
			LOG.debug("insertSagUserRecordDB Exception");
		}
		LOG.debug("insertSagUserRecordDB ended");
	}
	
	

	public void insertRagUserCatFile(Map<String, String> ragUserRecord, String msisdn) throws Exception {
		try {
			String sql = PropertiesLoader.getValue(INSERT_RAG_USER_CAT_PROC_NAME);
			Object[] args = new Object[] { msisdn, msisdn, ragUserRecord.get("WEEK_START_DATE"),
					ragUserRecord.get("WEEK_END_DATE"), ragUserRecord.get("NEXT_AVAILABLE_OFFER_DATE"),
					ragUserRecord.get("REWARD_CODE"), ragUserRecord.get("TARGET_TYPE"),
					ragUserRecord.get("PAYMENT_METHOD"), ragUserRecord.get("RECHARGE_TARGET"),
					Utils.getCurrentTimeStamp() };
			executeProc(sql, args);
		} catch (Exception e) {
			LOG.debug("insertRagUserCatFile Exception");
		}

	}

	public void insertSagUserCatFile(Map<String, String> sagUserRecord, String msisdn) throws Exception {
		try {
			String sql = PropertiesLoader.getValue(J4UOfferConstants.INSERT_SAG_USER_CAT_PROC_NAME);
			Object[] args = new Object[] { msisdn, msisdn, sagUserRecord.get("WEEK_START_DATE"),
					sagUserRecord.get("WEEK_END_DATE"), sagUserRecord.get("NEXT_AVAILABLE_OFFER_DATE"),
					sagUserRecord.get("REWARD_CODE"), sagUserRecord.get("TARGET_TYPE"),
					sagUserRecord.get("PAYMENT_METHOD"), sagUserRecord.get("SPEND_TARGET"),
					Utils.getCurrentTimeStamp() };
			executeProc(sql, args);
		} catch (Exception e) {
			LOG.debug("insertSagUserCatFile Exception");
		}

	}

	
	public void updateRAGOptInfo(RAGAndSAGUserInfo userInfo)
			throws NoConnectionsException, IOException, ProcCallException, Exception {
		LOG.info("MSISDN :: " + userInfo.getMsisdn());
		String msisdn = userInfo.getMsisdn();
		if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}
		
		LOG.info("");
		LOG.debug("updateOptInfo :: isRagNeverOptInFlag = " + userInfo.isRagNeverOptInFlag());

		if (userInfo.isRagNeverOptInFlag()) {
			String procName = "USSD_P_INSERT_RAG_OPT_INFO";
			String optInfo = userInfo.isRagOptInFlag() ? "Y" : "N";
			voltDbClient.callProcedure(new USSDDAOCallback(), procName, msisdn,
					userInfo.getRagInfo().get("RECHARGE_TARGET"), userInfo.getRagInfo().get("RECHARGE_TARGET"),
					optInfo);
			String procedure = "USSD_P_INSERT_ECMP_OPT_INFO";
			voltDbClient.callProcedure(new USSDDAOCallback(), procedure, msisdn, userInfo.getTxId(), optInfo);

		} else {
			String procName = "USSD_P_UPDATE_RAG_OPT_INFO";
			String optInfo = userInfo.isRagOptInFlag() ? "Y" : "O";
			voltDbClient.callProcedure(new USSDDAOCallback(), procName, optInfo, msisdn);

			String procedure = "USSD_P_INSERT_ECMP_OPT_INFO";
			voltDbClient.callProcedure(new USSDDAOCallback(), procedure, msisdn, userInfo.getTxId(), optInfo);
		}

	}

	public void insertAlreadyOptInUserRAG(RAGAndSAGUserInfo ragAndSAGUserInfo)
			throws ConfigException, NoConnectionsException, IOException {
		String msisdn = ragAndSAGUserInfo.getMsisdn();
		if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}

		String procName = "USSD_P_UPDATE_RAG_OPT_INFO";
		String optInfo = ragAndSAGUserInfo.isRagOptInFlag() ? "Y" : "O";
		voltDbClient.callProcedure(new USSDDAOCallback(), procName, optInfo, msisdn);

		String procedure = "USSD_P_INSERT_ECMP_OPT_INFO";
		voltDbClient.callProcedure(new USSDDAOCallback(), procedure, msisdn, ragAndSAGUserInfo.getTxId(), optInfo);

	}

	public void upsertAlreadyOptInUserSAG(RAGAndSAGUserInfo ragAndSAGUserInfo)
			throws ConfigException, NoConnectionsException, IOException {
		String msisdn = ragAndSAGUserInfo.getMsisdn();
		if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}

		String procName = "USSD_P_UPDATE_SAG_OPT_INFO";
		String optInfo = ragAndSAGUserInfo.isSagOptInFlag() ? "Y" : "O";
		voltDbClient.callProcedure(new USSDDAOCallback(), procName, optInfo, msisdn);

		String procedure = "USSD_P_INSERT_ECMP_OPT_INFO_SAG";
		voltDbClient.callProcedure(new USSDDAOCallback(), procedure, msisdn, ragAndSAGUserInfo.getTxId(), optInfo);

	}

	/*public void updateOptInfoRAG(RAGAndSAGUserInfo radandsagUserInfo)
			throws NoConnectionsException, IOException, ProcCallException, Exception {
		String msisdn = radandsagUserInfo.getMsisdn();
		if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}

		LOG.debug("updateOptInfo :: isRagNeverOptInFlag = " + radandsagUserInfo.isRagNeverOptInFlag());

		if (radandsagUserInfo.isRagNeverOptInFlag()) {
			String procName = "USSD_P_INSERT_RAG_OPT_INFO";
			LOG.info("RAG OPTIn flag " + radandsagUserInfo.isRagOptInFlag());
			String optInfo = radandsagUserInfo.isRagOptInFlag() ? "Y" : "N";
			voltDbClient.callProcedure(new USSDDAOCallback(), procName, msisdn,
					radandsagUserInfo.getRagInfo().get("RECHARGE_TARGET"),
					radandsagUserInfo.getRagInfo().get("RECHARGE_TARGET"), optInfo);
			String procedure = "USSD_P_INSERT_ECMP_OPT_INFO";
			voltDbClient.callProcedure(new USSDDAOCallback(), procedure, msisdn, radandsagUserInfo.getTxId(), optInfo);

		}

	}
*/
	public void updateSAGOptInfo(RAGAndSAGUserInfo userInfo)
			throws NoConnectionsException, IOException, ProcCallException, Exception {
		String msisdn = userInfo.getMsisdn();
		if (msisdn.length() == PropertiesLoader.getIntValue(SUBID_LENGTH)) {
			msisdn = msisdn.replaceFirst(PropertiesLoader.getValue(SUBID_CNTRYCD), "");
		}

		LOG.debug("updateOptInfo :: isSagNeverOptInFlag = " + userInfo.isSagNeverOptInFlag());

		if (userInfo.isRagNeverOptInFlag()) {
			String procName = "USSD_P_INSERT_SAG_OPT_INFO";
			String optInfo = userInfo.isSagOptInFlag() ? "Y" : "N";
			voltDbClient.callProcedure(new USSDDAOCallback(), procName, msisdn,
					userInfo.getSagInfo().get("SPEND_TARGET"), userInfo.getSagInfo().get("SPEND_TARGET"),
					optInfo);
			String procedure = "USSD_P_INSERT_ECMP_OPT_INFO_SAG";
			voltDbClient.callProcedure(new USSDDAOCallback(), procedure, msisdn, userInfo.getTxId(), optInfo);

		} else {
			String procName = "USSD_P_UPDATE_SAG_OPT_INFO";
			String optInfo = userInfo.isSagOptInFlag() ? "Y" : "O";
			voltDbClient.callProcedure(new USSDDAOCallback(), procName, optInfo, msisdn);

			String procedure = "USSD_P_INSERT_ECMP_OPT_INFO_SAG";
			voltDbClient.callProcedure(new USSDDAOCallback(), procedure, msisdn, userInfo.getTxId(), optInfo);
		}

	}

}

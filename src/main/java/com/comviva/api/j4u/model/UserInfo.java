package com.comviva.api.j4u.model;

import java.util.HashMap;
import java.util.List;

/**
 * detail of user
 *
 * @author chandra.tekam
 */
public class UserInfo {

	private String msisdn;
	private String destAddress;
	private String txId;
	private boolean randomFlag;
	private boolean mlFlag;
	private boolean JFUEligible;
	private String langCode;
	private int userMsgRef;
	private String messageBody;
	private String custBalance;
	private String offerRefreshFlag;
	private String selProdType;
	private String prodIds;
	private boolean updateCCR;
	private boolean ragEligibleFlag;
	private boolean ragOptInFlag;
	private boolean ragNeverOptInFlag;
	private boolean ragGoalReachedFlag;
	private boolean isRagUser;
	private boolean pedEligibility;
	private boolean sagEligibleFlag;
	private boolean sagOptInFlag;
	private boolean sagNeverOptInFlag;
	private boolean sagGoalReachedFlag;
	private boolean isSagUser;
	private String aValue;
	private String source;
	private boolean locationRandomFlag;
	private String morningofferEligilibiltyFlag;
	private String prefPayMethod;

	public String getPrefPayMethod() {
		return prefPayMethod;
	}

	public void setPrefPayMethod(String prefPayMethod) {
		this.prefPayMethod = prefPayMethod;
	}

	public boolean isSagEligibleFlag() {
		return sagEligibleFlag;
	}

	public void setSagEligibleFlag(boolean sagEligibleFlag) {
		this.sagEligibleFlag = sagEligibleFlag;
	}

	public boolean isSagOptInFlag() {
		return sagOptInFlag;
	}

	public void setSagOptInFlag(boolean sagOptInFlag) {
		this.sagOptInFlag = sagOptInFlag;
	}

	public boolean isSagNeverOptInFlag() {
		return sagNeverOptInFlag;
	}

	public void setSagNeverOptInFlag(boolean sagNeverOptInFlag) {
		this.sagNeverOptInFlag = sagNeverOptInFlag;
	}

	public boolean isSagGoalReachedFlag() {
		return sagGoalReachedFlag;
	}

	public void setSagGoalReachedFlag(boolean sagGoalReachedFlag) {
		this.sagGoalReachedFlag = sagGoalReachedFlag;
	}

	public boolean isSagUser() {
		return isSagUser;
	}

	public void setSagUser(boolean isSagUser) {
		this.isSagUser = isSagUser;
	}

	public boolean isRagUser() {
		return isRagUser;
	}

	public void setRagUser(boolean isRagUser) {
		this.isRagUser = isRagUser;
	}

	public String getMorningofferEligilibiltyFlag() {
		return morningofferEligilibiltyFlag;
	}

	public void setMorningofferEligilibiltyFlag(String morningofferEligilibiltyFlag) {
		this.morningofferEligilibiltyFlag = morningofferEligilibiltyFlag;
	}

	public boolean getLocationRandomFlag() {
		return locationRandomFlag;
	}

	public void setLocationRandomFlag(boolean locationRandomFlag) {
		this.locationRandomFlag = locationRandomFlag;
	}

	private HashMap<String, String> ragInfo = new HashMap<String, String>();

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getDestAddress() {
		return destAddress;
	}

	public void setDestAddress(String destAddress) {
		this.destAddress = destAddress;
	}

	public String getTxId() {
		return txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public boolean getRandomFlag() {
		return randomFlag;
	}

	public boolean isJFUEligible() {
		return JFUEligible;
	}

	public void setJFUEligible(boolean jFUEligible) {
		JFUEligible = jFUEligible;
	}

	public int getLangCode() {
		return Integer.parseInt(langCode);
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public int getUserMsgRef() {
		return userMsgRef;
	}

	public void setUserMsgRef(int userMsgRef) {
		this.userMsgRef = userMsgRef;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public String getCustBalance() {
		return custBalance;
	}

	public void setCustBalance(String custBalance) {
		this.custBalance = custBalance;
	}

	public String getOfferRefreshFlag() {
		return offerRefreshFlag;
	}

	public void setOfferRefreshFlag(String offerRefreshFlag) {
		this.offerRefreshFlag = offerRefreshFlag;
	}

	public String getSelProdType() {
		return selProdType;
	}

	public void setSelProdType(String selProdType) {
		this.selProdType = selProdType;
	}

	public String getProdIds() {
		return prodIds;
	}

	public void setProdIds(List<String> prodIds) {
		this.prodIds = prodIds.toString();
	}

	public void setProdIds(String prodIds) {
		this.prodIds = prodIds;
	}

	public boolean isUpdateCCR() {
		return updateCCR;
	}

	public void setUpdateCCR(boolean updateCCR) {
		this.updateCCR = updateCCR;
	}

	public boolean isRagEligibleFlag() {
		return ragEligibleFlag;
	}

	public void setRagEligibleFlag(boolean ragEligibleFlag) {
		this.ragEligibleFlag = ragEligibleFlag;
	}

	public boolean isRagOptInFlag() {
		return ragOptInFlag;
	}

	public void setRagOptInFlag(boolean ragOptInFlag) {
		this.ragOptInFlag = ragOptInFlag;
	}

	public boolean isRagGoalReachedFlag() {
		return ragGoalReachedFlag;
	}

	public void setRagGoalReachedFlag(boolean ragGoalReachedFlag) {
		this.ragGoalReachedFlag = ragGoalReachedFlag;
	}

	public HashMap<String, String> getRagInfo() {
		return ragInfo;
	}

	public void setRagInfo(HashMap<String, String> ragInfo) {
		this.ragInfo = ragInfo;
	}

	public boolean isRagNeverOptInFlag() {
		return ragNeverOptInFlag;
	}

	public void setRagNeverOptInFlag(boolean ragNeverOptInFlag) {
		this.ragNeverOptInFlag = ragNeverOptInFlag;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isRandomFlag() {
		return randomFlag;
	}

	public void setRandomFlag(boolean randomFlag) {
		this.randomFlag = randomFlag;
	}

	public boolean isMlFlag() {
		return mlFlag;
	}

	public void setMlFlag(boolean mlFlag) {
		this.mlFlag = mlFlag;
	}

	public String getaValue() {
		return aValue;
	}

	public void setaValue(String aValue) {
		this.aValue = aValue;
	}

	public void setPedEligibility(boolean pedEligibility) {
		this.pedEligibility = pedEligibility;

	}

	public boolean isPedEligibility() {
		return pedEligibility;
	}

}
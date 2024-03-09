package com.comviva.api.j4u.model;

import java.util.HashMap;

public class RAGAndSAGUserInfo {
	private boolean updateCCR;
	private String msisdn;
	private boolean ragEligibleFlag;
	private boolean ragOptInFlag;
	private boolean ragNeverOptInFlag;
	private boolean ragGoalReachedFlag;
	private boolean isRagUser;
	private boolean sagEligibleFlag;
	private boolean sagOptInFlag;
	private boolean sagNeverOptInFlag;
	private boolean sagGoalReachedFlag;
	private boolean isSagUser;
	private String langCode;
	private String TransactionID;
	private HashMap<String, String> ragInfo;
	private HashMap<String, String> sagInfo;

	public String getTxId() {
		return TransactionID;
	}

	public void setTxId(String transactionID) {
		TransactionID = transactionID;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
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

	public boolean isRagNeverOptInFlag() {
		return ragNeverOptInFlag;
	}

	public void setRagNeverOptInFlag(boolean ragNeverOptInFlag) {
		this.ragNeverOptInFlag = ragNeverOptInFlag;
	}

	public boolean isRagGoalReachedFlag() {
		return ragGoalReachedFlag;
	}

	public void setRagGoalReachedFlag(boolean ragGoalReachedFlag) {
		this.ragGoalReachedFlag = ragGoalReachedFlag;
	}

	public boolean isRagUser() {
		return isRagUser;
	}

	public void setRagUser(boolean isRagUser) {
		this.isRagUser = isRagUser;
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

	public HashMap<String, String> getRagInfo() {
		return ragInfo;
	}

	public void setRagInfo(HashMap<String, String> ragInfo) {
		this.ragInfo = ragInfo;
	}

	public HashMap<String, String> getSagInfo() {
		return sagInfo;
	}

	public void setSagInfo(HashMap<String, String> sagInfo) {
		this.sagInfo = sagInfo;
	}

}

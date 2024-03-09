package com.comviva.api.j4u.service;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Service to handle the ped module
 *
 * @author saloni.gupta
 */
public interface IPEDProcessService {
	/**
	 * @param requestjson
	 * @return
	 * @throws Exception
	 */
	public JSONObject getAvailablePlays(JSONObject requestJson) throws Exception;

	/**
	 * @param msisdn
	 * @return
	 * @throws Exception
	 */
	public int getAvailablePlayCount(String msisdn) throws Exception;


	/**
	 * @return
	 * @throws Exception
	 */
	public boolean checkPedProcessPlayFlag() throws Exception;

	/**
	 * @param requestJson
	 * @return
	 */
	public JSONObject redeemPlay(JSONObject requestJson) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	public int getPlayExpiryDays() throws Exception;

	String processPlay(String msisdn, String txnId, int languageCode);

	public JSONArray getPrizeHistory1(String msisdn) throws Exception;
}

package com.comviva.api.j4u.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum J4UOfferStatusCode {
	SUCCESS(0, "Success", ""), NO_PLAYS_AVAILABLE(1, "No plays Available", "No plays Available"), PARTIAL_CONTENT(20,
			"Invalid Request parameters", "Invalid Request parameters"), UNAUTHORIZED(21, "Authentication failed",
					"Authentication failed"), NO_OFFERS(30, "No offers are available",
							"Unable to provide the offers to the subscriber"), DOWNSTREAM(40,
									"Error from the downstream node",
									"Whenever there is an error from the OCS or other downstream node during activation"), GENERAL_FAILURE(
											90, "General failure", "General failure"), RAG_ALREADY_OPTED_IN(50,
													"RAG opted in",
													"General The user is already opted in RAG"), SAG_ALREADY_OPTED_IN(
															52, "SAG opted in",
															"General The user is already opted in SAG"), RAG_NOT_ALREADY_OPTED_IN(
																	51, "RAG Not opted in",
																	"General The user is NOT opted in RAG"), SAG_NOT_ALREADY_OPTED_IN(
																			53, "SAG Not opted in",
																			"General The user is NOT opted in SAG"), RAG_NOT_ELIGIBLE(
																					41, "Cusotmer not Eligible for RAG",
																					"Cusotmer not Eligible for RAG"), SAG_NOT_ELIGIBLE(
																							42,
																							"Cusotmer not Eligible for SAG",
																							"Cusotmer not Eligible for SAG"), RAG_GOAL_REACHED_FLAG(
																									43,
																									"RAG GOAL REACHED FLAG",
																									"RAG GOAL REACHED FLAG"), SAG_GOAL_REACHED_FLAG(
																											44,
																											"SAG GOAL REACHED FLAG",
																											"SAG GOAL REACHED FLAG");

	private static Map<Integer, String> statusMap = loadStatusMap();
	private int statusCode;
	private String StatusMessage;
	private String description;

	private J4UOfferStatusCode(int statusCode, String statusMessage, String description) {
		this.statusCode = statusCode;
		this.StatusMessage = statusMessage;
		this.description = description;
	}

	private static Map<Integer, String> loadStatusMap() {
		statusMap = new ConcurrentHashMap<Integer, String>();
		statusMap.put(0, "Success");
		statusMap.put(1, "No plays Available");
		statusMap.put(20, "Invalid Request parameters");
		statusMap.put(21, "Authentication failed");
		statusMap.put(30, "No offers are available");
		statusMap.put(40, "Error from the downstream node");
		statusMap.put(90, "General failure");
		statusMap.put(50, "RAG opted in");
		statusMap.put(52, "SAG Opt In");
		statusMap.put(51, "RAG Not Opted In");
		statusMap.put(53, "SAG Not Opted In");
		statusMap.put(41, "Cusotmer not Eligible for RAG");
		statusMap.put(42, "Cusotmer not Eligible for SAG");
		statusMap.put(43, "RAG GOAL REACHED FLAG");
		statusMap.put(44, "SAG GOAL REACHED FLAG");

		return statusMap;
	}

	public static String getStatusMap(Integer statusCode) {
		return statusMap.get(statusCode);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusMessage() {
		return StatusMessage;
	}

	public String getDescription() {
		return description;
	}

}

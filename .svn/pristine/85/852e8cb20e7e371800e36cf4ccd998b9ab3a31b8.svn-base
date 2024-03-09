package com.comviva.api.j4u.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.junit.Test;

public class ModelClassTest {

	private void HTTPResponse() {
		HTTPResponse httpResponses = new HTTPResponse(0, "Success");
		HTTPResponse httpResponse = new HTTPResponse();
		JSONObject responseJSON = new JSONObject();
		String output = "{\"access_token\":\"ewogICJhbGciOiAiSFMyNTYiLAogICJ0eXAiOiAiSldUIgp9.ewogICJpc3MiOiJpcGctYWEubS1wZXNhLnZvZGFjb20uY2QiLAogICJzdWIiOiJiMGYwZmQ4OTc4YjQ0NzI1YjY2NDNhZWJkMjk4OWE4MCIsCiAgImF1ZCI6Iko0VSBHZW5lcmljIiwKICAiZXhwIjoiMjAyMi0xMC0yNyAxMTo0NDoyNCIsCiAgImlhdCI6ICIyMDIyLTEwLTI3IDExOjM0OjI0Igp9.58c126f6caae4a118c1a01a2d880351940bafb08d564d89828fe2aa08da7c98b\",\"token_type\":\"Bearer\",\"issued_at\":\"2022-10-2711:34:24\",\"expiry_date\":\"2022-10-2711:44:24\",\"status\":0}";
		httpResponse.setOutput(output);
		httpResponse.setRecvTime("27 Oct 2022 11:34:20");
		httpResponse.setResponse("Success");
		httpResponse.setResponseJSON(responseJSON);
		httpResponse.setResponseTime(110);
		httpResponse.setSentTime("27 Oct 2022 11:34:20");
		httpResponse.setStatus(200);

		httpResponse.getResponseJSON();
		httpResponse.toString();

		assertTrue(output.equals(httpResponse.getOutput()));
		assertTrue("27 Oct 2022 11:34:20".equals(httpResponse.getSentTime()));
		assertTrue("27 Oct 2022 11:34:20".equals(httpResponse.getRecvTime()));
		assertTrue("Success".equals(httpResponse.getResponse()));
		assertEquals(110, httpResponse.getResponseTime());
		assertEquals(200, httpResponse.getStatus());
	}

	private void InboundUSSDMessage() {
		InboundUSSDMessage inboundUSSDMessage = new InboundUSSDMessage();
		List<String> prodIds = new ArrayList<>();
		prodIds.add("J4U");

		inboundUSSDMessage.setIncomingLabel(333);
		inboundUSSDMessage.setProdIds(prodIds);
		inboundUSSDMessage.setSelProdId("77889");

		inboundUSSDMessage.getClobString();
		assertFalse(inboundUSSDMessage.getProdIds().isEmpty());
		assertTrue(inboundUSSDMessage.getSelProdId().equals("77889"));
		assertEquals(333, inboundUSSDMessage.getIncomingLabel());
	}

	private void Offer() {
		Offer offers = new Offer("9 GB for 30 days at $10", "611009", 5000);
		Offer offer = new Offer();

		offer.setAmount(5000);
		offer.setOfferDesc("9 GB for 30 days at $10");
		offer.setOfferID("611009");

		offer.toString();
		Integer expected = 5000;
		assertEquals(expected, offer.getAmount());
		assertTrue(offer.getOfferDesc().equals("9 GB for 30 days at $10"));
		assertTrue(offer.getOfferID().equals("611009"));
		
	}

	private void OfferParams() {
		OfferParams offerParams = new OfferParams();

		offerParams.setbValue(300);
		offerParams.setcValue(400);
		offerParams.setExpectedValue(500);
		offerParams.setOfferId("5643883");
		offerParams.setOfferPrice(2100);
		offerParams.setProductDescription("9 GB for 30 days at $10");
		offerParams.setRfValue(200);
		
		offerParams.getbValue();
		offerParams.getcValue();
		offerParams.getExpectedValue();
		offerParams.getOfferPrice();
		offerParams.getRfValue();
		assertTrue(offerParams.getOfferId().equals("5643883"));
		assertTrue(offerParams.getProductDescription().equals("9 GB for 30 days at $10"));
		offerParams.toString();
	}

	private void OfferRequest() {
		Integer msisdn = 2438111;
		OfferRequest request = new OfferRequest("vdrc", "vdrc@123", msisdn, "Data", "web", "2", "Target_001");
		OfferRequest offerRequest = new OfferRequest();

		offerRequest.setCategory("Data");
		offerRequest.setChannel("web");
		offerRequest.setLanguage("2");
		offerRequest.setMsisdn(msisdn);
		offerRequest.setPassword("vdrc@123");
		offerRequest.setRefNum("Target_001");
		offerRequest.setUsername("vdrc");
		
		offerRequest.toString();
		assertTrue(offerRequest.getCategory().equals("Data"));
		assertTrue(offerRequest.getChannel().equals("web"));
		assertTrue(offerRequest.getLanguage().equals("2"));
		assertTrue(offerRequest.getPassword().equals("vdrc@123"));
		assertTrue(offerRequest.getRefNum().equals("Target_001"));
		assertTrue(offerRequest.getUsername().equals("vdrc"));
		assertEquals(msisdn, offerRequest.getMsisdn());
	}

	private void PEDRandomPrizeInfo() {
		PEDRandomPrizeInfo pedRandomPrizeInfo = new PEDRandomPrizeInfo();

		Integer minRange =1000;
	    Integer maxRange =5000;
	    Integer weightage =800;
		pedRandomPrizeInfo.setMaxRange(maxRange);
		pedRandomPrizeInfo.setMinRange(minRange);
		pedRandomPrizeInfo.setPrizeID("4567");
		pedRandomPrizeInfo.setWeightage(weightage);
		
		assertEquals(5000, pedRandomPrizeInfo.getMaxRange());
		assertEquals(1000, pedRandomPrizeInfo.getMinRange());
		assertEquals(800, pedRandomPrizeInfo.getWeightage());
	    assertTrue(pedRandomPrizeInfo.getPrizeID().equals("4567"));
		
	}

	private void PrizeLibrary() {
		PrizeLibrary library = new PrizeLibrary("1234", 2, 5, "9 GB for 30 days at $10", 3, "data", "321");
		PrizeLibrary prizeLibrary = new PrizeLibrary();

		prizeLibrary.setLanguageCode(2);
		prizeLibrary.setMaxWins(3);
		prizeLibrary.setPrizeDescription("9 GB for 30 days at $10");
		prizeLibrary.setPrizeId("1234");
		prizeLibrary.setPrizeType("data");
		prizeLibrary.setProbability(5);
		prizeLibrary.setRedemptionCode("321");

		assertEquals(2, prizeLibrary.getLanguageCode());
		assertEquals(3, prizeLibrary.getMaxWins());
		assertEquals(5, prizeLibrary.getProbability());
		assertTrue(prizeLibrary.getPrizeDescription().equals("9 GB for 30 days at $10"));
		assertTrue(prizeLibrary.getPrizeId().equals("1234"));
		assertTrue(prizeLibrary.getPrizeType().equals("data"));
		assertTrue(prizeLibrary.getRedemptionCode().equals("321"));
	}

	private void ProductInfo() {
		ProductInfo productInfo = new ProductInfo();

		productInfo.setbValue("300");
		productInfo.setcValue("400");
		productInfo.setLangCode(2);
		productInfo.setPoolID("34500");
		productInfo.setProductDesc("9 GB for 30 days at $10");
		productInfo.setProductID("765879342");
		productInfo.setProductSubType("j4u data");
		productInfo.setProductType("data");

		productInfo.toString();
		assertEquals(2, productInfo.getLangCode());
		assertTrue(productInfo.getbValue().equals("300"));
		assertTrue(productInfo.getcValue().equals("400"));
		assertTrue(productInfo.getPoolID().equals("34500"));
		assertTrue(productInfo.getProductDesc().equals("9 GB for 30 days at $10"));
		assertTrue(productInfo.getProductID().equals("765879342"));
		assertTrue(productInfo.getProductSubType().equals("j4u data"));
		assertTrue(productInfo.getProductType().equals("data"));
	}

	private void RAGAndSAGUserInfo() {
		RAGAndSAGUserInfo ragAndSAGUserInfo = new RAGAndSAGUserInfo();
		HashMap<String, String> ragInfo = new HashMap<>();
		HashMap<String, String> sagInfo = new HashMap<>();
		ragAndSAGUserInfo.setLangCode("2");
		ragAndSAGUserInfo.setMsisdn("749876238");
		ragAndSAGUserInfo.setRagEligibleFlag(true);
		ragAndSAGUserInfo.setRagGoalReachedFlag(true);
		ragAndSAGUserInfo.setRagInfo(ragInfo);
		ragAndSAGUserInfo.setRagNeverOptInFlag(true);
		ragAndSAGUserInfo.setRagOptInFlag(true);
		ragAndSAGUserInfo.setRagUser(true);
		ragAndSAGUserInfo.setSagEligibleFlag(true);
		ragAndSAGUserInfo.setSagGoalReachedFlag(false);
		ragAndSAGUserInfo.setSagInfo(sagInfo);
		ragAndSAGUserInfo.setSagNeverOptInFlag(true);
		ragAndSAGUserInfo.setSagOptInFlag(true);
		ragAndSAGUserInfo.setSagUser(true);
		ragAndSAGUserInfo.setTxId("75rh787");
		ragAndSAGUserInfo.setUpdateCCR(true);

		ragAndSAGUserInfo.getRagInfo();
		ragAndSAGUserInfo.getSagInfo();
		assertTrue(ragAndSAGUserInfo.getMsisdn().equals("749876238"));
		assertTrue(ragAndSAGUserInfo.getLangCode().equals("2"));
		assertTrue(ragAndSAGUserInfo.getTxId().equals("75rh787"));
		assertTrue(ragAndSAGUserInfo.isRagEligibleFlag());
		assertTrue(ragAndSAGUserInfo.isRagGoalReachedFlag());
		assertTrue(ragAndSAGUserInfo.isRagNeverOptInFlag());
		assertTrue(ragAndSAGUserInfo.isRagOptInFlag());
		assertTrue(ragAndSAGUserInfo.isRagUser());
		assertTrue(ragAndSAGUserInfo.isSagEligibleFlag());
		assertFalse(ragAndSAGUserInfo.isSagGoalReachedFlag());
		assertTrue(ragAndSAGUserInfo.isSagNeverOptInFlag());
		assertTrue(ragAndSAGUserInfo.isSagOptInFlag());
		assertTrue(ragAndSAGUserInfo.isSagUser());
		assertTrue(ragAndSAGUserInfo.isUpdateCCR());
	}

	private void RankingFormulae() {
		RankingFormulae rankingFormulae = new RankingFormulae();
		List<OfferParams> offerParams = new ArrayList<>();
		OfferParams offerParam = new OfferParams();
		offerParams.add(offerParam);

		rankingFormulae.setAaBalance(5000);
		rankingFormulae.setAaEligible(true);
		rankingFormulae.setAirtimeBalance(3400);
		rankingFormulae.setaValue(595);
		rankingFormulae.setCellId("677899");
		rankingFormulae.setOfferParams(offerParams);
		rankingFormulae.setPoolId("877899");
		rankingFormulae.setPrefPayMethod("G");

		rankingFormulae.getAaBalance();
		rankingFormulae.getAirtimeBalance();
		rankingFormulae.getaValue();
		rankingFormulae.getOfferParams();
		assertTrue(rankingFormulae.isAaEligible());
		assertTrue(rankingFormulae.getCellId().equals("677899"));
		assertTrue(rankingFormulae.getPoolId().equals("877899"));
		assertTrue(rankingFormulae.getPrefPayMethod().equals("G"));
	}

	private void TemplateDTO() {
		TemplateDTO templateDTO = new TemplateDTO();

		templateDTO.setLangCd(2);
		templateDTO.setOfferOrderCSV("9945u");
		templateDTO.setTempalteId("994544");
		templateDTO.setTemplate("aa");

		templateDTO.toString();
		assertEquals(2, templateDTO.getLangCd());
		assertTrue(templateDTO.getOfferOrderCSV().equals("9945u"));
		assertTrue(templateDTO.getTempalteId().equals("994544"));
		assertTrue(templateDTO.getTemplate().equals("aa"));
	}

	private void UserInfo() {
		UserInfo userInfo = new UserInfo();
		List<String> prodIds = new ArrayList<>();
		prodIds.add("J4U");
		HashMap<String, String> ragInfo = new HashMap<>();

		userInfo.setaValue("3455");
		userInfo.setCustBalance("5000");
		userInfo.setDestAddress("aaa");
		userInfo.setJFUEligible(true);
		userInfo.setLangCode("2");
		userInfo.setLocationRandomFlag(true);
		userInfo.setMessageBody("no message");
		userInfo.setMlFlag(true);
		userInfo.setMorningofferEligilibiltyFlag("no");
		userInfo.setMsisdn("62348559723");
		userInfo.setOfferRefreshFlag("true");
		userInfo.setPedEligibility(true);
		userInfo.setPrefPayMethod("G pay");
		userInfo.setProdIds(prodIds);
		userInfo.setProdIds("J4U");
		userInfo.setRagEligibleFlag(true);
		userInfo.setRagGoalReachedFlag(true);
		userInfo.setRagInfo(ragInfo);
		userInfo.setRagNeverOptInFlag(true);
		userInfo.setRagOptInFlag(true);
		userInfo.setRagUser(true);
		userInfo.setRandomFlag(true);
		userInfo.setSagEligibleFlag(true);
		userInfo.setSagGoalReachedFlag(true);
		userInfo.setSagNeverOptInFlag(true);
		userInfo.setSagOptInFlag(true);
		userInfo.setSagUser(true);
		userInfo.setSelProdType("data");
		userInfo.setSource("api offers");
		userInfo.setTxId("75hfs4ht");
		userInfo.setUpdateCCR(true);
		userInfo.setUserMsgRef(33);

		assertEquals(2, userInfo.getLangCode());
		assertEquals(33, userInfo.getUserMsgRef());
		assertTrue(userInfo.getaValue().equals("3455"));
		assertTrue(userInfo.getCustBalance().equals("5000"));
		assertTrue(userInfo.getDestAddress().equals("aaa"));
		assertTrue(userInfo.getMessageBody().equals("no message"));
		assertTrue(userInfo.getMorningofferEligilibiltyFlag().equals("no"));
		assertTrue(userInfo.getMsisdn().equals("62348559723"));
		assertTrue(userInfo.getOfferRefreshFlag().equals("true"));
		assertTrue(userInfo.getPrefPayMethod().equals("G pay"));
		userInfo.getRagInfo();
		assertTrue(userInfo.getSelProdType().equals("data"));
		assertTrue(userInfo.getSource().equals("api offers"));
		assertTrue(userInfo.getTxId().equals("75hfs4ht"));
		assertTrue(userInfo.getRandomFlag());
		assertTrue(userInfo.getLocationRandomFlag());
		userInfo.getProdIds();
		assertTrue(userInfo.isRagEligibleFlag());
		assertTrue(userInfo.isSagEligibleFlag());
		assertTrue(userInfo.isJFUEligible());
		assertTrue(userInfo.isMlFlag());
		assertTrue(userInfo.isPedEligibility());
		assertTrue(userInfo.isRagGoalReachedFlag());
		assertTrue(userInfo.isRagNeverOptInFlag());
		assertTrue(userInfo.isRagOptInFlag());
		assertTrue(userInfo.isRagUser());
		assertTrue(userInfo.isRandomFlag());
		assertTrue(userInfo.isSagGoalReachedFlag());
		assertTrue(userInfo.isSagNeverOptInFlag());
		assertTrue(userInfo.isSagOptInFlag());
		assertTrue(userInfo.isSagUser());
		assertTrue(userInfo.isUpdateCCR());
	}

	@Test
	public void test() {
		HTTPResponse();
		InboundUSSDMessage();
		Offer();
		OfferParams();
		OfferRequest();
		PEDRandomPrizeInfo();
		PrizeLibrary();
		ProductInfo();
		RAGAndSAGUserInfo();
		RankingFormulae();
		TemplateDTO();
		UserInfo();
	}

}

/*
 * COPYRIGHT: Mahindra Comviva Technologies Pvt. Ltd.
 *
 * This software is the sole property of Comviva and is protected
 * by copyright law and international treaty provisions. Unauthorized
 * reproduction or redistribution of this program, or any portion of
 * it may result in severe civil and criminal penalties and will be
 * prosecuted to the maximum extent possible under the law.
 * Comviva reserves all rights not expressly granted. You may not
 * reverse engineer, decompile, or disassemble the software, except
 * and only to the extent that such activity is expressly permitted
 * by applicable law notwithstanding this limitation.
 *
 * THIS SOFTWARE IS PROVIDED TO YOU "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE. YOU ASSUME THE ENTIRE RISK AS TO THE ACCURACY
 * AND THE USE OF THIS SOFTWARE. Comviva SHALL NOT BE LIABLE FOR
 * ANY DAMAGES WHATSOEVER ARISING OUT OF THE USE OF OR INABILITY TO
 * USE THIS SOFTWARE, EVEN IF Comviva HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.comviva.api.utils;

/**
 * @author nitin.gupta
 */
public interface XMLTagConstants {

    String RESPONSE_START_TAG = "<response>";
     String RESPONSE_END_TAG = "</response>";

     String NEWLINE = "\n";

     String TRANSACTIONID_START_TAG = "<transactionId>";
     String TRANSACTIONID_END_TAG = "</transactionId>";

     String TIERLEVEL_START_TAG = "<tierLevel>";
     String TIERLEVEL_END_TAG = "</tierLevel>";

     String RETURN_MSG_START_TAG = "<returnMsg>";
     String RETURN_MSG_END_TAG = "</returnMsg>";

     String ERROR_CODE_START_TAG = "<errorCode>";
     String ERROR_CODE_END_TAG = "</errorCode>";

     String ERROR_MSG_START_TAG = "<errorMsg>";
     String ERROR_MSG_END_TAG = "</errorMsg>";

     String RETURN_CODE_START_TAG = "<returnCode>";
     String RETURN_CODE_END_TAG = "</returnCode>";

     String ENROLL_STATUS_START_TAG = "<enrollStatus>";
     String ENROLL_STATUS_END_TAG = "</enrollStatus>";

     String REDEEMABLE_POINTS_START_TAG = "<redeemablePoints>";
     String REDEEMABLE_POINTS_END_TAG = "</redeemablePoints>";

     String TIER_POINTS_START_TAG = "<tierPoints>";
     String TIER_POINTS_END_TAG = "</tierPoints>";

     String TIER_LEVEL_START_TAG = "<tierStatus>";
     String TIER_LEVEL_END_TAG = "</tierStatus>";

     String REDEEMED_POINTS_START_TAG = "<redeemedPoints>";
     String REDEEMED_POINTS_END_TAG = "</redeemedPoints>";

     String EARNED_POINTS_START_TAG = "<earnedPoints>";
     String EARNED_POINTS_END_TAG = "</earnedPoints>";

     String PRODUCT_ID_START_TAG = "<productID>";
     String PRODUCT_ID_END_TAG = "</productID>";

     String RECEIVER_MSISDN_END_TAG = "<receiverMsisdn>";
     String RECEIVER_MSISDN_START_TAG = "</receiverMsisdn>";

     String TRANSFERRED_POINTS_END_TAG = "<transferredPoints>";
     String TRANSFERRED_POINTS_START_TAG = "</transferredPoints>";

     String VOUCHER_START_TAG = "<voucher>";
     String VOUCHER_END_TAG = "</voucher>";

     String VOUCHERS_START_TAG = "<vouchers>";
     String VOUCHERS_END_TAG = "</vouchers>";

     String VOUCHER_ID_START_TAG = "<voucherID>";
     String VOUCHER_ID_END_TAG = "</voucherID>";

     String PRODUCT_NAME_START_TAG = "<productName>";
     String PRODUCT_NAME_END_TAG = "</productName>";

     String PRODUCT_POINTS_START_TAG = "<productPoints>";
     String PRODUCT_POINTS_END_TAG = "</productPoints>";

     String PRODUCT_TYPE_START_TAG = "<productType>";
     String PRODUCT_TYPE_END_TAG = "</productType>";

     String PRODUCTS_START_TAG = "<products>";
     String PRODUCTS_END_TAG = "</products>";

     String PRODUCT_START_TAG = "<product>";
     String PRODUCT_END_TAG = "</product>";

     String CAMPAIGNS_START_TAG = "<campaigns>";
     String CAMPAIGNS_END_TAG = "</campaigns>";

     String CAMPAIGN_START_TAG = "<campaign>";
     String CAMPAIGN_END_TAG = "</campaign>";

     String CAMPAIGN_ID_START_TAG = "<campaignID>";
     String CAMPAIGN_ID_END_TAG = "</campaignID>";

     String CAMPAIGN_NAME_START_TAG = "<campaignName>";
     String CAMPAIGN_NAME_END_TAG = "</campaignName>";

     String CAMPAIGN_TYPE_START_TAG = "<campaignType>";
     String CAMPAIGN_TYPE_END_TAG = "</campaignType>";

     String CAMPAIGN_CATEGORY_START_TAG = "<campaignCategory>";
     String CAMPAIGN_CATEGORY_END_TAG = "</campaignCategory>";

     String CAMPAIGN_DESC_START_TAG = "<campaignDesc>";
     String CAMPAIGN_DESC_END_TAG = "</campaignDesc>";

     String CAMPAIGN_STARTDATE_START_TAG = "<campaignStartDate>";
     String CAMPAIGN_STARTDATE_END_TAG = "</campaignStartDate>";

     String CAMPAIGN_ENDDATE_START_TAG = "<campaignEndDate>";
     String CAMPAIGN_ENDDATE_END_TAG = "</campaignEndDate>";

     String OPT_STARTDATE_START_TAG = "<optStartDate>";
     String OPT_STARTDATE_END_TAG = "</optStartDate>";

     String OPT_ENDDATE_START_TAG = "<optEndDate>";
     String OPT_ENDDATE_END_TAG = "</optEndDate>";

     String OPTIN_STATUS_START_TAG = "<optInStatus>";
     String OPTIN_STATUS_END_TAG = "</optInStatus>";

     String OPTOUT_STATUS_START_TAG = "<optOutStatus>";
     String OPTOUT_STATUS_END_TAG = "</optOutStatus>";

     String TARGET_START_TAG = "<target>";
     String TARGET_END_TAG = "</target>";

     String ACHIEVEMENT_START_TAG = "<achievement>";
     String ACHIEVEMENT_END_TAG = "</achievement>";

     String TARGET_DATE_START_TAG = "<expiryDate>";
     String TARGET_DATE_END_TAG = "</expiryDate>";

     String TOTAL_COUNT_START_TAG = "<totalCount>";
     String TOTAL_COUNT_END_TAG = "</totalCount>";

     String TRANSACTIONS_START_TAG = "<transactions>";
     String TRANSACTIONS_END_TAG = "</transactions>";

     String TRANSACTION_START_TAG = "<transaction>";
     String TRANSACTION_END_TAG = "</transaction>";

     String TRANSACTION_DATE_START_TAG = "<transactionDate>";
     String TRANSACTION_DATE_END_TAG = "</transactionDate>";

     String TRANSACTION_AMOUNT_START_TAG = "<transactionAmount>";
     String TRANSACTION_AMOUNT_END_TAG = "</transactionAmount>";

     String TRANSACTION_TYPE_START_TAG = "<transactionType>";
     String TRANSACTION_TYPE_END_TAG = "</transactionType>";

     String POINTS_START_TAG = "<points>";
     String POINTS_END_TAG = "</points>";

     String EXPIRYDATE_START_TAG = "<expiryDate>";
     String EXPIRYDATE_END_TAG = "</expiryDate>";

     String REDEMPTION_DATE_START_TAG = "<redemptionDate>";
     String REDEMPTION_DATE_END_TAG = "</redemptionDate>";

     String REDEMPTION_TYPE_START_TAG = "<redemptionType>";
     String REDEMPTION_TYPE_END_TAG = "</redemptionType>";

     String REDEMPTION_ITEM_START_TAG = "<redemptionItem>";
     String REDEMPTION_ITEM_END_TAG = "</redemptionItem>";

     String POINTS_REDEEMED_START_TAG = "<pointsRedeemed>";
     String POINTS_REDEEMED_END_TAG = "</pointsRedeemed>";

     String ENROLL_DATE_START_TAG = "<enrollDate>";
     String ENROLL_DATE_END_TAG = "</enrollDate>";

     String POINTS_TO_EXPIRE_START_TAG = "<pointsToExpire>";
     String POINTS_TO_EXPIRE_END_TAG = "</pointsToExpire>";

     String EXPIRES_ON_START_TAG = "<expiresOn>";
     String EXPIRES_ON_END_TAG = "</expiresOn>";

     String CATEGORIES_START_TAG = "<categories>";
     String CATEGORIES_END_TAG = "</categories>";

     String CATEGORY_START_TAG = "<category>";
     String CATEGORY_END_TAG = "</category>";

}

/**
 *
 */
package com.comviva.api.conf;

/**
 * @author Nitin.Gupta
 */
public interface ConfConstants {

      String LOG_FILE_NAME = "log4j.properties";
      String CONF_FILE_NAME = "configurations.ini";
      byte CONF_FILE_ID = 1;
      String COMMON = "COMMON";
      long public_EVENT_RETRY_SLEEP_INTERVAL = 1000;
      String CONF_RELOAD_CHECK_INTERVAL = "CONF_RELOAD_CHECK_INTERVAL";
      int public_CONF_RELOAD_CHECK_INTERVEL = 60000;

      String MPESA_PARAMETERS = "MPESA";
      String URL_AUTHENTICATION = "URL_AUTHENTICATION";
      String USERID = "USERID";
     // String PWD = "PWD";
      String OFFER_ID = "OFFER_ID";
      String OFFER_DESC = "OFFER_DESC";
      String OFFER_AMOUNT = "OFFER_AMOUNT";

}

/**
 *
 */
package com.comviva.api.conf;

import com.comviva.api.exception.ConfigException;
import com.comviva.revenueplus.common.utils.IniUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @author Nitin.Gupta
 */
public class ConfLoader {
    private static final Logger LOGGER = Logger.getLogger(ConfLoader.class);

    private ConfValues confvalues = new ConfValues();
    
    public ConfLoader( String confPath) {
       // ConfValues.interfaceAdaptorConfPath = confPath;
 
         confvalues.setInterfaceAdaptorConfPath(confPath);
    }

    public void loadConfigurations() throws ConfigException {

        LOGGER.fatal("loading Configurations");
         IniUtils conf = new IniUtils();
        try {
            //conf.load(ConfValues.interfaceAdaptorConfPath);
            conf.load(confvalues.getInterfaceAdaptorConfPath());
        } catch (IOException e) {
            LOGGER.error("Exception while loading configuration file");
            throw new ConfigException("Exception while loading configuration file", e);
        }
        
        LOGGER.info("interfaceAdaptorConfPath :: " + confvalues.getInterfaceAdaptorConfPath());


        try {
           // ConfValues.confReloadCheckIntervel = Integer.parseInt(conf.get(ConfConstants.COMMON, ConfConstants.CONF_RELOAD_CHECK_INTERVAL));
            confvalues.setConfReloadCheckIntervel(Integer.parseInt(conf.get(ConfConstants.COMMON, ConfConstants.CONF_RELOAD_CHECK_INTERVAL)));
        } catch (Exception e) {
            LOGGER.error("Exception while loading " + ConfConstants.COMMON + " " + ConfConstants.CONF_RELOAD_CHECK_INTERVAL + " taking public value " + ConfConstants.public_CONF_RELOAD_CHECK_INTERVEL);
           // ConfValues.confReloadCheckIntervel = ConfConstants.public_CONF_RELOAD_CHECK_INTERVEL;
            confvalues.setConfReloadCheckIntervel(ConfConstants.public_CONF_RELOAD_CHECK_INTERVEL); 
        }
        
        LOGGER.info("confReloadCheckIntervel :: " + confvalues.getConfReloadCheckIntervel());


        try {

           // ConfValues.url_Authentication = Boolean.parseBoolean(conf.get(ConfConstants.MPESA_PARAMETERS, ConfConstants.URL_AUTHENTICATION));
            confvalues.setUrl_Authentication( Boolean.parseBoolean(conf.get(ConfConstants.MPESA_PARAMETERS, ConfConstants.URL_AUTHENTICATION)));
            
        } catch (Exception e) {
            LOGGER.error("Exception while loading " + ConfConstants.MPESA_PARAMETERS + " " + ConfConstants.URL_AUTHENTICATION + " taking public value: " + false);
        }
        LOGGER.info("url_Authentication :: " + ConfValues.isUrl_Authentication());

        

        try {

            //ConfValues.userid = conf.get(ConfConstants.MPESA_PARAMETERS, ConfConstants.USERID);
            confvalues.setUserid(conf.get(ConfConstants.MPESA_PARAMETERS, ConfConstants.USERID)); 
        } catch (Exception e) {
            LOGGER.error("Exception while loading " + ConfConstants.MPESA_PARAMETERS + " " + ConfConstants.USERID + " taking public value: " + ConfConstants.USERID);
            //ConfValues.userid = ConfConstants.USERID;
            confvalues.setUserid(ConfConstants.USERID); 



        }


       // LOGGER.info("User ID :: " + ConfValues.userid);
        LOGGER.info("User ID :: " + ConfValues.getUserid());

        try {

            confvalues.setPwd(conf.get(ConfConstants.MPESA_PARAMETERS, "PWD")); 

          //  ConfValues.pwd = conf.get(ConfConstants.MPESA_PARAMETERS, ConfConstants.PWD);
        } catch (Exception e) {
            LOGGER.error("Exception while loading " + ConfConstants.MPESA_PARAMETERS + " " + "PWD" + " taking public value: " + "PWD");
          //  ConfValues.pwd = ConfConstants.PWD;
            confvalues.setPwd("PWD");
        }
        LOGGER.info("password :: " + ConfValues.getPwd());


        try {

         //   ConfValues.offer_id = conf.get(ConfConstants.MPESA_PARAMETERS, ConfConstants.OFFER_ID);
            confvalues.setOffer_id(conf.get(ConfConstants.MPESA_PARAMETERS, ConfConstants.OFFER_ID));
        } catch (Exception e) {
            LOGGER.error("Exception while loading " + ConfConstants.MPESA_PARAMETERS + " " + ConfConstants.OFFER_ID + " taking public value: " + ConfConstants.OFFER_ID);
           // ConfValues.offer_id = ConfConstants.OFFER_ID;
            confvalues.setOffer_id(ConfConstants.OFFER_ID);

        }
        LOGGER.info("offer_id :: " + confvalues.isOffer_id());


        try {

            
          //  ConfValues.offer_desc = conf.get(ConfConstants.MPESA_PARAMETERS, ConfConstants.OFFER_DESC);
            confvalues.setOffer_desc(conf.get(ConfConstants.MPESA_PARAMETERS, ConfConstants.OFFER_DESC));
        } catch (Exception e) {
            LOGGER.error("Exception while loading " + ConfConstants.MPESA_PARAMETERS + " " + ConfConstants.OFFER_DESC + " taking public value: " + ConfConstants.OFFER_DESC);

            //ConfValues.offer_desc = ConfConstants.OFFER_DESC;
            confvalues.setOffer_desc(ConfConstants.OFFER_DESC);
        }
        LOGGER.info("offer_desc :: " + confvalues.getOffer_desc());


        try {

           // ConfValues.offer_amount = conf.get(ConfConstants.MPESA_PARAMETERS, ConfConstants.OFFER_AMOUNT);
            confvalues.setOffer_amount( conf.get(ConfConstants.MPESA_PARAMETERS, ConfConstants.OFFER_AMOUNT));
        } catch (Exception e) {
            LOGGER.error("Exception while loading " + ConfConstants.MPESA_PARAMETERS + " " + ConfConstants.OFFER_AMOUNT + " taking public value: " + ConfConstants.OFFER_AMOUNT);
            //ConfValues.offer_amount = ConfConstants.OFFER_AMOUNT;
            confvalues.setOffer_amount(ConfConstants.OFFER_AMOUNT);

        }
        LOGGER.info("offer_amount :: " + confvalues.getOffer_amount());


    }

    public void reLoadConfigurations() throws ConfigException {
        loadConfigurations();

    }

}

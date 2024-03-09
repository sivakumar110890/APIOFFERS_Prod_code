package com.comviva.api.j4u.config;

import static com.comviva.api.j4u.utils.J4UOfferConstants.BLACKLIST_ENABLED;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.comviva.api.exception.ConfigException;

/**
 * This class is used to load the properties file and get its value of property
 * by using defined methods
 *
 * @author chandra.tekam
 */
public class PropertiesLoader {

    private static final Logger LOGGER = Logger.getLogger(PropertiesLoader.class);
    protected static Properties j4uProperties = null;
    private static String userDir = System.getProperty("user.dir");
    private static String PROPERTIES_FILENAME = userDir + "/config/ApiOffers.properties";

    // These keys are defined in 4u-offer.properties file...
    public static void loadJ4UOfferProperty() throws ConfigException {
        File aFile = new File(PROPERTIES_FILENAME);
        if (aFile.exists()) {
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(aFile);
                j4uProperties = new Properties();
                j4uProperties.load(inputStream);
                LOGGER.debug("loadJ4UOfferProperty method :: loaded the property file");
                LOGGER.info("isBlacklistEnabled : " + ((getIntValue(BLACKLIST_ENABLED) == 1) ? 1 : 0));
            } catch (Exception e) {
                LOGGER.error(
                                "loadJ4UOfferProperty method :: Error Loading enba Properties From File: " + aFile.getName(), e);
                try {
                    throw e;
                } catch (Exception e1) {
                    LOGGER.error("Error" +e1);
                }
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                      
                        LOGGER.error("loadJ4UOfferProperty method :: Error Closing The Input Stream. Ignore For Now" +e);
                       
                    }
                }
            }
        } else {
            try {
                throw new FileNotFoundException(aFile.getAbsoluteFile().getName() + " Is NOT Found");
            } catch (FileNotFoundException e) {
                LOGGER.error("Error" +e);
            }
        }
    }

    /**
     * This method is used the to get the value of property key
     *
     * @param propertyKey name of the j4u offer property key
     * @return string value of the j4u offer property key
     * @throws Exception if property not found
     */
    public static String getValue(String propertyKey) throws ConfigException {
        try {
            if (j4uProperties == null) {
                LOGGER.info(
                                "getValue method :: First Call to get a value from ApiOffers.properties file. Should see this line only once...");
                loadJ4UOfferProperty();
            }
        } catch (Exception e) {
            LOGGER.error("getValue :: Mostly ApiOffers.properties File is NOT there. Error Getting the Value For Key: "
                            + propertyKey);
          
            throw e;
        }
        return j4uProperties.getProperty(propertyKey);
    }

    /**
     * This method is used the to get the value of property key
     *
     * @param propertyKey name of the j4u offer property key
     * @return string value of the j4u offer property key
     * @throws Exception if property not found
     */
    public static int getIntValue(String propertyKey) throws ConfigException {
        String stringValue = getValue(propertyKey);
        if (stringValue != null && stringValue.trim().length() != 0) {
            return (new Integer(stringValue)).intValue();
        }
        return 0;
    }

    /**
     * Reload the j4u offer properties
     *
     * @return SUCCESS string else exception
     * @throws Exception throws exception
     */
    public static String reloadProperties() throws ConfigException {
        try {
            j4uProperties = null;
            loadJ4UOfferProperty();
            return "SUCCESS";
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    public static boolean getBooleanValue(String cmpPropertyKey) throws ConfigException {
        String stringValue = getValue(cmpPropertyKey);
        if (stringValue != null && stringValue.trim().length() != 0) {
            return (new Boolean(stringValue)).booleanValue();
        }
        return false;
    }

}

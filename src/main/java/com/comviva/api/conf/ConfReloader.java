/**
 *
 */
package com.comviva.api.conf;

import com.comviva.revenueplus.common.confreloader.ConfigurationReLoader;
import org.apache.log4j.Logger;

import java.util.Hashtable;
import java.util.List;

/**
 * @author Nitin.Gupta
 */
public class ConfReloader implements ConfigurationReLoader {
    private static final Logger LOGGER = Logger.getLogger(ConfReloader.class);
    private Hashtable<Byte, String> filesToTrack = null;
    private ConfLoader confLoader = null;

    public ConfReloader() {
        filesToTrack = new Hashtable<>();
        LOGGER.fatal("Starting ConfReloader");
    }

    public void registerAbsoluteConfFilePathForTracking(byte key, String absoluteConfFilePath) {
        LOGGER.fatal("registerAbsoluteConfFilePathForTracking::" + absoluteConfFilePath);
        if (null != absoluteConfFilePath) {
            filesToTrack.put(key, absoluteConfFilePath);
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public Hashtable<Byte, String> getAbsoluteConfFilePathsForTracking() {
        return (Hashtable<Byte, String>) filesToTrack.clone();
    }

    @Override
    public void reloadConfigurationFromConfFile(final List<Byte> fileId) {
        LOGGER.fatal("reloadConfigurationFromConfFile");
        final int size = fileId.size();
        if (size <= 0) {
            return;
        }
        for (int i = 0; i < size; i++) {
            switch (fileId.get(i)) {
            case ConfConstants.CONF_FILE_ID:
                try {
                    confLoader.reLoadConfigurations();
                } catch (Exception e) {
                    LOGGER.error("Exception while reloading configurations " + e);
                }
                break;

            public:
                break;
            }
        }

    }

    @Override
    public void setConfigurationLoader(final Object object) {
        confLoader = (ConfLoader) object;

    }

}

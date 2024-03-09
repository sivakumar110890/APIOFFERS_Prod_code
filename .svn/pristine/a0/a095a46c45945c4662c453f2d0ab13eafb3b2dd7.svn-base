package com.comviva.api.j4u.utils;

import com.comviva.api.exception.APIException;
import com.comviva.api.j4u.dao.LookUpDAO;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CellIDPoolIDCache {
    private static final Logger LOGGER = Logger.getLogger(CellIDPoolIDCache.class);
    private final Map<String, String> cellPoolMapCache ;
    private static LocalDateTime cacheReloadTime ;

    private CellIDPoolIDCache () {
        cellPoolMapCache = new HashMap<>() ;
        initCache();
    }

    public String getPoolIDForCellID (String cellID) {
        return cellPoolMapCache.get(cellID) ;
    }

    public void reloadCache () {
        initCache();
    }

    public String toString () {
        StringBuilder stringBuilder = new StringBuilder() ;
        stringBuilder.append("Cache reloaded at - ") ;
        stringBuilder.append(cacheReloadTime.toString()) ;
        stringBuilder.append(" | Total records - ") ;
        stringBuilder.append(cellPoolMapCache.size());

        return  stringBuilder.toString() ;
    }

    public static CellIDPoolIDCache instance() {
        return InstanceHolder.cacheInstance;
    }


    private synchronized void initCache() {
        LOGGER.info("CellIDPoolIDCache - Caching START");
        try {
            cacheReloadTime = LocalDateTime.now();
            LookUpDAO dao = new LookUpDAO();
            Map<String, String> cellPoolMap = dao.getPoolIdForCellId();
            if (null != cellPoolMap) {
                cellPoolMapCache.clear();
                cellPoolMapCache.putAll(cellPoolMap);
            } else {
                throw new APIException("Could not find any cell ID vs Pool ID mapping");
            }
        } catch (Exception ex) {
            LOGGER.error("Error occured at ==> ", ex);
        }
        LOGGER.info("CellIDPoolIDCache - Caching END - Total records in Cache -" + cellPoolMapCache.size() );
    }

    private static class InstanceHolder {
        private static CellIDPoolIDCache cacheInstance = new CellIDPoolIDCache();
    }
}

package com.comviva.api.j4u.utils;

import com.comviva.api.exception.APIException;
import com.comviva.api.j4u.dao.LookUpDAO;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ProductPriceCache {
    private static final Logger LOGGER = Logger.getLogger(ProductPriceCache.class);
    private final Map<String, String> cache;
    private static LocalDateTime cacheReloadTime ;

    private ProductPriceCache() {
        cache = new HashMap<>();
        initCache ();
    }

    public void reloadCache () {
        initCache ();
    }

    public String toString () {
        StringBuilder stringBuilder = new StringBuilder() ;
        stringBuilder.append("Price Cache reloaded at - ") ;
        stringBuilder.append(cacheReloadTime.toString()) ;
        stringBuilder.append(" | Total records - ") ;
        stringBuilder.append(cache.size());

        return  stringBuilder.toString() ;
    }

    public static ProductPriceCache instance() {
        return InstanceHolder.cacheInstance;
    }

    public String get(String key) {
        return cache.get(key);
    }

    private synchronized void initCache () {
        LOGGER.info("ProductPriceCache - START");
        try {
            cacheReloadTime = LocalDateTime.now();
            LookUpDAO dao = new LookUpDAO();
            Map<String, String> productPriceMap = dao.getProductPriceMap();
            if (null != productPriceMap) {
                cache.clear();
                cache.putAll(productPriceMap);
            } else {
                throw new APIException("Could not find any product price from database");
            }
        } catch (Exception ex) {
            LOGGER.error("Error occured at ==> ", ex);
        }
        LOGGER.info("ProductPriceCache - END - Total Records - " + cache.size());

    }

    private static class InstanceHolder {
        private static ProductPriceCache cacheInstance = new ProductPriceCache();
    }
}
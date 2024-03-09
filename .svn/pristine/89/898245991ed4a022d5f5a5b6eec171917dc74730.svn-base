package com.comviva.api.j4u.utils;

import com.comviva.api.j4u.dao.LookUpDAO;
import com.comviva.api.j4u.model.TemplateDTO;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Cache for VOLT Tables
 */
public class TemplateCache {
    private final Map<String, TemplateDTO> cache;
    private final Map<String, TemplateDTO> mlTemplateCache;
    public static final Logger LOGGER = Logger.getLogger(TemplateCache.class);

    private TemplateCache() {
        cache = new HashMap<>();
        mlTemplateCache = new HashMap<>();
        try {
            init();
        } catch (Exception ex) {
            LOGGER.error("error" +ex);
        }
    }

    public static  TemplateCache instance() {
        return InstanceHolder.INSTANC;
    }

    public TemplateDTO get(String key) {
        return cache.get(key);
    }

    public TemplateDTO getMLMenu(String key) {
        return mlTemplateCache.get(key);
    }

    public void init() throws Exception {
        LookUpDAO dao = new LookUpDAO();
        Map<String, TemplateDTO> cacheMap = dao.getTemplatesMap();
        Map<String, TemplateDTO> mlCacheMap = dao.getMLTemplatesMap();

        if (null != cacheMap) {
            if (!( cache.isEmpty())) {
                cache.clear();
               
            }
            cache.putAll(cacheMap);
           
        } else {
            throw new Exception("Could not find any template data from database");
        }

        
        if (null != mlCacheMap) {
            if ( !mlTemplateCache.isEmpty()) {

                mlTemplateCache.clear();
            }
            mlTemplateCache.putAll(mlCacheMap);
        } else {
            throw new Exception("Could not find any template data from database for ML");
        }
        
      
    }

    private static class InstanceHolder {
        public static  final TemplateCache INSTANC = new TemplateCache();

  
    }
}

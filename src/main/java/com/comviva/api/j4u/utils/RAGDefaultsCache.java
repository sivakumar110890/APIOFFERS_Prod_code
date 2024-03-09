package com.comviva.api.j4u.utils;
import org.apache.log4j.Logger;

import com.comviva.api.j4u.dao.LookUpDAO;

import java.time.LocalDate;
import java.util.List;


public class RAGpublicsCache {
    private static String weekStartDate ;
    private static String weekEndDate ;
    private static String nextOfferAvailableDate ;
    private static LocalDate dateTime ;

    private static final Logger LOGGER = Logger.getLogger(RAGpublicsCache.class) ;

    private RAGpublicsCache() {
        try {
            init();
        } catch (Exception ex) {
            LOGGER.error("Error occured at ==> ", ex);
        }
    }

    public String getWeekStartDate() {
        LocalDate currentDate = LocalDate.now() ;
        if (!currentDate.equals(dateTime)) {
            try {
                init();
            } catch (Exception ex) {
                LOGGER.error("Error occured at ==> ", ex);
            }
        }
        return weekStartDate;
    }

    public  String getWeekEndDate() {
        LocalDate currentDate = LocalDate.now() ;
        if (!currentDate.equals(dateTime)) {
            try {
                init();
            } catch (Exception ex) {
                LOGGER.error("Error occured at ==> ", ex);
            }
        }
        return weekEndDate;
    }

    public String getNextOfferAvailableDate() {
        LocalDate currentDate = LocalDate.now() ;
        if (!currentDate.equals(dateTime)) {
            try {
                init();
            } catch (Exception ex) {
                LOGGER.error("Error occured at ==> ", ex);
            }
        }
        return nextOfferAvailableDate;
    }

    public void reloadCache () {
        try {
            init();
        } catch (Exception ex) {
            LOGGER.error("Error occured at ==> ", ex);
        }
    }

    public static RAGpublicsCache instance() { return InstanceHolder.INSTANCE; }

    private void init () throws Exception {
        LOGGER.info("RAGpublicsCache Loading START");
        dateTime = LocalDate.now() ;
        LookUpDAO lookUpDAO = new LookUpDAO() ;
        List<String> ragValues = lookUpDAO.getRagpublicValues() ;
        weekStartDate = ragValues.get(0) ;
        weekEndDate = ragValues.get(1) ;
        nextOfferAvailableDate = ragValues.get(2) ;
        LOGGER.info("RAGpublicsCache Loading END");
    }

    private static class InstanceHolder {
        private static RAGpublicsCache INSTANCE = new RAGpublicsCache();
    }
}

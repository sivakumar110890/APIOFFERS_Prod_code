package com.comviva.api.j4u.utils;

import java.time.LocalDate;
import java.util.List;

import org.apache.log4j.Logger;

import com.comviva.api.j4u.dao.LookUpDAO;

public class SAGpublicsCache {
	 private static String weekStartDate ;
	    private static String weekEndDate ;
	    private static String nextOfferAvailableDate ;
	    private static LocalDate dateTime ;

	    private static final Logger LOGGER = Logger.getLogger(SAGpublicsCache.class) ;

	    private SAGpublicsCache() {
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

	    public static SAGpublicsCache instance() { return InstanceHolder.INSTANCE; }

	    private void init () throws Exception {
	        LOGGER.info("SAGpublicsCache Loading START");
	        dateTime = LocalDate.now() ;
	        LookUpDAO lookUpDAO = new LookUpDAO() ;
	        List<String> sagValues = lookUpDAO.getSagpublicValues() ;
	        weekStartDate = sagValues.get(0) ;
	        weekEndDate = sagValues.get(1) ;
	        nextOfferAvailableDate = sagValues.get(2) ;
	        LOGGER.info("SAGpublicsCache Loading END");
	    }

	    private static class InstanceHolder {
	        private static SAGpublicsCache INSTANCE = new SAGpublicsCache();
	    }

}

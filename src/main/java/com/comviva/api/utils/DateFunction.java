package com.comviva.api.utils;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author chandrakantha.sn
 */

public class DateFunction {
    private static final Logger LOGGER = Logger.getLogger(DateFunction.class);

    /**
     * @param format
     * @return
     * @throws FunctionException
     */
    public static String getCurrentDateInFormat(String format) throws FunctionException {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("getCurrentDateInFormat : format : " + format);
        }
        if (format == null || format.trim().length() <= 0) {
            throw new FunctionException("Format is null or empty");
        }

        final Calendar cal = Calendar.getInstance();
        final DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(cal.getTime());
    }

    /**
     * @param date
     * @param outputFormat
     * @return
     * @throws FunctionException
     */
    public static String getDateInFormat(Date date, String outputFormat) throws FunctionException {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("getDateInFormat : date : " + date + " \n outputFormat : " + outputFormat);
        }
        if (date == null) {
            throw new FunctionException("Date object is null");
        }
        if (outputFormat == null || outputFormat.trim().length() <= 0) {
            throw new FunctionException("Format is null or empty");
        }

        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final DateFormat dateFormat = new SimpleDateFormat(outputFormat.trim());
        return dateFormat.format(cal.getTime());
    }

    /**
     * @param strDate
     * @param inputFormat
     * @param outputFormat
     * @return
     * @throws FunctionException
     */
    public static String getDateInFormat(String strDate, String inputFormat, String outputFormat) throws FunctionException {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("getDateInFormat : strDate : " + strDate + " \n inputFormat : " + inputFormat + " \n outputFormat : " + outputFormat);
        }
        if (strDate == null || strDate.trim().length() <= 0) {
            // throw new MacroException("strDate is null or empty");
            // strDate = "0000-00-00";
            // inputFormat = "yyyy-MM-dd";
            return "0000-00-00";
        }
        if (inputFormat == null || inputFormat.trim().length() <= 0) {
            throw new FunctionException("inputFormat is null or empty");
        }
        if (outputFormat == null || outputFormat.trim().length() <= 0) {
            throw new FunctionException("outputFormat is null or empty");
        }

        DateFormat formatter = new SimpleDateFormat(inputFormat.trim());
        Date date = null;
        try {
            date = (Date) formatter.parse(strDate);
        } catch (ParseException e) {
            throw new FunctionException(e);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(" date " + date);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        formatter = new SimpleDateFormat(outputFormat.trim());
        return formatter.format(cal.getTime());

    }

    /**
     * @param noOfDays
     * @param outputFormat
     * @return
     * @throws FunctionException
     */
    public static String getDateInFormat(int noOfDays, String outputFormat) throws FunctionException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getDateInFormat " + " format : " + outputFormat);
        }
        if (outputFormat == null || outputFormat.trim().length() <= 0) {
            throw new FunctionException("OutputFormat is null or empty");
        }
        Calendar cal = Calendar.getInstance();
        setTimeToZero(cal);
        cal.add(Calendar.DAY_OF_YEAR, noOfDays);
        final DateFormat dateFormat = new SimpleDateFormat(outputFormat);
        return dateFormat.format(cal.getTime());

    }

    /**
     * @param format
     * @return
     * @throws FunctionException
     */
    public static String getPreviousDateInFormat(String format) throws FunctionException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getPreviousDateInFormat " + " format : " + format);
        }
        if (format == null || format.trim().length() <= 0) {
            throw new FunctionException("Format is null or empty");
        }
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        final DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(cal.getTime());
    }

    /**
     * @param date
     * @param outputFormat
     * @return
     * @throws FunctionException
     */
    public static String getPreviousDateInFormat(Date date, String outputFormat) throws FunctionException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getPreviousDateInFormat : " + " date : " + date + "\n outputFormat : " + outputFormat);
        }
        if (date == null) {
            throw new FunctionException("Date object is null");
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        final DateFormat dateFormat = new SimpleDateFormat(outputFormat.trim());
        return dateFormat.format(cal.getTime());
    }

    /**
     * @param strDate
     * @param inputFormat
     * @param outputFormat
     * @return
     * @throws FunctionException
     */
    public static String getPreviousDateInFormat(String strDate, String inputFormat, String outputFormat) throws FunctionException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getPreviousDateInFormat : strDate : " + strDate + " \n inputFormat : " + inputFormat + " \n outputFormat : " + outputFormat);
        }
        if (strDate == null || strDate.trim().length() <= 0) {
            // throw new MacroException("strDate is null or empty");
            // strDate = "0000-00-00";
            // inputFormat = "yyyy-MM-dd";
            return "0000-00-00";
        }
        if (inputFormat == null || inputFormat.trim().length() <= 0) {
            throw new FunctionException("inputFormat is null or empty");
        }
        if (outputFormat == null || outputFormat.trim().length() <= 0) {
            throw new FunctionException("outputFormat is null or empty");
        }

        DateFormat formatter = new SimpleDateFormat(inputFormat.trim());
        Date date = null;
        try {
            date = (Date) formatter.parse(strDate);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("date : " + date);
            }
        } catch (ParseException e) {
            throw new FunctionException(e);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        formatter = new SimpleDateFormat(outputFormat.trim());
        return formatter.format(cal.getTime());
    }

    public static String getStartDateOfWeek(String outputFormat) throws FunctionException {

        if (outputFormat == null || outputFormat.trim().length() == 0) {
            throw new FunctionException("outputFormat is null or empty");
        }

        DateFormat df = new SimpleDateFormat(outputFormat.trim());
        Calendar cal_date = Calendar.getInstance();
        int day = cal_date.get(Calendar.DAY_OF_WEEK);
        cal_date.add(Calendar.DATE, -day + 1);
        return df.format(cal_date.getTime());
    }

    public static String getStartDateOfWeek(String strDate, String inputFormat, String outputFormat) throws FunctionException {

        if (inputFormat == null || inputFormat.trim().length() == 0) {
            throw new FunctionException("inputFormat is null or empty");
        }
        if (strDate == null || strDate.trim().length() == 0) {
            throw new FunctionException("input 'strDate' is null or empty");
        }
        if (outputFormat == null || outputFormat.trim().length() == 0) {
            throw new FunctionException("outputFormat is null or empty");
        }

        DateFormat formatter = new SimpleDateFormat(inputFormat.trim());
        Date date = null;
        try {
            date = (Date) formatter.parse(strDate.trim());
        } catch (ParseException e) {
            throw new FunctionException(e);
        }

        DateFormat df = new SimpleDateFormat(outputFormat.trim());
        Calendar cal_date = Calendar.getInstance();
        cal_date.setTime(date);
        int day = cal_date.get(Calendar.DAY_OF_WEEK);
        cal_date.add(Calendar.DATE, -day + 1);
        return df.format(cal_date.getTime());
    }

    /**
     * @param noOfWeeks
     * @param outputFormat
     * @return
     * @throws FunctionException
     */
    public static String getStartDateOfWeek(int noOfWeeks, String outputFormat) throws FunctionException {
        if (outputFormat == null || outputFormat.trim().length() == 0) {
            throw new FunctionException("outputFormat is null or empty");
        }
        DateFormat dateFormatter = new SimpleDateFormat(outputFormat.trim());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        setTimeToZero(cal);
        cal.add(Calendar.WEEK_OF_YEAR, noOfWeeks);
        return dateFormatter.format(cal.getTime());
    }

    /**
     * @param noOfWeeks
     * @param outputFormat
     * @return
     * @throws FunctionException
     */
    public static Date getStartDateOfWeek(int noOfWeeks) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        setTimeToZero(cal);
        cal.add(Calendar.WEEK_OF_YEAR, noOfWeeks);
        return cal.getTime();
    }

    /**
     * @param cal
     */
    private static void setTimeToZero(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    public static String getEndDateOfWeek(String strDate, String inputFormat, String outputFormat) throws FunctionException {

        if (inputFormat == null || inputFormat.trim().length() == 0) {
            throw new FunctionException("inputFormat is null or empty");
        }
        if (strDate == null || strDate.trim().length() == 0) {
            throw new FunctionException("input 'strDate' is null or empty");
        }
        if (outputFormat == null || outputFormat.trim().length() == 0) {
            throw new FunctionException("outputFormat is null or empty");
        }

        DateFormat formatter = new SimpleDateFormat(inputFormat.trim());
        Date date = null;
        try {
            date = (Date) formatter.parse(strDate);
        } catch (ParseException e) {
            throw new FunctionException(e);
        }

        DateFormat df = new SimpleDateFormat(outputFormat.trim());
        Calendar cal_date = Calendar.getInstance();
        cal_date.setTime(date);
        int day = cal_date.get(Calendar.DAY_OF_WEEK);
        cal_date.add(Calendar.DATE, -day + 7);
        return df.format(cal_date.getTime());
    }

    public static String getEndDateOfWeek(int noOfWeeks, String outputFormat) throws FunctionException {
        if (outputFormat == null || outputFormat.trim().length() == 0) {
            throw new FunctionException("outputFormat is null or empty");
        }
        DateFormat dateFormatter = new SimpleDateFormat(outputFormat.trim());
        Calendar cal = Calendar.getInstance();
        setTimeToZero(cal);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.add(Calendar.WEEK_OF_YEAR, noOfWeeks);
        int days = cal.getActualMaximum(Calendar.DAY_OF_WEEK);
        cal.set(Calendar.DAY_OF_WEEK, days);
        return dateFormatter.format(cal.getTime());
    }

    public static String getEndDateOfWeek(String outputFormat) throws FunctionException {

        if (outputFormat == null || outputFormat.trim().length() == 0) {
            throw new FunctionException("outputFormat is null or empty");
        }

        DateFormat df = new SimpleDateFormat(outputFormat.trim());
        Calendar cal_date = Calendar.getInstance();
        int day = cal_date.get(Calendar.DAY_OF_WEEK);
        cal_date.add(Calendar.DATE, -day + 7);
        return df.format(cal_date.getTime());
    }

    public static String getStartDateOfMonth(String strDate, String inputFormat, String outputFormat) throws FunctionException {

        if (inputFormat == null || inputFormat.trim().length() == 0) {
            throw new FunctionException("inputFormat is null or empty");
        }
        if (strDate == null || strDate.trim().length() == 0) {
            throw new FunctionException("input 'strDate' is null or empty");
        }
        if (outputFormat == null || outputFormat.trim().length() == 0) {
            throw new FunctionException("outputFormat is null or empty");
        }

        DateFormat formatter = new SimpleDateFormat(inputFormat.trim());
        Date date = null;
        try {
            date = (Date) formatter.parse(strDate);
        } catch (ParseException e) {
            throw new FunctionException(e);
        }

        DateFormat df = new SimpleDateFormat(outputFormat.trim());
        Calendar cal_date = Calendar.getInstance();
        cal_date.setTime(date);
        int day = cal_date.get(Calendar.DAY_OF_MONTH);
        cal_date.add(Calendar.DATE, -day + 1);
        return df.format(cal_date.getTime());
    }

    /**
     * @param noOfWeeks
     * @param outputFormat
     * @return
     * @throws FunctionException
     */
    public static String getStartDateOfMonth(int noOfMonths, String outputFormat) throws FunctionException {
        if (outputFormat == null || outputFormat.trim().length() == 0) {
            throw new FunctionException("OutputFormat is null or empty");
        }
        DateFormat dateFormatter = new SimpleDateFormat(outputFormat.trim());
        Calendar cal = Calendar.getInstance();
        setTimeToZero(cal);
        cal.add(Calendar.MONTH, noOfMonths);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return dateFormatter.format(cal.getTime());
    }

    /**
     * @param noOfWeeks
     * @param outputFormat
     * @return
     * @throws FunctionException
     */
    public static Date getStartDateOfMonth(int noOfMonths) {
        Calendar cal = Calendar.getInstance();
        setTimeToZero(cal);
        cal.add(Calendar.MONTH, noOfMonths);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * @param noOfWeeks
     * @param outputFormat
     * @return
     * @throws FunctionException
     */
    public static String getEndDateOfMonth(int noOfMonths, String outputFormat) throws FunctionException {
        if (outputFormat == null || outputFormat.trim().length() == 0) {
            throw new FunctionException("OutputFormat is null or empty");
        }
        DateFormat dateFormatter = new SimpleDateFormat(outputFormat.trim());
        Calendar cal = Calendar.getInstance();
        setTimeToZero(cal);
        cal.add(Calendar.MONTH, noOfMonths);
        int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return dateFormatter.format(cal.getTime());
    }

    public static String getStartDateOfMonth(String outputFormat) throws FunctionException {

        if (outputFormat == null || outputFormat.trim().length() == 0) {
            throw new FunctionException("outputFormat is null or empty");
        }

        DateFormat df = new SimpleDateFormat(outputFormat.trim());
        Calendar cal_date = Calendar.getInstance();
        int day = cal_date.get(Calendar.DAY_OF_MONTH);
        cal_date.add(Calendar.DATE, -day + 1);
        return df.format(cal_date.getTime());
    }

    public static String getEndDateOfMonth(String strDate, String inputFormat, String outputFormat) throws FunctionException {

        if (inputFormat == null || inputFormat.trim().length() == 0) {
            throw new FunctionException("inputFormat is null or empty");
        }
        if (strDate == null || strDate.trim().length() == 0) {
            throw new FunctionException("input 'strDate' is null or empty");
        }
        if (outputFormat == null || outputFormat.trim().length() == 0) {
            throw new FunctionException("outputFormat is null or empty");
        }

        DateFormat formatter = new SimpleDateFormat(inputFormat.trim());
        Date date = null;
        try {
            date = (Date) formatter.parse(strDate);
        } catch (ParseException e) {
            throw new FunctionException(e);
        }

        DateFormat df = new SimpleDateFormat(outputFormat.trim());
        Calendar cal_date = Calendar.getInstance();
        cal_date.setTime(date);
        int day = cal_date.get(Calendar.DAY_OF_MONTH);
        cal_date.add(Calendar.DATE, -day + cal_date.getActualMaximum(Calendar.DAY_OF_MONTH));
        return df.format(cal_date.getTime());
    }

    public static String getEndDateOfMonth(String outputFormat) throws FunctionException {

        if (outputFormat == null || outputFormat.trim().length() == 0) {
            throw new FunctionException("outputFormat is null or empty");
        }

        DateFormat df = new SimpleDateFormat(outputFormat.trim());
        Calendar cal_date = Calendar.getInstance();
        int day = cal_date.get(Calendar.DAY_OF_MONTH);
        cal_date.add(Calendar.DATE, -day + cal_date.getActualMaximum(Calendar.DAY_OF_MONTH));
        return df.format(cal_date.getTime());
    }

    public static Date getDateFromString(String date) {
        Date utilDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (date != null && date.trim().length() > 0) {
            try {
                utilDate = new Date(sdf.parse(date).getTime());
            } catch (Exception e) {
                LOGGER.error("ERROR : While parsing date | ", e);
            }
        }
        return utilDate;
    }

    public static String getStringFromDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static Date getPreviousDate() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static Date getPreviousDate(int noOfDays) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -noOfDays);
        return cal.getTime();
    }

    public static Long getDateInSecs(String dateStr, String format) throws FunctionException {
        if (dateStr == null) {
            return getCurrentDateInSecs();
        }
        if (format == null || format.trim().length() <= 0) {
            throw new FunctionException("Input Format is null or empty");
        }
        DateFormat formatter = new SimpleDateFormat(format.trim());
        Date date = null;
        try {
            date = (Date) formatter.parse(dateStr);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("date : " + date);
            }
        } catch (ParseException e) {
            throw new FunctionException(e);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getTimeInMillis() / 1000;
    }

    public static Long getCurrentDateInSecs() {
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis() / 1000;
    }

    public static Long getPreviousDateInSecs() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTimeInMillis() / 1000;
    }

    public static Long getCurrentDateInMillis() {
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis();
    }

    public static Long getDateInMillis(String dateStr, String format) throws FunctionException {
        if (dateStr == null) {
            return getCurrentDateInMillis();
        }
        if (format == null || format.trim().length() <= 0) {
            throw new FunctionException("Input Format is null or empty");
        }
        DateFormat formatter = new SimpleDateFormat(format.trim());
        Date date = null;
        try {
            date = (Date) formatter.parse(dateStr);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("date : " + date);
            }
        } catch (ParseException e) {
            throw new FunctionException(e);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getTimeInMillis();
    }

    public static Long getPreviousDateInMillis() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTimeInMillis();
    }

}

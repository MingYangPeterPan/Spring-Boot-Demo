package com.ito.vip.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

public final class DateFormatUtil {

    private static final String formatTemplate = "yy-MM-dd HH:mm:ss";

    private DateFormatUtil(){

    }

    public static String convertDateToString(Date date){
         String result =  StringUtils.EMPTY;
         if(date!=null){
             SimpleDateFormat formatter = new SimpleDateFormat(formatTemplate);
             result = formatter.format(date);
         }
         return result;
     }

    public static Date convertStringToDate(String dateStr){
        return convertStringToDate(dateStr, TimeZone.getDefault().getID(), formatTemplate);
    }

    public static Date convertStringToDate(String dateStr, String timeZoneID, String pattern) {
        Date result =  null;
        if(StringUtils.isNotBlank(dateStr)) {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            TimeZone timeZone = TimeZone.getTimeZone(timeZoneID);
            formatter.setTimeZone(timeZone);
            try {
                result = formatter.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}

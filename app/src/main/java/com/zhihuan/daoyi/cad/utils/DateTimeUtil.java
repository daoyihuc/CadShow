package com.zhihuan.daoyi.cad.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class DateTimeUtil {
    private static ThreadLocal<Map<String, SimpleDateFormat>> threadLocal = new ThreadLocal<Map<String, SimpleDateFormat>>();

    private static SimpleDateFormat getFormater(String pattern) {
        Map<String, SimpleDateFormat> formaterMap = threadLocal.get();
        if (formaterMap == null) {
            formaterMap = new HashMap<String, SimpleDateFormat>();
            threadLocal.set(formaterMap);
        }
        if (!formaterMap.containsKey(pattern)) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            formaterMap.put(pattern, sdf);
        }
        return formaterMap.get(pattern);
    }

    /**
     * @param dateStr     时间字符串
     * @param formateType 格式化的类型 形如 yyyy-MM-dd HH:mm:ss yyyy-MM-dd 等
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String dateStr, String formateType) throws ParseException {
        SimpleDateFormat sdf = getFormater(formateType);
        return sdf.parse(dateStr);
    }

    /**
     * @param date        时间
     * @param formateType 格式化的类型 形如 yyyy-MM-dd HH:mm:ss yyyy-MM-dd 等
     * @return
     */
    public static String DateToString(Date date, String formateType) {
        SimpleDateFormat sdf = getFormater(formateType);
        return sdf.format(date);
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = getFormater("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     *
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = getFormater("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = getFormater("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 获取传入时间的月份的第一天
     *
     * @param date
     * @return
     */
    public static Date getMonthFirstDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取传入时间的月份的最后一天
     *
     * @param date
     * @return
     */
    public static Date getMonthLastDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}

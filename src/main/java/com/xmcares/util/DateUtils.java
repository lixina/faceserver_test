/**
 * @(#)com.xmcares.scims.util.DateUtils.java Copyright (c) 2014-2018 厦门民航凯亚有限公司
 */
package com.xmcares.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 日期转换
 * 
 * @author Linyx
 * @version 1.0 2015-8-26
 */
public class DateUtils {
	/**
     * 获取当前日期
     */
    public static String getCurrentDate(String formate) {
        if (formate == null) {
            formate = "yyyyMMddHHmmssSSS";
        }
        Date t = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(formate);
        return sf.format(t);
    }
    
    public static Date dateToDate(Date date, String formatStr){
		if (date==null) {
			date = new Date();
		}
		return stringToDate(parseDateToString(date, formatStr), formatStr);
	}

	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(DateUtils.class);	
    /**
     * 将日期对象转换为日期格式字符串
     * 
     * @param date
     *            日期对象
     * @param pattern
     *            目标字符串格式（例如："yyyy-MM-dd"）
     * @return
     */
    public static String parseDateToString(Date date, String pattern) {
       try {
           DateFormat dateFormat = new SimpleDateFormat(pattern);
           return dateFormat.format(date);
       }catch(Exception e){
           return "";
       }
    }

    /**
     * 将日期转换为该日期人对象所表示时分秒字符串
     * 
     * @param date
     *            日期对象
     * @return
     */
    public static String parseTimeToString(Date date) {
        DateFormat dateFormat = DateFormat.getTimeInstance();
        return dateFormat.format(date);
    }

    /**
     * 将日期转换为该日期人对象所表示的日期以及时分秒字符串
     * 
     * @param date
     *            日期对象
     * @param pattern
     *            目标字符串格式（例如："yyyy-MM-dd HH:mm:ss"）
     * @return
     */
    public static String parseDateTimeToString(Date date, String pattern) {
       try {
           if (date != null) {
               DateFormat dateFormat = new SimpleDateFormat(pattern);
               return dateFormat.format(date);
           } else {
               return "";
           }
       }catch (Exception e){
           return "";
       }
    }

    /**
     * String To Date
     * 
     * @param
     * @return
     */
    public static Date stringToDate(String str, String eg) {
        DateFormat format = new SimpleDateFormat(eg);
        Date date = null;
        if (str != null && !"".equals(str)) {
            try {
                date = format.parse(str);
            } catch (Exception e) {
               logger.error("日期类型转换失败",e);
            }
        }
        return date;
    }

    /**
     * 判断日期字符串是否正确
     * @param str 要判断的字符串
     * @param mode 模板 如"yyyy/MM/dd"
     * @return
     */
    public static boolean isValidDate(String str, String mode) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat(mode);
        try {
            // 设置lenient为false.
            // 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }
    
    /**
     * 返回时间追加n年
     * @param date
     * @param n
     * @return
     */
    public static Date DateAddYear(Date date, int n){
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, n);
        
        return calendar.getTime();
    }
    
    
    /**
     * 返回时间月份n年
     * @param date
     * @param n
     * @return
     */
    public static Date DateAddMonth(Date date, int n){
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, n);
        
        return calendar.getTime();
    }
    /**
     * 返回时间追加n天
     * @param date
     * @param n
     * @return
     */
    public static Date DateAddDate(Date date, int n){
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n);
        
        return calendar.getTime();
    }
    
    /**
     * 追加n分钟
     * 
     * @param date
     * @param n
     * @return
     */
    public static Date DateAddMinute(Date date, int n){
	        
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        calendar.add(Calendar.MINUTE, n);
	        
	        return calendar.getTime();
	        }
    
  
    /**
     * 获取今天开始时间
     * @return
     */
    public static Date getStartDate(){
            return  stringToDate(parseDateTimeToString(new Date(), "yyyy-MM-dd")+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 获取今天结束时间
     * @return
     */
    public static Date getEndDate(){
            return  stringToDate(parseDateTimeToString(new Date(), "yyyy-MM-dd")+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
    }
}

package com.fb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * 轉換為民國日期字串
	 * @param date 欲轉換日期
	 * @return 民國日期字串 ex. 95/01/01
	 */
	public static String getRocDateString(Date date) {
		return getRocDateString(date, "/MM/dd");
	}

	/**
	 * 轉換為民國日期字串
	 * @param date 欲轉換日期
	 * @param pattern 輸出格式，參見 java.text.SimpleDateFormat
	 * @return 民國日期字串 ex. 95/01/01
	 */
	public static String getRocDateString(Date date, String pattern) {
		if(date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return String.valueOf(cal.get(Calendar.YEAR) - 1911) + sdf.format(cal.getTime());
	}

	/**
	 * 轉換為西元日期字串
	 * @param date 欲轉換日期
	 * @return 西元日期字串 ex. 2006/01/01
	 */
	
	public static String getDateString(Date date) {
		return getDateString(date, "yyyy/MM/dd");
	}
	
	/**
	 * 轉換為西元日期加時分秒字串
	 * @param date 欲轉換日期
	 * @return 西元日期字串 ex. 2006/01/01 00:00:00
	 */
	
	public static String getDateTimeString(Date date) {
		return getDateString(date, "yyyy/MM/dd HH:mm:ss");
	}
	
	/**
	 * 依照指定格式轉換為西元日期字串
	 * @param date 欲轉換日期
	 * @param pattern 輸出格式，參見 java.text.SimpleDateFormat
	 * @return 西元日期字串
	 * @see java.text.SimpleDateFormat
	 */
	public static String getDateString(Date date, String pattern) {
		if(date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	

	
	/**
	 * 將輸入字串轉為日期物件
	 * @param dateStr 日期字串 ex. 2006/01/01
	 * @return 日期物件
	 * @throws ParseException if the beginning of the specified string cannot be parsed
	 */
	public static Date getDateObject(String dateStr) throws ParseException {
		return getDateObject(dateStr, "yyyy/MM/dd");
	}
	
	/**
	 * 將輸入字串轉為日期物件
	 * @param dateStr 日期字串(須配合輸入格式)
	 * @param pattern 輸入日期字串格式 ex. yyyy/MM/dd，參見java.text.SimpleDateFormat
	 * @return 日期物件
	 * @throws ParseException if the beginning of the specified string cannot be parsed
	 * @see java.text.SimpleDateFormat
	 */
	public static Date getDateObject(String dateStr, String pattern) throws ParseException {
		if(dateStr == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(dateStr);
	}
	
	/**
	 * 比較兩個日期
	 * @param dateStr1 日期字串一, ex. 2006/01/01
	 * @param dateStr2 日期字串二, ex. 2006/12/31
	 * @return 若 日期一等於日期二,回傳0; 若日期一小於日期二,回傳-1; 若日期一大於日期二,回傳1。
	 * @throws ParseException 日期字串格式錯誤
	 */
	public static int compareDates(String dateStr1, String dateStr2) throws ParseException {
		Date date1 = getDateObject(dateStr1);
		Date date2 = getDateObject(dateStr2);
		return date1.compareTo(date2);
	}

}

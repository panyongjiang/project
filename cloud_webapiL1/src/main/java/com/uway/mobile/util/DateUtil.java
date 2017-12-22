package com.uway.mobile.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;


public class DateUtil {
	
	private static SimpleDateFormat  sdf  = null;
	
	private static final Logger logger = Logger.getLogger(DateUtil.class);
	/**
	 * 得到String类型的时间
	 * 
	 * @param source
	 * @param pattern
	 * @return
	 */
	public static String getStringTime(Date source, String pattern) {
		sdf = new SimpleDateFormat(pattern);
		return sdf.format(source);
	}
	
	/**
	 * 得到Date类型的时间
	 * 
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static java.util.Date getDateTime(String time, String pattern) {
		sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			logger.error("解析String时间类型为Date时间类型发生错误");
		}
		return null;

	}

	/**
	 * 获取几个月之前或之后的日期
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date getDiffMonth(Date date,int n){
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTime(date);
		c.add(Calendar.MONTH, n); 
        return c.getTime(); 
	}
	
	/**
	 * 获取日期所在月份的第一天
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfMonth(Date date){
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime(); 
	}
	
	/**
	 * 获取日期所在月份的最后一天的日期
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfMonth(Date date){
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 0);
        return c.getTime(); 
	}
	/**
	 * 获取两个时间之间所有日期的集合"yyyyMMdd"
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static List<String> getBetweenDate(String startTime,String endTime){
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd HH/mm/ss"); 
		Calendar calendar = Calendar.getInstance();	
		List<String> dateList = new ArrayList<String>();
		
		try {
			Date startDate = simpleDate.parse(startTime);
			Date endDate = simpleDate.parse(endTime);
			Calendar start = Calendar.getInstance();
			start.setTime(startDate); 
			Calendar end = Calendar.getInstance();
			end.setTime(endDate);
			if(start.before(end)){
				calendar.clear();
				calendar.setTime(startDate);
				while(!calendar.equals(end)){
					dateList.add(sdf2.format(calendar.getTime()).substring(0, 8));
					calendar.add(Calendar.DATE, 1);
				}
				dateList.add(sdf2.format(end.getTime()).substring(0, 8));
			}else{
				return null;
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateList;
	}
	/**
	 * 获取今日日期
	 * @return
	 * @author wanglei
	 * @create_time 2017年10月17日下午1:51:18
	 */
	public static String getDate() {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		return (dateFormater.format(date));
	}
	
	public static String getYesterday(){	
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,-1);
		Date time=cal.getTime();
		return df.format(time);
	}

	
	public static void main(String[] args) {		
		//System.out.println(DateUtil.getDate());
	}

}

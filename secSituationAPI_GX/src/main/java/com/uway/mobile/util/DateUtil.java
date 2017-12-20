package com.uway.mobile.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

public class DateUtil {

	private static SimpleDateFormat sdf = null;

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

}

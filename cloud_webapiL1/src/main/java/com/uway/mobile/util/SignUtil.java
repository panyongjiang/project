package com.uway.mobile.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.uway.mobile.common.Constance;

public class SignUtil {

	private static final String MAC_NAME = "HmacSHA1";    
    private static final String ENCODING = "UTF-8";     
    /**
     * 得到签名
     * @param strs
     * @return
     * @throws Exception
     */
	public static String getSign(String[] strs) throws Exception{
		String str = sort(strs);
		String sign = bytesToHexString(HmacSHA1Encrypt(str,Constance.WAF_API_KEY));
		return sign;
	}
	
	public static long getTime(){  
		Date date = new Date();
	    return date.getTime()/1000;  
	} 
	
	public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
								 
    public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception     
    {           
        byte[] data=encryptKey.getBytes(ENCODING);  
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称  
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);   
        //生成一个指定 Mac 算法 的 Mac 对象  
        Mac mac = Mac.getInstance(MAC_NAME);   
        //用给定密钥初始化 Mac 对象  
        mac.init(secretKey);    
          
        byte[] text = encryptText.getBytes(ENCODING);    
        //完成 Mac 操作   
        return mac.doFinal(text);
    }
    
    public static String sort(String[] strs){
    	String newStr = "";
    	Arrays.sort(strs);  
    	for(String str : strs) {  
    	    newStr += str+"&";
    	}
    	return newStr.substring(0,newStr.length()-1);
    }
    
    public static String getDate(){
    	SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMdd");
    	Date date=new Date();
    	return (dateFormater.format(date));
    }
    public static double getDoubleDate(){
    	return (Double.valueOf(getDate()));
    }
    public static double getDoubleStart(){
    	return (Double.valueOf(getStart()));
    }
    public static double getNow(){
    	SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
    	Date date=new Date();
    	return (Double.valueOf(dateFormater.format(date)));
    }
    public static long getNowLong(){
    	SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
    	Date date=new Date();
    	return (Long.valueOf(dateFormater.format(date)));
    }
    public static String getStart(){
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, -30);
        Date m = c.getTime();
        String startTime = format.format(m);
        return startTime;
    }
    public static String[] getDates(){
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        
    	String dates[] = new String[30];
    	for(int i=0;i<30;i++){
    		c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, -i);
            Date m = c.getTime();
            String time = format.format(m);
    		dates[i] = time;
    	}
    	return dates;
    }
    public static String[] getEightWeek(){
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        
        
    	String dates[] = new String[8];
    	for(int i=0;i<8;i++){
    		Calendar c = Calendar.getInstance();
    		 c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    		 c.add(Calendar.WEEK_OF_YEAR, -i);
            Date m = c.getTime();
            String time = format.format(m);
    		dates[i] = time;
    	}
    	return dates;
    }
    
    public static String[] getFromEnd(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd%20HH:mm:ss");
    	String dates[] = new String[2];
    		Calendar a = Calendar.getInstance();
    		a.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    		a.add(Calendar.WEEK_OF_YEAR, -1);
            Date f = a.getTime();
            String from = format.format(f);
            Calendar b = Calendar.getInstance();
            b.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
   		    b.add(Calendar.WEEK_OF_YEAR, 0);
   		    Date e = b.getTime();
            String end = format.format(e);
            
            dates[0] = from;
    		dates[1] = end;
    	    return dates;
    }
    
    public static String toUnicode(String str){
    	String res = "";
    	char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
          res += "\\u" + Integer.toString(chars[i], 16);
        }
             return res;
    }
    
 // 获得当天0点时间  
    public static Date getTimesmorning() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return cal.getTime();  
    }  
  
    // 获得当天24点时间  
    public static Date getTimesnight() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.HOUR_OF_DAY, 24);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return  cal.getTime();  
    }  
  
    // 获得本周一0点时间  
    public static Date getTimesWeekmorning() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);  
        return  cal.getTime();  
    }  
  
    // 获得本周日24点时间  
    public  static Date getTimesWeeknight() {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(getTimesWeekmorning());  
        cal.add(Calendar.DAY_OF_WEEK, 7);  
        return cal.getTime();  
    }  
  
    // 获得本月第一天0点时间  
    public static Date getTimesMonthmorning() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));  
        return  cal.getTime();  
   }  
  
    // 获得本月最后一天24点时间  
    public static Date getTimesMonthnight() {  
       Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
       cal.set(Calendar.HOUR_OF_DAY, 24);  
        return cal.getTime();  
    }  
    
    //获取当前时间
    public static long getNowTime(){
    	SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
    	Date date=new Date();
    	return (Long.valueOf(dateFormater.format(date)));
    }
    
    //获取去年的现在这个时间点的时间
    public static long getLastYearNowTime(){
    	SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)-1);
    	Date date=cal.getTime();   
        return (Long.valueOf(dateFormater.format(date)));
    }
    
    //获取去年的今天的0点时间
    public static long lastYearmorning(){
    	SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
    	Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)-1);
        Date date = cal.getTime(); 
        return (Long.valueOf(dateFormater.format(date)));
    }
    
    //获取上个月的当前时间点的时间
    public static long lastMonthNowTime(){
    	SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-1);
    	Date date=cal.getTime(); 
        return (Long.valueOf(dateFormater.format(date)));
    }
    
    //获取上个月的今天的0点时间
    public static long lastMonthmorning(){
    	SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss");
    	Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)-1);
        Date date = cal.getTime(); 
        return (Long.valueOf(dateFormater.format(date)));
    }
    
    //获取上个月的第一天
    public static String lastMonthStartTime(int n){
    	Calendar ca = Calendar.getInstance();
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");  
        ca.add(Calendar.MONTH, n);  
        ca.set(Calendar.DAY_OF_MONTH, 1);  
        return format.format(ca.getTime()); 
    }
    
    //获取上个月的最后一天
    public static String lastMonthEndTime(int n){
    	Calendar ca = Calendar.getInstance();
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");  
        ca.add(Calendar.MONTH, n);  
        ca.set(Calendar.DAY_OF_MONTH, 0);  
        return format.format(ca.getTime()); 
    }
    
    /**
     * 获取两个日期之间的月份
     * @param minDate
     * @param maxDate
     * @return
     * @throws ParseException
     */
    public static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
         result.add(sdf.format(curr.getTime()));
         System.out.println(sdf.format(curr.getTime()));
         curr.add(Calendar.MONTH, 1);
        }

        return result;
      }
    
    public static Date dateStrParse(String date,String formatter) throws ParseException{
    	SimpleDateFormat sdf = new SimpleDateFormat(formatter);
    	return sdf.parse(date);
    }
    
    public static String dateFormat(Date d,String formatter){
    	SimpleDateFormat sdf = new SimpleDateFormat(formatter);
    	return sdf.format(d);
    }
    //根据日期获得最近30天的日期集合Format("yyyyMMdd")
    public static List<String> getAllMoDays(String date) {  
    	SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd HH/mm/ss"); 
		Calendar calendar = Calendar.getInstance();
		try {			
			List<String> dateList = new ArrayList<String>();
			for(int i = 29; i >= 0; i --){
				calendar.clear();
				calendar.setTime(simpleDate.parse(date));
				calendar.add(Calendar.DATE, -i);
				dateList.add(sdf2.format(calendar.getTime()).substring(0, 8));
			}
			return dateList;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
    }  
    public static void main(String[] args) {
    	List<String> dateList = getAllMoDays("20170821");
    	for (String string : dateList) {
			System.out.println(string);
		}
	}
}

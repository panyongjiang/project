package com.uway.mobile.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uway.mobile.domain.DayFlow;
import com.uway.mobile.mapper.DeviceConfigMapper;

@Component
public class CalendarFormatterUtil {
	
	@Autowired
	public DeviceConfigMapper dcm;
	
	/**
	 * 获取当前时间
	 * 
	 * @return 格式：20180825
	 */
	public static String getCurrentTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar=Calendar.getInstance();
		String dateString = formatter.format(calendar.getTime());
		return dateString;
	}
	
	/**
	 * 获取当前月的第一天
	 * @param args
	 */
	public static String getFirstMonthDay(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(calendar.getTime());
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String firstday=formatter.format(calendar.getTime());
		return firstday;
	}
	
	/**
	 * 获取当前日期的指定前多少天
	 * @param args
	 */
	public static String getSpecifyDate(int day){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(calendar.getTime());
		calendar.add(Calendar.DATE, day);
		return formatter.format(calendar.getTime());
	}
	
	
	/**
	 * 获取指定月的最后一天
	 * @param args
	 */
	public static String getLastMaxMonthDate(int param) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        calendar.add(Calendar.MONTH, param);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return formatter.format(calendar.getTime());
    }
	
	/**
	 * 获取指定月的第一天
	 * @param args
	 */
	public static String getLastMinMonthDate(int param) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.MONTH, param);
	    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
	    return formatter.format(calendar.getTime());
	}
	
	/**
	 * 计算区间日期
	 * @param args
	 */
	public static List<String> countDate(String firstTime,String SecondTime){
		List<String> list = new ArrayList<String>();
		int first=Integer.parseInt(SecondTime);
		for(int i=0;i<=Integer.parseInt(firstTime)-Integer.parseInt(SecondTime);i++){
			list.add(Integer.toString(first));
			first++;
		}
		return list;
	}
	
	/**
	 * 计算区间内的月份
	 * @param args
	 */
	public static List<String> countMonth(String firstDate,String secondDate){
		List<String> list = new ArrayList<String>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		Calendar calendar = Calendar.getInstance();
		try {
			Date firstTime = new SimpleDateFormat("yyyyMMdd").parse(firstDate);//定义起始日期
			Date lastTime = new SimpleDateFormat("yyyyMMdd").parse(secondDate);//定义结束日期
			calendar.setTime(firstTime);
			while(calendar.getTime().before(lastTime)){
				String str=formatter.format(calendar.getTime());
				calendar.add(Calendar.MONTH, 1);
				list.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 流量统计
	 */
	public List<Object> countFlow(int day,List<String> deviceIds){
		List<String> firstList=new ArrayList<String>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		//获取当前时间的字符串
		String dateString =getSpecifyDate(-1);
		String firstday=getFirstMonthDay();
		if(Integer.parseInt(dateString)-Integer.parseInt(firstday)<day){
			String specifyDate=getSpecifyDate(-day);
			String lastMonthMaxDay= getLastMaxMonthDate(-1);
			firstList=countDate(lastMonthMaxDay,specifyDate);
			List<String> secondList=countDate(dateString,firstday);
			for(String value:secondList){
				firstList.add(value);
			}
			
		}else{
			if(day==7){
				firstday=getSpecifyDate(-7);
			}
			firstList=countDate(dateString,firstday);
		}
		Map<String,Object> paras = new HashMap<String,Object>();
		paras.put("date", getCurrentTime());
		paras.put("list", deviceIds);
		String month=getCurrentTime().substring(0,getCurrentTime().length()-2);
		paras.put("month", month);
		//查询7天的数据
		List<DayFlow> lists = dcm.selectFlow(paras);
		//查询所有流量
		List<DayFlow> totalFlow=dcm.selectTotalFlow(paras);
		List<Object> para = new ArrayList<Object>();
		for(String time:firstList){
			Map<String,Object> flow = new HashMap<String,Object>();
			double up_flow=0.0;
			double down_flow=0.0;
			BigDecimal up=new BigDecimal(0.0);
			BigDecimal down=new BigDecimal(0.0);
			for(DayFlow value:lists){
				if(value.getDate()!=null&&""!=value.getDate().toString()){
					String date=formatter.format(value.getDate());
					if(!"".equals(date)&&date!=null&&date.toString().equals(time)){
						up_flow+=up.add(value.getUpFlow()).doubleValue();
						down_flow+=down.add(value.getDownFlow()).doubleValue();
					}
				}
			}
			flow.put("upFlowSum", up_flow);
			flow.put("downFlowSum", down_flow);
			flow.put("date", time);
			para.add(flow);
		}
		Map<String,Object> total = new HashMap<String,Object>();
		if(totalFlow.size()>0){
			total.put("totalUpFlow",totalFlow.get(0).getTotalUpFlow());
			total.put("totalDownFlow", totalFlow.get(0).getTotalDownFlow());
		}else{
			total.put("totalUpFlow",0);
			total.put("totalDownFlow", 0);
		}
		
		para.add(total);
		return para;
	}
	
	
	
	
	
	
	/*public static void main(String[] args) {
		String ll = getCurrentTime();
		List<String> list1=countMonth("20150630","20170615");
		String str="20170630";
		String test = str.substring(0,6);
		List<String> month=countMonth(getLastMinMonthDate(-12), getLastMinMonthDate(-1));
		System.out.println(test);
		        //获取当前时间的字符串
				String dateString = getCurrentTime();
				System.out.println(dateString);
				String firstday=getFirstMonthDay();
				System.out.println(firstday);
				if(Integer.parseInt(dateString)-Integer.parseInt(firstday)<30){
					//获取30天前的日期
					String specifyDate=getSpecifyDate(-30);
					System.out.println(specifyDate);
					//获取上一个月的最后一天
					String lastMonthMaxDay= getLastMaxMonthDate(-1);
					System.out.println(lastMonthMaxDay);
					List<String> firstList=countDate(lastMonthMaxDay,specifyDate);
					List<String> secondList=countDate(dateString,firstday);
					for(String value:secondList){
						firstList.add(value);
					}
					System.out.println("-------------");
				}else{
					List<String> secondList=countDate(dateString,firstday);
				}
				System.out.println("----------------");
	}*/

}

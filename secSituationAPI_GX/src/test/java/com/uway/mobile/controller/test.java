package com.uway.mobile.controller;

import java.util.Calendar; 


public class test {

	
	public static void main(String[] args) {
		
		String time=get_mus("12月");
		System.out.println(time);
		
	} 
	
	
		public static String get_mus(String str) {
			str=str.trim();
			String str2="";
			String time="";
			if(str != null && !"".equals(str)){
				for(int i=0;i<str.length();i++){
					if(str.charAt(i)>=48 && str.charAt(i)<=57){
						str2+=str.charAt(i);
					}
				}

			}
			Integer sum=Integer.valueOf(str2);
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR); 
			int month = c.get(Calendar.MONTH); 
			if(month<sum){
				year=year-1;
			}			
			time= String.valueOf(year)+"年"+String.valueOf(sum);						
			return time;
		}			
	
}

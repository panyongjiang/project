package com.uway.tasks.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class HttpUtil {
	
	private static final Logger logger = Logger.getLogger(HttpUtil.class);
	
	/**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static void sendPost(String url, String param) {
    	HttpURLConnection httpConn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            httpConn = (HttpURLConnection)realUrl.openConnection();
            //设置参数
            httpConn.setDoOutput(true);   //需要输出
            httpConn.setDoInput(true);   //需要输入
            httpConn.setUseCaches(false);  //不允许缓存
            httpConn.setRequestMethod("POST");   //设置POST方式连接
            // 设置通用的请求属性
            //设置请求属性
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            httpConn.setRequestProperty("Charset", "UTF-8");
            //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
            httpConn.connect();
            
            //获得响应状态
            int resultCode = httpConn.getResponseCode();
            if(HttpURLConnection.HTTP_OK==resultCode){
            	StringBuffer sb=new StringBuffer();
                String readLine=new String();
                BufferedReader responseReader=new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8"));
                while((readLine=responseReader.readLine())!=null){
                  sb.append(readLine).append("\n");
                }
                responseReader.close();
                logger.debug("结果输出：" + sb.toString());
            }else{
            	logger.debug("调用失败");
            }
            
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
        }finally {
			if(httpConn != null) httpConn.disconnect();
		}
        
    }   
	
}

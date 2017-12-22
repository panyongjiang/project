package com.udp.util;

import java.text.SimpleDateFormat;

import org.elasticsearch.client.Client;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

public class Constants {

	//消息是否加密
	public static String isEn="N";
	//特殊字符
	public static final String OTHERS="#AfR5%^$";
	//随机码redis key前缀
	public static final String BEFS="uway";
	public static final String BEFSS="uwaysSeqs";
	//url redis key前缀
	public static final String URLBEFS="uwayurl";
	//url 不存在事  消息队列的KEY
	public static final String DKEYS="URLKEY";
	
	public static int PORT=9999;
	public static JedisConnectionFactory jedisConnectionFactory;
	public static RedisClusterConnection connection;
	public static Client esClient;
	
	//es 索引和类型
	public static String esIndex="uwayUdp";
	public static String esType="uwayNtfUrlEnil";
	
	public static final int REQ_URL_CHECK=2002;//检测URL地址 
	public static final int ACK_URL_CHECK=2003;//应答检测URL地址
	public static final int NTF_URL_EVIL=2004;//上报恶意URL代号 
	public static final int REQ_AUTH_NUMBER=2005;//触云设备请求服务端认证
	public static final int RSP_AUTH_NUMBER=2006;//云端应答设备认证
	public static final int REQ_URL_QUERY=20000;//恶意 URL 查询数据接口
	
	//头部长度
	public static int headLen=16;
	
	//算法
	public static String EncryptMethod="TinyEncrypt";
	
	//报头错误日志
	public static final String HEADERROR="fail,heads data format has problem, please check!";
	//报体错误日志
	public static final String BODYERROR="fail,bodys data format has problem, please check!";
	//解密失败，请检查密钥
	public static final String DEERROR="fail,decode bodys data fail, please check key!";
	//加密失败，请检查密钥
	public static final String ENERROR="fail,encode bodys data fail, please check key!";
	//错误日志
	public static final String DERROR="data format has problem, please check!";
	public static final String DERROR1="The key is invalid. Please check it!";
	public static final String DERROR2="The url is invalid. Please check it!";
	
	//redis 随机码超时时间
	public static int reTimeout=24*60*60;
	
	public static int i=0;
	
	public static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	
	//最大序号值
	public static final short maxKeySeq=0x7fff;
	
	public static int totals=10000;
}

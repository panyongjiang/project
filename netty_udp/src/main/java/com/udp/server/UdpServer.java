package com.udp.server;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.udp.config.ElasticSearchConfig;
import com.udp.server.decode.UdpServerHandler;
import com.udp.util.Constants;
import com.udp.util.StringUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * 服务监听、初始化类
 * */
public class UdpServer {
	
	public static final Logger log = Logger.getLogger(UdpServer.class);	
	public static Channel channel;	
	public static int ab=0;

	public static void run() throws Exception {  
		//启动服务
		startSer();  
        EventLoopGroup workerGroup = new NioEventLoopGroup();  
        final EventExecutorGroup group = new DefaultEventExecutorGroup(16);
        
        try {  
            Bootstrap b = new Bootstrap();//udp不能使用ServerBootstrap  
            b.group(workerGroup).channel(NioDatagramChannel.class)//设置UDP通道  
                    .handler(new ChannelInitializer<NioDatagramChannel>(){

						@Override
						protected void initChannel(NioDatagramChannel ch)
								throws Exception {
							// TODO Auto-generated method stub
							ChannelPipeline pipeline = ch.pipeline();  
							pipeline.addLast(group,"handler",new UdpServerHandler());//消息处理器  
						}
                    	
                    })//初始化处理器  
                    .option(ChannelOption.SO_BROADCAST, true)// 支持广播  
                    .option(ChannelOption.SO_RCVBUF, 2048 * 1024)// 设置UDP读缓冲区为2M  
                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024);// 设置UDP写缓冲区为1M  
  
            // 绑定端口，开始接收进来的连接  
            ChannelFuture f = b.bind(Constants.PORT).sync();  
            //获取channel通道
            channel=f.channel();            
            log.info("UDP Server 启动！");            
            // 等待服务器 socket 关闭 。  
            // 这不会发生，可以优雅地关闭服务器。  
            f.channel().closeFuture().sync();
  
        }finally {
            // 优雅退出 释放线程池资源
        	group.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }  

	public static void startSer() throws Exception{
		String relativelyPath=System.getProperty("user.dir");
		
		//log4j
		String log4jPath = relativelyPath+"/resources/log4j.properties";
		PropertyConfigurator.configure(log4jPath);
		
		//加载spring配置信息
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext("file:"+relativelyPath+"/resources/spring-redis.xml");
		Constants.jedisConnectionFactory = appCtx.getBean("jedisConnectionFactory",JedisConnectionFactory.class);
		Constants.connection  = Constants.jedisConnectionFactory.getClusterConnection();
		
		//加载配置文件
		Properties pro = new Properties();
		FileInputStream in = new FileInputStream(relativelyPath+"/resources/readConfig.properties");
		pro.load(in);
		in.close();
		//es地址
		if(pro.get("esAddress")!=null&&!pro.get("esAddress").toString().trim().equals("")){
			ElasticSearchConfig.ES_ADDRESS=pro.get("esAddress").toString().trim();
		}
		//es索引
		if(pro.get("esIndex")!=null&&!pro.get("esIndex").toString().trim().equals("")){
			Constants.esIndex=pro.get("esIndex").toString().trim();
		}
		//es类型
		if(pro.get("esType")!=null&&!pro.get("esType").toString().trim().equals("")){
			Constants.esType=pro.get("esType").toString().trim();
		}
		//es客户端
		Constants.esClient=ElasticSearchConfig.getInstance();
		log.info("redisFilePath"+relativelyPath+"/resources/spring-redis.xml");
		//服务器监听端口
		if(pro.get("port")!=null&&!pro.get("port").toString().trim().equals("")){
			Constants.PORT=Integer.parseInt(pro.get("port").toString().trim());
		}
		//redis 随机码超时时间
		if(pro.get("reTimeout")!=null&&!pro.get("reTimeout").toString().trim().equals("")){
			Constants.reTimeout=StringUtil.caucalStringToInt(pro.get("reTimeout").toString().trim());
		}
		//是否加密
		if(pro.get("isEn")!=null&&!pro.get("isEn").toString().trim().equals("")){
			Constants.isEn=pro.get("isEn").toString().trim();
		}
		//测试发收包总数
		if(pro.get("totals")!=null&&!pro.get("totals").toString().trim().equals("")){
			Constants.totals=Integer.parseInt(pro.get("totals").toString().trim());
		}
	}
}

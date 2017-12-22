package com.udp.client;

/*import java.io.FileInputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.udp.entity.ReqUrlQuery;
import com.udp.entity.RespUrlQuery;
import com.udp.util.Constants;*/

public class CYUDPClients implements Runnable {

	//public static final Logger log = Logger.getLogger(CYUDPClients.class);
	
	@Override
	public void run() {
		/*try {
			startSer();
			//RedisClusterConnection connection  = Constants.jedisConnectionFactory.getClusterConnection();
			@SuppressWarnings("unused")
			byte[] radnoms=connection.get("uway15646681".getBytes());
			int a=0;
			System.out.println(a);
			DatagramSocket client = new DatagramSocket(null);
			InetAddress addr = InetAddress.getByName("127.0.0.1");
			while(true){				
				if(client.isClosed()){
					log.info("初始化客户端请求");
					client = new DatagramSocket(null);
					int port = 31000+(int)Thread.currentThread().getId();
					
					try{
						log.info("客户端连接请求");
						client.bind(new InetSocketAddress(port));
					}catch (Exception e){
						log.info("客户端连接创宇失败，请检查host和port");
						e.printStackTrace();
						continue;
					}
					client.setSoTimeout(100);
					client.setReuseAddress(true);	
					
				}

				byte[] gurls=connection.rPop(Constants.DKEYS.getBytes());
				if(gurls!=null&&gurls.length>0){
					String urls=new String(gurls);
					String[] url=urls.split("\\|");
					if(!connection.exists((Constants.URLBEFS+url[0]).getBytes())){
						ReqUrlQuery ruq=new ReqUrlQuery();
						ruq.setVersion((byte)1);
						ruq.setResult((byte)0);
						
						Calendar calendar = Calendar.getInstance();
						short sk=(short)calendar.get(Calendar.SECOND);
						int ak=(int)(new Random().nextInt() & 0x7FFFFFFF);
						ByteBuffer b = ByteBuffer.allocate(6);
						b.putShort(sk);
						b.putInt(ak);
						ruq.setKey(Arrays.copyOfRange(b.array(), 0, 6));
						
						byte[] sr=new byte[8];
						ruq.setServer_reserve(sr);
						ruq.setUrl(url[0].getBytes());
						byte[] sendBuf=ruq.toData();
						DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, addr, 20000);
						log.info("客户端发送请求报文");
						client.send(sendPacket);
						
						byte[] recvBuf = new byte[100];
						DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
						try{
							log.info("客户端接收报文");
							client.receive(recvPacket);
						}catch(Exception e){
							log.info("客户端接收报文超时");
							continue;
						}
						log.info("客户端解析收到的报文消息");
						byte[] res = recvPacket.getData();
						
						byte[] hea = new byte[8];
						System.arraycopy(res, 0, hea, 0, 8);
						RespUrlQuery rsuq=new RespUrlQuery();
						rsuq.assign(hea);
						log.info("客户端存储报文结果");
						connection.set((Constants.URLBEFS+url[0]).getBytes(), ("#Ffs$SV5%^$"+url[2]+"|"+rsuq.getResult()).getBytes());
					}
				}
				log.info("关闭client");
				if(!client.isClosed()) client.close();
				Thread.sleep(5000);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	
	public static void startSer() throws Exception{
		/*String relativelyPath=System.getProperty("user.dir");
		
		//log4j
		String log4jPath = relativelyPath+"/resources/log4j.properties";
		PropertyConfigurator.configure(log4jPath);
		
		log.info(relativelyPath);
		
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext("file:"+relativelyPath+"/resources/spring-redis.xml");
		Constants.jedisConnectionFactory = appCtx.getBean("jedisConnectionFactory",JedisConnectionFactory.class);
		
		Properties pro = new Properties();
		FileInputStream in = new FileInputStream(relativelyPath+"/resources/readConfig.properties");
		pro.load(in);
		in.close();*/
	}
	
	public static void main(String[] args){
		CYUDPClients udp=new CYUDPClients();
		udp.run();
	}
}

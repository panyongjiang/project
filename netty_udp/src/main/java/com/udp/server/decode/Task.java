package com.udp.server.decode;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.udp.entity.AckUrlCheck;
import com.udp.entity.NtfUrlEnil;
import com.udp.entity.ReqAuthNumber;
import com.udp.entity.ReqUrlCheck;
import com.udp.entity.RspAuthNumber;
import com.udp.entity.UdpHead;
import com.udp.service.UdpService;
import com.udp.util.Constants;
import com.udp.util.NettyUtil;
import com.udp.util.StringUtil;
import com.udp.util.TEA;
import com.udp.util.Utilities;

/**
 * 任务类，
 * 解析，封装数据并回复消息
 * */
public class Task implements Runnable{

	private DatagramPacket packet;
	
	private UdpService udpService;
	private ChannelHandlerContext ctx;
	
	public static final Logger log = Logger.getLogger(Task.class);
	
	public Task(DatagramPacket packet,ChannelHandlerContext ctx,UdpService udpService) {
		this.packet = packet;
		this.ctx = ctx;
		this.udpService = udpService;
	}

	@Override
	public void run() {
		Constants.totals--;
		if(Constants.totals==0){
			log.error("totals taskStart and "+Constants.sdf.format(new Date()));
		}
		log.info("消息来源" +  packet.sender().getHostString() + ":"+ packet.sender().getPort()+"=="+Constants.sdf.format(new Date()));
		//消息处理,零拷贝
		ByteBuf buf = (ByteBuf)packet.copy().content();
		//返回消息
		byte[] sendBuf=Constants.DERROR.getBytes();
		if(buf!=null){        	
        	byte[] reqss = new byte[buf.readableBytes()];
            buf.readBytes(reqss);
            if(reqss.length<Constants.headLen){
            	//整个包长度小于headLen,回复信息
            	sendBuf=Constants.HEADERROR.getBytes();
            }else{            	
            	//报头信息
               	byte[] bh=new byte[Constants.headLen];
               	System.arraycopy(reqss, 0, bh, 0, Constants.headLen); 
               	UdpHead uh=new UdpHead();
				uh.assign(bh);

               	if(Constants.isEn.trim().equals("Y")&&uh.getCommand()!=Constants.REQ_AUTH_NUMBER){
               		//如果开启加密消息,获取密钥，密钥由16位随机byte+4位时间戳
               		byte[] cgets=Constants.connection.get((Constants.BEFS+uh.getPlugID()+uh.getEncryptSeq()).getBytes());
               		
               		if(cgets!=null&&cgets.length>=20){
               			sendBuf=Constants.DERROR1.getBytes();
               			//取出密钥
               			byte[] randoms=new byte[cgets.length-4];
               			System.arraycopy(cgets, 0, randoms, 0, cgets.length-4);
               			
               			//取出报体信息
               			byte[] bts=new byte[uh.getZLength()-Constants.headLen];
                   		System.arraycopy(reqss, Constants.headLen, bts, 0, uh.getZLength()-Constants.headLen);
               			
               			try {
               				//初始化TEA加密算法
							TEA tea = new TEA(StringUtil.getMD5(StringUtil.copyByte(Constants.OTHERS.getBytes(),randoms)));
							//业务逻辑处理
							sendBuf=reSolve(uh,bts,tea,randoms,udpService);
						} catch (NoSuchAlgorithmException e) {
							e.printStackTrace();
							log.info("密钥错误，初始化TEA失败");
							sendBuf=Constants.DERROR1.getBytes();
						}             			
               		}else{
               			sendBuf=Constants.DERROR1.getBytes();//DERROR1: The key is invalid. Please check it!
               		}
               	}else{
               		//不开启加密消息，报体信息
               		byte[] bts=new byte[uh.getLength()-Constants.headLen];
               		System.arraycopy(reqss, Constants.headLen, bts, 0, uh.getLength()-Constants.headLen);
               		//业务逻辑处理
               		sendBuf=reSolve(uh,bts,null,null,udpService);
               	}
            }
        }
        if(sendBuf!=null){
        	ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(1024);
        	buffer.writeBytes(sendBuf);
        	//ByteBuf bf=Unpooled.copiedBuffer(sendBuf);
    		DatagramPacket dp = new DatagramPacket(buffer, packet.sender());
    		ctx.writeAndFlush(dp); 
        }
        buf.release();
		packet.release();
        if(Constants.totals==0){
			log.error("totals end and "+Constants.sdf.format(new Date()));
		}
	}
	
	//业务逻辑处理公共方法  param:报头   报体  加密类  密钥  udpService
	public static byte[] reSolve(UdpHead uh,byte[] bts,TEA tea,byte[] rads,UdpService udpService){
		String str=Constants.BODYERROR;
		byte[] sendBuf=str.getBytes();
		if(uh.getCommand()==Constants.REQ_URL_CHECK){
			//2002消息
    		log.info("跳转到UdpReqUrl"+Constants.sdf.format(new Date()));    		  
       		byte[] bt = bts;//加密报体默认等于未加密报体
       		if(rads!=null){
       			//加密了消息
       			try {
       				//解密报体
    				bt = StringUtil.deCodeBodys(tea,rads, bts);
    			} catch (Exception e) {
    				sendBuf = Constants.DEERROR.getBytes();
    				e.printStackTrace();
    				log.info("UdpReqUrl"+Constants.DEERROR+"===="+Constants.sdf.format(new Date()));
    				return sendBuf;
    			}
       		}
			
       		uh.setCommand((short)Constants.ACK_URL_CHECK); 
       		//收到的报体
       		ReqUrlCheck rc=new ReqUrlCheck();
       		int len=rc.assign(bt);
       		if(len>0){
       			//回复报体
           		AckUrlCheck ac=udpService.getReqCheck(Constants.connection,uh,rc);
           		//加密报体默认等于未加密报体
           		byte[] acs=ac.toData();//未加密的报体
           		byte[] ascss = ac.toData();//加密的报体           		
           		if(rads!=null){
           			try {
           				//加密消息
    					ascss = StringUtil.enCodeBodys(tea,rads, acs);
    				} catch (Exception e) {
    					sendBuf = Constants.ENERROR.getBytes();
        				e.printStackTrace();
        				log.info("UdpReqUrl"+Constants.DEERROR+"===="+Constants.sdf.format(new Date()));
        				return sendBuf;
    				}
           		}
           		//回复信息
           		sendBuf=NettyUtil.sendByte(uh,acs, ascss);
       		} 
    	}else if(uh.getCommand()==Constants.NTF_URL_EVIL){ 
    		//2004消息
       		log.info("跳转到UdpNtfUrl");
       		byte[] bt=bts;//加密报体默认等于未加密报体
       		if(rads!=null){
       			try {
       				//解密消息
    				bt = StringUtil.deCodeBodys(tea,rads, bts);
    			} catch (Exception e) {
    				sendBuf = Constants.DEERROR.getBytes();
    				e.printStackTrace();
    				log.info("UdpNtfUrl"+Constants.DEERROR+"===="+Constants.sdf.format(new Date()));
    				return sendBuf;
    			}
       		}
			
       		uh.setCommand((short)Constants.ACK_URL_CHECK); 
       		NtfUrlEnil nue=new NtfUrlEnil();
    		int len=nue.assign(bt);            		
    		if(len>0){
    			//事务处理
                try {
					udpService.setNtfUrlEnil(uh, nue);
				} catch (Exception e) {
					e.printStackTrace();
				}            
                //回复信息
                str="success";
                sendBuf=str.getBytes();
    		}		
       	}else if(uh.getCommand()==Constants.REQ_AUTH_NUMBER){ 
    		//2005消息
    		log.info("跳转到UdpReqAuth"+"=="+Constants.sdf.format(new Date()));   	    		
    		uh.setCommand((short)Constants.RSP_AUTH_NUMBER);
    		//收到的报体
    		ReqAuthNumber ra=new ReqAuthNumber();
    		int len=ra.assign(bts);
    		if(len>0){
    			//用哪种算法，默认算法等于1 即TinyEncrypt算法
    			if(ra.getEncryptMethod()==1){
    				Constants.EncryptMethod="TinyEncrypt";
    			}
    			int mks = 0;
    			//获取该设备最新的加密序号
    			byte[] seq=Constants.connection.hGet((Constants.BEFSS).getBytes(),Utilities.longToBytes(uh.getPlugID()));
    			log.info("根据设备号查询最新序号"+"=="+Constants.sdf.format(new Date()));
    			boolean f=true;
    			byte[] rsas=new byte[20];
    			if(seq==null){
    				mks = 1;    
    				f=true;
    			}else{
    				mks=Utilities.byteArrayToInt(seq);
    				byte[] rdoms=Constants.connection.get((Constants.BEFS+uh.getPlugID()+mks).getBytes());
    				log.info("根据设备号和序号查询密钥"+"=="+Constants.sdf.format(new Date()));
    				if(rdoms!=null){    					
    					byte[] tims=new byte[4];
    					System.arraycopy(rdoms, rdoms.length-4, tims, 0, 4);
    					int t1=Utilities.byteArrayToInt(tims);
    					int t2=(int)(System.currentTimeMillis()/1000);
    					//如果获取到的密钥超过了24小时，则取出最大序号加1，否则取出当前最大序号的密钥
    					if(((t1-t2)/(60*60))<24){
    						f=false;
    						uh.setEncryptSeq((short)mks);
    						RspAuthNumber rsqn=new RspAuthNumber();
    						rsqn.setNumber(ra.getNumber());
    						
    						byte[] strRandoms=new byte[rdoms.length-4];
    						System.arraycopy(rdoms, 0, strRandoms, 0, rdoms.length-4);
    						rsqn.setRandomStr(strRandoms);
    						
    						rsas=rsqn.toData();
    						log.info("封装发送消息"+"=="+Constants.sdf.format(new Date()));
    					}else{
    						f=true;
    						mks=(mks==Constants.maxKeySeq?0:mks)+1;
    					}
    				}
    			}
    			if(f){
    				//redis库中不存在密钥，则要存储最新密钥
    				uh.setEncryptSeq((short)mks);
        			RspAuthNumber rsa=udpService.saveReqAuth(Constants.connection,uh, ra);    			
        			rsas=rsa.toData();
    			}
    			sendBuf=NettyUtil.sendByte(uh,rsas,rsas);
    		}
    	}
		return sendBuf;
	}
}
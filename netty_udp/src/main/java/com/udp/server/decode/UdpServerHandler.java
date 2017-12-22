package com.udp.server.decode;

import java.util.Date;

import org.apache.log4j.Logger;

import com.udp.server.UdpServer;
import com.udp.service.UdpService;
import com.udp.service.impl.UdpServiceImpl;
import com.udp.util.Constants;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.concurrent.EventExecutor;

/**
 * 消息处理类
 * 监听消息
 * */
@Sharable
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket>{

	public static final Logger log = Logger.getLogger(UdpServerHandler.class);
	
	private EventExecutor executor ;
	
	private UdpService udpService;
	
	public UdpServerHandler(){
		udpService = new UdpServiceImpl();
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("UDP通道已经连接");
		super.channelActive(ctx);
	}
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
		if(UdpServer.ab==0) log.error("totals start "+Constants.sdf.format(new Date()));
		//new Task(packet.copy(),ctx,udpService).run();
		if(executor==null) {
			executor = ctx.executor();
		}else{
			executor = executor.parent().next();
		}
		UdpServer.ab++;
		try {
			if (!executor.isShutdown()) {
				executor.execute(new Task(packet.copy(),ctx,udpService));
			}
			/*if (!executor.isTerminated()) {
				executor.execute(new Task(packet.copy(),ctx,udpService));
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
 
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
        //logger.log(Level.INFO, "AuthServerInitHandler exceptionCaught");
    	log.error("UdpServerHandler exceptionCaught"+cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}

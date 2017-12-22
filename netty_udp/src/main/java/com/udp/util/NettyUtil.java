package com.udp.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

import com.udp.entity.UdpHead;

public class NettyUtil {

	public static void sendMsg(byte[] bf,ChannelHandlerContext ctx,DatagramPacket packet){
    	//DatagramPacket dp = new DatagramPacket(Unpooled.copiedBuffer(bf), packet.sender());
    	//UdpServer.channel.writeAndFlush(dp);
    	packet.release();
    	ctx.close();
    }
    
    /** 
     *  
     * @param data1 
     * @param data2 
     * @return data1 与 data2拼接的结果 
     */  
    public static byte[] addBytes(byte[] data1, byte[] data2) {  
        byte[] data3 = new byte[data1.length + data2.length];  
        System.arraycopy(data1, 0, data3, 0, data1.length);  
        System.arraycopy(data2, 0, data3, data1.length, data2.length);  
        return data3;  
    }  
    
    //封装返回消息,data1 加密前的数据，data2加密后的数据，不加密则两者相同
    public static byte[] sendByte(UdpHead uh, byte[] data1,byte[] data2){
    	byte[] uhs=new byte[Constants.headLen];
    	
    	int lenss1=Constants.headLen+data1.length;
    	uh.setLength((short)lenss1);
    	
    	int lenss=Constants.headLen+data2.length;
    	byte[] sendByte=new byte[lenss];
		uh.setZLength((short)lenss); 
		
		uhs = uh.toData();
		//回复信息
		sendByte=addBytes(uhs, data2);
		return sendByte;
    }
}

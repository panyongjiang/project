package com.udp.client;

import java.net.*;
import java.security.MessageDigest;

import com.udp.entity.ReqUrlCheck;
import com.udp.util.StringUtil;
import com.udp.util.TEA;

import udp.ack_url_check;
import udp.head;
import udp.req_url_check;

public class R2003 implements Runnable {

	int sucRecvPacketsNum, totalSendPacketsNum,failReceTotal;

	public R2003() {
		sucRecvPacketsNum = 0;
		failReceTotal = 0;
		totalSendPacketsNum = 1;
	}

	/**
	 * 对字符串md5加密
	 *
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static byte[] getMD5(byte[] str) throws Exception {
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str);
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hashs值
			//return new BigInteger(1, md.digest()).toString(16);
			return md.digest();
		} catch (Exception e) {
			throw new Exception("MD5加密出现错误");
		}
	}

	public void statistic()
	{
		System.out.println("total send packets num:"+this.totalSendPacketsNum+" suc recv packtes num:"+this.sucRecvPacketsNum);
	}
	@Override
	public void run() {
		try {
			DatagramSocket client = new DatagramSocket(null);
			int port = 31000+(int)Thread.currentThread().getId();
			try{
				client.bind(new InetSocketAddress(port));
			}catch (Exception e){
				e.printStackTrace();
			}
			client.setSoTimeout(5000);
			client.setReuseAddress(true);
			
			InetAddress addr = InetAddress.getByName("127.0.0.1");
//			InetAddress addr = InetAddress.getByName("114.115.139.225");
			
			head h = new head();
			req_url_check r = new req_url_check();
			
			r.setNew((byte) 1);
			r.setNumber(33532);

			h.setCommand((short) 2002);
			h.setPlugID(358611020000107L);
			h.setEncryptSeq((short)1);
			
			byte[] sendBuf, bh, br,brs;
//			String s = "www.luse088.com/";
//			String s = "www.aotuporn.xyz";
//			String s = "www.duopapa.info";
			String s = "www.7060i.com   ";
//			String s = "www.avavav5.com ";
//			String s = "wap.goukanla.com";
//			String s = "www.wuyefuli.net";
//			String s = "www.xslxlxsl.com";
//			String s = "www.baidu.com   ";
//			String s = "www.gugugle.com ";
//			String s = "res.qcwddd.com  ";
			byte[] mds = R2003.getMD5(s.getBytes());		
			r.setURL(mds);
			r.setURLLen((short) mds.length);
			r.setURLSRC(s.getBytes());	
			r.setURLSRCLen((short) s.length());
			
			byte[] randoms = {-24, 114, 30, 85, -98, 10, 33, -113, -96, 42, -72, 26, 10, -32, -23, 13};
		    bh = h.toData();
		    brs = r.toData();
		    br = StringUtil.enCodeBodys(null,randoms, brs);

			int len = br.length + 16;
			int lenss = brs.length + 16;
			h.setLength((short) (lenss));
			h.setZLength((short) (len));
			bh = h.toData();
			int lens = br.length + 16;
			sendBuf = new byte[lens];

			System.arraycopy(bh, 0, sendBuf, 0, bh.length);
			System.arraycopy(br, 0, sendBuf, bh.length, br.length);				

			DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, addr, /*port*/9999);
			client.send(sendPacket);

			byte[] recvBuf = new byte[1024];
			DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
			for(int i=0;i<totalSendPacketsNum;i++){
				try{
					client.receive(recvPacket);
					sucRecvPacketsNum++;
					
				}catch(Exception e){
					failReceTotal++;
					continue;
				}
				
				byte[] res = recvPacket.getData();
				byte[] hea = new byte[16];
				System.arraycopy(res, 0, hea, 0, 16);
				h.assign(hea);
				byte[] bt = new byte[h.getZLength() - 16];
				System.arraycopy(res, 16, bt, 0, h.getZLength() - 16);
				
				ack_url_check au=new ack_url_check();
		        byte[] bts=StringUtil.deCodeBodys(null,randoms, bt);
		        au.assign(bts);		
		        
				System.out.println(h.getCommand()+"=="+au.getNumber()+"-=="+au.getResult()+"=="+au.getType());
				
//				ReqUrlCheck rc=new ReqUrlCheck();
//				rc.assign(bts);
//				byte[] url = rc.getUrl();
//				String isoString = new String(url,"ISO-8859-1");
//				System.out.println(isoString);
			}

			System.out.println("一共查询30000条，其中成功"+sucRecvPacketsNum+"条，失败"+failReceTotal+"条");
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		
		/*UDPClient udp=new UDPClient();
		Thread t=new Thread(udp);
		t.start();*/
		R2003 udp=new R2003();
		udp.run();
	}
}
